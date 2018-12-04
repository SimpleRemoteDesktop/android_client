package com.github.sylvain121.SimpleRemoteDesktop.player.video;

import android.view.SurfaceHolder;

import com.github.sylvain121.SimpleRemoteDesktop.player.Frame;

import java.util.Arrays;
import java.util.LinkedList;

public class VideoDecoderThread extends Thread {
    private final MediaCodecDecoderRenderer mediaCodec;
    private final LinkedList<Frame> videoQueue;
    private final int width;
    private final int height;
    private final SurfaceHolder surface;
    private int FrameNumber = 0;
    private boolean isRunning;

    public VideoDecoderThread(LinkedList<Frame> videoQueue, int width, int height, SurfaceHolder holder) {
        this.width = width;
        this.height = height;
        this.surface = holder;
        this.videoQueue = videoQueue;
        this.mediaCodec = new MediaCodecDecoderRenderer();

        this.mediaCodec.setRenderTarget(this.surface);
        this.mediaCodec.setup(this.width, this.height);
    }


    private void submitFrame(byte[] data) {
        this.mediaCodec.submitDecodeUnit(data, data.length, this.FrameNumber++, System.currentTimeMillis());
    }

    private void videoNALParser(byte[] frameData) {
        if (frameData[4] == 0x67) {
            int ppsOffset = 0;
            int dataOffset = 0;
            for (int i = 4; i < frameData.length; i++) {
                int NAL_START = frameData[i] << 16 | frameData[i + 1] << 8 | frameData[i + 2];
                if (NAL_START == 0x01 && frameData[i + 3] == 0x68) {
                    ppsOffset = i - 1;
                    i = i + 4;
                } else if (ppsOffset > 0 && NAL_START == 0x01) {
                    dataOffset = i;
                    break;
                }


            }

            byte[] sps = Arrays.copyOfRange(frameData, 0, ppsOffset);
            byte[] pps = Arrays.copyOfRange(frameData, ppsOffset, dataOffset);
            byte[] data = Arrays.copyOfRange(frameData, dataOffset, frameData.length);

            submitFrame(sps);
            submitFrame(pps);
            submitFrame(data);
        } else {
            submitFrame(frameData);
        }
    }

    @Override
    public void run() {
        this.mediaCodec.start();
        this.isRunning = true;
        while (this.isRunning) {
            if (!this.videoQueue.isEmpty()) {
                Frame frame = this.videoQueue.poll();
                this.submitFrame(frame.data);
            } else {
                try {
                    Thread.sleep(1); //FIXME
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        this.mediaCodec.stop();
        this.mediaCodec.cleanup();
        //FIXME stop
    }

    public void close() {
        this.isRunning = false;
    }
}

package com.github.sylvain121.SimpleRemoteDesktop.player.sound;

import com.github.sylvain121.SimpleRemoteDesktop.player.Frame;

import java.util.LinkedList;

public class SoundDecoderThread extends Thread {


    private final int sampleRate;
    private final int channels;
    private final LinkedList<Frame> queue;
    private final SoundDecoder decoder;
    private boolean isRunning = false;

    public SoundDecoderThread(int sampleRate, int channels, LinkedList<Frame> soundQueue) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.queue = soundQueue;

        this.decoder = new SoundDecoder(this.sampleRate, this.channels);
    }

    @Override
    public void run() {
        this.isRunning = true;
        while(this.isRunning) {
            if(!this.queue.isEmpty()) {
                Frame frame = this.queue.poll();
                this.decoder.decodeFrame(frame.data);
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.cleanQueue();
    }

    private void cleanQueue() {
        while(!this.queue.isEmpty()) {
            this.queue.poll();
        }
    }
}

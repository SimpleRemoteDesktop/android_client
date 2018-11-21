package com.github.sylvain121.SimpleRemoteDesktop.player;

import android.util.Log;

import com.github.sylvain121.SimpleRemoteDesktop.player.network.DataManagerChannel;
import com.github.sylvain121.SimpleRemoteDesktop.player.video.MediaCodecDecoderRenderer;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by sylvain121 on 06/10/2017.
 */

class ConnectionThread extends Thread {
    private final LinkedList<Message> inputQueue;
    private final LinkedList<Frame> videoQueue;
    private final LinkedList<Frame> soundQueue;
    private DataManagerChannel m_renderSock;
    private int FrameNumber = 0;

    private final static String TAG = "CONNEXION_THREAD";

    public ConnectionThread(LinkedList<Message> inputNetworkQueue, LinkedList<Frame> videoQueue, LinkedList<Frame> soundQueue) {

        this.inputQueue = inputNetworkQueue;
        this.videoQueue = videoQueue;
        this.soundQueue = soundQueue;

    }

    public void connect(String hostname, int port) {
        Log.d(TAG, "getting network instance");
        m_renderSock = DataManagerChannel.getInstance();
        Log.d(TAG, "connecting to serveur " + hostname + " port " + port);
        m_renderSock.connect(hostname, port);
        Log.d(TAG, "Connected");


    }

    public void sendStartPacket(int codecWidth, int codecHeight, int  bandwidth, int fps) {
        Log.d(TAG, "Send start message");
        m_renderSock.sendStartStream(fps, codecWidth, codecHeight, bandwidth);

    }

    @Override
    public void run() {

        while (!Thread.interrupted()) {
            Frame frame = m_renderSock.receive();
            switch (frame.type) {
                case Frame.VIDEO:
                    Log.d(TAG, "New video frame");
                    this.videoQueue.add(frame);
                    break;
                case Frame.AUDIO:
                    Log.d(TAG, "New audio frame");
                    this.soundQueue.add(frame);
                    Log.d(TAG, "New Audio frame, queue size : " + this.soundQueue.size());
            }
            while (!this.inputQueue.isEmpty()) {
                Message m = this.inputQueue.poll();
                Log.d(TAG, "send new input event");
                m_renderSock.send(m);
            }
        }
    }


   public void close() {
        m_renderSock.closeChannel();
    }
}

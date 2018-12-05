package com.github.sylvain121.SimpleRemoteDesktop.player;

import android.util.Log;

import com.github.sylvain121.SimpleRemoteDesktop.player.network.DataManagerChannel;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by sylvain121 on 06/10/2017.
 */

class ConnectionThread extends Thread {
    private final LinkedList<Message> inputQueue;
    private final LinkedList<Frame> videoQueue;
    private final LinkedList<Frame> soundQueue;
    private final int port;
    private ConnectionSendThread sendThread;
    private DataManagerChannel networkChan;
    private int FrameNumber = 0;

    private final static String TAG = "CONNEXION_THREAD";
    private String hostname;
    private boolean isRunning;

    public ConnectionThread(String hostname, int port, LinkedList<Message> inputNetworkQueue, LinkedList<Frame> videoQueue, LinkedList<Frame> soundQueue) {
        this.hostname = hostname;
        this.port = port;
        this.inputQueue = inputNetworkQueue;
        this.videoQueue = videoQueue;
        this.soundQueue = soundQueue;

    }

    public void connect(String hostname, int port) {
        Log.d(TAG, "getting network instance");
        this.networkChan = DataManagerChannel.getInstance();
        Log.d(TAG, "connecting to serveur " + hostname + " port " + port);
        this.networkChan.connect(hostname, port);
        Log.d(TAG, "Connected");
        this.sendThread = new ConnectionSendThread(this.inputQueue, this.networkChan);
        this.sendThread.start();


    }

    public void sendStartPacket(int codecWidth, int codecHeight, int bandwidth, int fps) {
        Log.d(TAG, "Send start message codec w: " + codecWidth + " h: " + codecHeight + " bw: " + bandwidth + " fps: " + fps);
        this.inputQueue.add(Message.startStream(fps, codecWidth, codecHeight,bandwidth));
    }

    @Override
    public void run() {
        this.isRunning = true;
        this.connect(this.hostname, this.port);
        while (this.isRunning) {

            Frame frame = networkChan.receive();
            switch (frame.type) {
                case Frame.VIDEO:
                    Log.d(TAG, "New video frame");
                    this.videoQueue.add(frame);
                    break;
                case Frame.AUDIO:
                    Log.d(TAG, "New audio frame");
                    this.soundQueue.add(frame);
                    Log.d(TAG, "New Audio frame, queue size : " + this.soundQueue.size());
                    break;
                case Frame.DIMENSION_FRAME:
                    Log.d(TAG, "dimension frame received");
                    break;

            }
        }
    }


    public void close() {
        this.isRunning = false;
        networkChan.closeChannel();

    }
}

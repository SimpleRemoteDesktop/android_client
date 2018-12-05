package com.github.sylvain121.SimpleRemoteDesktop.player;

import android.util.Log;

import com.github.sylvain121.SimpleRemoteDesktop.player.network.DataManagerChannel;

import java.util.LinkedList;

class ConnectionSendThread extends Thread {
    private final LinkedList<Message> queue;
    private final DataManagerChannel networkChan;
    private boolean isRunning = false;
    private String TAG = "CONNEXION_THREAD_SEND";

    public ConnectionSendThread(LinkedList<Message> inputQueue, DataManagerChannel networkChan) {
        this.queue = inputQueue;
        this.networkChan = networkChan;
    }


    @Override
    public void run() {
        Log.d(TAG, "starting thread");
        this.isRunning = true;
        while (this.isRunning) {
            if (!this.queue.isEmpty()) {
                Message m = this.queue.poll();
                Log.d(TAG, "send new input event type: " + m.getType());
                networkChan.send(m);
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.github.sylvain121.SimpleRemoteDesktop.player;

import android.util.Log;
import android.view.MotionEvent;


import java.util.LinkedList;

/**
 * Created by ESME7383 on 02/08/2017.
 */

class UserEventManager {

    public static String TAG = "EVENT LISTENER";
    private final LinkedList<Message> queue;
    private int previousButtonState = 0;




    private int leftMask = 1;
    private int rightMask = 2;
    private Boolean preLeft = false;
    private Boolean prevRight = false;
    private int screenWidth = 0;
    private int screenHeight = 0;

    public UserEventManager(LinkedList<Message> inputNetworkQueue) {
        this.queue = inputNetworkQueue;
    }


    public boolean genericMouseHandler(MotionEvent event) {

        boolean left = (event.getButtonState() & leftMask) == leftMask;
        boolean right = (event.getButtonState() & rightMask) == rightMask;
        Log.d(TAG, event.getButtonState()+"");
        Log.d(TAG, event.getAction()+"");
        Log.d(TAG, "left : " + left + " right : " + right);

                if (isMouseButtonStateChange(left, preLeft)) {
                    Log.d(TAG, "input : left click change detected");
                    preLeft = left;
                    sendMouseButtonUpdate("left", left);
                } else if (isMouseButtonStateChange(right, prevRight)) {
                    prevRight = right;
                    Log.d(TAG, "input : right click change detected");
                    sendMouseButtonUpdate("right", right);
                } else {
                    sendMousePosition(event.getX(), event.getY());
                }
                Log.d(TAG, "input add event to queue");

        return true;
    }

    private void sendMousePosition(float fx, float fy) {
        if( screenWidth > 0 && screenHeight > 0) {
            float x = fx / this.screenWidth;
            float y = fy / this.screenHeight;
            Log.d(TAG, "X : " + x + " Y : " + y);
            this.queue.add(Message.mouseMove(x, y));
            //DataManagerChannel.getInstance().sendMouseMotion(x, y);
        }else {
            Log.d(TAG, "input : Unable to send mouse position screen not initialized");
        }
    }

    private void sendMouseButtonUpdate(String buttonName, boolean isPressed) {
        Log.d(TAG, "input : send mouse button update " + buttonName + " isPressed ?: " + isPressed);
        if(isPressed) {
            this.queue.add(Message.mouseButtonDown(buttonName));
        } else {
            this.queue.add(Message.mouseButtonUp(buttonName));
        }
    }

    private boolean isMouseButtonStateChange(boolean mouseButton, Boolean previousMouseButtonState) {
        if (mouseButton != previousMouseButtonState) return true;
        return false;
    }

    public boolean onTouchHandler(MotionEvent event) {
        Log.d(TAG, "input: Touch event detected");
        sendMousePosition(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                sendMouseButtonUpdate("left", true);
                break;
            case MotionEvent.ACTION_UP:
                sendMouseButtonUpdate("left", false);
                break;
        }
        Log.d(TAG, "message queue: "+this.queue.size());
        return true;
    }

    public boolean onLongTouchHandler(MotionEvent e) {
        sendMousePosition(e.getX(), e.getY());
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                sendMouseButtonUpdate("right", true);
                break;
            case MotionEvent.ACTION_UP:
                sendMouseButtonUpdate("right", false);
                break;
        }
        return true;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void keyDown(int keyCode) {
        //DataManagerChannel.getInstance().sendKeyDown(keyCode);
    }

    public void keyUp(int keyCode) {
        //DataManagerChannel.getInstance().sendKeyUp(keyCode);
    }
}

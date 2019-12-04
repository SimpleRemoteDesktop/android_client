package com.github.sylvain121.SimpleRemoteDesktop.player.controller;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SRDScreenController implements View.OnTouchListener, View.OnGenericMotionListener {

    private final Context context;
    private final RelativeLayout relativeLayout;

    public SRDScreenController(Context context) {
        this.context = context;
        relativeLayout = new RelativeLayout(getContext());
    }

    private Context getContext() {
        return this.context;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.w("SRD_Touch", " touched");
        return false;
    }

    protected void placeView( View v, int left, int top, int width, int height) {
        RelativeLayout.LayoutParams params = null;
        params = new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = left;
        params.topMargin = top;
        relativeLayout.addView(v, params);
    }

    protected Button newButton(String label, int left, int top, int width, int height) {
        Button b = new Button(getContext());
        b.setTextSize(10);
        b.setText(label);
        placeView(b, left, top, width, height);
        return b;
    }

    public View getView() {
        return this.relativeLayout;
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {
        Log.w("STD_Touch", String.format("Motion : x : %s y: %s", motionEvent.getX(), motionEvent.getY()));
        return false;
    }
}

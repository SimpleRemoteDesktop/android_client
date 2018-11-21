package com.github.sylvain121.SimpleRemoteDesktop.player;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.sylvain121.SimpleRemoteDesktop.MainActivity;
import com.github.sylvain121.SimpleRemoteDesktop.player.sound.SoundDecoderThread;
import com.github.sylvain121.SimpleRemoteDesktop.player.video.MediaCodecDecoderRenderer;
import com.github.sylvain121.SimpleRemoteDesktop.player.video.VideoDecoderThread;
import com.github.sylvain121.SimpleRemoteDesktop.settings.SettingsActivity;

import java.util.LinkedList;

public class PlayerActivity extends Activity implements SurfaceHolder.Callback, InputManager.InputDeviceListener {

    private UserEventManager userEventManager;
    private String TAG = "PLAYER ACTIVITY";
    private String IPAddress;
    private boolean MouseIsPresent = false;
    private ConnectionThread connectionThread;
    private LinkedList<Message> inputNetworkQueue;
    private LinkedList<Frame> soundQueue;
    private LinkedList<Frame> videoQueue;
    private SoundDecoderThread soundThread;
    private int codec_width = 800;
    private int codec_height = 600;
    private int bandwidth = 1000000;
    private int fps = 30;
    private VideoDecoderThread videoThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SurfaceView sv = new SurfaceView(this);
        sv.getHolder().addCallback(this);

        Intent intent = getIntent();
        this.IPAddress = intent.getStringExtra(MainActivity.IP_ADDRESS);
        Log.d(TAG, "server address : " + this.IPAddress);

        this.soundQueue = new LinkedList<>();
        this.videoQueue = new LinkedList<>();
        this.inputNetworkQueue = new LinkedList<>();
        SharedPreferences sharedPreference = getBaseContext().getSharedPreferences(SettingsActivity.SIMPLE_REMOTE_DESKTOP_PREF, 0);


        String currentResolution = sharedPreference.getString(SettingsActivity.SIMPLE_REMOTE_DESKTOP_PREF_RESOLUTION, null);

        switch (currentResolution) {
            case "600p":
                this.codec_width = 800;
                this.codec_height = 600;
                break;
            case "720p":
                codec_width = 1280;
                codec_height = 720;
                break;
            case "1080p":
                codec_width = 1920;
                codec_height = 1080;
                break;
        }


        bandwidth = sharedPreference.getInt(SettingsActivity.SIMPLE_REMOTE_DESKTOP_PREF_BITRATE, 0);
        fps = sharedPreference.getInt(SettingsActivity.SIMPLE_REMOTE_DESKTOP_PREF_FPS, 0);

        userEventManager = new UserEventManager(inputNetworkQueue);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(sv);
        sv.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                Log.d(TAG, "event : " + event.getButtonState());
                return userEventManager.genericMouseHandler(event);

            }
        });

        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "touch event : " + event.getButtonState());
                if (event.getDevice().getSources() != InputDevice.SOURCE_MOUSE) {
                    return userEventManager.onTouchHandler(event);
                } else {
                    return userEventManager.genericMouseHandler(event);
                }
            }
        });


        soundThread = new SoundDecoderThread(48000, 2, this.soundQueue);
        soundThread.start();
        connectionThread = new ConnectionThread(this.inputNetworkQueue, this.videoQueue, this.soundQueue);
        connectionThread.start();


        setVolumeControlStream(AudioManager.STREAM_MUSIC);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("SURFACE", "SURFACE CREATED");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(videoThread != null) {
            this.videoThread.close();
            try {
                this.videoThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace(); //FIXME
            }
        }
        videoThread = new VideoDecoderThread(videoQueue, width, height, holder);
        videoThread.start();

       userEventManager.setScreenSize(width, height);
   }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("SURFACE", "SURFACE DESTROYED");
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        InputDevice device = InputDevice.getDevice(deviceId);
        if (device.getSources() == InputDevice.SOURCE_MOUSE) {
            Log.d(TAG, "Mouse plugged");
            this.MouseIsPresent = true;
        }
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        InputDevice device = InputDevice.getDevice(deviceId);
        if (device.getSources() == InputDevice.SOURCE_MOUSE) {
            Log.d(TAG, "Mouse Changed");

        }
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {
        InputDevice device = InputDevice.getDevice(deviceId);
        if (device.getSources() == InputDevice.SOURCE_MOUSE) {
            Log.d(TAG, "Mouse Unplugged");
            this.MouseIsPresent = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        Log.d(TAG, "key down " + keyCode);
        //userEventManager.keyDown(keyCode);
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
        Log.d(TAG, "key up " + keyCode);
        //userEventManager.keyUp(keyCode);
        return false;
    }
}
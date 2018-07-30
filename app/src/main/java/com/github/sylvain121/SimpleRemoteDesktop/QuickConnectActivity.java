package com.github.sylvain121.SimpleRemoteDesktop;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.sylvain121.SimpleRemoteDesktop.player.PlayerActivity;

public class QuickConnectActivity extends Activity {
    public static final String IP_ADDRESS = "com.simpleremotedesktop.IPAddress";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(R.layout.activity_quick_connect);


    }

    public void Onconnect(View view) {
        Intent intent = new Intent(this, PlayerActivity.class);
        EditText addressEditText = (EditText) findViewById(R.id.ipaddress);
        intent.putExtra(IP_ADDRESS, addressEditText.getText().toString());
        Log.d("QUICK_CONNECT", "start session for address : "+addressEditText.getText());
        startActivity(intent);
    }
}

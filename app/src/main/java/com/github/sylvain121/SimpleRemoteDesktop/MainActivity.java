package com.github.sylvain121.SimpleRemoteDesktop;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.github.sylvain121.SimpleRemoteDesktop.discovery.ServerAdapter;
import com.github.sylvain121.SimpleRemoteDesktop.player.PlayerActivity;
import com.github.sylvain121.SimpleRemoteDesktop.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class MainActivity extends ListActivity implements SurfaceHolder.Callback {

    private static final String SIMPLE_REMOTE_DESKTOP_SERVER_LIST = "com.github.sylvain121.simpleremotedesktop.android.server.list";
    private static final String SIMPLE_REMOTE_DESKTOP_SERVERS = "com.github.sylvain121.simpleremotedesktop.android.serverList";
    private List<String> serverList = new ArrayList<>();
    private ServerAdapter serverAdapter;
    public static final String IP_ADDRESS = "com.simpleremotedesktop.IPAddress";
    public static final String TAG = "MAIN ACTIVITY";
    private SharedPreferences sharedPreference;


    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        serverAdapter = new ServerAdapter(this, R.layout.listserverview_item_row, serverList);
        setListAdapter(serverAdapter);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        sharedPreference = getBaseContext().getSharedPreferences(SIMPLE_REMOTE_DESKTOP_SERVER_LIST, 0);
        String serverString = sharedPreference.getString(SIMPLE_REMOTE_DESKTOP_SERVERS, null);
        Log.d(TAG, "loading hostname history : " + serverString);
        if (serverString == null)
            serverString = "";
        String[] serverArray = serverString.split(";");
        for (int i = 0; i < serverArray.length; i++) {
            serverList.add(serverArray[i]);
        }
        serverAdapter.notifyDataSetChanged();
    }

    public void OnConnect(View view) {
        EditText addressEditText = (EditText) findViewById(R.id.ipaddress);
        String hostname = addressEditText.getText().toString();
        this.storeAndConnect(hostname);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String hostname = serverList.get(position);
        this.storeAndConnect(hostname);
    }

    private void storeAndConnect(String hostname) {
        Log.d(TAG, "serveur address from input : " + hostname);
        serverList.add(hostname);
        Set<String> saveList = new HashSet<String>(serverList);
        String serverString = "";
        Iterator<String> i = saveList.iterator();
        while (i.hasNext()) {
            serverString += i.next() + ";";
        }
        Log.d("MAIN_ACTIVITY", serverString);
        sharedPreference
                .edit()
                .putString(SIMPLE_REMOTE_DESKTOP_SERVERS, serverString)
                .apply();
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(IP_ADDRESS, hostname);
        Log.d("MAIN_ACTIVITY", "start session for address : " + hostname);
        startActivity(intent);
    }


    public List<String> getServerList() {
        return serverList;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        serverAdapter.notifyDataSetChanged();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        serverAdapter.notifyDataSetChanged();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Activity destroyed");
    }

    public void onSettingsClickListener(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onQuickConnect(View view) {
        Intent intent = new Intent(this, QuickConnectActivity.class);
        startActivity(intent);
    }
}


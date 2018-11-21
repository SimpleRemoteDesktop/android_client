package com.github.sylvain121.SimpleRemoteDesktop.log;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;

import com.github.sylvain121.SimpleRemoteDesktop.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class SendLog extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_send_log);

    }

    @Override
    public void onClick(View view) {

    }

    private String extractLogToFile() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {

        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER)) {
            model = Build.MANUFACTURER + " " + model;
        }

        String path = Environment.getExternalStorageDirectory() + "/" + "SimpleRemoteDesktop";
        String fullName = path + "stacktrace";

        File file = new File(fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;

        try {
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader((process.getInputStream()));

            writer = new FileWriter(file);
            writer.write("Android version : " + Build.VERSION.SDK_INT + "\n");
            writer.write("DEvice: " + model + "\n");
            writer.write("App version: " + (info == null ? "(null)" : info.versionCode + "\n"));

            char[] buffer = new char[10000];
            do {
                int n = reader.read(buffer, 0, buffer.length);
                if (n == -1) {
                    break;
                }
                writer.write(buffer, 0, n);
            } while (true);
            reader.close();
            writer.close();
        }
        catch (IOException e) {
            if(writer != null) {
                try{
                    writer.close();
                }catch (IOException e1 ){

                }
                if(reader != null) {
                    try{
                        reader.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            return null;
        }
        return fullName;
    }

    private void sendLogFile() {
        String fullName = extractLogToFile();
        if(fullName == null)
            return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"sylvain121@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "SRD Crash log");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(("file://"+fullName)));
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached");
        startActivity(intent);
    }

}

package com.github.sylvain121.SimpleRemoteDesktop;

import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashLogger {
    public static Intent reportCrash(Throwable e, String threadName) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String errors = sw.toString();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sylvain121@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "[SRD] Android crash log");
        email.putExtra(Intent.EXTRA_TEXT, errors);
        email.setType("message/rfc822");
        return Intent.createChooser(email, "Choose an Email client :");
    }
}

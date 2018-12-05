package com.github.sylvain121.SimpleRemoteDesktop;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.sylvain121.SimpleRemoteDesktop.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ServerAdapter extends ArrayAdapter<String> {


    private final Context context;
    private final int resource;
    private final List<String> data;

    public ServerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ServerHolder holder = new ServerHolder();
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder.hostname = (TextView) row.findViewById(R.id.hostname);

            row.setTag(holder);
        }
        else {
            holder = (ServerHolder) row.getTag();
        }

        String server = data.get(position);
        holder.hostname.setText(server);

        return row;

    }

    class ServerHolder {
        TextView hostname;
    }
}

package com.github.sylvain121.SimpleRemoteDesktop.player.network;

import android.util.Log;

import com.github.sylvain121.SimpleRemoteDesktop.player.Frame;
import com.github.sylvain121.SimpleRemoteDesktop.player.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;


/**
 * Created by user on 7/31/17.
 */


public class DataManagerChannel {
    private static DataManagerChannel instance = null;
    private static ByteBuffer buf = null;
    byte[] net_in = new byte[0];
    private boolean isFirstNal = true;
    public static final String TAG = "DATAMANAGER_CHANNEL";
    private SocketChannel chan = null;
    private OutputStream output;


    public void connect(String hostname, int port) {
        try {

            Log.v(TAG, "Connecting to socket" + hostname + ":" + port);
            final InetSocketAddress socketAddr = new InetSocketAddress(hostname, port);
            chan = SocketChannel.open();
            chan.connect(socketAddr);
            output = chan.socket().getOutputStream();
            buf = ByteBuffer.allocate(2048 * 1024);
            buf.order(ByteOrder.LITTLE_ENDIAN);
            buf.limit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DataManagerChannel() {

    }

    private void startStequence() {


    }

    public static DataManagerChannel getInstance() {
        if (instance == null) {
            instance = new DataManagerChannel();
        }
        return instance;
    }

    public Frame receive() {
        return getnewFrame();
    }

    private Frame getnewFrame() {
        Frame frame = new Frame();
        try {

            if (chan.isConnected()) {
                ensure(4, chan);
                frame.type = buf.getInt();
                Log.d(TAG, "receiving frame number : " + frame.type);
                ensure(4, chan);
                frame.size = buf.getInt();
                Log.d(TAG, "new frame size : " + frame.size);
                ensure(frame.size, chan);
                frame.data = new byte[frame.size];
                buf.get(frame.data, 0, frame.size);
                Log.d(TAG, "new frame array length :" + frame.size);
            } else {
                Log.d(TAG, "Socket not connected reconnect");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return frame;
    }

    private static void ensure(int len, ByteChannel chan) throws IOException {
        int test = buf.remaining();
        if (buf.remaining() < len) {
            buf.compact();
            buf.flip();
            do {
                buf.position(buf.limit());
                buf.limit(buf.capacity());
                chan.read(buf);
                buf.flip();
            } while (buf.remaining() < len);
        }
    }


    public void sendStartStream(int fps, int codec_width, int codec_height, int bandwidth) {
        try {
            Log.d(TAG, "send start message");
            output.write(Message.startStream(fps, codec_width, codec_height, bandwidth).toBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(Message m) {
        try {
            Log.d(TAG, "sending Message to server"+ m.getType());
            output.write(m.toBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMouseMotion(float x, float y) {
        try {
            output.write(Message.mouseMove(x, y).toBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMouseButton(String buttonName, boolean isPressed) {
        if (isPressed) {
            try {
                output.write(Message.mouseButtonDown(buttonName).toBytes());
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                output.write(Message.mouseButtonUp(buttonName).toBytes());
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        String result = "";
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
            result += " ";
            result += sb.toString();
        }
        Log.d(this.getClass().getName(), result + "\n");
    }

/*    private byte[] NALparser() {
        for(int i = 0; i < net_in.length - 5; i++) {
            if(nalStartCodeDected(i)) {
                if(!isPPS(i)) {
                    if(isFirstNal) {
                        isFirstNal = false;
                    } else {
                        isFirstNal = true;

                        return detachFrameFromBuffer(i);
                    }

                }
            }
        }
        return new byte[0];
    }*/

 /*   private byte[] detachFrameFromBuffer(int i) {
        frame = Arrays.copyOf(net_in, i);
        net_in = Arrays.copyOfRange(net_in, i, net_in.length);
        return frame;
    }*/

    private boolean isPPS(int i) {
        return net_in[i + 4] == 0x68;
    }

    private boolean nalStartCodeDected(int i) {
        return net_in[i] == 0x00 && net_in[i + 1] == 0x00 && net_in[i + 2] == 0x00 && net_in[i + 3] == 0x01;
    }

    private void addToNetworkBuffer(byte[] dataAvaibleLength) {
        byte[] net_temp = new byte[dataAvaibleLength.length + net_in.length];
        System.arraycopy(net_in, 0, net_temp, 0, net_in.length);
        System.arraycopy(dataAvaibleLength, 0, net_temp, net_in.length, dataAvaibleLength.length);
        net_in = net_temp;
    }

    public void closeChannel() {
        if (chan != null) {
            try {
                chan.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendKeyDown(int keyCode) {
        try {
            output.write(Message.keyDown(keyCode).toBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendKeyUp(int keyCode) {
        try {
            output.write(Message.KeyUp(keyCode).toBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

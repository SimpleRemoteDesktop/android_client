package com.github.sylvain121.SimpleRemoteDesktop.player;

public class Frame {
    public final static int VIDEO = 1;
    public final static int AUDIO = 2;
    public final static int DIMENSION_FRAME = 3;

    public int type;
    public int size;
    public byte[] data;
}

package com.github.sylvain121.SimpleRemoteDesktop.player.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

import com.github.sylvain121.SimpleRemoteDesktop.player.Frame;
import com.score.rahasak.utils.OpusDecoder;

import java.util.LinkedList;

public class SoundDecoder {
    private static String TAG = "SOUND_DECODER";
    private final int sampleRate;
    private final int channels;
    private final short[] outBuf;
    private static int FRAME_SIZE = 960;
    private final AudioTrack track;
    private final OpusDecoder decoder;


    public SoundDecoder(int sampleRate, int channels) {

        this.sampleRate = sampleRate;
        this.channels = channels;
        this.outBuf = new short[FRAME_SIZE * channels];
        int channelFormat = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelFormat,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate,
                channelFormat,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,
                AudioTrack.MODE_STREAM);

        decoder = new OpusDecoder();
        decoder.init(sampleRate, channels);
        Log.d(TAG, "Creating new sound decoder");
    }

    public void decodeFrame(byte[] inBuffer) {
        Log.d(TAG, "decode new frame");
        int decoded = decoder.decode(inBuffer, outBuf, FRAME_SIZE);
        track.write(outBuf, 0, decoded * channels);
    }
}

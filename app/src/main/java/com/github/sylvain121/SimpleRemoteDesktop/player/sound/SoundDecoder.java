package com.github.sylvain121.SimpleRemoteDesktop.player.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import com.score.rahasak.utils.OpusDecoder;

public class SoundDecoder {

    private final AudioTrack track;
    private final OpusDecoder decoder;
    private final int sampleRate;
    private final int channels;
    private short[] outBuf;
    private static int FRAME_SIZE = 960;

    public SoundDecoder(int sampleRate, int channels) {

        this.sampleRate = sampleRate;
        this.channels = channels;
        this.outBuf = new short[FRAME_SIZE * channels];
        int channelFormat = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
        int minBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelFormat,
                AudioFormat.ENCODING_PCM_16BIT);

        track = new AudioTrack(AudioManager.STREAM_SYSTEM,
                sampleRate,
                channelFormat,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize,
                AudioTrack.MODE_STREAM);

         decoder = new OpusDecoder();
         decoder.init(sampleRate, channels);

    }

    public void decodeFrame(byte[] inBuffer) {
        int decoded = decoder.decode(inBuffer, outBuf, FRAME_SIZE);
        track.write(outBuf, 0, decoded * channels);
    }
}

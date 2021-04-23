package com.dsx.audioclip.util;

/**
 * 利用 Lame 将WAV 转换MP3
 */
public class LameJni {
    static {
        System.loadLibrary("native-lib");
    }
    public native String getVersion();
    //初始化lame
    public native int pcmTomp3(String pcmPath, String mp3Path, int sampleRate, int channel, int bitRate);
    public native void destroy();

}

package com.dsx.audioclip.util;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工具类
 */
public class AudioUtil {
    private static final String TAG = "AudioUtil";
    private final ExecutorService mExecutorService;
    private AudioRecord mAudioRecord;
    private LameJni lameJni;
    private AudioUtil() {
        lameJni=new LameJni();
        mExecutorService = Executors.newSingleThreadExecutor();
    }


    /**
     * 释放资源
     */
    public void release() {
        if (mAudioRecord != null) {
            mAudioRecord.release();
        }
        if (lameJni!=null){
            lameJni.destroy();
        }
        if (mExecutorService!=null){
            mExecutorService.shutdown();
        }
    }

    /**
     * pcm文件转wav文件
     * @param pcmFile pcm文件
     * @param wavFile wav文件
     */
    public void pcm2Wav(File pcmFile, File wavFile) {
        if (!pcmFile.exists()){
            throw new RuntimeException(pcmFile.getAbsolutePath()+",there is no pcm file");
        }
        mExecutorService.execute(new PcmToWavThread(pcmFile,wavFile));
    }

    /**
     * pcm文件转换为mp3文件
     * @param pcmFile pcm文件
     * @param mp3File mp3文件
     */
    public void pcm2Mp3(File pcmFile, File mp3File,int sampleRate, int channel){
        if (!pcmFile.exists()){
            throw new RuntimeException(pcmFile.getAbsolutePath()+",there is no pcm file");
        }
        mExecutorService.execute(new PcmToMp3Thread(pcmFile,mp3File,sampleRate,channel));
    }

    class PcmToMp3Thread implements Runnable {
        File pcmFile;
        File mp3File;
        int channel;
        int sampleRate;

        public PcmToMp3Thread(File pcmFile, File mp3File,int sampleRate, int channel) {
            this.pcmFile = pcmFile;
            this.mp3File = mp3File;
            this.sampleRate = sampleRate;
            this.channel = channel;
        }

        @Override
        public void run() {
            if (!mp3File.exists()){
                try {
                    mp3File.createNewFile();
                    lameJni.pcmTomp3(pcmFile.getAbsolutePath(),mp3File.getAbsolutePath(),sampleRate, channel,128);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    class PcmToWavThread implements Runnable {
        File pcmFile;
        File wavFile;

        public PcmToWavThread(File pcmFile, File wavFile) {
            this.pcmFile = pcmFile;
            this.wavFile = wavFile;
        }

        @Override
        public void run() {
//            if (!wavFile.exists()){
//                try {
//                    wavFile.createNewFile();
//                    PcmToWavUtil util=new PcmToWavUtil(mSampleRate,mChannel,mFormat);
//                    util.pcmToWav(pcmFile.getAbsolutePath(),wavFile.getAbsolutePath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
    }
}

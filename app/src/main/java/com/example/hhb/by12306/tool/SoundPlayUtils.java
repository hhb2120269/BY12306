package com.example.hhb.by12306.tool;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.hhb.by12306.R;


/**
 * Created by hhb on 17/7/24.
 */

public class SoundPlayUtils {
    private static final int MAX_STREAMS = 1;     //传入音频数量

    public SoundPool mSoundPlayer;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    private static SoundPlayUtils instance = null;



    public synchronized static SoundPlayUtils getInstance(Context context) {
        if(instance == null){
            instance = new SoundPlayUtils();
            instance.mContext = context;
            //
            SoundPool.Builder spb= new SoundPool.Builder();
            //传入音频数量
            spb.setMaxStreams(MAX_STREAMS);
            //AudioAttributes是一个封装音频各种属性的方法
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适的属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            spb.setAudioAttributes(attrBuilder.build());//默认是media类型，其他的可以看看源码。都有

            instance.setmSoundPlayer(spb.build());
        }
        // 初始化声音
        mContext = context;
        instance.getmSoundPlayer().load(mContext, R.raw.music, 1);//1 在这集添加短音频 修改传入音频数量
//            mSoundPlayer.load(mContext, R.raw.beng, 1);//2
//            mSoundPlayer.load(mContext, R.raw.click, 1);//3
//            mSoundPlayer.load(mContext, R.raw.diang, 1);//4
        return instance;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public void play(int soundID) {
        instance.mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }


    /**getter and setter**/
    public void setmSoundPlayer(SoundPool mSoundPlayer) {
        this.mSoundPlayer = mSoundPlayer;
    }

    public SoundPool getmSoundPlayer() {
        return mSoundPlayer;
    }
}
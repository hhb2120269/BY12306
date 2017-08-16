package com.example.hhb.by12306.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.widget.Button;

/**
 * Created by hhb on 17/6/6.
 */

public class NetworkConnectChangedReceiver extends BroadcastReceiver {


    private Button netStateBtn;
    private String mShowNetInfo;

    private AnimationSet mAnimationSet;

    private Context context;

    private static NetworkConnectChangedReceiver instance = null;
    public synchronized static NetworkConnectChangedReceiver getInstance(Context context) {
        if(instance == null){
            Log.e("getInstance","NetworkConnectChangedReceiver");
            instance = new NetworkConnectChangedReceiver();
            instance.context = context;
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(instance, filter);
        }
        instance.context = context;
        return instance;
    }

    private NetworkConnectChangedReceiver(){
        if(mAnimationSet == null){
            setAnimation();
        }
    }
    private void reCreateReceiver(){
        try{
            context.unregisterReceiver(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(instance, filter);
    }

    public void setNetStateBtn(Button netStateBtn) {
        if(netStateBtn!=null){
            if(this.netStateBtn!=null ){
                this.netStateBtn.clearAnimation();
                this.netStateBtn = null;
            }
        }
        reCreateReceiver();
        this.netStateBtn = netStateBtn;
        this.netStateBtn.startAnimation(mAnimationSet);
        if(checkNet(context)){
            netStateBtn.setVisibility(View.GONE);
            netStateBtn.setBackgroundColor(Color.GREEN);
        }
    }

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "4G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    /**
     * 这里设置网络访问的是否显示
     * @param isConnected
     */
    private void setBtnAppearance(Boolean isConnected){
        if(netStateBtn == null){
            return;
        }
        netStateBtn.setText(mShowNetInfo);
        if(isConnected){
            netStateBtn.clearAnimation();
            netStateBtn.setVisibility(View.GONE);
            netStateBtn.setBackgroundColor(Color.GREEN);
        }else{
            netStateBtn.setVisibility(View.VISIBLE);
            netStateBtn.setBackgroundColor(Color.RED);
            netStateBtn.startAnimation(mAnimationSet);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:{
                    mShowNetInfo = "wifi不可用";
//                    netStateBtn.setText(mShowNetInfo);
                    setBtnAppearance(false);
                }
                break;
                case WifiManager.WIFI_STATE_DISABLING:{
                    mShowNetInfo = "wifi目前被禁用";
//                    netStateBtn.setText(mShowNetInfo);
                    setBtnAppearance(false);
                }
                break;
                case WifiManager.WIFI_STATE_ENABLING:{
                    mShowNetInfo = "wifi正在被启用";
//                    netStateBtn.setText(mShowNetInfo);
                    setBtnAppearance(false);
                }
                    break;
                case WifiManager.WIFI_STATE_ENABLED:{
                    mShowNetInfo = "wifi已启用";
//                    netStateBtn.setText(mShowNetInfo);
                    setBtnAppearance(true);
                }
                    break;
            }

        }
        // 监听wifi的连接状态即是否连上了一个有效无线路由
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                Log.e("TAG", "isConnected:" + isConnected);
                if (isConnected) {
                    mShowNetInfo = "已连接有效wifi...";
//                    netStateBtn.setText(mShowNetInfo);
                    setBtnAppearance(true);
                } else {
                    mShowNetInfo = "wifi网络链接中...";
//                    netStateBtn.setText(mShowNetInfo);
                    setBtnAppearance(false);
                }
            }
        }

        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent
                    .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI
                            || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Log.i("TAG", getConnectionType(info.getType()) + "连上");
                        mShowNetInfo = "网络连接成功";
                        setBtnAppearance(true);
                    }
                } else {
                    Log.i("TAG", getConnectionType(info.getType()) + "断开");
                    mShowNetInfo = "网络连接断开！";
                    setBtnAppearance(false);
                }
            }
        }
    }

    /**
     * 取消actitivy关联
     */
    public void dismiss(){
        if(context != null){
            context.unregisterReceiver(instance);
        }
    }

    /**
     * 设置渐隐动画
     */
    public void setAnimation(){
//        if(netStateBtn == null){
//            Log.d("setAnimation","Error: netStateBtn == null");
//            return;
//        }
        final int transDuration = 2000;
        final int alphaDuration = 1000;
        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setRepeatMode(Animation.RESTART);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 300);
        AlphaAnimation translateAnimation = new AlphaAnimation(0, Float.parseFloat("1.0"));
        translateAnimation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float arg0) {
                float ret = arg0 / (1.0f * transDuration / (transDuration + alphaDuration));
                return ret < 1 ? ret : 1;
            }
        });
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setDuration(transDuration + alphaDuration);
        AlphaAnimation alphaAnimation = new AlphaAnimation(Float.parseFloat("1.0"), 0);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setDuration(alphaDuration);
        alphaAnimation.setStartOffset(transDuration);
        mAnimationSet.addAnimation(translateAnimation);
        mAnimationSet.addAnimation(alphaAnimation);
//        netStateBtn.startAnimation(mAnimationSet);
    }

    /**
     * 清除动画
     */
    public void removeAnimation(){
        netStateBtn.clearAnimation();
        mAnimationSet = null;
    }

    /**
     * 判断Android客户端网络是否连接
     * 只能判断是否有可用的连接，而不能判断是否能连网
     * @param context
     * @return true/false
     */
    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}

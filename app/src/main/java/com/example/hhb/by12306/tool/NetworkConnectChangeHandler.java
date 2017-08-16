package com.example.hhb.by12306.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by hhb on 17/8/2.
 */
public class NetworkConnectChangeHandler extends BroadcastReceiver {
    private Context context;
    private String mNetInfo;

    private static NetworkConnectChangeHandler instance = null;
    public synchronized static NetworkConnectChangeHandler getInstance(Context context) {
        if(instance == null){
            Log.e("getInstance","NetworkConnectChangeHandler");
            instance = new NetworkConnectChangeHandler();
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

    /**
     * onReceive
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:{
                    mNetInfo = "wifi不可用";
                }
                break;
                case WifiManager.WIFI_STATE_DISABLING:{
                    mNetInfo = "wifi目前被禁用";
                }
                break;
                case WifiManager.WIFI_STATE_ENABLING:{
                    mNetInfo = "wifi正在被启用";
                }
                break;
                case WifiManager.WIFI_STATE_ENABLED:{
                    mNetInfo = "wifi已启用";
                    performNetConnectedAction(mNetInfo);// FIXME: 17/8/2 wifi已启用
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
                    mNetInfo = "已连接有效wifi...";// FIXME: 17/8/2 已连接有效wifi...
                    performNetConnectedAction(mNetInfo);
                } else {
                    mNetInfo = "wifi网络链接中...";

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
                        mNetInfo = "网络连接成功";
                        performNetConnectedAction(mNetInfo);// FIXME: 17/8/2 网络连接成功
                    }
                } else {
                    mNetInfo = "网络连接断开！";

                }
            }
        }
    }


    /**  interface **/
    OnNetworkConnectedListener mOnNetworkConnectedListener;

        public interface OnNetworkConnectedListener {

            /**
             * Callback method to http operation
             */
            void onConnected(Object data);
        }
        public void setOnConnectedListener(@Nullable OnNetworkConnectedListener listener) {
            mOnNetworkConnectedListener = listener;
        }

    /**
     *
     * @param data
     * @return
     */
    public boolean performNetConnectedAction(Object data) {
            final boolean result;
            if (mOnNetworkConnectedListener != null) {
    //            view.playSoundEffect(SoundEffectConstants.CLICK);//声音？？
                mOnNetworkConnectedListener.onConnected(data);
                result = true;
            } else {
                result = false;
            }
            return result;
    }
}

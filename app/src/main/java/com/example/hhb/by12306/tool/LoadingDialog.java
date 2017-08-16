package com.example.hhb.by12306.tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hhb.by12306.R;

/**
 * Created by hhb on 17/6/23.
 */

public class LoadingDialog {//还不知道该怎么写
    private ProgressDialog mProgressDialog;
    private Context context;

    private static LoadingDialog instance = null;

    public synchronized static LoadingDialog getInstance(@Nullable Context context) {
        if (instance == null) {
            instance = new LoadingDialog();
        }
        if(context != null){
            instance.setContext(context);
        }
        return instance;
    }

    public LoadingDialog() {
    }

    /** 创建 显示 **/
    public void showPD(String message, String title, Context context) {
        instance.setContext(context);
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(context);
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(R.style.LoadingProgressDialog);
        if(title != null || !title.equals("")){
            mProgressDialog.setTitle(title);
        }
        if(message != null || !message.equals("")){
            mProgressDialog.setMessage(message);
        }

        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("showPD","消失了");
//                Toast.makeText(this, "消失了", Toast.LENGTH_SHORT).show();
            }
        });

        mProgressDialog.show();
        Util.INSTANCE.isOnLoading = true;
    }
    /** 创建 显示 **/
    public void showPD(String message, String title) {
        if(instance.context == null){
            Log.d("showPD","showPD－Error： context ＝ null ！？");
            return;
        }
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(instance.context);
        }
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(R.style.LoadingProgressDialog);
        if(title != null || !title.equals("")){
            mProgressDialog.setTitle(title);
        }
        if(message != null || !message.equals("")){
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("showPD","消失了");
//                Toast.makeText(this, "消失了", Toast.LENGTH_SHORT).show();
            }
        });
        mProgressDialog.show();
        Util.INSTANCE.isOnLoading = true;
    }
    /** 创建 显示 **/
    public void showPD(String message) {
        if(instance.context == null)return;
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(instance.context);
        }
//        mProgressDialog.dismiss();
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(R.style.LoadingProgressDialog);
//        mProgressDialog.setTitle(title);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d("showPD","消失了");
//                Toast.makeText(this, "消失了", Toast.LENGTH_SHORT).show();
            }
        });
        if(message != null || !message.equals("")){
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
        Util.INSTANCE.isOnLoading = true;
    }
    /**
     * 隐藏
     */
    public void hidePD() {
//        mLoadingDialog.cancel();
        if(mProgressDialog == null)return;
        mProgressDialog.dismiss();
        mProgressDialog = null;
        Util.INSTANCE.isOnLoading = false;
    }

    /**
     * setter and getter
     **/
    public void setContext(Context context) {
        this.context = context;
    }

}

package com.example.hhb.by12306.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.hhb.by12306.adapter.TraceListAdapter;
import com.example.hhb.by12306.model.Msg;
import com.example.hhb.by12306.model.ResponseObject;
import com.example.hhb.by12306.model.Task;
import com.example.hhb.by12306.model.Trace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.hhb.by12306.R;
import com.example.hhb.by12306.tool.Constant;
import com.example.hhb.by12306.tool.LoadingDialog;
import com.example.hhb.by12306.tool.NetworkConnectChangedReceiver;
import com.example.hhb.by12306.tool.Soap;
import com.example.hhb.by12306.tool.Util;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by hhb on 17/8/21.
 */

public class MsgListActivity extends AppCompatActivity {

    private PullToRefreshView mPullToRefreshView;// 第三方下拉刷新控件
    private View containerView;// 列表及下拉动画控件的容器
    private LinearLayout mEmptyView;// 空占位图片

    private RecyclerView rvTrace;
    private List<Msg> mMsgList = new ArrayList<>();
    private TraceListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        mEmptyView = (LinearLayout)findViewById(R.id.empty);

        /** 设置toolBar **/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findView();
        initData();

        /** 监听网络状态 */
        setNetworkConnectChangedReceiver();

        /** 设置展位图片**/
        setIconEmpty(mMsgList.size()==0);
    }
    /**
     * handler 子线程刷新UI
     */
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            LoadingDialog.getInstance(null).hidePD();

            // TODO: 17/8/11 stopRefresh();
            mPullToRefreshView.setRefreshing(false);
            switch (msg.what){
                case Constant.LOAD_MSG:
                    if(mMsgList.size() == 0){
                        Toast.makeText(MsgListActivity.this, "当前无计划", Toast.LENGTH_LONG).show();
                        setIconEmpty(true);//emptyImage
                        break;
                    }else{
                        setIconEmpty(false);//emptyImage
                    }
                    try{
                        //刷新数据
                        adapter.setmMsgList(mMsgList);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Constant.SOAP_UNSUCCESS:
                    Toast.makeText(MsgListActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(MsgListActivity.this, "网络访问异常", Toast.LENGTH_LONG).show();
                    break;
            }
        };
    };

    /**
     * 监听网络状态
     */
    public void setNetworkConnectChangedReceiver() {
        NetworkConnectChangedReceiver.getInstance(MsgListActivity.this).setNetStateBtn((Button) findViewById(R.id.network_d));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected", "itemid--->" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("onOptionsItemSelected", "Click setting");
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findView() {
        rvTrace = (RecyclerView) findViewById(R.id.rvTrace);
    }

    private void initData() {
//        网络加载操作
        loadMsg("1");//todo：1小时 从哪里来的？
        adapter = new TraceListAdapter(this, mMsgList);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(adapter);


    }

    /**
     * loadMsg
     */
    private void loadMsg(String hours){
        // 模拟一些假的数据
        /** DEBUG 模式   or   RELEASE 模式 **/
        if (Constant.__IS_FAKE_DATA__) {
            loadListMsg_FakeDate();
        } else {
            loadListUnsignMsg(hours);
        }
    }
    /**
     * 设置empty图片的显示
     * @param flag
     */
    public void setIconEmpty(boolean flag){
        mEmptyView.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    /**
     * fake msg data
     */
    private void loadListMsg_FakeDate(){
        String infos = Util.getJson(this,"taskMsg.json");
        try{
            mMsgList = JSON.parseArray(infos, Msg.class);
            Log.d("json","json:"+mMsgList);
            Message msg = Message.obtain();
            msg.what = Constant.LOAD_TASKS;
            handler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = Message.obtain();
            msg.what = Constant.ERROR;
            msg.obj = "未知错误";
            handler.sendMessage(msg);
            e.printStackTrace();
        }

    }
    /**
     * 加载网络数据 taskList
     */
    private void loadListUnsignMsg(final String hours){

        //loading
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        final String sender = Util.INSTANCE.getUser().getWorkerCode();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseObject planListRes = Soap.getInstance().loadListUnsignMsg(sender,hours);
                    if(planListRes.isSuccess()){
                        mMsgList = JSON.parseArray(planListRes.getObj(),Msg.class);
                        Log.d("loadListMsg","loadListMsg"+mMsgList);
                        Message msg = Message.obtain();
                        msg.what = Constant.LOAD_MSG;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what = Constant.SOAP_UNSUCCESS;
                        msg.obj = planListRes.getMessage();
                        handler.sendMessage(msg);
                    }
                } catch (Exception e){
                    Message msg = Message.obtain();
                    msg.what = Constant.ERROR;
                    msg.obj = "访问出错！";
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

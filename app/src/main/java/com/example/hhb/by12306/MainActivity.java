package com.example.hhb.by12306;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.hhb.by12306.activity.LoginActivity;
import com.example.hhb.by12306.model.Msg;
import com.example.hhb.by12306.model.ResponseObject;
import com.example.hhb.by12306.tool.Constant;
import com.example.hhb.by12306.tool.Soap;
import com.example.hhb.by12306.tool.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    
    private Runnable autoUpdater = null;
    private List<Msg> mMsgList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = new Intent(this, LoginActivity.class);
         startActivityForResult(intent, Constant.LOGIN);//需要实现回调方法

        // TODO: 17/8/16  开启消息
        startAutoLoadMsg();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,""+resultCode);
    }
    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    /**
     * auto 自动加载planlist
     * @return
     */
    public Boolean startAutoLoadMsg(){
        endAutoLoadData();
        if(Util.INSTANCE.getUser().getWorkCode()==null || Util.INSTANCE.getUser().getWorkCode().equals("")){
            Log.d("startHeartBeat","startHeartBeat");
        }else{
            autoUpdater = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this,Constant.AUTO_DELAY);
                    Log.e("startHeartBeat","startHeartBeat");
                    autoloadPlanData();//planlist请求体
//                    doSendHeartBeat();
                }
            };
            handler.postDelayed(autoUpdater,Constant.AUTO_DELAY);//执行
        }

        return true;
    }

    /**
     * 停止自动刷新数据
     * @return
     */
    public Boolean endAutoLoadData(){
        try{
            handler.removeCallbacks(autoUpdater);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    /**
     * handler 子线程刷新UI
     */
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case Constant.SOAP_UNSUCCESS:
                    Toast.makeText(MainActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "网络访问异常", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**
     * 加载网络数据 planList --自动更新（没有loading）
     */
    private void autoloadPlanData(){
        if(Util.INSTANCE.isOnLoading == true){
            Log.d("autoloadPlanData","can't loading----onloading");
            return;
        }
        //loading
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = format.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FIXME: 17/8/16 loadMsg
                    ResponseObject planListRes = Soap.getInstance().loadPlanList(Util.INSTANCE.getUser().getWorkCode(),dateStr);
                    if(planListRes.isSuccess()){
                        String newStr = planListRes.getObj();
                        List<Msg> buf= JSON.parseArray(newStr,Msg.class);
//                        /**比较两个planlist byte长度**/
//                        if(!Util.INSTANCE.compareObjectByteSize(planlist,buf)){ //如果byte长度不相等
//                            SoundPlayUtils.getInstance(PlanListActivity.this).play(1);
//                        }
                        mMsgList = buf;
                        Log.d("loadMsgData","loadMsgData-mMsgList"+mMsgList);
                        Message msg = Message.obtain();
                        msg.what = Constant.LOAD_MSG;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what = Constant.SOAP_UNSUCCESS;
                        msg.obj = planListRes.getMessage();
                        handler.sendMessage(msg);
                    }

                }catch (Exception e){
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

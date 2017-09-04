package com.example.hhb.by12306.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.hhb.by12306.R;
import com.example.hhb.by12306.model.ResponseObject;
import com.example.hhb.by12306.model.Task;
import com.example.hhb.by12306.model.User;
import com.example.hhb.by12306.tool.Constant;
import com.example.hhb.by12306.tool.CustomDialog;
import com.example.hhb.by12306.tool.DBManager;
import com.example.hhb.by12306.tool.LoadingDialog;
import com.example.hhb.by12306.tool.MySQLiteHelper;
import com.example.hhb.by12306.tool.NetworkConnectChangedReceiver;
import com.example.hhb.by12306.tool.Soap;
import com.example.hhb.by12306.tool.Util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hhb on 17/8/22.
 */

public class TaskDetailActivity extends AppCompatActivity {
    private Task mTask;
    private Task mBufferTask;
    private int mIndex;
//    private Button mActionBtn;

    private Button btn_start_action;
    private Button btn_end_action;
    private ImageView state_finish;
    private ImageView state_changed;

    private TextView taskId; // 任务id：车次@日期
    private TextView trainNo; // 任务车次，一个车次一个任务
    private TextView arriveTime; // 本站到达时间
    private TextView leaveTime; // 本站出发时间
    private TextView trackAndPlatform; // 股道
    private TextView arriveLate; // 到达晚点
    private TextView leaveLate; // 出发晚点
    private TextView sendStartTime; // 开始送餐时间
    private TextView sendEndTime; // 开始送餐时间
    //    private TextView sendOverTime; // 送餐完成时间
    private TextView sender; // 送餐员

    private CustomDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        /**  根据intent 获取mTask数据**/
        Intent intent = this.getIntent();
        try {
            Object flag_task = intent.getSerializableExtra("task");
            Object index = intent.getSerializableExtra("index");
            mTask = (Task) flag_task;
            mIndex = (int)index;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("onCreate", "order" + mTask);
        /** 根据数据初始化UI **/
        setContentText();
        /** 设置内容信息 **/
        setContentTextData();

        /** DEBUG 模式   or   RELEASE 模式 **/
//        if (Constant.__IS_FAKE_DATA__) {
//            loadFakeDate_getSigned();
//        } else {
//            loadGetSign(mTask.getOrderCode());
//        }
        /** 设置toolBar **/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /** 监听网络状态 */
        setNetworkConnectChangedReceiver();

//        test
        arriveLate.setOnClickListener(gotoMsg);
        leaveLate.setOnClickListener(gotoMsg);
    }

    /**
     * 前往msg
     */
    private View.OnClickListener gotoMsg = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(TaskDetailActivity.this, TraceActivity.class);
            Bundle bundle = new Bundle();
//                bundle.putSerializable("tasklist", mTaskList);
            bundle.putSerializable("task", mTask);
            //                Intent.putExtras(, (Serializable))
            intent.putExtras(bundle);
            startActivityForResult(intent, Constant.LOAD_MSG);//需要实现回调方法
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OrderSignActivity", "onResume");
        setNetworkConnectChangedReceiver();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOAD_MSG){
            Log.d("onActivityResult","LOAD_MSG");
        }
    }

    /**
     * 初始化 ContentView
     */
    private void setViewContent() {

    }

    /**
     * 设置界面显示 mTask
     */
    private void setContentText() {

        this.state_finish = (ImageView) findViewById(R.id.state_finish_image);
        this.state_changed = (ImageView) findViewById(R.id.state_changed_image);

        this.btn_start_action = (Button) findViewById(R.id.btn_start_action);
        this.btn_end_action = (Button) findViewById(R.id.btn_end_action);
        this.taskId = (TextView) findViewById(R.id.taskId);
        this.trainNo = (TextView) findViewById(R.id.trainNo);
        this.arriveTime = (TextView) findViewById(R.id.arriveTime);
        this.leaveTime = (TextView) findViewById(R.id.leaveTime);
        this.trackAndPlatform = (TextView) findViewById(R.id.trackAndPlatform);
        this.arriveLate = (TextView) findViewById(R.id.arriveLate);
        this.leaveLate = (TextView) findViewById(R.id.leaveLate);
        this.sendStartTime = (TextView) findViewById(R.id.sendStartTime);
        this.sendEndTime = (TextView) findViewById(R.id.sendEndTime);
//      this.  sendOverTime= (TextView) findViewById(sendOverTime);
        this.sender = (TextView) findViewById(R.id.sender);

    }

    /**
     * 根据数据加载页面信息
     * @return
     */
    private boolean setContentTextData() {
        if (mTask == null) {
            Log.d("setContentText", "setContentText--error");
            return false;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formHMS = new SimpleDateFormat("HH:mm:ss");

        try{
            this.taskId.setText("送餐编号：" + mTask.getTaskId());
            this.trainNo.setText(mTask.getTrainNo());
            this.arriveTime.setText("到：" + formHMS.format(mTask.getArriveTime()));
            this.leaveTime.setText("发：" + formHMS.format(mTask.getLeaveTime()));
            this.trackAndPlatform.setText(mTask.getTrack() + "股道" + mTask.getPlatform() + "站台");
            this.arriveLate.setText("到正晚点：" + mTask.getArriveLate()+"分钟");
            this.leaveLate.setText("发正晚点：" +mTask.getLeaveLate()+"分钟");
            this.sender.setText("送餐员："+mTask.getSender());


            // FIXME:
            if(mTask.getSendStartTime() == null && mTask.getSendOverTime() == null){//未开始
                this.state_finish.setVisibility(View.GONE);
                this.btn_start_action.setVisibility(View.VISIBLE);
                this.btn_end_action.setVisibility(View.GONE);
            }
            if(mTask.getSendStartTime() != null && mTask.getSendOverTime() == null){//已开始未结束
                this.state_finish.setVisibility(View.GONE);
                this.btn_start_action.setVisibility(View.GONE);
                this.btn_end_action.setVisibility(View.VISIBLE);
            }
            if(mTask.getSendStartTime() != null && mTask.getSendOverTime() != null){//已结束
                this.state_finish.setVisibility(View.GONE);
                this.btn_start_action.setVisibility(View.GONE);
                this.btn_end_action.setVisibility(View.GONE);
            }
            // TODO: 17/8/25 判断是否显示已变更
            if(mTask.ischanged()){
                this.state_changed.setVisibility(View.VISIBLE);
            }else{
                this.state_changed.setVisibility(View.GONE);
            }



            String start = mTask.getSendStartTime()==null? "": formHMS.format(mTask.getSendStartTime());
            String end = mTask.getSendOverTime()==null? "": formHMS.format(mTask.getSendOverTime());
            this.sendStartTime.setText("开始送餐：" + start);
            this.sendEndTime.setText("结束送餐：" + end);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        设置点击开始任务
        this.btn_start_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogBuilder = new CustomDialog.Builder(TaskDetailActivity.this);
                mDialogBuilder.setMessage("确认开始执行任务？");
                mDialogBuilder.setTitle("提示");
                mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        loadBeginTask(mTask.getTaskId());
                    }
                });

                mDialogBuilder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                mDialogBuilder.create().show();

            }
        });

//        设置点击开始任务
        this.btn_end_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogBuilder = new CustomDialog.Builder(TaskDetailActivity.this);
                mDialogBuilder.setMessage("确认结束执行任务？");
                mDialogBuilder.setTitle("提示");
                mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        loadFinishTask(mTask.getTaskId());
                    }
                });

                mDialogBuilder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                mDialogBuilder.create().show();
            }
        });


        return true;
    }


    /**
     * toobar 点击回调方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("onOptionsItemSelected", "itemid--->" + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d("onOptionsItemSelected", "Click setting");
                break;
            case android.R.id.home:
//                setResult(Constant.LOAD_TASK_DETAIL, new Intent());
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", mTask);
                bundle.putSerializable("index", mIndex);
                intent.putExtras(bundle);
                setResult(Constant.LOAD_TASK_DETAIL, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 监听网络状态
     */
    public void setNetworkConnectChangedReceiver() {
        NetworkConnectChangedReceiver.getInstance(TaskDetailActivity.this).setNetStateBtn((Button) findViewById(R.id.network_d));
    }

    /**
     * handler 子线程刷新UI
     */
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            LoadingDialog.getInstance(null).hidePD();
            switch (msg.what) {
                case Constant.START_TASK:
                    mTask.setSendStartTime(new Timestamp(System.currentTimeMillis()));
                    setContentTextData();
                    break;
                case Constant.END_TASK:
                    mTask.setSendOverTime(new Timestamp(System.currentTimeMillis()));
                    setContentTextData();
                    break;
                case Constant.ERROR:
                    Toast.makeText(TaskDetailActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case Constant.SOAP_UNSUCCESS:
                    Toast.makeText(TaskDetailActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(TaskDetailActivity.this, "error", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**
     * 开始任务 BeginTask
     */
    private void loadBeginTask(final String taskId){
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = format.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseObject startRes = Soap.getInstance().loadBeginTask(dateStr,""+Util.INSTANCE.getUser().getId(),taskId);
                    if(startRes.isSuccess()){
                        mBufferTask = JSON.parseObject(startRes.getObj(),Task.class);
//                        if(planlist.size() != 0){
//                            SoundPlayUtils.getInstance()
//                        }
                        Log.d("loadTaskStartData","loadTaskStartData-mTask"+mBufferTask);
                        Message msg = Message.obtain();
                        msg.what = Constant.START_TASK;
//                        msg.obj = index;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what = Constant.SOAP_UNSUCCESS;
                        msg.obj = startRes.getMessage();
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

    /**
     * 结束任务
     */
    private void loadFinishTask(final String taskId){
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = format.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseObject endRes = Soap.getInstance().loadFinishTask(dateStr,""+Util.INSTANCE.getUser().getId(),taskId);
                    if(endRes.isSuccess()){
                        mBufferTask = JSON.parseObject(endRes.getObj(),Task.class);
                        Log.d("loadEndTaskData","loadEndTaskData-mBufferTask"+mBufferTask);
                        Message msg = Message.obtain();
                        msg.what = Constant.END_TASK;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what = Constant.SOAP_UNSUCCESS;
                        msg.obj = endRes.getMessage();
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

    /**
     * todo:存储OrderToSign到数据库，落地处理
     */
    private void readySQLToReSign(final String workCode,
                                  final String workName,
                                  final String orderCode,
                                  final String planDate,
                                  final String code,
                                  final String planType
    ) {
//        MySQLiteHelper dbhelper = DBManager.getInstance(this);
//        OrderToSign waitToStoreIn = new OrderToSign(workCode, workName, orderCode, planDate, code, planType);
//        List<OrderToSign> toSignList = dbhelper.queryOrderToSign();//从数据库中取出数据
//
//        int length = toSignList.size();
//        if(length == 0){
//            dbhelper.saveOrderNeedSign(waitToStoreIn);//存储到数据库
//        }
//        for(int i = 0; i < length; i++)
//        {
//            OrderToSign orderTosign = toSignList.get(i);
//            if(orderCode.equals(orderTosign.getOrderCode())){//如果有相同的数据,返回，防止重复添加
//                return;
//            }else{
//                dbhelper.saveOrderNeedSign(waitToStoreIn);//存储到数据库
//            }
//        }
    }


    /**
     * 重新加载命令
     * Exp:
     * refreshOrder(
     * user.getWorkCode(),
     * ""+ mPlan.getCode(),
     * mPlan.getPlanDate(),
     * ""+mPlan.getPlanType(),
     * mPlan.getDoSection());
     */
    private void refreshOrder(final String workCode,
                              final String code,
                              final String rq,
                              final String planType,
                              final String doSection) {
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    ResponseObject orderlist = Soap.getInstance().loadOrder(workCode,code, rq, planType, doSection);
//                    ResponseObject orderlist = Soap.getInstance().loadOrder("11","319522", "20170612", "0", "上海");
//                    if(orderlist.isSuccess()){
//                        Message msg = Message.obtain();
//                        msg.what = Constant.LOAD_ORDER;
//                        msg.obj = orderlist;
//                        handler.sendMessage(msg);
//                    }else{
//                        Toast.makeText(TaskDetailActivity.this, orderlist.getMessage(),Toast.LENGTH_LONG ).show();
//                    }

                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = Constant.ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 假签收数据
     */
    private void loadFakeDate_getSigned() {
        if (mTask.getSendOverTime() != null) {
            String infos = Util.getJson(this, "order_signed_list.json");
            try {
//                mSignedList = JSON.parseArray(infos, Signed.class);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //刷新界面
            setContentText();
        }

    }
}

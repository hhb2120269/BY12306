package com.example.hhb.by12306.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by hhb on 17/8/22.
 */

public class TaskDetailActivity extends BaseActivity {
    private Task mTask;
//    private Button mActionBtn;
    public Button btn_start_action;
    public Button btn_end_action;
    private ContentHolder contentHolder;

    private class ContentHolder{




        public LinearLayout btn_action_layout;
    }

    private CustomDialog.Builder mDialogBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        btn_start_action = (Button) findViewById(R.id.btn_start_action);
        btn_end_action = (Button) findViewById(R.id.btn_end_action);

        /**  根据intent 获取order数据**/
        Intent intent = this.getIntent();
        try {
            Object flag_order = intent.getSerializableExtra("task");
//            Object flag_plan = intent.getSerializableExtra("plan");
            mTask = (Task) flag_order;
//            mPlan = (Plan) flag_plan;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("onCreate", "order" + mTask);

        /** 根据数据初始化UI **/
        setContentHolder();
        setContentText();
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


        btn_start_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTask.getSendOverTime() != null) {
                    Toast.makeText(TaskDetailActivity.this, "开始",Toast.LENGTH_SHORT ).show();
                } else {
                    //设置并显示 弹框 － 签令
                    showDialogForSignOrder(mTask);
                }
            }
        });
        /** 监听网络状态 */
        setNetworkConnectChangedReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("OrderSignActivity","onResume");
        setNetworkConnectChangedReceiver();

    }

    /**
     * 初始化 ContentView
     */
    private void  setContentHolder(){
        if(contentHolder == null){
            contentHolder = new ContentHolder();
        }
//        contentHolder.orderCodeFormer = (TextView)findViewById(R.id.orderCodeFormer);
//        contentHolder.code = (TextView)findViewById(R.id.code);
//        contentHolder.doSection = (TextView)findViewById(R.id.doSection);
//        contentHolder.orderPoint = (TextView)findViewById(R.id.orderPoint);
//        contentHolder.signUnit = (TextView)findViewById(R.id.signUnit);
//        contentHolder.linear_doSection = (LinearLayout)findViewById(R.id.linear_doSection);
//        contentHolder.linear_orderPoint = (LinearLayout)findViewById(R.id.linear_orderPoint);
//        contentHolder.linear_signUnit = (LinearLayout)findViewById(R.id.linear_signUnit);
//        contentHolder.sendTime = (TextView)findViewById(R.id.sendTime);
//        contentHolder.orderContentStr = (TextView)findViewById(R.id.orderContentStr);
//        contentHolder.linear_signedUser = (LinearLayout) findViewById(R.id.linear_signedUser);
    }

    /**
     * 设置界面显示 required (Order) mTask
     */
    private void setContentText(){
        if(mTask == null){
            Log.d("setContentText","setContentText--error");
            return;
        }
//        contentHolder.orderCodeFormer.setText("命令编号："+mTask.getOrderCodeFormer());
//        contentHolder.sendTime.setText(mTask.getSendTime());
//        contentHolder.orderContentStr.setText(mTask.getOrderContentStr());
//        contentHolder.code.setText(""+mTask.getCode());
//        contentHolder.doSection.setText(mTask.getDoSection());
//        contentHolder.orderPoint.setText(mTask.getOrderPoint());
//        contentHolder.signUnit.setText(mTask.getSignUnit());

        //设置显示项
//        contentHolder.linear_doSection.setVisibility(View.GONE);
//        contentHolder.linear_orderPoint.setVisibility(View.GONE);
//        contentHolder.linear_signUnit.setVisibility(View.GONE);
//
//        contentHolder.linear_signedUser.setVisibility(View.VISIBLE);
//        addSignedUser(contentHolder.linear_signedUser);
//        if(mTask.getSign()){
//            mActionBtn.setVisibility(View.GONE);
//        }else{
//            Log.d("setContentText","setContentText--none");
//            mActionBtn.setVisibility(View.VISIBLE);
//            return;
//        }


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
//                setResult(Constant.SIGN_ORDER, new Intent());
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 监听网络状态
     */
    public void setNetworkConnectChangedReceiver() {
        NetworkConnectChangedReceiver.getInstance(TaskDetailActivity.this).setNetStateBtn((Button)findViewById(R.id.network_d));
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

                    break;
                case Constant.END_TASK:
//                    ResponseObject responseObject = (ResponseObject) msg.obj;
//                    Toast.makeText(TaskDetailActivity.this, responseObject.getMessage(), Toast.LENGTH_LONG).show();
//                    // TODO: 17/6/26 Intent的参数
//                    Intent intent = getIntent();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("name", "signSuccess");
//                    bundle.putSerializable("task", mTask);
//                    //Intent.putExtras(, (Serializable))
//                    intent.putExtras(bundle);
//
//                    setResult(Constant.SIGN_ORDER, intent);
//                    finish();
                    break;
                case Constant.ERROR:
                    Toast.makeText(TaskDetailActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case Constant.SOAP_UNSUCCESS:
                    Toast.makeText(TaskDetailActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(TaskDetailActivity.this, "error", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**
     * 加载 签收命令
     */
//    private void loadSignOrder(final String workCode,
//                               final String workName,
//                               final String orderCode,
//                               final String planDate,
//                               final String code,
//                               final String planType
//    ) {
//        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ResponseObject signOrder = Soap.getInstance().loadToStartTask(workCode, workName, orderCode, planDate,code,planType);
//                    Log.d("signOrder", "loadsignOrder-signOrder" + signOrder);
//                    if(signOrder.isSuccess()){
//                        Message msg = Message.obtain();
//                        msg.what = Constant.SIGN_ORDER;
//                        msg.obj = signOrder;
//                        handler.sendMessage(msg);
//                    }else{
//                        Message msg = Message.obtain();
//                        msg.what = Constant.SOAP_UNSUCCESS;
//                        msg.obj = signOrder.getMessage();
////                        msg.obj = signOrder.getMessage()+"，重连后自动提交";
//                        handler.sendMessage(msg);
//                        readySQLToReSign(workCode, workName, orderCode, planDate, code, planType);//落地处理
//                    }
//
//                } catch (Exception e){
//                    Message msg = Message.obtain();
//                    msg.what = Constant.ERROR;
//                    msg.obj = "访问出错！";
////                    msg.obj = "访问出错！重连后自动提交";
//                    handler.sendMessage(msg);
//                    readySQLToReSign(workCode, workName, orderCode, planDate, code, planType);//落地处理
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

    /**
     * 存储OrderToSign到数据库，落地处理
     */
    private void readySQLToReSign(final String workCode,
                                  final String workName,
                                  final String orderCode,
                                  final String planDate,
                                  final String code,
                                  final String planType
    ){
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
     * 确认签收命令 －弹框
     */
    private void showDialogForSignOrder(final Task order) {
        mDialogBuilder = new CustomDialog.Builder(this);
        mDialogBuilder.setMessage("确认签收命令？");
        mDialogBuilder.setTitle("提示");
        mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                User user = Util.INSTANCE.getUser();
                //设置你的操作事项
//                loadSignOrder(user.getWorkCode(), user.getWorkName(), order.getOrderCode(), mPlan.getPlanDate(),""+mPlan.getCode(),""+mPlan.getPlanType());
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

    /**
     * 重新加载命令
     * Exp:
     * refreshOrder(
     user.getWorkCode(),
     ""+ mPlan.getCode(),
     mPlan.getPlanDate(),
     ""+mPlan.getPlanType(),
     mPlan.getDoSection());
     */
    private void refreshOrder(final String workCode,
                              final String code,
                              final String rq,
                              final String planType,
                              final String doSection){
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

                }catch (Exception e){
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
    private void loadFakeDate_getSigned(){
        if(mTask.getSendOverTime() !=null){
            String infos = Util.getJson(this,"order_signed_list.json");
            try{
//                mSignedList = JSON.parseArray(infos, Signed.class);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //刷新界面
            setContentText();
        }

    }
}

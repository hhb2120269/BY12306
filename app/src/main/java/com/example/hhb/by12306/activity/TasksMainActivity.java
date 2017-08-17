package com.example.hhb.by12306.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.alibaba.fastjson.JSON;
import com.example.hhb.by12306.R;
import com.example.hhb.by12306.adapter.TasksListAdapter;
import com.example.hhb.by12306.model.Task;
import com.example.hhb.by12306.tool.Constant;
import com.example.hhb.by12306.tool.CustomDialog;
import com.example.hhb.by12306.tool.LoadingDialog;
import com.example.hhb.by12306.tool.NetworkConnectChangedReceiver;
import com.example.hhb.by12306.tool.Util;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

/**
 * Created by hhb on 17/8/9.
 */

public class TasksMainActivity extends BaseActivity {
    private static final String TAG = "TasksMainActivity";

    private PullToRefreshView mPullToRefreshView;// 第三方下拉刷新控件
    private View containerView;// 列表及下拉动画控件的容器
    private Bitmap bitmap;// 左侧下拉菜单切换时的截屏
    private LinearLayout mEmptyView;// 空占位图片
    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;

    private ActionBarDrawerToggle drawerToggle;
    private ViewAnimator viewAnimator;

    private List<Task> mTaskList;
    private Task mTask;
//    private List<Task> mTaskList;
    private ListView listView;
    private TasksListAdapter mTaskListAdapter;


//    private TasksDialog.Builder mTDBuilder;//任务弹窗
    
    private CustomDialog.Builder mDialogBuilder;//选择框 弹窗

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // TODO: 17/7/27 安卓机型不一样 展示也不一样
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (LinearLayout)findViewById(R.id.empty);
        mTaskListAdapter = new TasksListAdapter(this, 0 , mTaskList);
        mTaskListAdapter.setOnItemClickListener(new TasksListAdapter.OnCellSelectedListener() {
            @Override
            public void onCellSelect(AdapterView<?> parent, View view, int position, Object data) {
                // TODO: 17/7/25
            }

            @Override
            public void onButtonSelect(View view, int position, Object data, int whichOne) {
                Log.d("onButtonSelect","onButtonSelect");

            }
        });
//        mPlanListAdapter.notifyDataSetChanged();
        listView.setAdapter(mTaskListAdapter);

        loadTasksData();

        /** 下拉刷新 **/
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTasksData();
            }
        });
        /** 设置网络状态监听器 **/
        setNetworkConnectChangedReceiver();
    }

//    @Override
//    /**
//     * 显示／刷新 任务列表dialog
//     * @param tasklist
//     */
//    private void showTaskListDialog(Task order,List<Task> tasklist) {
//        if(mTDBuilder == null){
//            /** 创建orderListDialog **/
//            mTDBuilder = new TasksDialog.Builder(this);
//            /** required!! 设置数据源 **/
//            mTDBuilder.setParams(order,tasklist);
//            mTDBuilder.setNegativeButtonClickListener(
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            mTDBuilder.setmOnBtnSelectedListener(new TasksDialog.Builder.OnBtnSelectedListener() {
//                /** dialog list 点击adapter内部btn 跳转orderSign **/
//                @Override
//                public void onBtnSelect(Task order, Object data, String whichOne) {
//                    Task task = (Task) data;
//                    // TODO: 17/8/16  TaskSignActivity
////                    Intent intent = new Intent(TasksMainActivity.this, TaskSignActivity.class);
////                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("task", task);
////                    bundle.putSerializable("order", order);
////                    intent.putExtras(bundle);
////                    startActivityForResult(intent,Constant.SIGN_TASK);//需要实现回调方法
//                }
//            });
//            /** 显示dialgo **/
//            mTDBuilder.create().show();
//        }else{
//            /** 刷新orderlistDialog **/
//            mTDBuilder.refreshTaskList(tasklist);
//
//            if(!mTDBuilder.isShowing()){
//                /** 显示dialgo **/
//                mTDBuilder.create().show();
//            }
//
//        }
//
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        //通过代码的方式来添加Menu
        //添加菜单项（组ID，菜单项ID，排序，标题）
        menu.add(0, 1, Constant.EXIT_LOGOUT, "退出登录");
//        menu.
//        menu.add(0, 2, 200, "Over");
        //添加子菜单
//        SubMenu sub1 = menu.addSubMenu("setting");
//        sub1.add(1, 3, 300, "声音设置");
//        sub1.add(1, 4, 400, "背景设置");
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult:"+resultCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        endAutoLoadData();//停止自动刷新数据
        Log.d(TAG,"onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新数据
        if (Constant.__IS_FAKE_DATA__) {
            //假数据
//            getFakeData();//加载planlist
        } else {
//            startAutoLoadData();//延时自动更新数据
        }
        Log.d(TAG,"onResume");
        setNetworkConnectChangedReceiver();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //刷新数据
        if (Constant.__IS_FAKE_DATA__) {
            //假数据
//            getFakeData();//加载planlist
        } else {
//            startAutoLoadData();//延时自动更新数据
        }
        Log.d(TAG,"onResume");
        setNetworkConnectChangedReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        endAutoLoadData();//停止自动刷新数据
        Log.d(TAG,"onStop");
    }

    /**
     * toobar 点击回调方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (drawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        switch (item.getOrder()){
                    case Constant.EXIT_LOGOUT:
                        Log.d(TAG,"action_settings:logout");
                        mDialogBuilder = new CustomDialog.Builder(this);
                        mDialogBuilder.setMessage(getResources().getString(R.string.logout_message));
                        mDialogBuilder.setTitle(getResources().getString(R.string.logout_title));
                        mDialogBuilder.setPositiveButton(getResources().getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //退出登录
                                Util.INSTANCE.setUser(null);//清空user
                                Intent intent = getIntent();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("value", "logout");
                                intent.putExtras(bundle);
                                setResult(Constant.LOAD_ORDERS,intent);
                                finish();
                            }
                        });

                        mDialogBuilder.setNegativeButton(getResources().getString(R.string.action_no),
                                new android.content.DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        mDialogBuilder.create().show();
                        break;
                    default:
                        return super.onOptionsItemSelected(item);
                }
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG,"action_settings:logout");
                mDialogBuilder = new CustomDialog.Builder(this);
                mDialogBuilder.setMessage(getResources().getString(R.string.logout_message));
                mDialogBuilder.setTitle(getResources().getString(R.string.logout_title));
                mDialogBuilder.setPositiveButton(getResources().getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //退出登录
                        Util.INSTANCE.setUser(null);//清空user
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("value", "logout");
                        intent.putExtras(bundle);
                        setResult(Constant.LOAD_ORDERS,intent);
                        finish();
                    }
                });

                mDialogBuilder.setNegativeButton(getResources().getString(R.string.action_no),
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                mDialogBuilder.create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * handler 子线程刷新UI
     */
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            LoadingDialog.getInstance(null).hidePD();
            // TODO: 17/8/11 stopRefresh();
            switch (msg.what){
                case Constant.LOAD_TASKS:
                    if(mTaskList.size() == 0){
                        Toast.makeText(TasksMainActivity.this, "当前无计划", Toast.LENGTH_LONG).show();
                        setIconEmpty(true);//emptyImage
                        break;
                    }else{
                        setIconEmpty(false);//emptyImage
                    }
                    try{
                        switchFilter(Constant.DEFAULT);//planlist fllter  &  刷新UI
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Constant.SOAP_UNSUCCESS:
                    Toast.makeText(TasksMainActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(TasksMainActivity.this, "网络访问异常", Toast.LENGTH_LONG).show();
                    break;
            }
        };
    };

    /**
     * 监听网络状态
     */
    public void setNetworkConnectChangedReceiver(){
        NetworkConnectChangedReceiver.getInstance(TasksMainActivity.this).setNetStateBtn((Button)findViewById(R.id.network_d));
    }


    /**
     * 加载数据
     */
    private void loadTasksData(){
        loadFakeTasks();
        // TODO: 17/8/11
//        if (Constant.__IS_FAKE_DATA__) {
//            loadFakeTask();
//        } else {
//            //获取网络 命令数据 刷新
//            User user = Util.INSTANCE.getUser();
//            //加载order
//            loadTask(plan);
        }

    /**
     * 筛选当前tasklist
     * @param name
     */
    private void switchFilter(int name){
        switch (name) {
            case Constant.CLOSE:
                break;
            case Constant.DEFAULT:{
                setTasklistFilter_All();
            }
            break;
            case Constant.FINISHED:{
// TODO: 17/8/11  setPlanlistFilter_Done();
            }
            break;
            case Constant.UNFINISHED:{
// TODO: 17/8/11  setPlanlistFilter_Doing();
            }
            break;
            default:
                break;
        }
    }
    /** 筛选方法，返回newPlanList  **/
    private List<Task> setTasklistFilter_All(){
        //替换list 并刷新UI
        mTaskListAdapter.setmTaskList(mTaskList);
        mTaskListAdapter.notifyDataSetChanged();
        if(mPullToRefreshView != null){
            mPullToRefreshView.setRefreshing(false);
        }
        return mTaskList;
    }
    /**
     *  获取假数据 orderlist
     */
    private void loadFakeOrders() {

        String infos = Util.getJson(this,"order_list.json");
        try{
            mTaskList = JSON.parseArray(infos, Task.class);
            Log.d("json","json:"+mTaskList);
            Message msg = Message.obtain();
            msg.what = Constant.LOAD_ORDERS;
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
     *  获取假数据 tasklist
     */
    private void loadFakeTasks() {

        String infos = Util.getJson(this,"task_list.json");
        try{
            mTaskList = JSON.parseArray(infos, Task.class);
            Log.d("json","json:"+mTaskList);
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
     * 停止刷新动画
     */
    public void stopRefresh(){
        if(mPullToRefreshView!=null){
            mPullToRefreshView.setRefreshing(false);
        }
    }

    /**
     * 设置empty图片的显示
     * @param flag
     */
    public void setIconEmpty(boolean flag){
        mEmptyView.setVisibility(flag?View.VISIBLE:View.GONE);
    }



}

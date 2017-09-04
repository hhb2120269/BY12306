package com.example.hhb.by12306.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.example.hhb.by12306.R;
import com.example.hhb.by12306.adapter.TasksListAdapter;
import com.example.hhb.by12306.model.ResponseObject;
import com.example.hhb.by12306.model.Task;
import com.example.hhb.by12306.model.User;
import com.example.hhb.by12306.tool.Constant;
import com.example.hhb.by12306.tool.CustomDialog;
import com.example.hhb.by12306.tool.LoadingDialog;
import com.example.hhb.by12306.tool.NetworkConnectChangedReceiver;
import com.example.hhb.by12306.tool.Soap;
import com.example.hhb.by12306.tool.SoundPlayUtils;
import com.example.hhb.by12306.tool.Util;
import com.yalantis.phoenix.PullToRefreshView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import io.codetail.animation.ViewAnimationUtils;
import com.example.hhb.by12306.tool.sidemenu.interfaces.Resourceble;
import com.example.hhb.by12306.tool.sidemenu.interfaces.ScreenShotable;
import com.example.hhb.by12306.tool.sidemenu.model.SlideMenuItem;
import com.example.hhb.by12306.tool.sidemenu.util.ViewAnimator;

/**
 * Created by hhb on 17/8/9.
 */

public class TasksMainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener{
    private static final String TAG = "TasksMainActivity";

    private PullToRefreshView mPullToRefreshView;// 第三方下拉刷新控件
    private View containerView;// 列表及下拉动画控件的容器
    private Bitmap bitmap;// 左侧下拉菜单切换时的截屏
    private LinearLayout mEmptyView;// 空占位图片
    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;

    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
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
        setContentView(R.layout.activity_tasks);

        /** view 显示**/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        setBarAndLeftFilter();
        viewAnimator = new ViewAnimator<>(this, list, drawerLayout, this);

        listView = (ListView) findViewById(R.id.list_view);
        mEmptyView = (LinearLayout)findViewById(R.id.empty);
        mTaskListAdapter = new TasksListAdapter(this, listView , mTaskList);
        mTaskListAdapter.setOnItemClickListener(new TasksListAdapter.OnCellSelectedListener() {
            @Override
            public void onCellSelect( View view, int position, Object data) {
                // TODO: 17/7/25
                try{

                    Task task = mTaskList.get(position);
                    Intent intent = new Intent(TasksMainActivity.this, TaskDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("task", task);
                    bundle.putSerializable("index", position);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constant.LOAD_TASK_DETAIL);//需要实现回调方法
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onButtonSelect(View view, int position, Object data, int whichOne) {
                Log.d("onButtonSelect","onButtonSelect");
                switch (whichOne){
                    case 1:
                        // TODO: 17/8/17 开始作业
                        startTaskSending(position);
                        break;
                    case 2:
                        // TODO: 17/8/17 结束作业
                        endTaskSending(position);
                        break;
                    default:
                        break;
                }

            }
        });
//        mPlanListAdapter.notifyDataSetChanged();
        listView.setAdapter(mTaskListAdapter);

        //获取数据
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

    /**
     * 回退键方法
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            logout();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    /**
     * 侧滑栏点击事件
     * @param slideMenuItem
     * @param position
     * @return
     */
    @Override
    public void onSwitch(Resourceble slideMenuItem, int position) {
        this.switchFilter(slideMenuItem.getName());
    }
    /**
     * 筛选当前tasklist
     * @param name
     */
    private void switchFilter(String name){
        switch (name) {
            case Constant.CLOSE:
                setTasklistFilter_All_Undo();
                break;
            case Constant.DEFAULT:{
                // TODO: 17/6/7
                setTasklistFilter_All();
            }
            break;
            case Constant.STANDBY:{
                // TODO: 17/6/7
                setTasklistFilter_stanby();
            }
            break;
            case Constant.FINISHED:{
                // TODO: 17/6/7
                setTasklistFilter_start();
            }
            break;
            case Constant.UNFINISHED:{
                // TODO: 17/6/7
                setTasklistFilter_end();
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
    /** 筛选方法，返回newPlanList  **/
    private List<Task> setTasklistFilter_All_Undo(){
        //替换list 并刷新UI
        List<Task> newList = new ArrayList<Task>();

        for(int i = 0; i < mTaskList.size(); i++)
        {
            Task task = (Task) mTaskList.get(i);
            if(task.getSendStartTime() == null){
                newList.add(task);
            }else{
                if(task.getSendOverTime()==null){
                    newList.add(task);
                }
            }
        }
        mTaskListAdapter.setmTaskList(newList);
        mTaskListAdapter.notifyDataSetChanged();
        mPullToRefreshView.setRefreshing(false);
        return newList;
    }
    /** 筛选方法，返回newPlanList  **/
    private List<Task> setTasklistFilter_stanby(){
        //替换list 并刷新UI
        List<Task> newList = new ArrayList<Task>();

        for(int i = 0; i < mTaskList.size(); i++)
        {
            Task task = (Task) mTaskList.get(i);
            if(task.getSendStartTime() == null && task.getSendOverTime()==null){
                newList.add(task);
            }
        }
        mTaskListAdapter.setmTaskList(newList);
        mTaskListAdapter.notifyDataSetChanged();
        mPullToRefreshView.setRefreshing(false);
        return newList;
    }
    /** 筛选方法，返回newPlanList  **/
    private List<Task> setTasklistFilter_start(){
        //替换list 并刷新UI
        List<Task> newList = new ArrayList<Task>();

        for(int i = 0; i < mTaskList.size(); i++)
        {
            Task task = (Task) mTaskList.get(i);
            if(task.getSendStartTime() != null && task.getSendOverTime()==null){
                newList.add(task);
            }
        }
        mTaskListAdapter.setmTaskList(newList);
        mTaskListAdapter.notifyDataSetChanged();
        mPullToRefreshView.setRefreshing(false);
        return newList;
    }
    /** 筛选方法，返回newPlanList  **/
    private List<Task> setTasklistFilter_end(){
        //替换list 并刷新UI
        //替换list 并刷新UI
        List<Task> newList = new ArrayList<Task>();

        for(int i = 0; i < mTaskList.size(); i++)
        {
            Task task = (Task) mTaskList.get(i);
            if(task.getSendStartTime() != null && task.getSendOverTime()!=null){
                newList.add(task);
            }
        }
        mTaskListAdapter.setmTaskList(newList);
        mTaskListAdapter.notifyDataSetChanged();
        mPullToRefreshView.setRefreshing(false);
        return newList;
    }
    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        return screenShotable;
    }

    /**
     * 添加控制menuList 控制item显示的方法（失效）
     * @param slideMenuItemList List<SlideMenuItem>
     * @param position int index
     * @return
     */
    @Override
    public void onSelectInList(List slideMenuItemList, int position) {
        /*** 失效 **/
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }
    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
    /**
     * 设置toolbar  和拓展栏
     * @param
     */
    private void setBarAndLeftFilter(){
        /**menu**/
        SlideMenuItem menuItem0 = new SlideMenuItem(Constant.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(Constant.DEFAULT, R.drawable.icon_default_default);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(Constant.UNFINISHED, R.drawable.icon_finished_default);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(Constant.FINISHED, R.drawable.icon_unfinished_default);
        list.add(menuItem3);
        /**toolbar**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // TODO: 17/7/27 安卓机型不一样 展示也不一样
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /** menu adction**/

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
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
                        switchFilter(Constant.CLOSE);//planlist fllter  &  刷新UI
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case Constant.START_TASK:
                    // FIXME: 17/9/1
                    int startIndex = (int)msg.obj;
                    Task tagTaskStart = mTaskList.get(startIndex);
//                    tagTaskStart.setSendStartTime(mTask.getSendStartTime());//更新任务开始时间
                    tagTaskStart.setSendStartTime(new Timestamp(System.currentTimeMillis()));//更新任务开始时间
                    // TODO: 17/9/1  调用当前筛选条件，而不是写死
                    mTaskListAdapter.updateItem(startIndex);
//                    setTasklistFilter_All();
//                    Toast.makeText(TasksMainActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();

                    break;
                case Constant.END_TASK:
                    // FIXME: 17/9/1
                    int endIndex = (int)msg.obj;
                    Task tagTaskEnd = mTaskList.get(endIndex);
//                    tagTaskEnd.setSendOverTime(mTask.getSendOverTime());//更新任务开始时间
                    tagTaskEnd.setSendOverTime(new Timestamp(System.currentTimeMillis()));//更新任务开始时间
                    // TODO: 17/9/1  调用当前筛选条件，而不是写死
                    mTaskListAdapter.updateItem(endIndex);
//                    Toast.makeText(TasksMainActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case Constant.SOAP_UNSUCCESS:
                    Toast.makeText(TasksMainActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case Constant.TASK_UPDATE://更新task
                    Toast.makeText(TasksMainActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(TasksMainActivity.this, "网络访问异常", Toast.LENGTH_LONG).show();
                    break;
            }
        };
    };


    /**
     *
     * @param
     * @return
     */
    public void startTaskSending(final int index) {
        final Task task = mTaskList.get(index);
        mDialogBuilder = new CustomDialog.Builder(this);
        mDialogBuilder.setMessage(getResources().getString(R.string.startSend_message));
        mDialogBuilder.setTitle(getResources().getString(R.string.startSend_title));
        mDialogBuilder.setPositiveButton(getResources().getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                task.setSendStartTime(new Timestamp(System.currentTimeMillis()));
                // TODO: 17/8/17  网络请求
                loadBeginTask(index,task.getTaskId());
            }
        });

        mDialogBuilder.setNegativeButton(getResources().getString(R.string.action_no),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mDialogBuilder.create().show();

    }

    /**
     *
     * @param
     * @return
     */
    public void endTaskSending(final int index){
        final Task task = mTaskList.get(index);
        mDialogBuilder = new CustomDialog.Builder(this);
        mDialogBuilder.setMessage(getResources().getString(R.string.endSend_message));
        mDialogBuilder.setTitle(getResources().getString(R.string.endSend_title));
        mDialogBuilder.setPositiveButton(getResources().getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                task.setSendOverTime(new Timestamp(System.currentTimeMillis()));
                // TODO: 17/8/17  网络请求
                loadFinishTask(index,task.getTaskId());
            }
        });
        mDialogBuilder.setNegativeButton(getResources().getString(R.string.action_no),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mDialogBuilder.create().show();
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
        menu.add(0, 1, Constant.LOAD_MSG, "消息");
        menu.add(1, 1, Constant.EXIT_LOGOUT, "退出登录");

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
        if(resultCode ==Constant.LOAD_TASK_DETAIL){
            // TODO: 17/8/24 刷新adapter 刷新数据
            String info = data.getStringExtra("name");
            Log.d("navOrderSign","navOrderSign---->"+info);
            Task task = (Task) data.getSerializableExtra("task");//
            int index = (int) data.getSerializableExtra("index");//
            mTaskList.remove(index);
            mTaskList.add(index,task);
            mTaskListAdapter.setmTaskList(mTaskList);
            mTaskListAdapter.updateItem(index);
        }
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
     * 登出方法
     */
    private void logout(){
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

        switch (item.getOrder()) {
            case Constant.EXIT_LOGOUT:
                logout();
                break;
            case Constant.LOAD_MSG:

                Intent intent = new Intent(TasksMainActivity.this, MsgListActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putSerializable("", );
                intent.putExtras(bundle);
                startActivityForResult(intent, Constant.LOAD_MSG);//需要实现回调方法
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * 监听网络状态
     */
    public void setNetworkConnectChangedReceiver(){
        NetworkConnectChangedReceiver.getInstance(TasksMainActivity.this).setNetStateBtn((Button)findViewById(R.id.network_d));
    }

    /**
     * 加载数据
     */
    private void loadTasksData() {

        // TODO: 17/8/11
        if (Constant.__IS_FAKE_DATA__) {
            loadFakeTasks();
        } else {
            //加载order
            loadTasks();
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

    /**
     * 加载网络数据 taskList
     */
    private void loadTasks(){
        
        //loading
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = format.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseObject planListRes = Soap.getInstance().loadTaskList(dateStr,Util.INSTANCE.getUser().getWorkerCode());
                    if(planListRes.isSuccess()){
                        mTaskList = JSON.parseArray(planListRes.getObj(),Task.class);
//                        if(planlist.size() != 0){
//                            SoundPlayUtils.getInstance()
//                        }
                        Log.d("loadTaskListData","loadTaskListData-tasklist"+mTaskList);
                        Message msg = Message.obtain();
                        msg.what = Constant.LOAD_TASKS;
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

    // TODO: 17/8/24  自动更新
    /**
     * 加载网络数据 planList --自动更新（没有loading）
     */
    private void autoloadTaskData(){
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
                    ResponseObject planListRes = Soap.getInstance().loadTaskList(Util.INSTANCE.getUser().getWorkerCode(),dateStr);
                    if(planListRes.isSuccess()){
                        String newStr = planListRes.getObj();
                        List<Task> buf= JSON.parseArray(newStr,Task.class);
                        /**比较两个planlist byte长度**/
                        if(!Util.INSTANCE.compareObjectByteSize(mTaskList,buf)){ //如果byte长度不相等
                            SoundPlayUtils.getInstance(TasksMainActivity.this).play(1);
                        }
                        mTaskList = buf;
                        //比较两个plan array的item 每个都比较，然后保留状态
//                        planlist = Util.INSTANCE.comparePlanList(planlist,buf);

                        Log.d("loadTaskData","loadTaskData-taskList"+mTaskList);
                        Message msg = Message.obtain();
                        msg.what = Constant.LOAD_TASKS;
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

    /**
     * 开始任务 BeginTask
     */
    private void loadBeginTask(final int index,final String taskId){
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = format.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseObject startRes = Soap.getInstance().loadBeginTask(dateStr,""+Util.INSTANCE.getUser().getId(),taskId);
                    if(startRes.isSuccess()){
                        mTask = JSON.parseObject(startRes.getObj(),Task.class);
//                        if(planlist.size() != 0){
//                            SoundPlayUtils.getInstance()
//                        }
                        Log.d("loadTaskStartData","loadTaskStartData-mTask"+mTask);
                        Message msg = Message.obtain();
                        msg.what = Constant.START_TASK;
                        msg.obj = index;
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
    private void loadFinishTask(final int index,final String taskId){
        LoadingDialog.getInstance(this).showPD(getString(R.string.loading_message));
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = format.format(new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ResponseObject endRes = Soap.getInstance().loadFinishTask(dateStr,""+Util.INSTANCE.getUser().getId(),taskId);
                    if(endRes.isSuccess()){
                        mTask = JSON.parseObject(endRes.getObj(),Task.class);
                        Log.d("loadTaskListData","loadTaskListData-tasklist"+mTaskList);
                        Message msg = Message.obtain();
                        msg.what = Constant.END_TASK;
                        msg.obj = index;
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

}

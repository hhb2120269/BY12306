package com.example.hhb.by12306.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.example.hhb.by12306.adapter.TraceListAdapter;
import com.example.hhb.by12306.model.Msg;
import com.example.hhb.by12306.model.Trace;

import java.util.ArrayList;
import java.util.List;
import com.example.hhb.by12306.R;
import com.example.hhb.by12306.tool.Constant;
import com.example.hhb.by12306.tool.NetworkConnectChangedReceiver;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by hhb on 17/8/21.
 */

public class MsgListActivity extends AppCompatActivity {
    private PullToRefreshView mPullToRefreshView;// 第三方下拉刷新控件
    private View containerView;// 列表及下拉动画控件的容器
    private LinearLayout mEmptyView;// 空占位图片

    private RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>();
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
        setIconEmpty(traceList.size()==0);
    }
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
        // 模拟一些假的数据
        traceList.add(new Trace("2016-05-25 17:01:00", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 14:13:00", "D1232 17:80发车 [DR490EJK25Kwerq] 任务变更!"));
        traceList.add(new Trace("2016-05-25 13:01:04", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));
        traceList.add(new Trace("2016-05-25 12:19:47", "D123 17:19发车 [DR490EJK0JKJE] 任务变更!"));

        adapter = new TraceListAdapter(this, traceList);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(adapter);


    }
    /**
     * 设置empty图片的显示
     * @param flag
     */
    public void setIconEmpty(boolean flag){
        mEmptyView.setVisibility(flag?View.VISIBLE:View.GONE);
    }
}

package com.example.hhb.by12306.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hhb.by12306.adapter.TraceListAdapter;
import com.example.hhb.by12306.model.Trace;

import java.util.ArrayList;
import java.util.List;
import com.example.hhb.by12306.R;

/**
 * Created by hhb on 17/8/21.
 */

public class TraceActivity extends BaseActivity {
    private RecyclerView rvTrace;
    private List<Trace> traceList = new ArrayList<>(10);
    private TraceListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        findView();
        initData();
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
        adapter = new TraceListAdapter(this, traceList);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(adapter);
    }
}

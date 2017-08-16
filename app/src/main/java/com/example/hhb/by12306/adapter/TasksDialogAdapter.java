package com.example.hhb.by12306.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.byxx.gtz.jinanapk.R;
import com.byxx.gtz.jinanapk.model.Task;
import com.byxx.gtz.jinanapk.model.Order;
import com.example.hhb.by12306.model.Order;
import com.example.hhb.by12306.model.Task;

import java.util.List;

/**
 * Created by hhb on 17/7/24.
 */

public class TasksDialogAdapter extends BaseAdapter {
    private List<Task> mTaskList;
    private Order mOrder;
    private Context mContext;
    private TasksDialogAdapter.ViewHolder viewHolder;




    //viewHolder
    static class ViewHolder{
        TextView sendTime;   //令下达时间
        TextView orderCodeFormer;   //命令编号
        Button goSignTask; // 签命令按钮
        ImageView img_is_sign;   // 完成水印

    }

    public TasksDialogAdapter(Context context, Order order, List<Task> orderList){
        this.mContext = context;
        this.mTaskList = orderList;
        this.mOrder = order;
    }

    @Override
    public int getCount() {
        if(mTaskList == null)return 0;
        mTaskList = this.getmTaskList();
        return mTaskList==null?0:this.mTaskList.size();
    }

    @Override
    public Object getItem(int i) {
        return mTaskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {
        final Task order = (Task) getItem(i);
        if(convertView == null) {
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orderlist_dialog, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.orderCodeFormer = (TextView) convertView.findViewById(R.id.orderCodeFormer);
            viewHolder.sendTime = (TextView) convertView.findViewById(R.id.sendTime);
            viewHolder.goSignTask = (Button) convertView.findViewById(R.id.go_sign_order);
            viewHolder.img_is_sign = (ImageView) convertView.findViewById(R.id.img_is_sign);

            convertView.setTag(viewHolder);
        }else{
            //可以复用的convertView
            viewHolder = (ViewHolder) convertView.getTag();

        }
//        设置文字
        viewHolder.orderCodeFormer.setText(order.getTaskCodeFormer());
        viewHolder.sendTime.setText(order.getSendTime());
        if(order.getSign()){
            viewHolder.img_is_sign.setVisibility(View.VISIBLE);
            viewHolder.goSignTask.setText("查看");
        }else{
            viewHolder.img_is_sign.setVisibility(View.GONE);
            viewHolder.goSignTask.setText("查看／签收");
        }


        viewHolder.goSignTask.setOnClickListener(new View.OnClickListener() {//设置按钮点击-前往命令详签收页
            @Override
            public void onClick(View view) {
                Button btn = (Button)view;
                // TODO: 17/7/24  前往命令详签收页
                performButtonClick(view,i,order,"goSignTask",mOrder);
            }
        });

        return convertView;
    }
    /**  interface **/
    OnBtnSelectedListener mOnBtnSelectedListener;

    public interface OnBtnSelectedListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         */
        void onBtnSelect(View view, int position, Object data, String whichOne, Order order);
    }
    public void setmOnBtnSelectedListener(@Nullable OnBtnSelectedListener listener) {
        mOnBtnSelectedListener = listener;
    }
    /**
     * 我只需为界面添加按钮事件  就是这个事件－－调用inderface
     * @param view
     * @param position
     * @param data
     * @return
     */
    public boolean performButtonClick(View view, int position, Object data, String whichOne, Order order) {
        final boolean result;
        if (mOnBtnSelectedListener != null) {
            mOnBtnSelectedListener.onBtnSelect( view, position, data,whichOne, order);
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /** getter and setter **/
    public List<Task> getmTaskList() {
        return mTaskList;
    }

    public void setmTaskList(List<Task> mTaskList) {
        this.mTaskList = mTaskList;
    }

    public Order getmOrder() {
        return mOrder;
    }

    public void setmOrder(Order mOrder) {

        this.mOrder = mOrder;
    }

}

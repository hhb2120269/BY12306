package com.example.hhb.by12306.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hhb.by12306.model.Task;
import com.example.hhb.by12306.tool.CustomDialog;
import com.example.hhb.by12306.R;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by wgary on 2017/5/30.
 */
public class TasksListAdapter extends BaseAdapter {



    private CustomDialog.Builder mDialogBuilder;

    static class ViewHolder{
        int index;

        ImageView flagImage;// 已完成标志

        TextView text_taskId;
        TextView text_sender;
        TextView text_arriveTime;
        TextView text_arriveLate;
        TextView text_leaveTime;
        TextView text_leaveLate;
        TextView text_position;


        RelativeLayout sendingStart;
        TextView sendingStartTime;
        Button startBtn;

        RelativeLayout sendingEnd;
        TextView sendingEndTime;
        Button endBtn;

    }

    private ViewHolder mViewHolder;
    private Context mContext;
    private ListView mlv;
    private List<Task> mTaskList;



    public TasksListAdapter(Context context, ListView listview, List<Task> planList){
        this.mContext = context;
        this.mlv = listview;
        this.mTaskList = planList;
    }



    @Override
    public int getCount() {
        return mTaskList == null ? 0 : mTaskList.size();
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
        try {
            if(mTaskList.size()==0){
                Log.d("error","Error: mTaskList.size＝＝0");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ViewHolder viewHolder = null;
        final Task itemData = mTaskList.get(i);


//        final SlideLayout slideLayout = (SlideLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_planlist, viewGroup, false);

        if(convertView == null) {

            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tasks, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.index = i;

            viewHolder.flagImage = (ImageView) convertView.findViewById(R.id.flagImage);// 已完成标志
            viewHolder.flagImage.setVisibility(View.GONE);// 已完成标志

            viewHolder.text_taskId = (TextView) convertView.findViewById(R.id.text_taskId);
            viewHolder.text_sender = (TextView) convertView.findViewById(R.id.text_sender);
            viewHolder.text_arriveTime = (TextView) convertView.findViewById(R.id.text_arriveTime);
            viewHolder.text_arriveLate = (TextView) convertView.findViewById(R.id.text_arriveLate);
            viewHolder.text_leaveTime = (TextView) convertView.findViewById(R.id.text_leaveTime);
            viewHolder.text_leaveLate = (TextView) convertView.findViewById(R.id.text_leaveLate);
            viewHolder.text_position = (TextView) convertView.findViewById(R.id.text_position);

            viewHolder.sendingStart = (RelativeLayout) convertView.findViewById(R.id.sendingStart);
            viewHolder.sendingStartTime = (TextView) convertView.findViewById(R.id.sendingStartTime);
            viewHolder.startBtn = (Button)convertView.findViewById(R.id.startBtn);
            viewHolder.sendingStart.setVisibility(View.VISIBLE);

            viewHolder.sendingEnd = (RelativeLayout) convertView.findViewById(R.id.sendingEnd);
            viewHolder.sendingEndTime = (TextView) convertView.findViewById(R.id.sendingEndTime);
            viewHolder.endBtn = (Button)convertView.findViewById(R.id.endBtn);
            viewHolder.sendingEnd.setVisibility(View.GONE);

            convertView.setTag(viewHolder);
        }else{
            //可以复用的convertView
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.index = i;
        }

        /** 单双区块颜色区分 **/
        if(i%2 != 0){
            convertView.setBackgroundColor(Color.LTGRAY);
        }else{
//            convertView.setBackgroundColor(Color.rgb(248,248,255));
            convertView.setBackgroundColor(Color.argb(125,250,250,255));
        }

        /**设置文字信息**/
        setViewContent(itemData,viewHolder);


        // TODO: 17/8/6 判断是否已完成送餐
//        if()...



        final RelativeLayout ss = viewHolder.sendingStart;
        final RelativeLayout se = viewHolder.sendingEnd;
        viewHolder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performButtonClick(v, i, mTaskList.get(i),1);
                ss.setVisibility(View.GONE);
                se.setVisibility(View.VISIBLE);
            }
        });
        viewHolder.endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performButtonClick(v, i, mTaskList.get(i),2);
                ss.setVisibility(View.VISIBLE);

                se.setVisibility(View.GONE);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performItemClick(v,i,itemData);
            }
        });
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        convertView.measure(w, h);
        int height = convertView.getMeasuredHeight();
        int width = convertView.getMeasuredWidth();
//        viewHolder.textSlide.setHeight(height);


        return convertView;
    }

    /**
     * 刷新指定item
     * @param itemIndex
     */
    public void updateItem(int itemIndex) {
        /**以上主要实现dataList指定item值加1**/
        View mView = mlv.getChildAt(itemIndex-mlv.getFirstVisiblePosition());//获取指定itemIndex在屏幕中的view
        ViewHolder mViewHolder = (ViewHolder) mView.getTag();
        Task itemData = mTaskList.get(itemIndex);
        /**设置文字信息**/
        setViewContent(itemData,mViewHolder);
    }

    /**
     * 设置文字信息
     * @param itemData
     * @param mViewHolder
     */
    private void setViewContent(Task itemData, ViewHolder mViewHolder){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formHMS = new SimpleDateFormat("HH:mm:ss");
            mViewHolder.text_taskId.setText("送餐编号:"+itemData.getTaskId());
            mViewHolder.text_sender.setText("送餐员:"+itemData.getSender());
            mViewHolder.text_arriveTime.setText("到站:" + formatter.format(itemData.getArriveTime()));
            mViewHolder.text_arriveLate.setText("晚点："+itemData.getArriveLate());
            mViewHolder.text_leaveTime.setText("发车:" + formatter.format(itemData.getLeaveTime()));
            mViewHolder.text_leaveLate.setText("晚点："+itemData.getLeaveLate());
            mViewHolder.text_position.setText(itemData.getTrainNo() + "   " + itemData.getTrack() + "   " + itemData.getPlatform());

            String startDate = itemData.getSendStartTime()!=null? formHMS.format(itemData.getSendStartTime()):"";
            String endDate = itemData.getSendOverTime()!=null? formHMS.format(itemData.getSendOverTime()):"";
            mViewHolder.sendingEndTime.setText(startDate+"--"+endDate);
            mViewHolder.sendingStartTime.setText(startDate+"--"+endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 确认签收命令 －弹框确认
     */
    private void setDialogForSignTask(){
        mDialogBuilder = new CustomDialog.Builder(mContext);
        mDialogBuilder.setMessage("确认签收命令？");
        mDialogBuilder.setTitle("提示");
        mDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
            }
        });

        mDialogBuilder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }

    /**
     * 显示签收弹窗
     */
    private void showSignTask(){
        if (mDialogBuilder == null)setDialogForSignTask();
        mDialogBuilder.create().show();
    }

/** setter  and  getter **/
    public void setmTaskList(List<Task> mTaskList) {
        this.mTaskList = mTaskList;
    }

//    public void setFlag(int flag){
//        this.flag = flag;
//    }


    public ListView getMlv() {
        return mlv;
    }

    public void setMlv(ListView mlv) {
        this.mlv = mlv;
    }

    /**  interface **/
    OnCellSelectedListener mOnCellSelectedListener;

    public interface OnCellSelectedListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         */
        void onCellSelect( View view, int position, Object data);
//        void onButtonSelect(AdapterView<?> parent, View view, int position, Object data, String whichOne);
        void onButtonSelect(View view, int position, Object data, int whichOne);
    }
    public void setOnItemClickListener(@Nullable OnCellSelectedListener listener) {
        mOnCellSelectedListener = listener;
    }

    /**
     * 我只需为界面添加按钮事件  就是这个事件－－调用inderface
     * @param view
     * @param position
     * @param data
     * @return
     */
    public boolean performItemClick( View view, int position, Object data) {
        final boolean result;
        if (mOnCellSelectedListener != null) {
//            view.playSoundEffect(SoundEffectConstants.CLICK);//声音？？
            mOnCellSelectedListener.onCellSelect( view, position, data);
            result = true;
        } else {
            result = false;
        }
        return result;
    }
    /**
     * 我只需为界面添加按钮事件  就是这个事件－－调用inderface
     * @param view
     * @param position
     * @param data
     * @return
     */
    public boolean performButtonClick(View view, int position, Object data, int whichOne) {
        final boolean result;
        if (mOnCellSelectedListener != null) {
//            view.playSoundEffect(SoundEffectConstants.CLICK);//声音？？
            mOnCellSelectedListener.onButtonSelect( view, position, data,whichOne);
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}

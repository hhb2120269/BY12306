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
import android.widget.TextView;

import com.example.hhb.by12306.model.Task;
import com.example.hhb.by12306.tool.CustomDialog;
import com.example.hhb.by12306.R;

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
        TextView text_trainNo;
        TextView text_arriveTime;
        TextView text_arriveLate;
        TextView text_leaveTime;
        TextView text_leaveLate;
        TextView text_track;
        TextView text_platform;
        TextView text_stratTime;
        TextView text_overTime;
        TextView text_sender;
        Button showContentBtn;// 项目内容
    }

    private ViewHolder mViewHolder;
    private Context mContext;
    private int flag = 0;
    private List<Task> mTaskList;



    public TasksListAdapter(Context context, int flag, List<Task> planList){
        this.mContext = context;
        this.flag = flag;
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

            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orders, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.index = i;

            viewHolder.flagImage = (ImageView) convertView.findViewById(R.id.flagImage);// 已完成标志
            viewHolder.flagImage.setVisibility(View.GONE);// 已完成标志

            viewHolder.showContentBtn = (Button) convertView.findViewById(R.id.showContentBtn);

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

        // TODO: 17/8/6 判断是否已完成
//        if()...

        viewHolder.showContentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performButtonClick(v, i, mTaskList.get(i),1);
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

    public void setFlag(int flag){
        this.flag = flag;
    }


/**  interface **/
    OnCellSelectedListener mOnCellSelectedListener;

    public interface OnCellSelectedListener {

        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         */
        void onCellSelect(AdapterView<?> parent, View view, int position, Object data);
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
    public boolean performItemClick(AdapterView<?> parent, View view, int position, Object data) {
        final boolean result;
        if (mOnCellSelectedListener != null) {
//            view.playSoundEffect(SoundEffectConstants.CLICK);//声音？？
            mOnCellSelectedListener.onCellSelect(parent, view, position, data);
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

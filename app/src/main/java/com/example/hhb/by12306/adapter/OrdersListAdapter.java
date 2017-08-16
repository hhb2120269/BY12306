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

import com.example.hhb.by12306.model.Order;
import com.example.hhb.by12306.tool.CustomDialog;
import com.example.hhb.by12306.R;

import java.util.List;


/**
 * Created by wgary on 2017/5/30.
 */
public class OrdersListAdapter extends BaseAdapter {

    private CustomDialog.Builder mDialogBuilder;

    static class ViewHolder{
        int index;

        ImageView flagImage;// 已完成标志

        TextView planId;// 施工编号
        TextView planTime;// 时间
        TextView name;// 施工项目
        Button showContentBtn;// 项目内容


        View bottomPadding;//下方padding
    }

    private ViewHolder mViewHolder;
    private Context mContext;
    private int flag = 0;
    private List<Order> mOrderList;



    public OrdersListAdapter(Context context, int flag, List<Order> planList){
        this.mContext = context;
        this.flag = flag;
        this.mOrderList = planList;
    }



    @Override
    public int getCount() {
        return mOrderList == null ? 0 : mOrderList.size();
    }

    @Override
    public Object getItem(int i) {
        return mOrderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, final ViewGroup viewGroup) {
        try {
            if(mOrderList.size()==0){
                Log.d("error","Error: mOrderList.size＝＝0");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ViewHolder viewHolder = null;
        final Order itemData = mOrderList.get(i);



//        final SlideLayout slideLayout = (SlideLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_planlist, viewGroup, false);

        if(convertView == null) {

            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orders, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.index = i;
//
//            viewHolder.flagImage = (ImageView) convertView.findViewById(R.id.flagImage);// 已完成标志
//            viewHolder.flagImage.setVisibility(View.GONE);// 已完成标志
//
//            viewHolder.bottomPadding = (View) convertView.findViewById(R.id.bottomPadding);// 下方padding

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
    private void setDialogForSignOrder(){
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
    private void showSignOrder(){
        if (mDialogBuilder == null)setDialogForSignOrder();
        mDialogBuilder.create().show();
    }

/** setter  and  getter **/
    public void setmOrderList(List<Order> mOrderList) {
        this.mOrderList = mOrderList;
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

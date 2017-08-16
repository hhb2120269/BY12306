package com.example.hhb.by12306.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.byxx.gtz.jinanapk.R;
import com.byxx.gtz.jinanapk.adapter.OrderDialogAdapter;
import com.byxx.gtz.jinanapk.model.Order;
import com.byxx.gtz.jinanapk.model.Plan;

import java.util.List;

/**
 * Created by hhb on 17/7/24.
 */

public class TaskDialog extends Dialog {

    public TaskDialog(@NonNull Context context) {
        super(context);
    }

    public TaskDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected TaskDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{
        private Context context;
        private TaskDialog mDialog;
        private List<Order> mOrderList;
        private Plan mPlan;
        private OnClickListener negativeButtonClickListener;
        private OnClickListener onOrderButtonClickListener;
        private ListView mlistView;
        private OrderDialogAdapter orderDialogAdapter=null;

        public void setNegativeButtonClickListener(OnClickListener listener) {
            this.negativeButtonClickListener = listener;
        }
        public void setOnOrderButtonClickListener(OnClickListener onOrderButtonClickListener) {
            this.onOrderButtonClickListener = onOrderButtonClickListener;
        }
        public Builder(Context context) {
            this.context = context;
        }

        public void setTask(List<Order> mOrderList) {
            this.mOrderList = mOrderList;
        }
        /** 加载数据源 **/
        public void setParams(Plan plan,List<Order> mOrderList){
            this.mOrderList = mOrderList;
            this.mPlan = plan;
        }


        public TaskDialog create() {


            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            mDialog = new TaskDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_orders, null);
            if (negativeButtonClickListener != null) {
                ((Button) layout.findViewById(R.id.btn_exit))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                negativeButtonClickListener.onClick(mDialog,
                                        DialogInterface.BUTTON_NEGATIVE);
                            }
                        });
            }

            mlistView = (ListView) layout.findViewById(R.id.orderListView);
            orderDialogAdapter = new OrderDialogAdapter(context,mPlan, mOrderList);
            setAdapterBtnSelectedInterface();
            mlistView.setAdapter(orderDialogAdapter);
            mDialog.setContentView(layout);
            mDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
            return mDialog;
        }

        /**
         *
         * @return
         */
        public boolean isShowing(){
            if(mDialog != null){
                return mDialog.isShowing();
            }else{
                return false;
            }

        }

        /**
         * 刷新数据
         */
        public void refreshOrderList(List<Order> newOrderlist){
            if(mlistView != null && orderDialogAdapter !=null){
                if(newOrderlist != null && newOrderlist.size() != 0){
                    mOrderList = newOrderlist;
                    orderDialogAdapter.setmOrderList(mOrderList);
                    orderDialogAdapter.notifyDataSetChanged();//刷新数据
                }
            }
        }

        /**
         * 设置 dialog 中 adapter－Interface 点击按钮方法
         */
        public void setAdapterBtnSelectedInterface(){
            if(orderDialogAdapter != null){
                orderDialogAdapter.setmOnBtnSelectedListener(new TasksDialogAdapter.OnBtnSelectedListener() {
                    @Override
                    public void onBtnSelect(View view, int position, Object data, String whichOne, Plan plan) {
                        performButtonClick(plan, data, whichOne);
                    }
                });
            }

        }
        /**  interface **/
        OnBtnSelectedListener mOnBtnSelectedListener;

        public interface OnBtnSelectedListener {

            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             */
            void onBtnSelect(Plan plan, Object data, String whichOne);
        }
        public void setmOnBtnSelectedListener(@Nullable OnBtnSelectedListener listener) {
            mOnBtnSelectedListener = listener;
        }
        public boolean performButtonClick(Plan plan, Object data, String whichOne) {
            final boolean result;
            if (mOnBtnSelectedListener != null) {
                mOnBtnSelectedListener.onBtnSelect(plan, data,whichOne);
                result = true;
            } else {
                result = false;
            }
            return result;
        }
    }
}

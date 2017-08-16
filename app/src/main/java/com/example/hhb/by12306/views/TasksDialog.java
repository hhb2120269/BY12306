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


import com.example.hhb.by12306.R;
import com.example.hhb.by12306.adapter.TasksDialogAdapter;
import com.example.hhb.by12306.model.Order;
import com.example.hhb.by12306.model.Task;

import java.util.List;

/**
 * Created by hhb on 17/7/24.
 */

public class TasksDialog extends Dialog {

    public TasksDialog(@NonNull Context context) {
        super(context);
    }

    public TasksDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected TasksDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder{
        private Context context;
        private TasksDialog mDialog;
        private List<Task> mTaskList;
        private Order mOrder;
        private OnClickListener negativeButtonClickListener;
        private OnClickListener onTaskButtonClickListener;
        private ListView mlistView;
        private TasksDialogAdapter taskDialogAdapter=null;

        public void setNegativeButtonClickListener(OnClickListener listener) {
            this.negativeButtonClickListener = listener;
        }
        public void setOnTaskButtonClickListener(OnClickListener onTaskButtonClickListener) {
            this.onTaskButtonClickListener = onTaskButtonClickListener;
        }
        public Builder(Context context) {
            this.context = context;
        }

        public void setTask(List<Task> mTaskList) {
            this.mTaskList = mTaskList;
        }
        /** 加载数据源 **/
        public void setParams(Order order,List<Task> mTaskList){
            this.mTaskList = mTaskList;
            this.mOrder = order;
        }


        public TasksDialog create() {


            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            mDialog = new TasksDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_tasks, null);
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
            taskDialogAdapter = new TasksDialogAdapter(context,mOrder, mTaskList);
            setAdapterBtnSelectedInterface();
            mlistView.setAdapter(taskDialogAdapter);
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
        public void refreshTaskList(List<Task> newTasklist){
            if(mlistView != null && taskDialogAdapter !=null){
                if(newTasklist != null && newTasklist.size() != 0){
                    mTaskList = newTasklist;
                    taskDialogAdapter.setmTaskList(mTaskList);
                    taskDialogAdapter.notifyDataSetChanged();//刷新数据
                }
            }
        }

        /**
         * 设置 dialog 中 adapter－Interface 点击按钮方法
         */
        public void setAdapterBtnSelectedInterface(){
            if(taskDialogAdapter != null){
                taskDialogAdapter.setmOnBtnSelectedListener(new TasksDialogAdapter.OnBtnSelectedListener() {
                    @Override
                    public void onBtnSelect(View view, int position, Object data, String whichOne, Order order) {
                        performButtonClick(order, data, whichOne);
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
            void onBtnSelect(Order order, Object data, String whichOne);
        }
        public void setmOnBtnSelectedListener(@Nullable OnBtnSelectedListener listener) {
            mOnBtnSelectedListener = listener;
        }
        public boolean performButtonClick(Order order, Object data, String whichOne) {
            final boolean result;
            if (mOnBtnSelectedListener != null) {
                mOnBtnSelectedListener.onBtnSelect(order, data,whichOne);
                result = true;
            } else {
                result = false;
            }
            return result;
        }
    }
}

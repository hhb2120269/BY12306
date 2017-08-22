package com.example.hhb.by12306.view;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by hhb on 17/8/21.
 */

public class StepsView {

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
}

package com.example.hhb.by12306.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by hhb on 17/8/16.
 */

public class Task extends BaseObject implements Serializable{

    private String taskId; // 任务id：车次@日期
    private String trainNo; // 任务车次，一个车次一个任务
    private Timestamp arriveTime; // 本站到达时间
    private Timestamp leaveTime; // 本站出发时间
    private String track; // 股道
    private String platform; // 站台
    private int arriveLate; // 到达晚点
    private int leaveLate; // 出发晚点
    private Timestamp sendStartTime; // 开始送餐时间
    private Timestamp sendOverTime; // 送餐完成时间
    private String sender; // 送餐员
    private Timestamp signTime;
    private String taskType;

    public Task() {
    }

    public Task(String taskId) {
        this.taskId = taskId;
    }

    public Task(String taskId, String trainNo, Timestamp arriveTime, Timestamp leaveTime, String track, String platform, int arriveLate, int leaveLate, Timestamp sendStartTime, Timestamp sendOverTime, String sender, Timestamp signTime, String taskType) {
        this.taskId = taskId;
        this.trainNo = trainNo;
        this.arriveTime = arriveTime;
        this.leaveTime = leaveTime;
        this.track = track;
        this.platform = platform;
        this.arriveLate = arriveLate;
        this.leaveLate = leaveLate;
        this.sendStartTime = sendStartTime;
        this.sendOverTime = sendOverTime;
        this.sender = sender;
        this.signTime = signTime;
        this.taskType = taskType;
    }
    /** getter and setter **/

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public Timestamp getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(Timestamp arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Timestamp getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Timestamp leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getArriveLate() {
        return arriveLate;
    }

    public void setArriveLate(int arriveLate) {
        this.arriveLate = arriveLate;
    }

    public int getLeaveLate() {
        return leaveLate;
    }

    public void setLeaveLate(int leaveLate) {
        this.leaveLate = leaveLate;
    }

    public Timestamp getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(Timestamp sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public Timestamp getSendOverTime() {
        return sendOverTime;
    }

    public void setSendOverTime(Timestamp sendOverTime) {
        this.sendOverTime = sendOverTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Timestamp getSignTime() {
        return signTime;
    }

    public void setSignTime(Timestamp signTime) {
        this.signTime = signTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}

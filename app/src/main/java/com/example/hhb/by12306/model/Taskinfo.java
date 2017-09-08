package com.example.hhb.by12306.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by hhb on 17/9/8.
 */


public class Taskinfo extends BaseObject
        implements Serializable {
    public static String TaskType_AUTO = "AUTO";
    public static String TaskType_MANUAL = "MANUAL";
    private String taskId;
    private String trainNo;
    private Timestamp arriveTime;
    private Timestamp leaveTime;
    private String track;
    private String platform;
    private int arriveLate;
    private int leaveLate;
    private Timestamp sendStartTime;
    private Timestamp sendOverTime;
    private String sender;
    private Timestamp signTime;
    private String taskType;

    public Taskinfo() {
    }

    public Taskinfo(String taskId) {
        this.taskId = taskId;
    }

    public Taskinfo(String taskId, String trainNo, Timestamp arriveTime, Timestamp leaveTime, String track, String platform, int arriveLate, int leaveLate, Timestamp sendStartTime, Timestamp sendOverTime, String sender, Timestamp signTime, String taskType) {
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

    public static String getTaskType_AUTO() {
        return TaskType_AUTO;
    }

    public static void setTaskType_AUTO(String taskType_AUTO) {
        TaskType_AUTO = taskType_AUTO;
    }

    public static String getTaskType_MANUAL() {
        return TaskType_MANUAL;
    }

    public static void setTaskType_MANUAL(String taskType_MANUAL) {
        TaskType_MANUAL = taskType_MANUAL;
    }

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
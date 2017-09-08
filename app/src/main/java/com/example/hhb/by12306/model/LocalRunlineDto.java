package com.example.hhb.by12306.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by hhb on 17/9/8.
 */

public class LocalRunlineDto extends BaseObject implements Serializable{
    // 日计划日期
    private String dayplanDay = "";
    // 客车日班计划类型（高铁"H"/普速"P"）
    private String dayplanType = "";
    // 运行线ID
    private Integer runlineId;
    // 开行标志
    private Integer stopflag;
    // 开车日期
    private String runDate = "";
    // 编组名
    private String bzm = "";
    // 车次
    private String cc = "";
    // 图定始发时间
    private Timestamp beginTime;
    // 图定终到时间
    private Timestamp endTime;
    // 始发站
    private String sfz = "";
    // 终到站
    private String zdz = "";
    // 列车类型
    private String trainKind = "";
    // 客运担当单位
    private String token = "";
    // 机车担当单位
    private String carToken = "";
    // 全局ID
    private Integer globalId;

    // 行调站名
    private String xdName;
    // 到达车次
    private String arrCc;
    // 出发车次
    private String depCc;
    // 图定到达时间
    private Timestamp arrTime;
    // 图定出发时间
    private Timestamp goTime;
    // 股道
    private String track;


    public String getDayplanDay() {
        return dayplanDay;
    }

    public void setDayplanDay(String dayplanDay) {
        this.dayplanDay = dayplanDay;
    }

    public String getDayplanType() {
        return dayplanType;
    }

    public void setDayplanType(String dayplanType) {
        this.dayplanType = dayplanType;
    }

    public Integer getRunlineId() {
        return runlineId;
    }

    public void setRunlineId(Integer runlineId) {
        this.runlineId = runlineId;
    }

    public Integer getStopflag() {
        return stopflag;
    }

    public void setStopflag(Integer stopflag) {
        this.stopflag = stopflag;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public String getBzm() {
        return bzm;
    }

    public void setBzm(String bzm) {
        this.bzm = bzm;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getZdz() {
        return zdz;
    }

    public void setZdz(String zdz) {
        this.zdz = zdz;
    }

    public String getTrainKind() {
        return trainKind;
    }

    public void setTrainKind(String trainKind) {
        this.trainKind = trainKind;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCarToken() {
        return carToken;
    }

    public void setCarToken(String carToken) {
        this.carToken = carToken;
    }

    public Integer getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Integer globalId) {
        this.globalId = globalId;
    }

    public String getXdName() {
        return xdName;
    }

    public void setXdName(String xdName) {
        this.xdName = xdName;
    }

    public String getArrCc() {
        return arrCc;
    }

    public void setArrCc(String arrCc) {
        this.arrCc = arrCc;
    }

    public String getDepCc() {
        return depCc;
    }

    public void setDepCc(String depCc) {
        this.depCc = depCc;
    }

    public Timestamp getArrTime() {
        return arrTime;
    }

    public void setArrTime(Timestamp arrTime) {
        this.arrTime = arrTime;
    }

    public Timestamp getGoTime() {
        return goTime;
    }

    public void setGoTime(Timestamp goTime) {
        this.goTime = goTime;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}

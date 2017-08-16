package com.example.hhb.by12306.model;

import java.util.List;

/**
 * Created by hhb on 17/8/16.
 */

public class Task extends BaseObject {
    private String orderId;
    private String merchant;
    private String userName;
    private String userTel;
    private String cc;
    private String cx;
    private String xw;
    private String orderDate;
    private String orderNeedSendDate;
    private String taskState;
    private String tagStation;
    private String tagDetailPlace;
    private String sender;
    private String remark;


    /** getter and setter **/
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNeedSendDate() {
        return orderNeedSendDate;
    }

    public void setOrderNeedSendDate(String orderNeedSendDate) {
        this.orderNeedSendDate = orderNeedSendDate;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTagStation() {
        return tagStation;
    }

    public void setTagStation(String tagStation) {
        this.tagStation = tagStation;
    }

    public String getTagDetailPlace() {
        return tagDetailPlace;
    }

    public void setTagDetailPlace(String tagDetailPlace) {
        this.tagDetailPlace = tagDetailPlace;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

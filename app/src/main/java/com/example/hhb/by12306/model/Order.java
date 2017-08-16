package com.example.hhb.by12306.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hhb on 17/8/11.
 */

public class Order extends BaseObject implements Serializable {
    public Order(){

    }
    private String orderId;
    private String merchant;
    private String userName;
    private String userTel;
    private String cc;
    private String cx;
    private String xw;
    private String orderDate;
    private List<?> signer;
    private String remark;

    /** getter and setter **/

    public String getOrderId() {
        return orderId;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public List<?> getSigner() {
        return signer;
    }

    public void setSigner(List<?> signer) {
        this.signer = signer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

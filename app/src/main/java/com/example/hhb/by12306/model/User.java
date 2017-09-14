package com.example.hhb.by12306.model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by hhb on 17/8/8.
 */

public class User {
    private String workerCode;
    private String workerName;
    private String password;
    private String phoneNum;
    private Timestamp lastLogin;
    private String privilege;
    private String isSending;

    public String getWorkerCode() {
        return workerCode;
    }

    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getIsSending() {
        return isSending;
    }

    public void setIsSending(String isSending) {
        this.isSending = isSending;
    }
}

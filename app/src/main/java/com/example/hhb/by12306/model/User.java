package com.example.hhb.by12306.model;

import java.util.ArrayList;

/**
 * Created by hhb on 17/8/8.
 */

public class User {
    private String duty;      //"工作人员",
//    private ArrayList<?> fingerList;      //[],
    private long id;      //0,
    private long limit;      //0,
    private String password;      //"11",
    private String simpleRole;      //"联络员",
    private String telNumber;      //"35255",
    private String workCode;      //"11",
    private String workName;      //"测试联络员",
    private String workStation;      //"上海工务段",
    private String workType;      //"YT46"


    /** getter and setter ***/
    public void setDuty(String duty) {
        this.duty = duty;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setSimpleRole(String simpleRole) {
        this.simpleRole = simpleRole;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    /** getter and setter ***/

    public String getDuty() {
        return duty;
    }

    public long getId() {
        return id;
    }

    public long getLimit() {
        return limit;
    }



    public String getPassword() {
        return password;
    }


    public String getSimpleRole() {
        return simpleRole;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public String getWorkCode() {
        return workCode;
    }

    public String getWorkName() {
        return workName;
    }

    public String getWorkStation() {
        return workStation;
    }

    public String getWorkType() {
        return workType;
    }
//
//    public ArrayList<?> getFingerList() {
//        return fingerList;
//    }
//
//    public void setFingerList(ArrayList<?> fingerList) {
//        this.fingerList = fingerList;
//    }
}

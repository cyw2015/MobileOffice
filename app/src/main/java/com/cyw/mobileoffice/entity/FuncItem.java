package com.cyw.mobileoffice.entity;

/**
 * Created by cyw on 2016/5/28.
 */
public class FuncItem {
    private int iId;
    private String iName;
    public FuncItem(){

    }
    public FuncItem(int iId,String iName){
        this.iId = iId;
        this.iName=iName;
    }

    public int getiId() {
        return iId;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}

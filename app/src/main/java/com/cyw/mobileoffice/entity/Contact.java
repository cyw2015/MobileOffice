package com.cyw.mobileoffice.entity;

/**
 * Created by cyw on 2016/5/28.
 */
public class Contact  {
    private  String phone;
    private String url;
    private String name;
    private String position;
    private String department;
    private String email;
    public Contact(){}
    public Contact( String name, String url, String position,String phone) {
        this.phone = phone;
        this.url = url;
        this.name = name;
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

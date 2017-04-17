package com.yang.foodsearch.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-04-16.
 */
public class User extends BmobObject implements Serializable{
    private String userName;
    private String userPassword;

    public User() {
    }

    public User(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
}

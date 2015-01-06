package com.jikabao.android.merchant.model;

import com.jikabao.android.common.model.BaseObject;

/**
 * Created by pjq on 1/4/15.
 */
public class User extends BaseObject {
    private String userId;
    private String userName;
    private String userEmail;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

package com.example.courierapp;

public class UserInformation {
    private String userRole;
    private String userId;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }


    public UserInformation(String userId, String userRole)
    {
        setUserId(userId);
        setUserRole(userRole);
    }


}

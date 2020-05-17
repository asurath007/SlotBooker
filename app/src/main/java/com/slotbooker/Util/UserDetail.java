package com.slotbooker.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetail {
    private String id,CODUserName,PUBGUserName,lastName,firstName,userName,userEmail;
    private static UserDetail instance;
    private List<String>MatchRegistered;

    public UserDetail(String email, String userName, String currentUserId) {
        this.userEmail = email;
        this.userName = userName;
        this.id = currentUserId;
    }

    public static UserDetail getInstance(){
        if(instance == null)
            instance = new UserDetail();
        return instance;
    }

    public UserDetail(){}

    public UserDetail( String fn, String ln ,String pun, String cun){
//        this.userEmail = email;
//        this.userName = userName;
        this.firstName = fn;
        this.lastName = ln;
        this.PUBGUserName = pun;
        this.CODUserName = cun;
    }
    public UserDetail(String email, String userName, String fn, String ln , String pun, String cun) {
        this.userEmail = email;
        this.userName = userName;
        this.firstName = fn;
        this.lastName = ln;
        this.PUBGUserName = pun;
        this.CODUserName = cun;
    }

    public UserDetail(String email, String userName, String fn, String ln , String pun, String cun, List<String>MatchRegistered){
        this.userEmail = email;
        this.userName = userName;
        this.firstName = fn;
        this.lastName = ln;
        this.PUBGUserName = pun;
        this.CODUserName = cun;
        this.MatchRegistered = MatchRegistered;
    }

    public Map<String,Object> newUser(){
        HashMap<String, Object> user = new HashMap<>();
        user.put("userEmail",userEmail);
        user.put("userId",id);
        user.put("userName",userName);
        user.put("FirstName",firstName);
        user.put("LastName",lastName);
        user.put("PUBG",PUBGUserName);
        user.put("COD",CODUserName);
        user.put("MatchRegistered","");
//        user.put("ID",id);
//        user.put();

        return user;
    }

    public String getEmail() {
        return userEmail;
    }

    public void setEmail(String email) {
        this.userEmail = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPUBGUserName() {
        return PUBGUserName;
    }

    public void setPUBGUserName(String PUBGUserName) {
        this.PUBGUserName = PUBGUserName;
    }

    public String getCODUserName() {
        return CODUserName;
    }

    public void setCODUserName(String CODUserName) {
        this.CODUserName = CODUserName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}

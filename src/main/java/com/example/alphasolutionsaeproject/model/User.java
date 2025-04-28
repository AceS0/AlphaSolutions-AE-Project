package com.example.alphasolutionsaeproject.model;

public class User {
    private String eid;
    private String uid;
    private String pw;


    public User(String eid, String uid, String pw){
        this.eid = eid;
        this.uid = uid;
        this.pw = pw;
    }

    public User(){}


    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public String getPw(){
        return pw;
    }

    public void setPw(String pw){
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "Users{" +
                "eid='" + eid + '\'' +
                "uid='" + uid + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }
}

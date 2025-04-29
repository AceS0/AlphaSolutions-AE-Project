package com.example.alphasolutionsaeproject.model;

public class User {
    private String eid;
    private String uid;
    private String pw;
    private Role role;


    public User(String eid, String uid, String pw, Role role){
        this.eid = eid;
        this.uid = uid;
        this.pw = pw;
        this.role = role;
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

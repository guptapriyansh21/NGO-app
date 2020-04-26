package com.ngo.finder;

public class Request {
    String userid,teacherid,status,dated,name,details;

    public Request(String userid, String teacherid, String status, String dated, String name,String details) {
        this.userid = userid;
        this.teacherid = teacherid;
        this.status = status;
        this.dated = dated;
        this.name = name;
        this.details = details;
    }

    public Request() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

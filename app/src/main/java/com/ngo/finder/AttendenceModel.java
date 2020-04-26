package com.ngo.finder;

/**
 * Created by inspirin on 11/16/2017.
 */

public class AttendenceModel {
    String userid;
    String name;
    String rdate;
    String time;
boolean status;
long total_present=0;

    public long getTotal_present() {
        return total_present;
    }

    public void setTotal_present(long total_present) {
        this.total_present = total_present;
    }

    public AttendenceModel() {

    }

    public AttendenceModel(String userid, String name, String rdate, String time, boolean status) {
        this.userid = userid;
        this.name = name;
        this.rdate = rdate;
        this.time = time;
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRdate() {
        return rdate;
    }

    public void setRdate(String rdate) {
        this.rdate = rdate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

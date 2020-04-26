package com.ngo.finder;

import android.os.Environment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by inspirin on 11/16/2017.
 */

public class Constant {
  public static final String SDCARD = Environment.getExternalStorageDirectory()+"/download/";
    public static final String STORAGE_PATH_UPLOADS = "upload/";
    //28.70322,77.12866666666667altitude=378.1
  public   static  String DB="wings_ngo";
  public   static  String subject_teacher="subject_teacher";
  public   static  double latitude=28.6778;
  public   static  double longitude=77.2613;
  public static double altitude=0;
    public static String user_attendence="users_attendence";
    public static String users="users";
    public static String attendence ="attendence";
    public static String single_chat="single_chat";
    public static String result="result";
    public static String faq="faq";
    public static String notification="notification";
    public static String requests="requests";
    public static String firebase_fcm_key="AIzaSyCK0P4r3iNL1bJO4e2EsOtd2FzYaDvLWOs";
    public static String upload="upload";
    public static String upload_video="upload_video";
    public static String eventData="eventData";

    public static String getDate() {
    DateFormat dateFormat = new SimpleDateFormat(
            "yyyy/MM/dd");

    Calendar cal = Calendar.getInstance();

    return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
  }
  public static String getTime() {
    DateFormat dateFormat = new SimpleDateFormat(
            "hh:mm:ss");

    Calendar cal = Calendar.getInstance();

    return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
  }public static String getDateTime() {
    DateFormat dateFormat = new SimpleDateFormat(
            "yyyy_MM_dd_hh_mm_ss");

    Calendar cal = Calendar.getInstance();

    return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
  }
}

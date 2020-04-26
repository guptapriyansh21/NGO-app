package com.ngo.finder;


public class EventData {
    String imageUrl,dated,album_name,description,latitude,longitude;
long upload_time;
String display_name;

    public EventData(String imageUrl, String dated, String album_name, String description, String latitude, String longitude, long upload_time, String display_name) {
        this.imageUrl = imageUrl;
        this.dated = dated;
        this.album_name = album_name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upload_time = upload_time;
        this.display_name = display_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(long upload_time) {
        this.upload_time = upload_time;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventData() {
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }
}
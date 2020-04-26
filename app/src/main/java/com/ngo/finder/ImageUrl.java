package com.ngo.finder;


public class ImageUrl {
    String imageUrl,dated,album_name,description;
long upload_time;
String display_name;

    public ImageUrl(String imageUrl, String dated, String album_name, String description, long upload_time,String display_name) {
        this.imageUrl = imageUrl;
        this.dated = dated;
        this.album_name = album_name;
        this.description = description;
        this.upload_time = upload_time;
        this.display_name = display_name;
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

    public ImageUrl() {
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
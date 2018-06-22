package com.maxime_malosse.mymarvelapp;

public class FeedItem {
    private String title;
    private String date;
    private String thumbnail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        String onSaleDate;
        onSaleDate = date.substring(0,27);
        return onSaleDate;
    }

    public void setDate(String date) { this.date = date; }
}

package com.maxime_malosse.mymarvelapp.JobClasses;

import java.util.ArrayList;
import java.util.List;

public class ComicItem {
    private String title;
    private String date;
    private String thumbnail;
    private String diamondCode;
    private List<Creators> creators = new ArrayList<>();
    private String webSite;
    private String copyright;
    private String verticalThumbnail;

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

    public String getVerticalThumbnail() {
        return verticalThumbnail;
    }

    public void setVerticalThumbnail(String verticalThumnail) {
        this.verticalThumbnail = verticalThumnail;
    }


    // Date output format
    public String getDate() {
        String onSaleDate;
        onSaleDate = date.substring(0,27);
        return onSaleDate;
    }


    public void setDate(String date) { this.date = date; }

    public String getDiamondCode() {
        return diamondCode;
    }

    public void setDiamondCode(String diamondCode) {
        this.diamondCode = diamondCode;
    }

    public List<Creators> getCreators() {
        return creators;
    }

    public void setCreators(List<Creators> creators) {
        this.creators = creators;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}

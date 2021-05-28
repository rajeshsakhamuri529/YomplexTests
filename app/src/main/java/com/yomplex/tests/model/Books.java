package com.yomplex.tests.model;

public class Books {

    private String Title;
    private String FolderName;
    private String Category;
    private String SourceUrl;
    private String Thumbnail;
    private String PublishedOn;
    private int readstatus;
    private int starredstatus;
    private int bookdownloadstatus;
    private String sortorder;
    private String version;
    private int copystatus;
    private int readfilestatus;

    public Books(String title, String category, String sourceurl, String thumbnail, String publishedon,int readstatus,int starredstatus,int downloadstatus,String sortorder,String version,int copystatus,int readfilestatus,String folderName){
        this.Title = title;
        this.Category = category;
        this.SourceUrl = sourceurl;
        this.Thumbnail = thumbnail;
        this.PublishedOn = publishedon;
        this.readstatus = readstatus;
        this.starredstatus = starredstatus;
        this.bookdownloadstatus = downloadstatus;
        this.sortorder = sortorder;
        this.version = version;
        this.readfilestatus = readfilestatus;
        this.copystatus = copystatus;
        this.FolderName = folderName;
    }

    public Books() {

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSourceUrl() {
        return SourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        SourceUrl = sourceUrl;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getPublishedOn() {
        return PublishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        PublishedOn = publishedOn;
    }

    public int getReadstatus() {
        return readstatus;
    }

    public void setReadstatus(int readstatus) {
        this.readstatus = readstatus;
    }

    public int getStarredstatus() {
        return starredstatus;
    }

    public void setStarredstatus(int starredstatus) {
        this.starredstatus = starredstatus;
    }

    public int getBookdownloadstatus() {
        return bookdownloadstatus;
    }

    public void setBookdownloadstatus(int bookdownloadstatus) {
        this.bookdownloadstatus = bookdownloadstatus;
    }

    public String getSortorder() {
        return sortorder;
    }

    public void setSortorder(String sortorder) {
        this.sortorder = sortorder;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCopystatus() {
        return copystatus;
    }

    public void setCopystatus(int copystatus) {
        this.copystatus = copystatus;
    }

    public int getReadfilestatus() {
        return readfilestatus;
    }

    public void setReadfilestatus(int readfilestatus) {
        this.readfilestatus = readfilestatus;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }
}

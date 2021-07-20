package com.example.cst_2335_final_project;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageData {

    //long is important for use with database
    private long id;
    private String date;
    private String explanation;
    private String hdUrlString;
    private String title;
    private String urlString;
    private String fileName;

    //private URL url;
    //private URL hdUrl;

    /* ImageData Constructor

       Initializes object with class data members as parameters.


     */

    ImageData(long id, String date, String explanation, String hdUrlString, String title, String urlString, String fileName ){

        this.id = id;
        this.date = date;
        this.explanation = explanation;
        this.hdUrlString = hdUrlString;
        this.title = title;
        this.urlString = urlString;
        this.fileName = fileName;
/*
        try {
            this.url = new URL(urlString);
            this.hdUrl = new URL(hdUrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
*/
    }



//Getter and setter methods for class members

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getHdUrlString() {
        return hdUrlString;
    }

    public void setHdUrlString(String hdUrlString) {
        this.hdUrlString = hdUrlString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }








}

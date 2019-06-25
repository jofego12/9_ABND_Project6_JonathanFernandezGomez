package com.example.abnd_project6_jonathanfernandezgomez;

public class News {

    private String title;
    private String time;
    private String section;
    private String URL;
    private String author;

    public News(String newsTitle, String newsTime, String newsSection, String newsURL, String newsAuthor) {
        title = newsTitle;
        time = newsTime;
        section = newsSection;
        URL = newsURL;
        author = newsAuthor;
    }

    public News(String newsTitle, String newsTime, String newsSection, String newsURL) {
        title = newsTitle;
        time = newsTime;
        section = newsSection;
        URL = newsURL;
    }

    public String getTitle(){return title;}
    public String getTime(){return time;}
    public String getSection(){return section;}
    public String getURL(){return URL;}
    public String getAuthor(){return author;}
}
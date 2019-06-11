package com.example.abnd_project6_jonathanfernandezgomez;

public class News {

    public final String title;

    public final long time;

    public final String section;

    public News(String newsTitle, long newsTime, String newsSection) {
        title = newsTitle;
        time = newsTime;
        section = newsSection;
    }
}
/*
The following code has being created using Udacity's ud843-QuakeReport (https://github.com/udacity/ud843-QuakeReport)
and ud843_Soonami (https://github.com/udacity/ud843_Soonami) apps as a templates.
So it may be possible to find some similarities between looking at variable names or styles.
*/

package com.example.abnd_project6_jonathanfernandezgomez;

public class News {

    private String title;
    private String title2;
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

    public String getTitle() {
        return title;
    }
    public String getTitle2() {
        return title2;
    }

    public String getTime() {
        return time;
    }

    public String getSection() {
        return section;
    }

    public String getURL() {
        return URL;
    }

    public String getAuthor() {
        return author;
    }
}
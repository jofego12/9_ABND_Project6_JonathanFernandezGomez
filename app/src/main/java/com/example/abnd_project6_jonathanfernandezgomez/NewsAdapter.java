/*
The following code has being created using Udacity's ud843-QuakeReport (https://github.com/udacity/ud843-QuakeReport)
and ud843_Soonami (https://github.com/udacity/ud843_Soonami) apps as a templates.
So it may be possible to find some similarities between looking at variable names or styles.
*/

package com.example.abnd_project6_jonathanfernandezgomez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context mainContext) {
        super(mainContext, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_items, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView sectionView = listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(currentNews.getTime());

        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setText(currentNews.getAuthor());

        return listItemView;
    }
}
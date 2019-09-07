/*
The following code has being created using Udacity's ud843-QuakeReport (https://github.com/udacity/ud843-QuakeReport)
and ud843_Soonami (https://github.com/udacity/ud843_Soonami) apps as a templates.
So it may be possible to find some similarities between looking at variable names or styles.
*/

package com.example.abnd_project6_jonathanfernandezgomez;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final int NEWS_LOADER_ID = 1;
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    Context mainContext;
    NewsAdapter adapter;
    public TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        mainContext = this;
        adapter = new NewsAdapter(this);

        final ListView newsListView = findViewById(R.id.list);
        newsListView.setAdapter(adapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                @SuppressWarnings("unchecked")
                News currentNews = (News) newsListView.getItemAtPosition(i);
                Uri newsUri = Uri.parse(currentNews.getURL());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsLoader(this, uriBuilder.toString(), MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        } else {
            mEmptyStateTextView = findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.empty_data);        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
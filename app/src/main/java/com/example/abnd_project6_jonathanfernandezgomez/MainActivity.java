package com.example.abnd_project6_jonathanfernandezgomez;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    Context mainContext;
    NewsAdapter adapter;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int NEWS_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        mainContext = this;
        adapter = new NewsAdapter(this);
        NewsAsyncTask task = new NewsAsyncTask();
        task.execute();

        final ListView newsListView = findViewById(R.id.list);
        final NewsAdapter adapter = new NewsAdapter(this);
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

        android.app.LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

  /*  @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, USGS_REQUEST_URL);
    }
*/

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();

    }

    /*class NewsAsyncTask extends AsyncTask<URL, Void, ArrayList<News>> {
        @Override
        protected ArrayList<News> loadInBackground(URL... urls) {
            URL url = createUrl(USGS_REQUEST_URL);

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            ArrayList<News> news = extractResultsFromJson(jsonResponse);
            return news;
        }*/

        private URL createUrl(String stringUrl) {
            URL url;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        public ArrayList<News> extractResultsFromJson(String newsJSON) {

            final ArrayList<News> footballNews = new ArrayList<>();

            try {
                JSONObject baseResponseObject = new JSONObject(newsJSON);
                JSONObject baseJsonResponse = baseResponseObject.getJSONObject("response");
                JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
                /*JSONObject resultsKey = baseJsonResponse.getJSONObject("results");
                JSONArray authorsArray = resultsKey.getJSONArray("tags");*/

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject firstResult = resultsArray.getJSONObject(i);/*
                    JSONObject authorResult = authorsArray.getJSONObject(0);*/

                    String title = firstResult.getString("webTitle");
                    String time = firstResult.getString("webPublicationDate").substring(0, 10);
                    String section = firstResult.getString("sectionName");
                    String URL = firstResult.getString("webUrl");
/*
                    String author = authorResult.getString("webTitle");
*/

             /*     if (authorResult.getString("webTitle") != null) {

                        News news = new News(title, time, section, URL, author);
                        footballNews.add(news);
                    } else {
                        News news = new News(title, time, section, URL);
                        footballNews.add(news);
                    }*/
                    News news = new News(title, time, section, URL);
                    footballNews.add(news);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    NewsAdapter adapter = new NewsAdapter(mainContext, footballNews);
                    ListView listView = findViewById(R.id.list);
                    listView.setAdapter(adapter);
                }
            });

            return footballNews;
        }
    }
}
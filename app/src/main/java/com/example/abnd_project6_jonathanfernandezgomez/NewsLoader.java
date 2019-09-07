/*
The following code has being created using Udacity's ud843-QuakeReport (https://github.com/udacity/ud843-QuakeReport)
and ud843_Soonami (https://github.com/udacity/ud843_Soonami) apps as a templates.
So it may be possible to find some similarities between looking at variable names or styles.
*/

package com.example.abnd_project6_jonathanfernandezgomez;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

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

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();
    public TextView mEmptyStateTextView;
    private String mUrl;
    public MainActivity mainActivityContext;

    public NewsLoader(Context context, String url, MainActivity mainActivityContext) {
        super(context);
        mUrl = url;
        mEmptyStateTextView = mainActivityContext.findViewById(R.id.empty_text_view);
        this.mainActivityContext = mainActivityContext;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        URL url = createUrl(mUrl);

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            mEmptyStateTextView = mainActivityContext.findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.no_request);
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<News> news = extractResultsFromJson(jsonResponse);
        return news;
    }

    private URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            mEmptyStateTextView = mainActivityContext.findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.no_data_available);
            Log.e(LOG_TAG, "Problem building the URL ", exception);
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
            mEmptyStateTextView = mainActivityContext.findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.no_displaying);
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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
        } else {
            mEmptyStateTextView = mainActivityContext.findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.no_input);
            Log.e("readingError", "Problem to receive data.");
        }
        return output.toString();
    }

    public ArrayList<News> extractResultsFromJson(String newsJSON) {

        final ArrayList<News> footballNews = new ArrayList<>();

        try {
            JSONObject baseResponseObject = new JSONObject(newsJSON);
            JSONObject baseJsonResponse = baseResponseObject.getJSONObject("response");
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject firstResult = resultsArray.getJSONObject(i);
                JSONArray authorsArray = firstResult.getJSONArray("tags");
                JSONObject authorResult = authorsArray.getJSONObject(0);

                String title = firstResult.getString("webTitle");
                String time = firstResult.getString("webPublicationDate").substring(0, 10);
                String section = firstResult.getString("sectionName");
                String URL = firstResult.getString("webUrl");

                String author = authorResult.getString("webTitle");

                if (authorResult.getString("webTitle") != null) {
                    News news = new News(title, time, section, URL, author);
                    footballNews.add(news);
                }
                News news = new News(title, time, section, URL);
                footballNews.add(news);
            }
        } catch (JSONException e) {
            mEmptyStateTextView = mainActivityContext.findViewById(R.id.empty_text_view);
            mEmptyStateTextView.setText(R.string.no_parsing);
            Log.e("NewsArrayError", "Problem parsing the news JSON results", e);
        }
        return footballNews;
    }
}
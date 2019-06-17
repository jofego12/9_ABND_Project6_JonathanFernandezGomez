package com.example.abnd_project6_jonathanfernandezgomez;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    Context mainContext;

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?q=soccer&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        mainContext = this;

        NewsAsyncTask task = new NewsAsyncTask();
        task.execute();

    }

    class NewsAsyncTask extends AsyncTask<URL, Void, ArrayList<News>> {
        @Override
        protected ArrayList<News> doInBackground(URL... urls) {
            URL url = createUrl(USGS_REQUEST_URL);

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            ArrayList<News> news = extractResultsFromJson(jsonResponse);
            return news;
        }

        @Override
        protected void onPostExecute(ArrayList<News> news) {
            if (news == null) {
                return;
            }
        }

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
                JSONObject baseJsonResponse = new JSONObject(newsJSON);
                JSONArray resultsArray = baseJsonResponse.getJSONArray("response");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject firstResult = resultsArray.getJSONObject(i);

                    JSONObject firstNews = firstResult.getJSONObject("results");

                    String title = firstNews.getString("webTitle");
                    String time = firstNews.getString("webPublicationDate").substring(0, 10);
                    String section = firstNews.getString("sectionName");
                    String URL = firstNews.getString("webUrl");

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
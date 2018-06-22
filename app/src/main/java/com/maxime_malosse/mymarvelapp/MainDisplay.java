package com.maxime_malosse.mymarvelapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainDisplay extends AppCompatActivity {

    private static final String TAG = "RecyclerViewExample";

    private List<FeedItem> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        String url = "https://gateway.marvel.com:443/v1/public/comics?format=comic&dateDescriptor=lastWeek&ts=1524161673&apikey=2fb3c607374cd614f32c819c48e9db0c&hash=4da7ecb9bd380ff6092e35da2a123cc7";
        new DownloadTask().execute(url);
    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // HTTP OK
                if (statusCode == 200) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!"
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerViewAdapter(MainDisplay.this, feedsList);
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(FeedItem item) {
                        Toast.makeText(MainDisplay.this, item.getTitle(), Toast.LENGTH_LONG).show();

                    }
                });

            } else {
                Toast.makeText(MainDisplay.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            Log.d(TAG, response.toString());

            JSONObject datas = response.optJSONObject("data");
            Log.d(TAG, datas.toString());

            JSONArray results = datas.optJSONArray("results");
            Log.d(TAG, results.toString());

            feedsList = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                FeedItem item = new FeedItem();
                JSONObject post = results.optJSONObject(i);
                JSONArray dates = post.optJSONArray("dates");
                JSONObject onSaleDate = dates.getJSONObject(0);
                item.setDate(getResources().getString(R.string.date) + "\n" + onSaleDate.optString("date"));
                Log.d(TAG, onSaleDate.toString());
                JSONArray images = post.getJSONArray("images");
                JSONObject image = images.optJSONObject(0);
                Log.d(TAG, "IMAGE : " + image.toString());
                item.setTitle(post.optString("title"));
                item.setThumbnail(image.optString("path")+"/landscape_xlarge.jpg");

                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

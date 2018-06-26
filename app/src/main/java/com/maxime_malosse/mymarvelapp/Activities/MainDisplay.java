package com.maxime_malosse.mymarvelapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxime_malosse.mymarvelapp.JobClasses.ComicItem;
import com.maxime_malosse.mymarvelapp.JobClasses.Creators;
import com.maxime_malosse.mymarvelapp.R;
import com.maxime_malosse.mymarvelapp.RecyclerView.MyRecyclerViewAdapter;
import com.maxime_malosse.mymarvelapp.RecyclerView.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainDisplay extends AppCompatActivity {

    private static final String TAG = "MyMarvelTAG";

    private List<ComicItem> feedsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView waiting;
    private Toolbar toolbar;
    private static final String url = "https://gateway.marvel.com:443/v1/public/comics?format=comic&dateDescriptor=lastWeek&ts=1524161673&apikey=2fb3c607374cd614f32c819c48e9db0c&hash=4da7ecb9bd380ff6092e35da2a123cc7";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

        // Initializing ToolBar
        initToolbar();

        // Initializing
        initViews();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // launching the AsynchTask
        new DownloadTask().execute(url);
    }

    public class DownloadTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            waiting.setVisibility(View.VISIBLE);
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
                    result = 1; // Successfully fetched data
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
            waiting.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);

            if (result == 1) {
                adapter = new MyRecyclerViewAdapter(feedsList);
                mRecyclerView.setAdapter(adapter);

                mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ComicItem item = feedsList.get(position);
                        // passing data to the DetailDisplay Activity
                        Intent intent = new Intent(MainDisplay.this, DetailDisplay.class);
                        intent.putExtra("title", item.getTitle());
                        intent.putExtra("thumbnail", item.getThumbnail());
                        intent.putExtra("date", item.getDate());
                        intent.putExtra("creators", (Serializable) item.getCreators());
                        intent.putExtra("diamondCode", item.getDiamondCode());
                        intent.putExtra("webSite", item.getWebSite());
                        intent.putExtra("copyright", item.getCopyright());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        // Not used here
                    }
                }));

                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainDisplay.this, R.string.FailedFetch, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            // Principal object
            JSONObject response = new JSONObject(result);
            Log.d(TAG, response.toString());

            // Copyright for pictures
            String copyright = response.getString("attributionText");

            // the whole block of data
            JSONObject datas = response.optJSONObject("data");
            Log.d(TAG, datas.toString());
            JSONArray results = datas.optJSONArray("results");
            Log.d(TAG, results.toString());

            for (int i = 0; i < results.length(); i++) {
                ComicItem item = new ComicItem();
                //ReclyclerView principals data :
                item.setCopyright(copyright);

                JSONObject post = results.optJSONObject(i);
                JSONArray dates = post.optJSONArray("dates");
                JSONObject onSaleDate = dates.getJSONObject(0);
                item.setDate(getResources().getString(R.string.date) + "\n" + onSaleDate.optString("date"));
                Log.d(TAG, "DATE : " + onSaleDate.toString());

                JSONArray images = post.getJSONArray("images");
                Log.d(TAG, "IMAGES : " + images.toString());
                JSONObject image = images.optJSONObject(0);
                item.setThumbnail(image.optString("path")+"/landscape_incredible.jpg");
                Log.d(TAG, "IMAGE : " + image.toString());

                item.setTitle(post.optString("title"));

                //DetailView extra data
                item.setDiamondCode(post.getString("diamondCode"));
                JSONArray urls = post.getJSONArray("urls");
                Log.d(TAG, "URLS : " + urls.toString());
                JSONObject url = urls.getJSONObject(0);
                Log.d(TAG, "URL : " + url.toString());
                item.setWebSite(url.getString("url"));
                JSONObject creators = post.getJSONObject("creators");
                Log.d(TAG, "CREATORS : " + creators.toString());
                JSONArray items = creators.getJSONArray("items");
                // generate a Creator Object for the list of comic creators and their roles
                List<Creators> creatorsList = new ArrayList<>();
                for(int j = 0; j<items.length(); j++) {
                    Creators creatorEach = new Creators();
                    creatorEach.setName(items.getJSONObject(j).getString("name"));
                    creatorEach.setRole(items.getJSONObject(j).getString("role"));
                    creatorsList.add(creatorEach);
                }

                item.setCreators(creatorsList);
                feedsList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView back = new ImageView(this);
        back.setImageResource(R.mipmap.ic_back2);
        Toolbar.LayoutParams param1 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        param1.gravity = Gravity.END;
        back.setLayoutParams(param1);
        toolbar.addView(back);

        TextView title = new TextView(this);
        title.setText(R.string.last_week_display);
        title.setTextColor(Color.WHITE);
        title.setTextSize(25f);
        Toolbar.LayoutParams param3 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        param3.gravity = Gravity.CENTER;
        title.setLayoutParams(param3);
        toolbar.addView(title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        waiting = (TextView) findViewById(R.id.tvWaiting);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

}

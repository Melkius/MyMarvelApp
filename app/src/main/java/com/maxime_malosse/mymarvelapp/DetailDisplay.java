package com.maxime_malosse.mymarvelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

public class DetailDisplay extends AppCompatActivity {

    private TextView title;
    private TextView date;
    private TextView diamondCode;
    private ImageView image;
    private TextView creatorsView;
    private TextView copyright;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_display);

        // Initialising Views
        title = (TextView) findViewById(R.id.tvTitle);
        date = (TextView) findViewById(R.id.tvDate);
        diamondCode = (TextView) findViewById(R.id.tvDiamondCode);
        image = (ImageView) findViewById(R.id.ivComic);
        creatorsView = (TextView) findViewById(R.id.tvCreators);
        copyright =(TextView) findViewById(R.id.tvCopyright);

        // Grab Intent extra data and filling Views
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        diamondCode.setText(intent.getStringExtra("diamondCode"));
        copyright.setText(intent.getStringExtra("copyright"));
        url = intent.getStringExtra("webSite");
        List<Creators> creatorsList = (List<Creators>) intent.getSerializableExtra("creators");

        // Using Picasso to fill the ImageView
        if (!TextUtils.isEmpty(intent.getStringExtra("thumbnail"))) {
            Picasso.get().load(intent.getStringExtra("thumbnail"))
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(image);
        }

        // creating Iterator for the creatorsList
        Iterator<Creators> iter = creatorsList.iterator();

        // Filling Creators View
        while(iter.hasNext()){
            Creators creators;
            creators = iter.next();
            creatorsView.append("- " + creators.getName() + "\n");
            creatorsView.append("    " + creators.getRole() + "\n");
            creatorsView.append("\n");
        }
    }

    // Redirection method to the Marvel webSite
    public void onClickWebSite(View view) {
        Uri uri = Uri.parse(url);
        Intent intentWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intentWeb);
    }
}

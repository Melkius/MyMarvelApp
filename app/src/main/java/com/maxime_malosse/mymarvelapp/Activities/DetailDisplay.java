package com.maxime_malosse.mymarvelapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxime_malosse.mymarvelapp.JobClasses.Creators;
import com.maxime_malosse.mymarvelapp.R;
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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_display);

        // Initializing ToolBar
        initToolbar();

        // Initializing Views
        initViews();

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

    public void initToolbar() {
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

        ImageView share = new ImageView(this);
        share.setImageResource(R.mipmap.ic_share);
        Toolbar.LayoutParams param2 = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        param2.gravity = Gravity.END;
        param2.rightMargin = 20;
        share.setLayoutParams(param2);
        toolbar.addView(share);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                i.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(Intent.createChooser(i, "Share Comics"));
            }
        });
    }

    public void initViews(){
        title = (TextView) findViewById(R.id.tvTitle);
        date = (TextView) findViewById(R.id.tvDate);
        diamondCode = (TextView) findViewById(R.id.tvDiamondCode);
        image = (ImageView) findViewById(R.id.ivComic);
        creatorsView = (TextView) findViewById(R.id.tvCreators);
        copyright =(TextView) findViewById(R.id.tvCopyright);
    }
}

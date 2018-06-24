package com.maxime_malosse.mymarvelapp;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

    private List<ComicItem> comicItemList;
    private ComicItem comicItem;

    public MyRecyclerViewAdapter(List<ComicItem> comicItemList) {
        this.comicItemList = comicItemList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        comicItem = comicItemList.get(i);

        // Using Picasso to fill the ImageView
        if (!TextUtils.isEmpty(comicItem.getThumbnail())) {
            Picasso.get().load(comicItem.getThumbnail())
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(customViewHolder.imageView);
        }

        // Setting text view title, date and copyright
        customViewHolder.title.setText(Html.fromHtml(comicItem.getTitle()));
        customViewHolder.date.setText(Html.fromHtml(comicItem.getDate()));
        customViewHolder.copyright.setText(Html.fromHtml(comicItem.getCopyright()));
    }

    @Override
    public int getItemCount() {
        return comicItemList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView title;
        protected TextView date;
        private TextView copyright;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
            this.date = (TextView) view.findViewById(R.id.date);
            this.copyright = (TextView) view.findViewById(R.id.copyright);
        }
    }

}

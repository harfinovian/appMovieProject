package com.example.harfi.appprojectmovie3.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.activity.DetailsActivity;
import com.example.harfi.appprojectmovie3.model.Results;

import io.realm.RealmResults;

public class RecyclerViewHolders
        extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public ImageView moviePhoto;
    private RealmResults<Results> tempData;

    public RecyclerViewHolders(View itemView, RealmResults<Results> temp) {
        super(itemView);
        this.tempData = temp;
        itemView.setOnClickListener(this);
        moviePhoto = (ImageView)itemView.findViewById(R.id.movie_photo);
    }

    @Override
    public void onClick(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), DetailsActivity.class)
            .putExtra("movieId",tempData.get(getPosition()).getId()));
    }
}
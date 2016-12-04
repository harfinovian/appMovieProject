package com.example.harfi.appprojectmovie3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.model.Results;
import com.facebook.stetho.inspector.protocol.module.Console;

import io.realm.RealmResults;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private Context context;
    private RealmResults<Results> temp;

    public RecyclerViewAdapter(Context context, RealmResults<Results> data) {
        this.temp = data;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView, temp);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        Glide.with(context).load(temp.get(position).getPoster_path())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePhoto);
    }
    @Override
    public int getItemCount() {
        return this.temp.size();
    }
}

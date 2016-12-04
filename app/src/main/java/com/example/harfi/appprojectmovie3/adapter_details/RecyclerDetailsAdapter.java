package com.example.harfi.appprojectmovie3.adapter_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.model.VideoDetails;

import io.realm.RealmResults;

/**
 * Created by harfi on 11/18/2016.
 */

public class RecyclerDetailsAdapter extends RecyclerView.Adapter<RecyclerDetailsHolders>{

    private Context context;
    private RealmResults<VideoDetails> itemList;

    public RecyclerDetailsAdapter(Context context , RealmResults<VideoDetails> item){
        this.context = context;
        this.itemList = item;
    }
    @Override
    public RecyclerDetailsHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_trailer_list, parent, false);
        RecyclerDetailsHolders rcv = new RecyclerDetailsHolders(layoutView, itemList);
        return rcv;
    }
    @Override
    public void onBindViewHolder(RecyclerDetailsHolders holder, int position) {
        holder.playBtn.setText("Trailer " + (position + 1));
    }

    @Override
    public int getItemCount() {return this.itemList.size();}
}

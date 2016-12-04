package com.example.harfi.appprojectmovie3.adapter_details;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.model.VideoDetails;

import io.realm.RealmResults;

/**
 * Created by harfi on 11/18/2016.
 */

public class RecyclerDetailsHolders
        extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    public Button playBtn;
    private RealmResults<VideoDetails> videoUrl;

    public RecyclerDetailsHolders(View itemView, RealmResults<VideoDetails> temp){
        super(itemView);
        this.videoUrl = temp;
        playBtn = (Button)itemView.findViewById(R.id.trailer_item);
        playBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.youtube.com/watch?v=" + videoUrl.get(getPosition()).getKey()));
        view.getContext().startActivity(i);
        Toast.makeText(view.getContext(), "Click Test",Toast.LENGTH_SHORT);
    }
}

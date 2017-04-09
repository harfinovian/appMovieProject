package com.example.harfi.appprojectmovie3.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.adapter_details.RecyclerDetailsAdapter;
import com.example.harfi.appprojectmovie3.model.VideoDetails;
import com.example.harfi.appprojectmovie3.model.VideoList;
import com.example.harfi.appprojectmovie3.model.api.MovieService;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by harfi on 11/25/2016.
 */

public class TrailerList extends Fragment {

    RecyclerView rView;
    View myFragment;
    Realm realm = Realm.getDefaultInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.recycler_trailer_list, container, false);
        Long id = getArguments().getLong("id");

        LinearLayoutManager lLayout = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        lLayout.setOrientation(LinearLayoutManager.VERTICAL);

        rView = (RecyclerView)myFragment.findViewById(R.id.recycler_view_trailer);
        rView.setLayoutManager(lLayout);
        rView.setHasFixedSize(true);
        rView.setFocusable(false);

        Log.e("Test","testmasuk");
        getAllVideoData(id);

        return myFragment;
    }
    @Override
    public void onAttach(Activity activity) {super.onAttach(activity);}

    @Override
    public void onDetach() {super.onDetach();}

    private void getAllVideoData(final Long id){

        final RealmResults<VideoDetails> query = realm.where(VideoDetails.class)
                .equalTo("movieId",id)
                .findAll();

        if(query.isEmpty()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.themoviedb.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieService movieService = retrofit.create(MovieService.class);

            movieService.getVideo(id).enqueue(new Callback<VideoList>() {
                @Override
                public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                    VideoList videoList = response.body();

                    realm.beginTransaction();
                    for (int i = 0; i < videoList.getResults().size(); i++) {
                        VideoDetails video = realm.createObject(VideoDetails.class);
                        video.setMovieId(id);
                        video.setKey(videoList.getResults().get(i).getKey());
                    }
                    realm.commitTransaction();

                    RealmResults<VideoDetails> temp = realm.where(VideoDetails.class)
                            .equalTo("movieId",id)
                            .findAll();

                    fillLayout(temp);
                    Log.e("videoList", videoList.toString());
                }
                @Override
                public void onFailure(Call<VideoList> call, Throwable t) {
                    Log.e("gagal", t.getMessage());
                }
            });
        }else{
            Log.e("cacheVideo", query.toString());
            fillLayout(query);
        }
    }
    private void fillLayout(RealmResults<VideoDetails> temp){
        RecyclerDetailsAdapter rcAdapter = new RecyclerDetailsAdapter(getActivity(), temp);
        rView.setAdapter(rcAdapter);
    }
}
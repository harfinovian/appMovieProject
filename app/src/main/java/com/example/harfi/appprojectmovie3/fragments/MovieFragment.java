package com.example.harfi.appprojectmovie3.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.adapter.RecyclerViewAdapter;
import com.example.harfi.appprojectmovie3.model.Report;
import com.example.harfi.appprojectmovie3.model.Results;
import com.example.harfi.appprojectmovie3.model.api.MovieService;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieFragment extends Fragment{

    private View myFragment;
    private ProgressDialog pDialog;
    private RecyclerView rView;
    RecyclerViewAdapter rcAdapter;
    private SwipeRefreshLayout refreshLayout;
    public boolean refreshData, viewCreated;
    Realm realm = Realm.getDefaultInstance();

    public MovieFragment() {
        // Required empty public constructor
    }
    public MovieFragment popular(String category){
        MovieFragment myFragment = new MovieFragment();

        Bundle args = new Bundle();
        args.putString("category", category);
        myFragment.setArguments(args);

        return myFragment;
    }
    public MovieFragment top_rated(String category){
        MovieFragment myFragment = new MovieFragment();

        Bundle args = new Bundle();
        args.putString("category", category);
        myFragment.setArguments(args);

        return myFragment;
    }
    public MovieFragment favorite(String category){
        MovieFragment myFragment = new MovieFragment();

        Bundle args = new Bundle();
        args.putString("category", category);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myFragment = inflater.inflate(R.layout.fragment_movie, container, false);

        pDialog = new ProgressDialog(getContext());

        GridLayoutManager lLayout = new GridLayoutManager(getContext(), 2);

        rView = (RecyclerView)myFragment.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setItemViewCacheSize(20);
        rView.setDrawingCacheEnabled(true);
        rView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rView.setLayoutManager(lLayout);
        rView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout = (SwipeRefreshLayout) myFragment.findViewById(R.id.refreshContainer);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData = true;
                getAllItemList();
            }
        });

        return myFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllItemList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreated = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (isVisibleToUser && viewCreated) {
            // If we are becoming invisible, then...
            getAllItemList();
        }
    }

    private void getAllItemList(){

        if (!refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }

        if(!getArguments().getString("category").equals("favorite")) {
            MovieService movie = retrofitConnect();
            movie.getMovie(getArguments().getString("category")).enqueue(new Callback<Report>() {
                @Override
                public void onResponse(Call<Report> call, Response<Report> response) {
                    final Report report = response.body();

                    Log.e("factory", report.getResults().get(0).getPoster_path().toString());

                    RealmResults<Results> results = getResult();

                    realm.beginTransaction();
                    results.deleteAllFromRealm();
                    for (int i = 0; i < report.getResults().size(); i++) {
                        Results insert = realm.createObject(Results.class);
                        insert.setId(report.getResults().get(i).getId());
                        insert.setPoster_path(report.getResults().get(i).getPoster_path());
                        insert.setCategory(getArguments().getString("category"));
                    }
                    realm.commitTransaction();
                }

                @Override
                public void onFailure(Call<Report> call, Throwable t) {
                    Log.e("factory", "gagal " + t.getMessage());
                    Toast.makeText(getContext(), "Internetnya Gak Ada", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(rView.getAdapter() == null || rcAdapter == null || refreshData  == true) {
            rcAdapter = new RecyclerViewAdapter(getContext(), getResult());
            rView.setAdapter(rcAdapter);
            Log.e("Database", "data : " + realm.where(Results.class).findAll());
        }else if(rcAdapter != null){
            rcAdapter.setTemp(getResult());
        }

        refreshData = false;
        refreshLayout.setRefreshing(false);
    }
    private MovieService retrofitConnect(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieTemp = retrofit.create(MovieService.class);

        return movieTemp;
    }
    private RealmResults<Results> getResult(){

        RealmResults<Results> results = realm.where(Results.class)
                .equalTo("category",getArguments().getString("category"))
                .findAll();

        return results;
    }
}

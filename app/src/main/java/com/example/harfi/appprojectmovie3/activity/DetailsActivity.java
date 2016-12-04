package com.example.harfi.appprojectmovie3.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.harfi.appprojectmovie3.R;
import com.example.harfi.appprojectmovie3.fragments.TrailerList;
import com.example.harfi.appprojectmovie3.model.MovieDetails;
import com.example.harfi.appprojectmovie3.model.Results;
import com.example.harfi.appprojectmovie3.model.api.MovieService;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by harfi on 11/15/2016.
 */

public class DetailsActivity extends AppCompatActivity {

    public static String BASE_URL = "http://api.themoviedb.org/";
    public static String IMG_URL = "http://image.tmdb.org/t/p/w500";
    private ProgressDialog pDialog;
    private CheckBox favoriteBt;
    private Bundle b;
    private Call<MovieDetails> call;
    Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_detail);
        toolbar.setTitleTextColor(-1);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);

        getAllDetailData();
        getDisplayListVideo(b.getLong("movieId"));
    }

    private void getDisplayListVideo(Long id){
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);

        android.support.v4.app.Fragment fragment = new TrailerList();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body , fragment);
        fragmentTransaction.commit();
    }

    private void getAllDetailData(){
        pDialog.setMessage("Loading Content...");
        pDialog.show();

        Intent i = getIntent();
        b = i.getExtras();

        MovieDetails query = realm.where(MovieDetails.class)
                .equalTo("id",b.getLong("movieId"))
                .findFirst();

        if(query == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieService movieService = retrofit.create(MovieService.class);

            call = movieService.getDetails(b.getLong("movieId"));
            call.enqueue(new Callback<MovieDetails>() {
                @Override
                public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                    MovieDetails Details = response.body();
                    Log.e("Test", "Test" + Details.getId().toString());

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(Details);
                    realm.commitTransaction();

                    fillLayout(realm.where(MovieDetails.class)
                            .equalTo("id",b.getLong("movieId"))
                            .findFirst());
                }
                @Override
                public void onFailure(Call<MovieDetails> call, Throwable t) {
                    Log.e("factory", "gagal " + t.getMessage());
                }
            });
        }else{
            fillLayout(query);
            Log.e("Test","test data = "+query.getPoster_path());
        }
        pDialog.hide();
    }
    @Override
    public boolean onSupportNavigateUp() {
        if(call != null) call.cancel();
        onBackPressed();
        return true;
    }

    private void fillLayout(final MovieDetails temp){

        final ImageView movieImg = (ImageView)findViewById(R.id.moviePic);
        TextView title = (TextView)findViewById(R.id.title);
        TextView average = (TextView)findViewById(R.id.ratingTx);
        TextView overview = (TextView)findViewById(R.id.desc);
        TextView year = (TextView)findViewById(R.id.yearTx);
        TextView runtime = (TextView)findViewById(R.id.timeTx);

        Glide.with(DetailsActivity.this)
                .load(IMG_URL + temp.getPoster_path())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(movieImg);

        title.setText(temp.getOriginal_title());
        average.setText(temp.getVote_average().toString()+"/10");
        overview.setText(temp.getOverview());
        year.setText(temp.getRelease_date().substring(0,4));
        runtime.setText(temp.getRuntime().toString()+"min");

        favoriteBt = (CheckBox) findViewById(R.id.favorite);
        favoriteBt.setChecked(checked(temp.getId()));
        favoriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                switch(arg0.getId()) {
                    case R.id.favorite:
                        if(checked(temp.getId()) == false) {
                            favoriteBt.setChecked(true);
                            favoriteBt.setBackgroundColor(getResources().getColor(R.color.colorDetailComponent));
                            Toast.makeText(DetailsActivity.this,"Favorited", Toast.LENGTH_SHORT).show();
                            PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this).edit()
                                    .putBoolean(temp.getId().toString(), true).commit();

                            realm.beginTransaction();
                            Results insert = realm.createObject(Results.class);
                            insert.setId(temp.getId());
                            insert.setPoster_path(temp.getPoster_path());
                            insert.setCategory("favorite");
                            realm.commitTransaction();

                        }else {
                            favoriteBt.setChecked(false);
                            favoriteBt.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
                            Toast.makeText(DetailsActivity.this,"Unfavorited", Toast.LENGTH_SHORT).show();
                            PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this).edit()
                                    .putBoolean(temp.getId().toString(), false).commit();

                            Results results = realm.where(Results.class)
                                                    .equalTo("id", temp.getId())
                                                    .equalTo("category", "favorite")
                                                    .findFirst();
                            Log.d("delete", results.toString());
                            realm.beginTransaction();
                            results.deleteFromRealm();
                            realm.commitTransaction();
                        }
                        break;
                }
            }
        });
    }
    public Boolean checked(Long tempId){
        boolean checked = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this)
                .getBoolean(tempId.toString(), false);

        if(checked == true){
            favoriteBt.setBackgroundColor(getResources().getColor(R.color.colorDetailComponent));
        }else{
            favoriteBt.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        }
        return checked;
    }
}

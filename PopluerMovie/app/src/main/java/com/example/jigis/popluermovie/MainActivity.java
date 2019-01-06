package com.example.jigis.popluermovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jigis.popluermovie.Adapter.RecylerAdapter;
import com.example.jigis.popluermovie.ApiCall.ApiCall;
import com.example.jigis.popluermovie.Model.Movie;
import com.example.jigis.popluermovie.Model.MovieResponse;
import com.example.jigis.popluermovie.Retrofit.ApiClient;
import com.example.jigis.popluermovie.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rec_movies)
    RecyclerView recyclerView;
    RecylerAdapter recylcerAdapter;

    /*
     *
     * progressDialog
     *
     * */
    ProgressDialog progressDoalog;
    public static final String LOG_TAG = MainActivity.class.getName();

    //Call interface
    ApiInterface apiInterface;

    //layout manager
    RecyclerView.LayoutManager layoutManager;
    List<Movie> moviesList ;
    public static boolean menuitem_selected = true;


    //save instance
    private static final String movieinstance = "movieInstance";

    @BindView(R.id.error_tv)
    TextView error_tv;
    @BindView(R.id.error_img)
    ImageView img_error;
    @BindView(R.id.error_btn)
    Button error_btn;
    @BindView(R.id.ly_layout)
    LinearLayout mLinearLayout;

    private SharedPreferences preferences;
    private Menu mOptionsMenu;
    @BindView(R.id.my_toolbar)
    Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ButterKnife.bind(this);
        initview(savedInstanceState);
        setSupportActionBar(mTopToolbar);
    }


    //save instance
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putParcelableArrayList(movieinstance, (ArrayList<? extends Parcelable>) moviesList);

     }

    //init method
    private void initview(Bundle savedInstanceState) {
      if (savedInstanceState!=null){
          moviesList=savedInstanceState.getParcelableArrayList(movieinstance);
            ConfigurationOrientation();
            recylcerAdapter = new RecylerAdapter(MainActivity.this, moviesList);
          recyclerView.setAdapter(recylcerAdapter);
          recylcerAdapter.notifyDataSetChanged();


      }
      else {
          if (InternetConnection.checkConnection(this)) {

              if (menuitem_selected == true) {
                  loadPopularMoviesData();
              } else {
                  loadTopratedMoviesData();
              }
          } else {
              showErrorMessage();
          }
      }
    }

    // error method
    private void showErrorMessage() {
        mLinearLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    //request To server
    public void reuqestToServer(Call<MovieResponse> movieCall) {
        progreDolog();
        try {
            movieCall.enqueue(new Callback<MovieResponse>() {

                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {


                    if (response.isSuccessful() && !response.body().getResults().isEmpty()) {
                        moviesList = response.body().getResults();
                        Log.e("log", "" + response.body().getResults());
                    }


                    recylcerAdapter = new RecylerAdapter(MainActivity.this, moviesList);
                    recyclerView.setAdapter(recylcerAdapter);
                    progressDoalog.dismiss();
                    mLinearLayout.setVisibility(View.GONE);
                    ConfigurationOrientation();
                    recylcerAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    progressDoalog.dismiss();
                    Toast.makeText(getApplicationContext(), "Faild to load the data", Toast.LENGTH_SHORT).show();
                    showErrorMessage();

                }
            });
        } catch (Exception e) {
            progressDoalog.dismiss();
            Toast.makeText(getApplicationContext(), "Internet Connection lose.", Toast.LENGTH_SHORT).show();
            showErrorMessage();

        }

    }

    // Configuration  Orientation
    private void ConfigurationOrientation() {
        final int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, spanCount));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    //progress dialog
    private void progreDolog() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Loading Data ...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        progressDoalog.show();

    }

    //Popular Movies
    public void loadPopularMoviesData() {

        setTitle(R.string.popular_movies);
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<MovieResponse> call = apiInterface.getPopularMovies(ApiCall.API_KEY);
        reuqestToServer(call);

    }
    //Load Top rated movies
    public void loadTopratedMoviesData() {

        setTitle(R.string.top_rated);
//        getSupportActionBar().setTitle(R.string.top_rated);
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<MovieResponse> call = apiInterface.getTopRated(ApiCall.API_KEY);
        reuqestToServer(call);
    }


    //menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popMovies:
                //add the function to perform here
                menuitem_selected = true;
                loadPopularMoviesData();
                return (true);
            case R.id.topMovies:
                //add the function to perform here
                menuitem_selected = false;
                loadTopratedMoviesData();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }


    public void RetryCheck(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
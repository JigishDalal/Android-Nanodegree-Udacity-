package com.example.jigis.moviestage2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jigis.moviestage2.Adapter.FavouriteAdapter;
import com.example.jigis.moviestage2.DataBase.AppExecutors;
import com.example.jigis.moviestage2.DataBase.MovieDatabase;
import com.example.jigis.moviestage2.DataBase.MovieModelDatabase;
import com.example.jigis.moviestage2.ViewModel.MovieViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouriteActivity extends AppCompatActivity {
    private static final String TAG = FavouriteActivity.class.getSimpleName();
    private MovieDatabase mMovieDatabase;
    //    @BindView(R.id.tv_fav)
//    TextView mTextView;
    @BindView(R.id.toolbar_fav)
    Toolbar mToolbar;
    @BindView(R.id.ry_favourite)
    RecyclerView favouriteRecyclerView;
    int spanCount;
    FavouriteAdapter favouriteAdapter;

    List<MovieModelDatabase> movieModelDatabases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Favourite");
//        mTextView.setText(getResources().getString(R.string.no_data_found));


        mMovieDatabase = MovieDatabase.getInstance(getApplicationContext());
        Configuration newConfig = getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4;
        } else {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                spanCount = 2;
            }
        }

        FavouriteMethod();
        RetriveData();
    }

    private void FavouriteMethod() {

        //database
        mMovieDatabase = mMovieDatabase.getInstance(getApplicationContext());

        favouriteRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        favouriteRecyclerView.setHasFixedSize(true);
        favouriteAdapter = new FavouriteAdapter(FavouriteActivity.this, movieModelDatabases);
        favouriteRecyclerView.setAdapter(favouriteAdapter);
    }
        @Override
        public boolean onSupportNavigateUp () {
            onBackPressed();
            return true;
        }

        public void RetriveData(){
           // final LiveData<List<MovieModelDatabase>> movieModelDatabases = mMovieDatabase.moviesDao().getAllList();
            MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

            movieViewModel.getMoviedatabase().observe(this, new Observer<List<MovieModelDatabase>>() {
                @Override
                public void onChanged(@Nullable List<MovieModelDatabase> movieModelDatabases) {
                    Log.e("Data", "Receiving live data");
                    favouriteAdapter.setMovies(movieModelDatabases);
                }
            });
        }
    }

package com.example.jigis.moviestage2.Fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jigis.moviestage2.Adapter.MovieClickListener;
import com.example.jigis.moviestage2.Adapter.PopularMovieAdapter;
import com.example.jigis.moviestage2.ApiCall.ApiCallIng;
import com.example.jigis.moviestage2.ApiCall.ApiInterface;
import com.example.jigis.moviestage2.DetailsActivity;
import com.example.jigis.moviestage2.Main2Activity;
import com.example.jigis.moviestage2.Model.Movie;
import com.example.jigis.moviestage2.Model.MovieResponse;
import com.example.jigis.moviestage2.Retrofit.ApiClient;
import com.example.jigis.moviestage2.Utils.InternetConnection;
import com.example.jigis.moviestage2.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment implements MovieClickListener{

    @BindView(R.id.popular_recyclerview)
    RecyclerView ryvPopular;
    PopularMovieAdapter popularMovieAdapter;
//
//    @BindView(R.id.tv_title)
//    TextView TvTitle ;

    Button btnNowMoview;
    //Call interface
    ApiInterface apiInterface;

    //layout manager
    RecyclerView.LayoutManager layoutManager;
    List<Movie> moviesList ;

    //ProgressBar
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    //save instance
    private static final String movieInstance = "movieInstance";

    //pagination
    private int PAGE_START = 1;
    private int currentPage = PAGE_START;
    GridLayoutManager grid;
    //onclick transition
    public static final String EXTRA_MOVIE_ITEM = "movie_item_url";
    public static final String EXTRA_MOVIE_TRANSITION_NAME = "movie_image_transition_name";




    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        //set toolbar
      //  setUpToolbar(view);
        //call initview
        initview(savedInstanceState);
// Set title bar
        ((Main2Activity) getActivity())
                .setActionBarTitle(getResources().getString(R.string.populer_movie));
        return view;
    }

    private void initview(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            moviesList=savedInstanceState.getParcelableArrayList(movieInstance);
            ConfigurationOrientation();
            popularMovieAdapter= new PopularMovieAdapter(moviesList, PopularMoviesFragment.this);
            grid=new GridLayoutManager(getActivity(),2);
            ryvPopular.setAdapter(popularMovieAdapter);
            popularMovieAdapter.notifyDataSetChanged();
        }
        else {
            //internet checking
            if (InternetConnection.checkConnection(getActivity())) {
                //load data
                loadPopularMoviesData();
            } else {
                //error message
                showErrorMessage();
            }
        }
    }

    //save instance
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(movieInstance, (ArrayList<? extends Parcelable>) moviesList);

    }
    // error method
    private void showErrorMessage() {
        ryvPopular.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
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

                    popularMovieAdapter = new PopularMovieAdapter(moviesList, PopularMoviesFragment.this);
                    ryvPopular.setAdapter(popularMovieAdapter);
                    progressBar.setVisibility(View.GONE);
                    ConfigurationOrientation();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(getActivity(),R.string.failedtoload, Toast.LENGTH_SHORT).show();
                    showErrorMessage();

                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
            showErrorMessage();

        }
    }

    /*
     *
     * Create Material Design Toolbar
     * */


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }
        // Configuration  Orientation
        private void ConfigurationOrientation() {
            final int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
            ryvPopular.setHasFixedSize(true);
            ryvPopular.setLayoutManager(new GridLayoutManager(getActivity(),spanCount));
            ryvPopular.setItemAnimator(new DefaultItemAnimator());

        }


        //progress dialog
        private void progreDolog() {

            progressBar.setVisibility(View.VISIBLE);
        }

        //Popular Movies
        public void loadPopularMoviesData() {


            ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<MovieResponse> call = apiInterface.getPopularMovies(ApiCallIng.API_KEY,"en_US",
                    currentPage);
            reuqestToServer(call);

        }
    /*
     * Create interface OnClick Item pass Data
     * */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMoviePosterClick(Movie movieModel, ImageView imageView, String transitionName) {

       // Toast.makeText(getActivity(), "popular movies"+movieModel.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeBasic();
        intent.putExtra("movieBundel", movieModel);
        intent.putExtra(EXTRA_MOVIE_TRANSITION_NAME, transitionName);

        optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                imageView.getTransitionName());

        startActivity(intent,optionsCompat.toBundle());

    }






}

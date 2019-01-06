package com.example.jigis.moviestage2.Fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jigis.moviestage2.Adapter.MovieClickListener;
import com.example.jigis.moviestage2.Adapter.PopularMovieAdapter;
import com.example.jigis.moviestage2.ApiCall.ApiCallIng;
import com.example.jigis.moviestage2.ApiCall.ApiInterface;
import com.example.jigis.moviestage2.DetailsActivity;
import com.example.jigis.moviestage2.Main2Activity;
import com.example.jigis.moviestage2.Model.Movie;
import com.example.jigis.moviestage2.Model.MovieResponse;
import com.example.jigis.moviestage2.R;
import com.example.jigis.moviestage2.Retrofit.ApiClient;
import com.example.jigis.moviestage2.Utils.InternetConnection;
import com.example.jigis.moviestage2.Utils.NavigationIconClickListener;

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
public class TopRatedFragment extends Fragment implements MovieClickListener {
    @BindView(R.id.recycler_view_topRated)
    RecyclerView ryTopMovies;
    PopularMovieAdapter popularMovieAdapter;
//
//    @BindView(R.id.tv_title)
//    TextView TvTitle;

    //Call interface
    ApiInterface apiInterface;

    //layout manager
    RecyclerView.LayoutManager layoutManager;
    List<Movie> moviesList;

    //ProgressBar
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    //save instance
    private static final String movieinstance = "movieInstance";
    //onclick transition
    public static final String EXTRA_MOVIE_ITEM = "movie_item_url";
    public static final String EXTRA_MOVIE_TRANSITION_NAME = "movie_image_transition_name";


    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_rated, container, false);
        ButterKnife.bind(this, view);
        //set toolbar

        //call initview
              initview(savedInstanceState);
// Set title bar
        ((Main2Activity) getActivity())
                .setActionBarTitle(getResources().getString(R.string.top_rated));
        return view;
    }

    private void initview(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            moviesList = savedInstanceState.getParcelableArrayList(movieinstance);
            ConfigurationOrientation();
            popularMovieAdapter = new PopularMovieAdapter(moviesList, TopRatedFragment.this);
            ryTopMovies.setAdapter(popularMovieAdapter);
            popularMovieAdapter.notifyDataSetChanged();

        } else {
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
        outState.putParcelableArrayList(movieinstance, (ArrayList<? extends Parcelable>) moviesList);

    }

    // error method
    private void showErrorMessage() {
//        mLinearLayout.setVisibility(View.VISIBLE);
        ryTopMovies.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    // Configuration  Orientation
    private void ConfigurationOrientation() {
        final int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        ryTopMovies.setHasFixedSize(true);
        ryTopMovies.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        ryTopMovies.setItemAnimator(new DefaultItemAnimator());
    }

    //progress bar
    private void progreDolog() {

        progressBar.setVisibility(View.VISIBLE);
    }

    //Popular Movies
    public void loadPopularMoviesData() {


        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<MovieResponse> call = apiInterface.getTopRated(ApiCallIng.API_KEY);
        reuqestToServer(call);

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
                        Log.e("log", "now " + response.body().getResults());
                    }

                    popularMovieAdapter = new PopularMovieAdapter(moviesList, TopRatedFragment.this);
                    ryTopMovies.setAdapter(popularMovieAdapter);
//                    progressDoalog.dismiss();
                    progressBar.setVisibility(View.GONE);
//                    mLinearLayout.setVisibility(View.GONE);
                    ConfigurationOrientation();
                    popularMovieAdapter.notifyDataSetChanged();
                    ryTopMovies.smoothScrollToPosition(0);

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.failedtoload, Toast.LENGTH_SHORT).show();
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




    /*
     * Create interface OnClick Item pass Data
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMoviePosterClick(Movie movieModel, ImageView imageView, String transitionName) {

        //Toast.makeText(getActivity(), "Top Rated movie"+movieModel.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeBasic();
        intent.putExtra("movieBundel", movieModel);
        intent.putExtra(EXTRA_MOVIE_TRANSITION_NAME, transitionName);

        optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                imageView.getTransitionName());

        startActivity(intent, optionsCompat.toBundle());


    }
}

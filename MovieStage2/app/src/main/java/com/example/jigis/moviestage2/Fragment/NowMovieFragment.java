package com.example.jigis.moviestage2.Fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
public class NowMovieFragment extends Fragment implements MovieClickListener {
    @BindView(R.id.recycler_view_nowMovie)
    RecyclerView ryNowMovie;
    @BindView(R.id.nowMovies_grid)
    NestedScrollView nestedScrollViewMovie;
    PopularMovieAdapter popularMovieAdapter;


    //Call interface
    ApiInterface apiInterface;

    //layout manager
    List<Movie> moviesList;

    //ProgressBar
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    //save instance
    private static final String movieinstance = "movieInstance";
    //onclick transition
    public static final String EXTRA_MOVIE_ITEM = "movie_item_url";
    public static final String EXTRA_MOVIE_TRANSITION_NAME = "movie_image_transition_name";

    //pagination
    private int PAGE_START = 1;
    private int currentPage = PAGE_START;
    GridLayoutManager layoutManager;
    int mAdapterPosition = 0;


    public NowMovieFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_movie, container, false);
        ButterKnife.bind(this, view);

        //call initview
        initview(savedInstanceState);

        // Set title bar
        ((Main2Activity) getActivity())
                .setActionBarTitle(getResources().getString(R.string.now_movie));

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initview(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            moviesList = savedInstanceState.getParcelableArrayList(movieinstance);
            ConfigurationOrientation();
            popularMovieAdapter = new PopularMovieAdapter(moviesList, NowMovieFragment.this);
            ryNowMovie.setAdapter(popularMovieAdapter);
            popularMovieAdapter.notifyDataSetChanged();

        } else {
            //internet checking
            if (InternetConnection.checkConnection(getActivity())) {
                //load data
                loadPopularMoviesData(currentPage);
                implementPagination();


            } else {
                //error message
                showErrorMessage();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void implementPagination() {


//
//        ryNowMovie.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int lastVisibleItemPosition = ((GridLayoutManager) ryNowMovie.getLayoutManager())
//                        .findLastCompletelyVisibleItemPosition();
//                if (lastVisibleItemPosition == popularMovieAdapter.getItemCount() - 1) {
//                    Log.e("onScrolled: ","total item count"+lastVisibleItemPosition+"adapter position"+popularMovieAdapter.getItemCount() );
//                    currentPage++;
//                    loadPopularMoviesData(currentPage);
//                }
//
//            }
//        });


        nestedScrollViewMovie.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY>0) {
                    Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                    int totalItemCount = layoutManager.getItemCount();
                    Log.e("onScrolled: ", "total item count" + totalItemCount);
                    int lastVisibleItemPosition = ((GridLayoutManager) ryNowMovie.getLayoutManager())
                            .findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == popularMovieAdapter.getItemCount() - 1) {
                        Log.e("onScrolled: ", "last position" + lastVisibleItemPosition + "adapter position" + popularMovieAdapter.getItemCount());
                        currentPage++;
                        Log.e("onScrolled: ", "page " + currentPage);
                        loadPopularMoviesData(currentPage);
                    }
                }

            }
        });



/*
        ryNowMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy>0) {
                    Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
                    int totalItemCount = layoutManager.getItemCount();
                    Log.e("onScrolled: ", "total item count" + totalItemCount);
                    int lastVisibleItemPosition = ((GridLayoutManager) ryNowMovie.getLayoutManager())
                            .findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition == popularMovieAdapter.getItemCount() - 1) {
                        Log.e("onScrolled: ", "last position" + lastVisibleItemPosition + "adapter position" + popularMovieAdapter.getItemCount());
                        currentPage++;
                        Log.e("onScrolled: ", "page " + currentPage);
                        loadPopularMoviesData(currentPage);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "upper", Toast.LENGTH_SHORT).show();
                }


                // mCallPage: Has value 1 at first, increments by 1
                // dy > 0: Checks if scroll direction is downwards
                // ((totalItemCount / 20) == mCallPage)): Checks if page needs to be incremented,
                // here the API returns 20 items per page

*/
/*                if ((dy > 0) && ((totalItemCount / 20) == currentPage)) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    Log.e("onScrolled: ","visible item count"+visibleItemCount );
                    Log.e("onScrolled: ","past item count"+pastVisibleItems );


                    // Checks whether reached near the end of recycler view, 10 items less than total items
                    if (pastVisibleItems + visibleItemCount >= (totalItemCount - 10)) {
                        currentPage++;
                        Log.e("onScrolled: ","page number"+currentPage );
                        loadPopularMoviesData(currentPage);

                    }
                }*//*


            }

        });
*/

    }

    //save instance
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(movieinstance, (ArrayList<? extends Parcelable>) moviesList);

    }

    // error method
    private void showErrorMessage() {
        ryNowMovie.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }


    // Configuration  Orientation
    private void ConfigurationOrientation() {
        final int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        ryNowMovie.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), spanCount);
        ryNowMovie.setLayoutManager(layoutManager);
        ryNowMovie.setItemAnimator(new DefaultItemAnimator());

    }

    //progress bar
    private void progreDolog() {
        progressBar.setVisibility(View.VISIBLE);
    }

    //Popular Movies
    public void loadPopularMoviesData(int page) {

        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<MovieResponse> call = apiInterface.getNowMovie(ApiCallIng.API_KEY, page);
        reuqestToServer(call);
    }

    //request To server
    public void reuqestToServer(Call<MovieResponse> movieCall) {
        progreDolog();

        try {
            movieCall.enqueue(new Callback<MovieResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    moviesList = new ArrayList<>();
                    if (response.isSuccessful() && !response.body().getResults().isEmpty()) {
                        moviesList.addAll(response.body().getResults());
                        Log.e("log", "now " + response.body().getResults());
                    }

                    popularMovieAdapter = new PopularMovieAdapter(moviesList, NowMovieFragment.this);
                    ryNowMovie.setAdapter(popularMovieAdapter);
//                    progressDoalog.dismiss();
                    progressBar.setVisibility(View.GONE);
//                    mLinearLayout.setVisibility(View.GONE);
                    popularMovieAdapter.notifyItemInserted(moviesList.size() - 1);
                    popularMovieAdapter.notifyDataSetChanged();
                    ConfigurationOrientation();

                    ryNowMovie.scheduleLayoutAnimation();
                    Log.e("onResponse: ", "Scroll");
                    if (mAdapterPosition != 0) {
                        ryNowMovie.smoothScrollToPosition(mAdapterPosition);
                        mAdapterPosition = 0;
                    }


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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.shr_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    /*
     * Create interface OnClick Item pass Data
     * */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMoviePosterClick(Movie movieModel, ImageView imageView, String transitionName) {
       // Toast.makeText(getActivity(), "Now movies " + movieModel.getTitle(), Toast.LENGTH_SHORT).show();
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

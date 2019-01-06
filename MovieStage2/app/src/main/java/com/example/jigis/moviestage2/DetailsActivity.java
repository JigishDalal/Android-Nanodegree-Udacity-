package com.example.jigis.moviestage2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.jigis.moviestage2.Adapter.CastAdapter;
import com.example.jigis.moviestage2.Adapter.PopularMovieAdapter;
import com.example.jigis.moviestage2.Adapter.ReviewAdapter;
import com.example.jigis.moviestage2.Adapter.VideoAdapter;
import com.example.jigis.moviestage2.ApiCall.ApiCallIng;
import com.example.jigis.moviestage2.ApiCall.ApiInterface;
import com.example.jigis.moviestage2.DataBase.AppExecutors;
import com.example.jigis.moviestage2.DataBase.MovieDatabase;
import com.example.jigis.moviestage2.DataBase.MovieModelDatabase;
import com.example.jigis.moviestage2.Model.CastModel;
import com.example.jigis.moviestage2.Model.CastResponse;
import com.example.jigis.moviestage2.Model.Movie;
import com.example.jigis.moviestage2.Model.Reviews;
import com.example.jigis.moviestage2.Model.ReviewsResponse;
import com.example.jigis.moviestage2.Model.Video;
import com.example.jigis.moviestage2.Model.VideoResponse;
import com.example.jigis.moviestage2.Retrofit.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    public static Movie movie;
    //poster
    @BindView(R.id.image_background)
    ImageView imgBackGround;
    //title
    @BindView(R.id.tv_title_original)
    TextView title;
    //poster
    @BindView(R.id.poster_images)
    ImageView imgposter;
    //ratingbar
    @BindView(R.id.movie_ratingBar)
    RatingBar movieRate;
    //Time
    @BindView(R.id.tv_time)
    TextView tvtime;
    //date
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.toolbardes)
    Toolbar toolbar;
    //overview
    @BindView(R.id.tv_overviewdes)
    TextView overview;

    @BindView(R.id.btn_share)
    ImageButton btnShare;

    //review
    @BindView(R.id.ry_review)
    RecyclerView ryReview;
    ReviewAdapter reviewAdapter;
    List<Reviews> reviewsItem;
    @BindView(R.id.tv_review)
    TextView noReview;


    //video
    @BindView(R.id.ry_trailers)
    RecyclerView ryTrailers;
    VideoAdapter videoAdapter;
    List<Video> videoitem;

    //database
    private MovieDatabase MoviemDb;
    @BindView(R.id.Fab_fav)
    FloatingActionButton favbtn;
    int favmarkCount = 1;
    //favouritedatabase
    MovieModelDatabase movieModelDatabase;

    //cast
    @BindView(R.id.moive_cast)
    RecyclerView ryCastname;
    CastAdapter castAdapter;
    List<CastModel> castModelList;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        supportPostponeEnterTransition();

        ButterKnife.bind(this);

        //backPress arrow
        setSupportActionBar(toolbar);

        MoviemDb = MovieDatabase.getInstance(getApplicationContext());


        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }
        postponeEnterTransition();

        if (getIntent().getParcelableExtra("Favourite") != null) {
            movieModelDatabase = getIntent().getParcelableExtra("Favourite");
            favourite();
        } else if (getIntent().getParcelableExtra("movieBundel") != null) {
            movie = getIntent().getParcelableExtra("movieBundel");
            initview();
        }

    }

    private void favourite() {

        favbtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        favbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                MoviemDb.moviesDao().delete(movieModelDatabase);
                favbtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                getWindow().setSharedElementReturnTransition(null);
                getWindow().setSharedElementReenterTransition(null);
                imgposter.setTransitionName(null);
            }
        });


        //tootlbar title
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.main_collapsing);
        collapsingToolbar.setTitle(movieModelDatabase.getTitle());
        //movie title
        if (!movieModelDatabase.getOriginal_title().isEmpty()) {
            title.setText(movieModelDatabase.getOriginal_title().toString());
        } else {
            title.setText(R.string.no_data);
        }
        //date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        try {
            Date date = simpleDateFormat1.parse(movieModelDatabase.getRelease_date());
            tvDate.setText(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //time
        tvtime.setText("");
        //rating bar
        Log.e("vote", "vote" + movieModelDatabase.getVote_average());
        Float rating = (float) movieModelDatabase.getVote_average();
        movieRate.setRating(rating / 2);
        //overview
        overview.setText(movieModelDatabase.getOverview());
        //background poster
        Glide.with(getApplicationContext()).load(movieModelDatabase.getBackdrop_path()).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground))
                .into(imgBackGround);
        //poster images
        supportPostponeEnterTransition();
        Glide.with(getApplicationContext()).load(movieModelDatabase.getPoster_path())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                }).apply(new RequestOptions()
                .dontAnimate()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground))
                .into(imgposter);

        final List<Video> movieTrailers = movieModelDatabase.getVideos();
        //  String name= movieTrailers.get(2).getName();
        if (movieTrailers != null) {
            VideoSetup(movieTrailers);
        }
        final List<Reviews> movieReview = movieModelDatabase.getReviews();
        if (movieReview != null) {
            ReviewSetUp(movieReview);
        } else {
//                String reviewDetails = "No Reviews Available";
//                noReview.setText(reviewDetails);
        }
        final List<CastModel> castModels = movieModelDatabase.getCastModelList();
        if (castModels != null) {
            castRY(castModels);
        }
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData(movieTrailers);
            }
        });

    }


    /*
     *
     * Checked item Favourite id
     * */
    void alreadyselcted() {
        //item selected already then show favourite already
        final Integer movieId = movie.getId();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final Integer moviefavId = MoviemDb.moviesDao().getID(movieId);
                if (moviefavId != null) {
                    Log.e("FavouritedItem: ", "" + moviefavId);
                    favbtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    favmarkCount = 2;
                }


            }
        });
    }

    /*
     *
     *       Data load From the model class
     *
     * */
    private void initview() {
        alreadyselcted();
        //tootlbar title
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.main_collapsing);
        collapsingToolbar.setTitle(movie.getTitle());
        //movie title
        if (!movie.getOriginal_title().isEmpty()) {
            title.setText(movie.getOriginal_title().toString());
        } else {
            title.setText(R.string.no_data);
        }
        //date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        try {
            Date date = simpleDateFormat1.parse(movie.getRelease_date());
            tvDate.setText(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //time
        tvtime.setText("");
        //rating bar
        Log.e("vote", "vote" + movie.getVote_average());
        Float rating = (float) movie.getVote_average();
        movieRate.setRating(rating / 2);
        overview.setText(movie.getOverview());

        //background poster
        final String movie_poster_url = ApiCallIng.IMAGE_URL + ApiCallIng.BACK_POSTER_IMAGE_SIZE_500 + "/" + movie.getBackdrop_path();
        Glide.with(getApplicationContext()).load(movie_poster_url).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground))
                .into(imgBackGround);

        //poster images
        final String movie_thumb_url = ApiCallIng.IMAGE_URL + ApiCallIng.IMAGE_SIZE_185 + "/" + movie.getPoster_path();
        supportPostponeEnterTransition();
        Glide.with(getApplicationContext()).load(movie_thumb_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                }).apply(new RequestOptions()
                .dontAnimate()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground))
                .into(imgposter);
        loadDataReview();
        loadVideo();
        loadCast();
        /*
         *
         * Favourite Button Selected Click event
         * */
        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavouritedItem();
            }
        });


    }

    /*
     * favourite item select and  store in database
     *
     *
     * */
    private void FavouritedItem() {
        final String movie_poster_url = ApiCallIng.IMAGE_URL + ApiCallIng.BACK_POSTER_IMAGE_SIZE_500 + "/" + movie.getBackdrop_path();
        final String movie_thumb_url = ApiCallIng.IMAGE_URL + ApiCallIng.IMAGE_SIZE_185 + "/" + movie.getPoster_path();

        int movieId = movie.getId();
        String movieTitle = movie.getTitle();
        String moviePoster = movie_thumb_url;
        String movieBackDrop = movie_poster_url;
        String movieReleaseDate = movie.getRelease_date();
        double movieRating = movie.getVote_average();
        boolean video = movie.isVideo();
        double voteaverage = movie.getVote_average();
        double popularity = movie.getPopularity();
        String original_title = movie.getOriginal_title();
        String originalLanguage = movie.getOriginal_language();
        boolean adult = movie.isAdult();
        String overview = movie.getOverview();


        final List<Video> movieTrailer = getTrailers();
        final List<Reviews> movieReview = getReviews();
        final List<CastModel> castModels = getCast();


        final MovieModelDatabase movieModelDatabase = new MovieModelDatabase(movieId, video, voteaverage, movieTitle, popularity
                , moviePoster, originalLanguage, original_title,
                movieBackDrop, adult, overview, movieReleaseDate,
                movieTrailer, movieReview, castModels) {
        };
        if (favmarkCount == 1) {
            favbtn.setImageResource(R.drawable.ic_favorite_black_24dp);
            favmarkCount = 2;

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    MoviemDb.moviesDao().insert(movieModelDatabase);
                    Log.v("Database saved", "" + movieTrailer.size());

                }
            });


        } else if (favmarkCount == 2) {
            favbtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            favmarkCount = 1;
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    MoviemDb.moviesDao().delete(movieModelDatabase);
                    Log.v("Database deleted", movieModelDatabase.getTitle());
                }
            });

        }
    }

    /*
     *
     * Load Video
     * */
    private void loadVideo() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        String id = String.valueOf(movie.getId());
        Log.e("loadVideo: ", "Id " + id);
        final Call<VideoResponse> videoReview = apiInterface.getVideo(id, ApiCallIng.API_KEY);
        try {
            videoReview.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    if (response.isSuccessful() && !response.body().getResults().isEmpty()) {
                        videoitem = response.body().getResults();
                        Log.e("URL", "Size" + videoitem.size() + "url" + response.raw().request().url().toString());
                    }
                    btnShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ShareData(videoitem);

                        }
                    });

                    VideoSetup(videoitem);
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Internet Connection lose.", Toast.LENGTH_SHORT).show();


        }

    }

    private void ShareData(List<Video> videoitem) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String youtubevideo = ApiCallIng.VIDEOS_PATH + videoitem.get(0).getKey();
        shareIntent.putExtra(Intent.EXTRA_STREAM, "MOVIE STAGE");
        shareIntent.putExtra(Intent.EXTRA_TITLE, videoitem.get(0).getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, youtubevideo);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

    }

    /*
     * Load Review
     *
     * */

    private void loadDataReview() {

        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        String id = String.valueOf(movie.getId());
        Call<ReviewsResponse> reviewCall = apiInterface.getReview(id, ApiCallIng.API_KEY);
        try {
            reviewCall.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {

                    if (response.isSuccessful() && !response.body().getResults().isEmpty()) {
                        reviewsItem = response.body().getResults();
                        Log.e("log", "now " + response.body().getResults());
                    }
                    ReviewSetUp(reviewsItem);

                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Failed to load the data", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Internet Connection lose.", Toast.LENGTH_SHORT).show();

        }

    }

    void VideoSetup(List<Video> video) {
        videoAdapter = new VideoAdapter(this, video);
        ryTrailers.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        ryTrailers.setHasFixedSize(true);
        ryTrailers.setAdapter(videoAdapter);
        setTrailers(video);
    }

    private void ReviewSetUp(List<Reviews> movieReview) {
        reviewAdapter = new ReviewAdapter(this, movieReview);
        ryReview.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        ryReview.setHasFixedSize(true);
        ryReview.setAdapter(reviewAdapter);
        setReviews(movieReview);
    }


    void loadCast() {
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        String id = String.valueOf(movie.getId());
        Log.e("loadVideo: ", "Id " + id);
        final Call<CastResponse> Cast = apiInterface.getCastChar(id, ApiCallIng.API_KEY);
        try {
            Cast.enqueue(new Callback<CastResponse>() {
                @Override
                public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                    if (response.isSuccessful() && !response.body().getCast().isEmpty()) {
                        castModelList = response.body().getCast();
                        castAdapter = new CastAdapter(DetailsActivity.this, castModelList);
                        //  Log.e("URL","Size"+videoitem.size()+"url"+response.raw().request().url().toString());

                    }
                    castRY(castModelList);
                }

                @Override
                public void onFailure(Call<CastResponse> call, Throwable t) {

                }

            });
        } catch (Exception e) {
        }


    }

    private void castRY(List<CastModel> castModelList) {
        castAdapter = new CastAdapter(DetailsActivity.this, castModelList);
        ryCastname.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        ryCastname.setHasFixedSize(true);
        ryCastname.setAdapter(castAdapter);
        setName(castModelList);
    }

    private void setName(List<CastModel> castList) {
        castModelList = castList;
    }

    private List<CastModel> getCast() {
        return castModelList;
    }


    //offline
    private void setReviews(List<Reviews> reviews) {
        reviewsItem = reviews;
    }

    private List<Reviews> getReviews() {
        return reviewsItem;
    }

    private void setTrailers(List<Video> trailers) {
        videoitem = trailers;
    }

    private List<Video> getTrailers() {
        return videoitem;
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "not load the Data ", Toast.LENGTH_SHORT).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

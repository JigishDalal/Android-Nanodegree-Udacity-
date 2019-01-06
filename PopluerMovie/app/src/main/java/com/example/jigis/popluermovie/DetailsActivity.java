package com.example.jigis.popluermovie;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.jigis.popluermovie.ApiCall.ApiCall;
import com.example.jigis.popluermovie.Model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    public static Movie movie;
    @BindView(R.id.img_posterback)
    ImageView movie_poster;
    @BindView(R.id.img_thumb)
    ImageView movie_thumb;
    @BindView(R.id.tv_movie_title)
    TextView movie_title;
    @BindView(R.id.tv_overview)
    TextView movie_overview;
    @BindView(R.id.movie_date)
    TextView movie_relase;
    @BindView(R.id.tv_de_audiolang)
    TextView movie_lang;
    @BindView(R.id.tv_de_vote)
    TextView movie_vote;
    @BindView(R.id.tv_de_popularity)
    TextView movie_popular;
    String movie_poster_url, movie_thumb_url;
    @BindView(R.id.my_toolbar_det)
    Toolbar mTopToolbar;
    Animation animFadeIn, animFadeout;
    boolean enableBackBtn = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        initView();
        postponeEnterTransition();

    }

    private void initView() {
        if ((getIntent() == null) || (!getIntent().hasExtra("movie"))) {
            finish();
        }
        Intent intent = getIntent();

//        movie object
        movie = intent.getParcelableExtra("movie");
//        Title ,overview ,relase date,language, vote ,popularity

        movie_title.setText(movie.getOriginal_title().toString());
        movie_overview.setText(movie.getOverview().toString());
        movie_relase.setText(movie.getRelease_date());
        ResizableCustomView.doResizeTextView(movie_overview, 2, "Read More", true);

        if (movie.getOriginal_language().isEmpty()) {
            movie_lang.setText("NA");
        } else {
            movie_lang.setText(movie.getOriginal_language());
        }
        movie_vote.setText("" + movie.getVote_average());
        movie_popular.setText("" + movie.getPopularity());


//background poster
        movie_poster_url = ApiCall.IMAGE_URL + ApiCall.BACK_POSTER_IMAGE_SIZE_500 + "/" + movie.getBackdrop_path();

        Glide.with(getApplicationContext()).load(movie_poster_url).apply(new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground))
                .into(movie_poster);
//        Thumb poster

        movie_thumb_url = ApiCall.IMAGE_URL + ApiCall.IMAGE_SIZE_185 + "/" + movie.getPoster_path();
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
                .into(movie_thumb);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}




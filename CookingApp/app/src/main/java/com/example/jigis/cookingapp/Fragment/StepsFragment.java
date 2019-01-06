package com.example.jigis.cookingapp.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.jigis.cookingapp.MainActivity;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Model.Steps;
import com.example.jigis.cookingapp.R;
import com.example.jigis.cookingapp.RecipeDetailsActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.jigis.cookingapp.RecipeDetailsActivity.mTwoPane;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment  {

    private static final String LOG_TAG = StepsFragment.class.getSimpleName();

    @BindView(R.id.exoplayer_view)
    PlayerView simpleExoPlayerView;


    SimpleExoPlayer exoPlayer;
    ImageView fullscreenIcon;
    static boolean exoFullscreen;

    @BindView(R.id.stepdetails)
    TextView stepDescription;

    @BindView(R.id.prev_btn)
    Button previousSButton;

    @BindView(R.id.next_btn)
    Button nextStepButton;


    @BindView(R.id.toolbar_stepfragment)
    Toolbar toolbar;

    @BindView(R.id.steps_image)
    ImageView stepsImages;

    ArrayList<Steps> stepsList;
    Recipes recipe;
    Steps steps;
    int Sizeoflist;
    Uri videoUri;

    //its for orations
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";

    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;


    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this,view);
        //get the data from bundle


        if (savedInstanceState == null) {
            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = C.INDEX_UNSET;
            }
            else
        {
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }

        if (getArguments() != null) {

            //get from Step Activity step list recipemodel
            stepsList = getArguments().getParcelableArrayList("steplist");
            recipe = getArguments().getParcelable("recipemodel");
            toolbar.setTitle(recipe.getName());
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPink));


            //set the data
            steps = getArguments().getParcelable("steps");
            Sizeoflist=stepsList.size();

        }
        if (mTwoPane== false) {
            //phone layout
            steps = getArguments().getParcelable("steps");
            getActivity().setTitle(steps.getDescription());
            populateUI(getContext());
          //  initializeFullScreenButton();
            initializeButtons();
            hideButtons();
        }


        return view ;
    }
    public void receiveStepInterface(Context context, Steps currentStep) {
        //tablet layout
        steps = currentStep;
        populateUI(context);
        initializeButtons();
        //initializeFullScreenButton();
        hideButtons();
        Log.v(LOG_TAG, currentStep + "");
    }

    @SuppressLint("CheckResult")
    private void populateUI(Context context) {
        //defualt image in player
        simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

        if (steps.getVideoURL().isEmpty()) {
            videoUri=Uri.parse(steps.getThumbnailURL());
            initializePlayer(context,videoUri);
            Glide.with(this)
                    .load(steps.getThumbnailURL())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                            simpleExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                        return true;
                        }
                    });

        } else {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(context, Uri.parse(steps.getVideoURL()));
            stepsImages.setVisibility(View.GONE);
        }
        //Set the Text on the description view to the step description
        stepDescription.setText(steps.getDescription());
    }

    //setVideo Player
    private void initializePlayer(Context context, Uri mediaLink) {
        if (exoPlayer == null && mediaLink != null) {
            //Create an Instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            //Prepare the MediaSource
            String userAgent = Util.getUserAgent(context, context.getResources().getString(R.string.app_name));
            //Create the DataSource
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    userAgent, null);
            //Create the Media Source
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaLink);

            boolean haveStartPosition =playbackPosition != C.INDEX_UNSET;
            if (haveStartPosition) {
                exoPlayer.seekTo(playbackPosition);
            }

            exoPlayer.prepare(mediaSource,!haveStartPosition,false);
            simpleExoPlayerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(playWhenReady);
        }
//        else
//        {
////            releasePlayer();
////            populateUI(getContext());
//
//        }
    }
    //set Button next and previous
    private void initializeButtons() {
        //Initialize both onClickListeners for both buttons
        getNextStep();
        getPreviousStep();
    }
    private void getPreviousStep() {
        previousSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (steps.getId() > 0) {
                    int currentStep;
                    currentStep = steps.getId();
                    steps = stepsList.get(currentStep - 1);
                    refreshFragment();
                    Log.v(LOG_TAG, steps.getId() + "previous step");
                    Log.v(LOG_TAG, stepsList.size() + "Size");
                }

                if (steps.getId() < stepsList.size() - 1 || (steps.getId() == stepsList.size() && recipe.getName().equals("Yellow Cake"))) {
                    nextStepButton.setVisibility(View.VISIBLE);
                }

                if (steps.getId() == 0) {
                    previousSButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void refreshFragment() {
        if (mTwoPane== true) {
            //tablet layout
            populateUI(getContext());
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.player_fragment, this);
        }
        else{
            //phone
            populateUI(getContext());
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_recipe_info_steps, this);
        }
    }

    /**
     * This method is used to retrieve the next Step from the list
     * Special case for Yellow Cake as the data was not in order
     */
    private void getNextStep() {
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (steps.getId() < Sizeoflist - 1 || ((recipe.getName().equals("Yellow Cake")) && steps.getId() < Sizeoflist)) {
                    int currentStep;
                    currentStep = steps.getId();
                    int addstep=(currentStep+1);
                    Log.e("step","step"+stepsList.get(addstep));
                    steps = stepsList.get(addstep);
                    Log.v(LOG_TAG, steps.getId() + "next step");
                    Log.v(LOG_TAG, stepsList.size() + "Size");
                    refreshFragment();

                }

                if (steps.getId() >= 1) {
                    previousSButton.setVisibility(View.VISIBLE);
                }

                if (steps.getId() == stepsList.size() - 1 || (steps.getId() == stepsList.size() && recipe.getName().equals("Yellow Cake"))) {
                    nextStepButton.setVisibility(View.GONE);
                }
            }
        });
    }



    private void goFullscreen() {
        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        simpleExoPlayerView.setLayoutParams(params);
        fullscreenIcon.setImageResource(R.drawable.compress_white);
        previousSButton.setVisibility(View.GONE);
        nextStepButton.setVisibility(View.GONE);
    }

    private void closeFullscreen() {
        FrameLayout.LayoutParams params =
                (FrameLayout.LayoutParams) simpleExoPlayerView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int) getResources().getDimension(R.dimen.exo_player_height);
        simpleExoPlayerView.setLayoutParams(params);
        fullscreenIcon.setImageResource(R.drawable.enlarge_white);
        nextStepButton.setVisibility(View.VISIBLE);
        previousSButton.setVisibility(View.VISIBLE);
    }

    private void initializeFullScreenButton() {
        PlayerControlView controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
        FrameLayout fullscreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        fullscreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!exoFullscreen) {
                    Toast.makeText(getActivity(), "full screen", Toast.LENGTH_SHORT).show();
                    goFullscreen();
                    exoFullscreen = true;
                } else {
                    Toast.makeText(getActivity(), "compress screen", Toast.LENGTH_SHORT).show();
                    closeFullscreen();
                    exoFullscreen = false;
                }
            }
        });
    }
    private void hideButtons() {
        if (steps.getId() == 0) {
            previousSButton.setVisibility(View.GONE);
        }

        if (steps.getId() == stepsList.size() - 1 && !(recipe.getName().equals("Yellow Cake"))) {
            nextStepButton.setVisibility(View.GONE);
        }

        if (steps.getId() == stepsList.size() && recipe.getName().equals("Yellow Cake")) {
            nextStepButton.setVisibility(View.GONE);
        }
    }



    private void releasePlayer() {

        if (exoPlayer!=null) {
            updateStartPosition();
            //Stop and release the player to avoid Memory leaks
            exoPlayer.release();
            exoPlayer = null;

        }
    }

    private void updateStartPosition() {
        playbackPosition = exoPlayer.getCurrentPosition();
        currentWindow = exoPlayer.getCurrentWindowIndex();
        playWhenReady = exoPlayer.getPlayWhenReady();
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            populateUI(getContext());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || exoPlayer==null)
        {
            populateUI(getContext());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop:called ");

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (exoPlayer != null) {
//            exoPlayer.stop();
//            releasePlayer();
//        }
//    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

            updateStartPosition();
            outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
            outState.putInt(KEY_WINDOW, currentWindow);
            outState.putLong(KEY_POSITION, playbackPosition);
        super.onSaveInstanceState(outState);


        //TODO Implement both methods to handle phone rotations
    }



}

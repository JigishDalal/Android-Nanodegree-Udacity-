package com.example.teleprompter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.teleprompter.R;
import com.example.teleprompter.database.entity.Script;

public class PlayActivity extends AppCompatActivity {

    private Script mScript;

    private TextView scriptView;
    private ScrollView scrollView;
    private ImageButton controlButton;
    private TextView progressTextView;
    private SeekBar seekBar;

    private int scrollTo = 100;
    private boolean isPlaying = false;
    private boolean hasReachedBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mScript = bundle.getParcelable("script");
            }
        }

        scriptView = (TextView) findViewById(R.id.scriptTextView);
        progressTextView = (TextView) findViewById(R.id.speedValue);
        seekBar = (SeekBar) findViewById(R.id.speedSeekBar);
        scrollView = (ScrollView) findViewById(R.id.scriptScrollView);
        controlButton = (ImageButton) findViewById(R.id.controlButton);

        initViews();

        controlButton.setOnClickListener(v -> {
            if (isPlaying) {
                controlButton.setImageResource(R.drawable.ic_play);
            } else {
                controlButton.setImageResource(R.drawable.ic_pause);
                scroll();
            }
            isPlaying = !isPlaying;
        });
        scriptView.setOnClickListener(v -> isPlaying = !isPlaying);
    }

    private void initViews() {
        scriptView.setText(mScript.getBody());
        scriptView.setTextSize(45);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    updateSpeed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            hasReachedBottom = scrollView.getChildAt(0).getBottom() <= (scrollView.getHeight() + scrollView.getScrollY());
            if (!hasReachedBottom && isPlaying) {
                scroll();
            }
        });
    }

    private void updateSpeed(int progress) {
        progressTextView.setText(String.valueOf(progress));
    }

    public void scroll() {
        scrollView.postDelayed(() -> scrollView.smoothScrollTo(0, scrollTo), 120);
        scrollTo += 10;
    }
}
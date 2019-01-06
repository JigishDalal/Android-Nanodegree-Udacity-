package com.example.teleprompter.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.example.teleprompter.R;
import com.example.teleprompter.adapter.ScriptAdapter;
import com.example.teleprompter.database.entity.Script;
import com.example.teleprompter.view.fragment.AddScriptFragment;
import com.example.teleprompter.view.fragment.PlayScriptFragment;
import com.example.teleprompter.view.fragment.ScriptListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ScriptAdapter.OnScriptSelectedListener,
        ScriptListFragment.OnAddScriptListener {

    @BindView(R.id.toolbar)
    Toolbar bar;

    private static final String SCRIPT_KEY = "Script.key";
    Script script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(bar);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


        ScriptListFragment fragment = ScriptListFragment.newInstance();
        fragment.setListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void scriptSelected(Script script) {
        this.script = script;
        PlayScriptFragment fragment = PlayScriptFragment.newInstance(script, script.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(PlayScriptFragment.FRAGMENT_TAG)
                .commit();
    }

    public void startIntent(PlayScriptFragment playScriptFragment, Intent intent, int requestCode) {
        ActivityOptionsCompat optionsCompat = null;
        if (requestCode == 1000) {
            optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(MainActivity.this,
                            playScriptFragment.scriptView, ViewCompat.getTransitionName(playScriptFragment.scriptView));
        } else if (requestCode == 2000) {
            optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(MainActivity.this,
                            playScriptFragment.scriptView, getResources().getString(R.string.please_wait));
        }
        startActivityFromFragment(playScriptFragment, intent, requestCode, optionsCompat.toBundle());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SCRIPT_KEY, script);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().show();
        bar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void createNewScript(String scriptBody) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, AddScriptFragment.newInstance(scriptBody))
                .addToBackStack(AddScriptFragment.FRAGMENT_TAG)
                .commit();
    }
}

package com.example.teleprompter.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.teleprompter.R;
import com.example.teleprompter.database.entity.Script;
import com.example.teleprompter.view.activity.MainActivity;
import com.example.teleprompter.view.activity.PlayActivity;
import com.example.teleprompter.view.activity.UploadFileActivity;
import com.example.teleprompter.viewmodel.ScriptViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayScriptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayScriptFragment extends Fragment {

    public static final String FRAGMENT_TAG = "PlayScriptFragment.TAG";
    private static final String ARG_PARAM1 = "param1";

    private ScriptViewModel viewModel;

    public TextView scriptView;
    private ScrollView scrollView;
    private MainActivity activity;
    private Script script;
    private int id;

    public PlayScriptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param script Parameter 1.
     * @return A new instance of fragment PlayScriptFragment.
     */
    public static PlayScriptFragment newInstance(Script script, int id) {
        PlayScriptFragment fragment = new PlayScriptFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, script);
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            script = getArguments().getParcelable(ARG_PARAM1);
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_script, container, false);
        viewModel = ViewModelProviders.of(this).get(ScriptViewModel.class);
        scriptView = view.findViewById(R.id.scriptTextView);
        scrollView = view.findViewById(R.id.scriptScrollView);
        activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        return view;
    }

    private void initViews() {
        activity.getSupportActionBar().setTitle(script.getTitle());
        scriptView.setText(script.getBody());
    }

    public void scroll() {
        scrollView.postDelayed(() -> scrollView.smoothScrollTo(0, scriptView.getBottom() / 2), 1000);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_preview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_play:
                Intent playIntent = new Intent(getActivity(), PlayActivity.class);
                Bundle playBundle = new Bundle();
                playBundle.putParcelable("script", script);
                playIntent.putExtras(playBundle);
                activity.startIntent(this, playIntent, 1000);
                return true;
            case R.id.action_upload:
                Intent uploadIntent = new Intent(getActivity(), UploadFileActivity.class);
                Bundle uploadBundle = new Bundle();
                uploadBundle.putString("title", script.getTitle());
                uploadBundle.putString("body", script.getBody());
                uploadIntent.putExtras(uploadBundle);
                activity.startIntent(this, uploadIntent, 2000);
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                activity.getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_delete_script))
                .setPositiveButton(getString(R.string.delete_positive), (dialog, which) -> {
                    viewModel.deleteScript(id);
                    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    activity.getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(getString(R.string.delete_negative), (dialog, which) -> dialog.cancel())
                .show();
    }
}

package com.example.teleprompter.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.teleprompter.R;
import com.example.teleprompter.adapter.ScriptAdapter;
import com.example.teleprompter.view.activity.MainActivity;
import com.example.teleprompter.viewmodel.ScriptViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScriptListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScriptListFragment extends Fragment {

    private static int CODE = 200;

    @BindView(R.id.add_new)
    FloatingActionButton addNew;
    @BindView(R.id.rv_scripts)
    RecyclerView recyclerView;

    private ScriptViewModel viewModel;
    private MainActivity activity;
    private OnAddScriptListener listener;

    public ScriptListFragment() {
        // Required empty public constructor
    }

    public interface OnAddScriptListener {
        void createNewScript(String scriptBody);
    }

    public static ScriptListFragment newInstance() {
        return new ScriptListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_script_list, container, false);
        ButterKnife.bind(this, view);

        activity = (MainActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setTitle(R.string.app_name);


        viewModel = ViewModelProviders.of(this).get(ScriptViewModel.class);
        ScriptAdapter adapter = new ScriptAdapter();
        viewModel.getAllScripts().observe(this, scripts -> {
            adapter.setScripts(scripts);
            adapter.setListener(activity);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        });

        //fab button
        addNew.setOnClickListener(view1 -> showOptions());

        adapter.setListener(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void setListener(OnAddScriptListener listener) {
        this.listener = listener;
    }

    private void showOptions() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.options_menu);
        Button upload = dialog.findViewById(R.id.upload);
        Button create = dialog.findViewById(R.id.create);
        Button delete = dialog.findViewById(R.id.delete_all);
        upload.setOnClickListener(v -> {
            dialog.dismiss();
            browseFile();
        });
        create.setOnClickListener(v -> {
            dialog.dismiss();
            listener.createNewScript(null);
        });
        delete.setOnClickListener(v -> {
            dialog.dismiss();
            showDeleteConfirmationDialog();
        });
        dialog.show();
    }

    //upload gallery
    private void browseFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dataText;
        Uri uri;
        if (requestCode == CODE) {
            if (data != null) {
                uri = data.getData();
                try {
                    dataText = readTextFromUri(uri);
                    listener.createNewScript(dataText);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//    delete All script
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.confirm_delete_scripts))
                .setPositiveButton(getString(R.string.delete_positive_all), (dialog, which) -> {
                    viewModel.deleteAllScripts();
                    activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                })
                .setNegativeButton(getString(R.string.delete_negative), (dialog, which) -> dialog.cancel())
                .show();
    }

    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}

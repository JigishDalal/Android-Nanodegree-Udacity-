package com.udacity.gradle.builditbigger;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.builditbigger.JokeAsyncTask;
import com.example.builditbigger.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = com.udacity.gradle.builditbigger.MainActivityFragment.class.getSimpleName();
   // ProgressDialog dialog=new com.udacity.gradle.builditbigger.ProgressDialog();
   private ProgressBar progressBar;



    public MainActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_main_activity, container, false);
        progressBar=  root.findViewById(R.id.progressBar);


        Button button = root.findViewById(R.id.btn_joke);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                tellJoke();
            }
        });

        return root;

    }
    public void tellJoke() {
        new JokeAsyncTask(getActivity(),progressBar).execute();
    }


}

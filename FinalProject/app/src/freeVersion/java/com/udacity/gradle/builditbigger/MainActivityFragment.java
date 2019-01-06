package com.udacity.gradle.builditbigger;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.builditbigger.JokeAsyncTask;
import com.example.builditbigger.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {
    private PublisherInterstitialAd interstitialAd = null;
    private final String LOG_TAG = com.example.builditbigger.MainActivity.class.getSimpleName();
    private ProgressBar progressBar;

    //    ProgressDialog dialog=new com.udacity.gradle.builditbigger.ProgressDialog();



    public MainActivityFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_main_activity, container, false);
        progressBar=  root.findViewById(R.id.progressBar);

        interstitialAd = new PublisherInterstitialAd(Objects.requireNonNull(getContext()));
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //process the joke Request
//                progressBar.setVisibility(View.VISIBLE);
             //   dialog.showDailyPopup(getActivity());
               progressBar.setVisibility(View.VISIBLE);
                tellJoke();

                //pre-fetch the next ad
                requestNewInterstitial();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);

                Log.i(LOG_TAG, "Failing");

                //prefetch the next ad
                requestNewInterstitial();

            }

            @Override
            public void onAdLoaded() {
                Log.i(LOG_TAG, "Loading");
                super.onAdLoaded();
            }
        });

        requestNewInterstitial();
        Button button = root.findViewById(R.id.btn_joke);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                   // dialog.showDailyPopup(getActivity());
                    tellJoke();

                }
            }
        });



        //Bottom ads Code
        AdView mAdView = (AdView) root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return root;
    }
    public void tellJoke()
    {
            new JokeAsyncTask(getActivity(),progressBar).execute();
       /* Intent intent = new Intent(getActivity(), JokeActivity.class);
    //    intent.putExtra(JokeActivity.JOKE_KEY, s);
        getActivity().startActivity(intent);
*/
    }

    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        interstitialAd.loadAd(adRequest);
    }

}

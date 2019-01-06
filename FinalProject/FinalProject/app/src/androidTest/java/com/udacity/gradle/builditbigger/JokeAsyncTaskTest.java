package com.udacity.gradle.builditbigger;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.builditbigger.JokeAsyncTask;
import com.example.builditbigger.MainActivity;
import com.example.builditbigger.ProgressDialog;
import com.example.builditbigger.R;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class JokeAsyncTaskTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void jokeAsyncTaskTest() throws InterruptedException {
        onView(withId(R.id.btn_joke))
                .check(matches(withText(R.string.button_text)));


        String result=null;
        ProgressDialog dialog = null;
        Context context=null;
        JokeAsyncTask jokeAsyncTask = new JokeAsyncTask(mActivityTestRule.getActivity(),null);
        jokeAsyncTask.execute();
        try {

            result= jokeAsyncTask.get(30L, TimeUnit.SECONDS);
            Log.d("testing", "Retrieved a non-empty string successfully: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(result);
//        Assert.assertTrue("Retrieved a non-empty string successfully: ",result.equals("null"));
    }




}

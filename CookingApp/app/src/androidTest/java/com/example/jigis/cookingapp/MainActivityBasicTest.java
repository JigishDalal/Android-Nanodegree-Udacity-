package com.example.jigis.cookingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;


@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    @Rule public ActivityTestRule<MainActivity> mainActivityActiveTestRule=
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ScrollToItem(){
       onView(ViewMatchers.withId(R.id.ry_home_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(2,click()));
   }



}

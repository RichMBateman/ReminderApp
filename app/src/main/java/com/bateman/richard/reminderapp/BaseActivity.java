package com.bateman.richard.reminderapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    /**
     * Allows an activity to show the toolbar (which includes the activity title),
     * and whether the home button will be shown.  The home button is a back arrow to take the user back to the home screen.
     * @param enableHome
     */
    void activateToolbar(boolean enableHome) {
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) {
            Log.d(TAG, "activateToolbar: actionBar is null.  Retrieving toolbar from view.");
            Toolbar toolbar = findViewById(R.id.m_toolbar);

            if(toolbar != null) {
                // setSupportActionBar sets a Toolbar to act as the ActionBar for this activity window.
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if(actionBar != null) {
            Log.d(TAG, "activateToolbar: setting home as enabled?: " + enableHome);
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}




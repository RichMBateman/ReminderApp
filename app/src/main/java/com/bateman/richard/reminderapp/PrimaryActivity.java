package com.bateman.richard.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class PrimaryActivity extends BaseActivity
    implements RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "PrimaryActivity";

    private final ReminderCollection m_reminderCollection = new ReminderCollection();
    private ReminderEntryRecyclerViewAdapter m_reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        activateToolbar(false);
        setupFloatingActionButton();
        setupRecyclerView();
        Log.d(TAG, "onCreate: end");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: start");
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: start");
        Intent intent = new Intent(this, DetailActivity.class);
        // In order to put the reminder into the intent, it must implement Serializable.
        intent.putExtra(INTENT_KEY_REMINDER_DETAIL, m_reminderCollection.getReminderAt(position));
        startActivity(intent);
    }

    @Override
    public void onItemSwipeRight(View view) {
        Log.d(TAG, "onItemSwipeRight: start");
    }

    @Override
    public void onItemSwipeLeft(View view) {
        Log.d(TAG, "onItemSwipeLeft: start");
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: start");

        RecyclerView recyclerView = findViewById(R.id.m_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        m_reminderAdapter = new ReminderEntryRecyclerViewAdapter(this, m_reminderCollection);
        recyclerView.setAdapter(m_reminderAdapter);

        Log.d(TAG, "setupRecyclerView: end");
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.m_floatingActionButton);
        // Notice the use of the lambda expression.  Requires Java 8.
        fab.setOnClickListener((view) -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });
//        This is the equivalent code without using a lambda.
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


}

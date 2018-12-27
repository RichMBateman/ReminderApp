package com.bateman.richard.reminderapp;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class PrimaryActivity extends BaseActivity
    implements RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "PrimaryActivity";
    private static final int CHANNEL_ID = 123456321; // Arbitrary number I came up with.
    private final int NEW_REMINDER_REQUEST=1;
    private final int REMINDER_DETAIL_REQUEST=2;
    private final String BUNDLE_KEY_REMINDER_COLLECTION="ReminderCollection";

    private final ReminderCollection m_reminderCollection = new ReminderCollection();
    private ReminderEntryRecyclerViewAdapter m_reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        createNotificationChannel();
        activateToolbar(false);
        setupFloatingActionButton();
        setupRecyclerView();
        setupReminderChecker();
        Log.d(TAG, "onCreate: end");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: start");
        super.onResume();
        restoreSavedData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == NEW_REMINDER_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                ReminderEntry newEntry = (ReminderEntry) data.getSerializableExtra(INTENT_KEY_REMINDER_DETAIL);
                m_reminderCollection.addReminder(newEntry, true);
                m_reminderAdapter.notifyDataSetChanged(); // Let the adapter know there is a new reminder.

                saveReminderCollectionData();
            }
        } else if(requestCode== REMINDER_DETAIL_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                int updatedReminderPosition = data.getIntExtra(INTENT_KEY_REMINDER_POSITION, 0);
                m_reminderCollection.removeReminderAt(updatedReminderPosition);
                ReminderEntry updatedReminderEntry = (ReminderEntry) data.getSerializableExtra(INTENT_KEY_REMINDER_DETAIL);
                m_reminderCollection.addReminder(updatedReminderEntry, true);
                m_reminderAdapter.notifyDataSetChanged();

                saveReminderCollectionData();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if(data.hasExtra(INTENT_KEY_REMINDER_POSITION)) {
                    int updatedReminderPosition = data.getIntExtra(INTENT_KEY_REMINDER_POSITION, 0);
                    m_reminderCollection.removeReminderAt(updatedReminderPosition);
                    m_reminderAdapter.notifyDataSetChanged();

                    saveReminderCollectionData();
                }
            }
        }
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
        intent.putExtra(INTENT_KEY_REMINDER_POSITION, position);
        //startActivity(intent);
        startActivityForResult(intent, REMINDER_DETAIL_REQUEST);
    }

    @Override
    public void onItemSwipeRight(View view) {
        Log.d(TAG, "onItemSwipeRight: start");
    }
    @Override
    public void onItemSwipeLeft(View view) {
        Log.d(TAG, "onItemSwipeLeft: start");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: start");
        outState.putSerializable(BUNDLE_KEY_REMINDER_COLLECTION, m_reminderCollection);
        super.onSaveInstanceState(outState);
    }

    private void saveReminderCollectionData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String reminderCollectionString = ObjectSerializerHelper.objectToString(m_reminderCollection);
        editor.putString(BUNDLE_KEY_REMINDER_COLLECTION, reminderCollectionString);
        editor.commit();
    }

    private void restoreSavedData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String reminderCollectionString = sharedPreferences.getString(BUNDLE_KEY_REMINDER_COLLECTION, "");
        ReminderCollection reminderCollection = (ReminderCollection) ObjectSerializerHelper.stringToObject(reminderCollectionString);
        if(reminderCollection!= null) {
            m_reminderCollection.copyFrom(reminderCollection);
        }
    }

    /**
     * This method is called after onStart() when the activity is being re-initialized from a previously saved state.
     * I don't think this is a good place for restoring saved data.
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: start");
        ReminderCollection reminderCollection = (ReminderCollection) savedInstanceState.getSerializable(BUNDLE_KEY_REMINDER_COLLECTION);
        m_reminderCollection.copyFrom(reminderCollection);
        super.onRestoreInstanceState(savedInstanceState);
    }

    /** You must create the notification channel before posting any notifications on Android 8.0 and higher.
     * You should execute this code as soon as app starts.  it's safe to call repeatedly because
     * creating an existing notification channel performs no operation.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "WeeklyReminders"; // getString(R.string.channel_name);
            String description = "Weekly Reminders"; // getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: start");

        RecyclerView recyclerView = findViewById(R.id.m_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        m_reminderAdapter = new ReminderEntryRecyclerViewAdapter(this, m_reminderCollection);
        recyclerView.setAdapter(m_reminderAdapter);

        // This enables us to swipe reminders left and right.
        SwipeController swipeController = new SwipeController(m_reminderCollection, m_reminderAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        Log.d(TAG, "setupRecyclerView: end");
    }

    private void setupFloatingActionButton() {
        Log.d(TAG, "setupFloatingActionButton: start");
        FloatingActionButton fab = findViewById(R.id.m_floatingActionButton);
        // Notice the use of the lambda expression.  Requires Java 8.
        fab.setOnClickListener((view) -> {
            launchNewReminderActivity();
            // Below is an example of using Snackbar to show text.
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
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

    private void launchNewReminderActivity() {
        Log.d(TAG, "launchNewReminderActivity: start");
        Intent intent = new Intent(this, DetailActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, NEW_REMINDER_REQUEST);
    }

    private void setupReminderChecker() {
        Context appContext = this;
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        int lapsedReminderCount = m_reminderCollection.getCountOfLapsedReminders();
                        // Only send a notification if there are some overdue reminders.
                        if(lapsedReminderCount > 0) {
                            NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
                            int notificationId = 1;

                            String contentMessage = String.format("You have %d overdue reminder(s).", lapsedReminderCount);
                            String contentTitle = "Weekly Reminders";

                            Intent intent = new Intent(appContext, PrimaryActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext, "1")
                                    // .setSmallIcon(R.drawable.notification_icon)
                                    .setSmallIcon(R.drawable.notification_icon_rat)
                                    .setContentTitle(contentTitle)
                                    .setContentText(contentMessage)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    // Set the intent that will fire when the user taps the notification
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);


                            Notification notification = mBuilder.build();
                            notificationManager.notify(notificationId, notification);
                        }
                    }
                    catch (Exception e) {
                        //Snackbar.make()
                    }
                });
            }
        };

        int delayInMs = 0;
        int periodInMs = 60 * 1000;
        // "schedule" only does it ONE time.
        //timer.schedule(doAsynchronousTask, delayInMs);
        timer.scheduleAtFixedRate(doAsynchronousTask, delayInMs, periodInMs);
    }
}

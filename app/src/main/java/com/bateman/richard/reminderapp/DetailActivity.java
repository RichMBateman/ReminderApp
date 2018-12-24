package com.bateman.richard.reminderapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It was necessary to create a simple layout similar to "activity_primary.xml", except it would contain a "reminder_entry" layout.
        // Previously, I naively set the content view to be "reminder_detail"... but there was no toolbar!
        setContentView(R.layout.activity_reminder);

        activateToolbar(true);

        Intent intent = getIntent();
        ReminderEntry reminderEntry = (ReminderEntry) intent.getSerializableExtra(INTENT_KEY_REMINDER_DETAIL);
        if(reminderEntry != null) {
            TextView reminderText = findViewById(R.id.m_reminderText);
            reminderText.setText(reminderEntry.getReminderText());

//
//            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
//            Resources resources = getResources();
//            String text = resources.getString(R.string.photo_title_text, photo.getTitle());
//            photoTitle.setText(text);
////            photoTitle.setText("Title: " + photo.getTitle());
//
//            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
//            photoTags.setText(resources.getString(R.string.photo_tags_text, photo.getTags()));
////            photoTags.setText("Tags: " + photo.getTags());
//
//            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
//            photoAuthor.setText(photo.getAuthor());
        }
    }
}

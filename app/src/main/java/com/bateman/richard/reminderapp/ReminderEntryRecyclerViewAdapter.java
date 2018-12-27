package com.bateman.richard.reminderapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;

/**
 * An adapter for displaying Reminders in a RecyclerView.
 */
public class ReminderEntryRecyclerViewAdapter extends RecyclerView.Adapter<ReminderEntryRecyclerViewAdapter.ReminderEntryViewHolder> {
    private static final String TAG = "ReminderEntryRecyclerVi";
    private final ReminderCollection m_reminderCollection;
    private final Context m_context;
    private final SimpleDateFormat m_daySepFormatter = new SimpleDateFormat("EEEE, MMMM d yyyy");
    private final SimpleDateFormat m_remTimeFormatter = new SimpleDateFormat("hh:mm a");
//    private final DateTimeFormatter m_daySepFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d u");
//    private final DateTimeFormatter m_remTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");



    /**
     * (Reminder: A static inner class is basically like a class defined in its own file, but for convenience,
     * has been packaged inside another class.)
     * This class is a ViewHolder, which is meant to hold onto references to all of the views inside a RecyclerView view.
     *
     */
    static class ReminderEntryViewHolder extends RecyclerView.ViewHolder {
       // private final DateTimeFormatter m_daySepFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d u");
       // private final DateTimeFormatter m_remTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        private static final String TAG = "ReminderEntryViewHolder";
        private TextView m_labelDateTime;
        private TextView m_labelReminderText;
        private TextView m_daySeparatorBar;
        private ReminderEntry m_entry;

        public ReminderEntryViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ReminderEntryViewHolder: start");
            m_labelDateTime = itemView.findViewById(R.id.m_labelDateTime);
            m_labelReminderText = itemView.findViewById(R.id.m_lblReminderText);
            m_daySeparatorBar = itemView.findViewById(R.id.m_daySeparatorBar);
        }

//        public void updateUI() {
//            Boolean isEntryFirstInGroup = m_entry.getFirstInGroup();
//            // View.INVISIBLE is similar to GONE EXCEPT the hidden widget still takes up space.
//            m_daySeparatorBar.setVisibility(isEntryFirstInGroup ? View.VISIBLE : View.GONE);
//            LocalDate reminderGroupDate = m_entry.getNextOccurrence().toLocalDate();
//            String formattedDaySepText = reminderGroupDate.format(m_daySepFormatter);
//            m_daySeparatorBar.setText(formattedDaySepText);
//            m_labelReminderText.setText(m_entry.getReminderText());
//
//            // Need to get the reminder's next occurrence (and not the reminder time) because the user may have snoozed.
//            String formattedReminderTimeText = m_entry.getNextOccurrence().toLocalTime().format(m_remTimeFormatter);
//            m_labelDateTime.setText(formattedReminderTimeText);
//        }

        public void setReminderEntry(ReminderEntry entry) {m_entry = entry;}
        public ReminderEntry getReminderEntry() {return m_entry;}

        public TextView getLabelDateTime() {
            return m_labelDateTime;
        }

        public TextView getLabelReminderText() {
            return m_labelReminderText;
        }

        public TextView getDaySeparatorBar() {
            return m_daySeparatorBar;
        }
    }

    public ReminderEntryRecyclerViewAdapter(Context context, ReminderCollection reminderCollection) {
        m_context=context;
        m_reminderCollection=reminderCollection;
    }

    /**
     * Called by the layout manager when it needs a new view.
     */
    @NonNull
    @Override
    public ReminderEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_entry, parent, false);
        return new ReminderEntryViewHolder(view);
    }

    /**
     * Called by the layout manager when it wants new data in an existing view (row)
     * If we have no reminders, we will set some default properties into the placeholder.
     * Otherwise, get the specific reminder using the position argument, and update the views in the holder accordingly.
     */
    @Override
    public void onBindViewHolder(@NonNull ReminderEntryViewHolder holder, int position) {
        if(m_reminderCollection == null || m_reminderCollection.getCount() == 0) {

        } else {
            ReminderEntry reminderEntry = m_reminderCollection.getReminderAt(position);
            holder.setReminderEntry(reminderEntry);

            Boolean isEntryFirstInGroup = reminderEntry.getFirstInGroup();
            // View.INVISIBLE is similar to GONE EXCEPT the hidden widget still takes up space.
            holder.m_daySeparatorBar.setVisibility(isEntryFirstInGroup ? View.VISIBLE : View.GONE);

            Date reminderGroupDate = reminderEntry.getNextOccurrence();
            String formattedDaySepText = m_daySepFormatter.format(reminderGroupDate);
            holder.m_daySeparatorBar.setText(formattedDaySepText);
            holder.m_labelReminderText.setText(reminderEntry.getReminderText());

            // Need to get the reminder's next occurrence (and not the reminder time) because the user may have snoozed.
            String formattedReminderTimeText = m_remTimeFormatter.format(reminderEntry.getNextOccurrence());
            holder.m_labelDateTime.setText(formattedReminderTimeText);
        }
    }

    @Override
    public int getItemCount() {
        // Ask the reminder collection how many items there are.
        // If you want to always display some kind of empty placeholder thing, always return at least 1.
        return m_reminderCollection.getCount();
    }
}

package com.bateman.richard.reminderapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * An adapter for displaying Reminders in a RecyclerView.
 */
public class ReminderEntryRecyclerViewAdapter extends RecyclerView.Adapter<ReminderEntryRecyclerViewAdapter.ReminderEntryViewHolder> {
    private static final String TAG = "ReminderEntryRecyclerVi";
    private final ReminderCollection m_reminderCollection;
    private final Context m_context;
    private final DateTimeFormatter m_daySepFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d u");
    private final DateTimeFormatter m_remTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    /**
     * (Reminder: A static inner class is basically like a class defined in its own file, but for convenience,
     * has been packaged inside another class.)
     * This class is a ViewHolder, which is meant to hold onto references to all of the views inside a RecyclerView view.
     *
     */
    static class ReminderEntryViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ReminderEntryViewHolder";
        private TextView m_labelDateTime;
        private TextView m_labelReminderText;
        private TextView m_daySeparatorBar;

        public ReminderEntryViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ReminderEntryViewHolder: start");
            m_labelDateTime = itemView.findViewById(R.id.m_labelDateTime);
            m_labelReminderText = itemView.findViewById(R.id.m_lblReminderText);
            m_daySeparatorBar = itemView.findViewById(R.id.m_daySeparatorBar);
        }

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
            Boolean isEntryFirstInGroup = reminderEntry.getFirstInGroup();
            // View.INVISIBLE is similar to GONE EXCEPT the hidden widget still takes up space.
            holder.m_daySeparatorBar.setVisibility(isEntryFirstInGroup ? View.VISIBLE : View.GONE);
            LocalDate reminderGroupDate = reminderEntry.getNextOccurrence().toLocalDate();
            String formattedDaySepText = reminderGroupDate.format(m_daySepFormatter);
            holder.m_daySeparatorBar.setText(formattedDaySepText);
            holder.m_labelReminderText.setText(reminderEntry.getReminderText());

            String formattedReminderTimeText = reminderEntry.getReminderTime().format(m_remTimeFormatter);
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

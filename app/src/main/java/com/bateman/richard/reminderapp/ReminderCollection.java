package com.bateman.richard.reminderapp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Manages the collection of reminders, and when to create new ones.
 */
public class ReminderCollection {
    private final List<ReminderEntry> m_reminderList;

    public ReminderCollection() {
        m_reminderList = new ArrayList<>();
        // Uncomment below if you want to populate your reminder collection with some test data.
        addTestData();
    }

    public ReminderEntry getReminderAt(int position) {
        ReminderEntry entry = m_reminderList.get(position);
        return entry;
    }

    public int getCount() {
        return m_reminderList.size();
    }

    public void addReminder(ReminderEntry entry, boolean deriveNextOccurrence) {
        if(deriveNextOccurrence) {
            entry.deriveNextOccurrence();
        }
        int insertPos = findInsertPosition(entry);
        m_reminderList.add(insertPos, entry);
        deriveFirstInGroups();
    }

    public void removeReminderAt(int position) {
        m_reminderList.remove(position);
        deriveFirstInGroups();
    }

    // binary search: https://www.programcreek.com/2013/01/leetcode-search-insert-position/
    private int findInsertPosition(ReminderEntry newEntry) {
        int position = 0;

        if(m_reminderList.size() > 0) {
            int left = 0;
            int right = m_reminderList.size();
            while(left < right) {
                int mid = left + (right-left)/2;
                if(newEntry.compareTo(m_reminderList.get(mid)) > 0) {
                    // The new entry is greater than middle, so move up the left boundary
                    left = mid+1;
                } else {
                    // Otherwise, we need to move in the right boundary.
                    right = mid;
                }
            }

            position = left;
        }

        return position;
    }

    private void sortReminders() {
        Collections.sort(m_reminderList);
    }

    /**
     * Iterate over all reminders and determine whether each entry is the first in the group or not;
     * Assumes the records are already sorted by date.
     */
    private void deriveFirstInGroups() {
        LocalDate groupDate = null;
        for(int i = 0; i < m_reminderList.size(); i++) {
            ReminderEntry entry = m_reminderList.get(i);
            if(i == 0) {
                // The first reminder is always the first in the group
                entry.setFirstInGroup(true);
                groupDate = entry.getNextOccurrence().toLocalDate();
            } else {
                LocalDate currentEntryDate = entry.getNextOccurrence().toLocalDate();
                if(currentEntryDate.equals(groupDate)) {
                    entry.setFirstInGroup(false);
                } else {
                    entry.setFirstInGroup(true);
                    groupDate = currentEntryDate;
                }
            }
        }
    }

    /**
     * This function is used to initialize our reminder collection with some sample data.
     * This is for testing purposes.
     */
    private void addTestData() {
        ReminderEntry entry01 = new ReminderEntry(LocalTime.of(9,0), "Brush teeth.", EnumSet.allOf(DayOfWeek.class), true);
        ReminderEntry entry02 = new ReminderEntry(LocalTime.of(18,0), "Put out garbage.", EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), true);
        ReminderEntry entry03 = new ReminderEntry(LocalTime.of(8,45), "Check mailbox for package.", EnumSet.of(DayOfWeek.TUESDAY), false);
        ReminderEntry entry04 = new ReminderEntry(LocalTime.of(22,15), "Go to bed asap.  Go to bed asap. Go to bed asap.  Go to bed asap. Go to bed asap.  Go to bed asap. Go to bed asap.  Go to bed asap. Go to bed asap.  Go to bed asap. ", EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.THURSDAY), true);
        ReminderEntry entry05 = new ReminderEntry(LocalTime.of(9,0), "Check my wallet for cash.", EnumSet.allOf(DayOfWeek.class), true);
        ReminderEntry entry06 = new ReminderEntry(LocalTime.of(5,20), "Wake up for work.", EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY), true);
        ReminderEntry entry07 = new ReminderEntry(LocalTime.of(17,30), "Make sure you wish Joe a happy birthday.", EnumSet.of(DayOfWeek.THURSDAY), false);
        ReminderEntry entry08 = new ReminderEntry(LocalTime.of(12,0), "Log what you ate for the day.", EnumSet.allOf(DayOfWeek.class), true);
        ReminderEntry entry09 = new ReminderEntry(LocalTime.of(13,30), "Update progress on project.  Make sure to notify supervisor of any important updates.", EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY), true);
        ReminderEntry entry10 = new ReminderEntry(LocalTime.of(18,15), "Half-day at work, today.", EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY), false);

        m_reminderList.add(entry01);
        m_reminderList.add(entry02);
        m_reminderList.add(entry03);
        m_reminderList.add(entry04);
        m_reminderList.add(entry05);
        m_reminderList.add(entry06);
        m_reminderList.add(entry07);
        m_reminderList.add(entry08);
        m_reminderList.add(entry09);
        m_reminderList.add(entry10);

        sortReminders();
        deriveFirstInGroups();
    }
}

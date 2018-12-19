package com.bateman.richard.reminderapp;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.TreeSet;

/**
 * Manages the collection of reminders, and when to create new ones.
 */
public class ReminderCollection {
    private final TreeSet<ReminderEntry> m_set;

    public ReminderCollection() {
        m_set = new TreeSet<>();
        // Temporary code just for testing.
        addTestData();
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

        m_set.add(entry01);
        m_set.add(entry02);
        m_set.add(entry03);
        m_set.add(entry04);
        m_set.add(entry05);
        m_set.add(entry06);
        m_set.add(entry07);
        m_set.add(entry08);
        m_set.add(entry09);
        m_set.add(entry10);
    }
}

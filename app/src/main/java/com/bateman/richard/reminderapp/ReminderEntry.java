package com.bateman.richard.reminderapp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;

/**
 * Represents a single reminder.
 */
public class ReminderEntry implements Comparable<ReminderEntry> {
    private final int SNOOZE_LIMIT_FOR_CALCULATION = 4;
    private final int[] SNOOZE_ARRAY = {1,2,4,8,24};

    private LocalDateTime m_nextOccurrence;
    private LocalTime m_reminderTime;
    private EnumSet<DayOfWeek> m_selectedDaysOfWeek;
    private String m_reminderText;
    private Boolean m_recurs;
    private int m_timesSnoozed;

    /**
     * Creates a new reminder at the specified time.
     * @param nextOccurrence The datetime when this reminder will next go off.
     * @param reminderTime The hour and minute this reminder should occur.
     * @param reminderText The text for this reminder.
     * @param selectedDaysOfWeek What days should this reminder be for.
     * @param recurs Whether this reminder recurs each week.
     */
    public ReminderEntry(LocalDateTime nextOccurrence, LocalTime reminderTime, String reminderText, EnumSet<DayOfWeek> selectedDaysOfWeek, Boolean recurs) {
        this.m_nextOccurrence = nextOccurrence;
        this.m_reminderTime = reminderTime;
        this.m_selectedDaysOfWeek = selectedDaysOfWeek;
        this.m_reminderText = reminderText;
        this.m_recurs = recurs;
    }

    /**
     * Creates a new reminder entry, and assumes the next occurrence is based on the current date and time.
     * @param reminderTime
     * @param reminderText
     * @param selectedDaysOfWeek
     * @param recurs
     */
    public ReminderEntry(LocalTime reminderTime, String reminderText, EnumSet<DayOfWeek> selectedDaysOfWeek, Boolean recurs) {
        this.m_reminderTime = reminderTime;
        this.m_selectedDaysOfWeek = selectedDaysOfWeek;
        this.m_reminderText = reminderText;
        this.m_recurs = recurs;

        deriveNextOccurrence();
    }

    /**
     * Snoozes this reminder for some number of hours, based on how often it's been snoozed already.
     */
    public void snooze() {
        int hoursToAdd = SNOOZE_ARRAY[m_timesSnoozed];
        if(m_timesSnoozed < SNOOZE_LIMIT_FOR_CALCULATION) {
            m_timesSnoozed++;
        }
        m_nextOccurrence = m_nextOccurrence.plusHours(hoursToAdd);
    }

    /**
     * Marks this reminder as complete.  If the reminder is not marked to recur, there is no point to mark reminder as complete.  Just remove it.
     */
    public void complete() {
        if(m_recurs) {
            m_timesSnoozed=0;
            // Before deriving next occurrence, set the current time to 00:00 for the next day.
            // Realize that a user could mark a reminder way in advance.
            m_nextOccurrence = LocalDateTime.of(m_nextOccurrence.plusDays(1).toLocalDate(), LocalTime.of(0,0));
            deriveNextOccurrence();
        }
    }

    /**
     * @param that The object to compare to.
     * @return A negative integer, zero, or positive if this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(ReminderEntry that) {
        final int EQUAL = 0;

        if(this==that) return EQUAL;

        int compareTimes = m_reminderTime.compareTo(that.m_reminderTime);
        if(compareTimes != EQUAL) return compareTimes;
        else
        {
            // The times are equal.  Compare the strings.
            // Ignore case
            int compareText = m_reminderText.toLowerCase().compareTo(that.m_reminderText.toLowerCase());
            return compareText;
        }
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(!(that instanceof ReminderEntry)) return false;
        ReminderEntry thatEntry = (ReminderEntry) that;
        boolean equals = (m_reminderTime.equals(thatEntry.m_reminderTime) && m_reminderText.equalsIgnoreCase(thatEntry.m_reminderText));
        return equals;
    }

    /**
     * Given that the reminder entry has everything set except for its next occurrence, figure out the next time this reminder should occur.
     */
    private void deriveNextOccurrence() {
        LocalDateTime rightNow = LocalDateTime.now();
        LocalDate currentDate = rightNow.toLocalDate();
        LocalTime currentTime = rightNow.toLocalTime();

        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
        DayOfWeek startDayToCheck = currentDayOfWeek;
        int daysToAdd = 0;

        // If it's past the reminder time, then start by checking tomorrow.  Else, we can start by checking today.
        if(currentTime.compareTo(m_reminderTime) > 0) {
            startDayToCheck = startDayToCheck.plus(1);
            daysToAdd++;
        }

        DayOfWeek dayToCheck = startDayToCheck;
        do {
            if(m_selectedDaysOfWeek.contains(dayToCheck)) {
                break;
            }
            daysToAdd++;
            dayToCheck = dayToCheck.plus(1);
        } while(dayToCheck != startDayToCheck); // Stop once we've advanced to next week

        m_nextOccurrence = LocalDateTime.of(currentDate.plusDays(daysToAdd), m_reminderTime);
    }
}
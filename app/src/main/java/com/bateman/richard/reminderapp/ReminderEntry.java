package com.bateman.richard.reminderapp;

import java.io.Serializable;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

/**
 * Represents a single reminder.
 */
public class ReminderEntry implements Comparable<ReminderEntry>, Serializable {
    private static final long serialVersionUID = 1L;

    private final int SNOOZE_LIMIT_FOR_CALCULATION = 4;
    private final int[] SNOOZE_ARRAY = {1,2,4,8,24};

//    private LocalDateTime m_nextOccurrence;
//    private LocalTime m_reminderTime;
//    private EnumSet<DayOfWeek> m_selectedDaysOfWeek;

    private Date m_nextOccurrence;
    private Date m_reminderTime;
    private EnumSet<CalDayOfWeek> m_selectedDaysOfWeek;

    private String m_reminderText;
    private Boolean m_recurs;
    private int m_timesSnoozed;
    private Boolean m_firstInGroup;

    /**
     * Creates a new reminder at the specified time.
     * @param nextOccurrence The datetime when this reminder will next go off.
     * @param reminderTime The hour and minute this reminder should occur.
     * @param reminderText The text for this reminder.
     * @param selectedDaysOfWeek What days should this reminder be for.
     * @param recurs Whether this reminder recurs each week.
     */
    //public ReminderEntry(LocalDateTime nextOccurrence, LocalTime reminderTime, String reminderText, EnumSet<DayOfWeek> selectedDaysOfWeek, Boolean recurs) {
    public ReminderEntry(Date nextOccurrence, Date reminderTime, String reminderText, EnumSet<CalDayOfWeek> selectedDaysOfWeek, Boolean recurs) {
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
    //public ReminderEntry(LocalTime reminderTime, String reminderText, EnumSet<DayOfWeek> selectedDaysOfWeek, Boolean recurs) {
    public ReminderEntry(Date reminderTime, String reminderText, EnumSet<CalDayOfWeek> selectedDaysOfWeek, Boolean recurs) {
        this.m_reminderTime = reminderTime;
        this.m_selectedDaysOfWeek = selectedDaysOfWeek;
        this.m_reminderText = reminderText;
        this.m_recurs = recurs;

        deriveNextOccurrence();
    }

    public ReminderEntry() {

    }

    /**
     * Returns the datetime this reminder should occur.
     * @return
     */
    //public LocalDateTime getNextOccurrence() {
    public Date getNextOccurrence() {
        return m_nextOccurrence;
    }

    /**
     * Returns the time at which this reminder is set to occur (NOT THE DATE!)
     * @return
     */
    //public LocalTime getReminderTime() {
    public Date getReminderTime() {
        return m_reminderTime;
    }

    //public void setReminderTime(LocalTime reminderTime) {
    public void setReminderTime(Date reminderTime) {
        m_reminderTime = reminderTime;
    }

    //public EnumSet<DayOfWeek> getSelectedDaysOfWeek() {
    public EnumSet<CalDayOfWeek> getSelectedDaysOfWeek() {
        return m_selectedDaysOfWeek;
    }

    //public void setSelectedDaysOfWeek(EnumSet<DayOfWeek> selectedDaysOfWeek) {
    public void setSelectedDaysOfWeek(EnumSet<CalDayOfWeek> selectedDaysOfWeek) {
        m_selectedDaysOfWeek = selectedDaysOfWeek;
    }

    public String getReminderText() {
        return m_reminderText;
    }

    public void setReminderText(String reminderText) {
        m_reminderText = reminderText;
    }

    public Boolean getRecurs() {
        return m_recurs;
    }

    public void setRecurs(Boolean recurs) {
        m_recurs = recurs;
    }

    public Boolean getFirstInGroup() {
        return m_firstInGroup;
    }

    /**
     * Used to indicate that this reminder entry is the first in its group
     * (so on the app, the separator bar should display)
     * @param firstInGroup
     */
    public void setFirstInGroup(Boolean firstInGroup) {
        m_firstInGroup = firstInGroup;
    }

    /**
     * Snoozes this reminder for some number of hours, based on how often it's been snoozed already.
     * After snoozing, you need to put this reminder into an appropriate position.
     */
    public void snooze() {
        int hoursToAdd = SNOOZE_ARRAY[m_timesSnoozed];
        if(m_timesSnoozed < SNOOZE_LIMIT_FOR_CALCULATION) {
            m_timesSnoozed++;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(m_nextOccurrence);
        cal.add(Calendar.HOUR, hoursToAdd);

        //m_nextOccurrence = m_nextOccurrence.plusHours(hoursToAdd);
        m_nextOccurrence = cal.getTime();
    }

    /**
     * Marks this reminder as complete.  If the reminder is not marked to recur, there is no point to mark reminder as complete.  Just remove it.
     * If it DOES recur, it will be updated with the next date of its occurrence,
     * in which case this item must be put into an appropriate position in the collection.
     */
    public void complete() {
        if(m_recurs) {
            m_timesSnoozed=0;
            // Before deriving next occurrence, set the current time to 00:00 for the next day.
            // Realize that a user could mark a reminder way in advance.

            //m_nextOccurrence = LocalDateTime.of(m_nextOccurrence.plusDays(1).toLocalDate(), LocalTime.of(0,0));
            Calendar cal = Calendar.getInstance();
            cal.setTime(m_nextOccurrence);
            cal.add(Calendar.DAY_OF_YEAR, 1);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            m_nextOccurrence = cal.getTime();

            deriveNextOccurrence(m_nextOccurrence);
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

        int compareTimes = m_nextOccurrence.compareTo(that.m_nextOccurrence);
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

    public void deriveNextOccurrence() {
        //LocalDateTime rightNow = LocalDateTime.now();
        Date rightNow = Calendar.getInstance().getTime();
        deriveNextOccurrence(rightNow);
    }

    /**
     * Given that the reminder entry has everything set except for its next occurrence, figure out the next time this reminder should occur.
     */
    public void deriveNextOccurrence(Date baseTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseTime);

        int currentDayOfWeekIntVal = cal.get(Calendar.DAY_OF_WEEK);
        CalDayOfWeek currentDayOfWeek = CalDayOfWeek.fromInt(currentDayOfWeekIntVal);

        CalDayOfWeek startDayToCheck = currentDayOfWeek;
        int daysToAdd = 0;

        // If it's past the reminder time, then start by checking tomorrow.  Else, we can start by checking today.
        int currentTimeInMinutes = cal.get(Calendar.HOUR) * 60 + cal.get(Calendar.MINUTE);
        cal.setTime(m_reminderTime);
        int baseReminderTimeInMinutes = cal.get(Calendar.HOUR) * 60 + cal.get(Calendar.MINUTE);

        if(currentTimeInMinutes < baseReminderTimeInMinutes) {
            startDayToCheck = CalDayOfWeek.fromInt((startDayToCheck.getValue() + 1) % CalDayOfWeek.SATURDAY.getValue() );
            daysToAdd++;
        }

        CalDayOfWeek dayToCheck = startDayToCheck;
        do {
            if(m_selectedDaysOfWeek.contains(dayToCheck)) {
                break;
            }
            daysToAdd++;
            dayToCheck = dayToCheck.addOne();
        } while(dayToCheck != startDayToCheck); // Stop once we've advanced to next week

        Calendar reminderTimeCal = Calendar.getInstance();
        reminderTimeCal.setTime(m_reminderTime);

        cal.setTime(baseTime);
        cal.set(Calendar.HOUR, reminderTimeCal.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE, reminderTimeCal.get(Calendar.MINUTE));
        cal.add(Calendar.DAY_OF_YEAR, daysToAdd);
        m_nextOccurrence = cal.getTime();
        //m_nextOccurrence = LocalDateTime.of(currentDate.plusDays(daysToAdd), m_reminderTime);
    }

//    public void deriveNextOccurrence(LocalDateTime baseTime) {
////        LocalDateTime rightNow = LocalDateTime.now();
//        LocalDate currentDate = baseTime.toLocalDate();
//        LocalTime currentTime = baseTime.toLocalTime();
//
//        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
//        DayOfWeek startDayToCheck = currentDayOfWeek;
//        int daysToAdd = 0;
//
//        // If it's past the reminder time, then start by checking tomorrow.  Else, we can start by checking today.
//        if(currentTime.compareTo(m_reminderTime) > 0) {
//            startDayToCheck = startDayToCheck.plus(1);
//            daysToAdd++;
//        }
//
//        DayOfWeek dayToCheck = startDayToCheck;
//        do {
//            if(m_selectedDaysOfWeek.contains(dayToCheck)) {
//                break;
//            }
//            daysToAdd++;
//            dayToCheck = dayToCheck.plus(1);
//        } while(dayToCheck != startDayToCheck); // Stop once we've advanced to next week
//
//        m_nextOccurrence = LocalDateTime.of(currentDate.plusDays(daysToAdd), m_reminderTime);
//    }
}

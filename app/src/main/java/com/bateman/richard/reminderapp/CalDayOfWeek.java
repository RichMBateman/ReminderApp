package com.bateman.richard.reminderapp;


import java.util.Calendar;

/**
 * Simple enumeration for calendar days of the week.
 */
public enum CalDayOfWeek {
    SUNDAY(Calendar.SUNDAY),
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY);

    private final int m_value;
    //public final int MAX_DAY = Calendar.SATURDAY; // 7

    CalDayOfWeek(int value) {
        m_value = value;
    }

    public int getValue() {return m_value;}

    /**
     * Returns a new CalDayOfWeek that is one day ahead of this one.
     * @return
     */
    public CalDayOfWeek addOne() {
        int newValue = (m_value + 1) % 8;
        if(newValue == 0) newValue++;
        return CalDayOfWeek.fromInt(newValue);
    }

    public static CalDayOfWeek fromInt(int x) {
        CalDayOfWeek value = SUNDAY;

        switch(x) {
            case Calendar.SUNDAY: value = SUNDAY; break;
            case Calendar.MONDAY: value = MONDAY; break;
            case Calendar.TUESDAY: value = TUESDAY; break;
            case Calendar.WEDNESDAY: value = WEDNESDAY; break;
            case Calendar.THURSDAY: value = THURSDAY; break;
            case Calendar.FRIDAY: value = FRIDAY; break;
            case Calendar.SATURDAY: value = SATURDAY; break;
        }

        return value;
    }
}


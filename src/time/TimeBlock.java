/**
 * TimeBlock contains a date as well as a start and end time and implements various helper methods
 * 
 * @author Sean Yang
 * @since May 30, 2025
 */

package time;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TimeBlock {
    public enum Month {
        JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC
    }

    private final int year;
    private final Month month;
    private final int day;
    private final double startHour;
    private final double endHour;

    public static final int DAYS_IN_YEAR = 365;
    public static final Map<Month, Integer> DAYS_BEFORE_MONTH;
    static {
        Map<Month, Integer> foo = new HashMap<>();
        foo.put(Month.JAN, 0);
        foo.put(Month.FEB, 31);
        foo.put(Month.MAR, 59);
        foo.put(Month.APR, 90);
        foo.put(Month.MAY, 120);
        foo.put(Month.JUN, 151);
        foo.put(Month.JUL, 181);
        foo.put(Month.AUG, 212);
        foo.put(Month.SEP, 243);
        foo.put(Month.OCT, 273);
        foo.put(Month.NOV, 304);
        foo.put(Month.DEC, 334);

        DAYS_BEFORE_MONTH = Collections.unmodifiableMap(foo);
    }
    public static final Map<Month, Integer> DAYS_IN_MONTH;
    static {
        Map<Month, Integer> bar = new HashMap<>();
        bar.put(Month.JAN, 31);
        bar.put(Month.FEB, 28);
        bar.put(Month.MAR, 31);
        bar.put(Month.APR, 30);
        bar.put(Month.MAY, 31);
        bar.put(Month.JUN, 30);
        bar.put(Month.JUL, 31);
        bar.put(Month.AUG, 31);
        bar.put(Month.SEP, 30);
        bar.put(Month.OCT, 31);
        bar.put(Month.NOV, 30);
        bar.put(Month.DEC, 31);

        DAYS_IN_MONTH = Collections.unmodifiableMap(bar);
    }
    public static final Map<String, Month> ABBR_TO_MONTH;
    static {
        Map<String, Month> baz = new HashMap<>();
        baz.put("JAN", Month.JAN);
        baz.put("FEB", Month.FEB);
        baz.put("MAR", Month.MAR);
        baz.put("APR", Month.APR);
        baz.put("MAY", Month.MAY);
        baz.put("JUN", Month.JUN);
        baz.put("JUL", Month.JUL);
        baz.put("AUG", Month.AUG);
        baz.put("SEP", Month.SEP);
        baz.put("OCT", Month.OCT);
        baz.put("NOV", Month.NOV);
        baz.put("DEC", Month.DEC);

        ABBR_TO_MONTH = Collections.unmodifiableMap(baz);
    }
    public static final int HOURS_IN_DAY = 24;

    /**
     * TimeBlock constructor for a whole-day event
     * 
     * @param year
     * @param month
     * @param day
     */
    public TimeBlock(int year, Month month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        startHour = 0;
        endHour = 24;
    }

    /**
     * TimeBlock constructor for an event without an end time
     * 
     * @param year
     * @param month
     * @param day
     * @param startHour
     */
    public TimeBlock(int year, Month month, int day, double startHour) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        endHour = startHour;
    }

    /**
     * TimeBlock constructor for an event with a duration
     * 
     * @param year
     * @param month
     * @param day
     * @param startHour
     * @param duration
     */
    public TimeBlock(int year, Month month, int day, double startHour, double duration) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        endHour = startHour + duration;
    }

    public TimeBlock() {
        this.year = 2025;
        this.month = Month.JAN;
        this.day = 1;
        this.startHour = 0;
        endHour = startHour;
    }

    /**
     * TimeBlock constructor for a TimeBlock with the same date as other but
     * different start time and duration
     * 
     * @param other
     * @param startHour
     * @param duration
     */
    public TimeBlock(TimeBlock other, double startHour, double duration) {
        this.year = other.year;
        this.month = other.month;
        this.day = other.day;
        this.startHour = startHour;
        endHour = startHour + duration;
    }

    /**
     * checks whether a year is a leap year
     * 
     * @param year the year
     * @return whether it is a leap year
     */
    public static boolean isLeapYear(int year) {
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else
            return year % 4 == 0;
    }

    public static Month nextMonth(Month month) {
        return switch (month) {
            case JAN -> Month.FEB;
            case FEB -> Month.MAR;
            case MAR -> Month.APR;
            case APR -> Month.MAY;
            case MAY -> Month.JUN;
            case JUN -> Month.JUL;
            case JUL -> Month.AUG;
            case AUG -> Month.SEP;
            case SEP -> Month.OCT;
            case OCT -> Month.NOV;
            case NOV -> Month.DEC;
            default -> Month.JAN;
        };
    }

    public TimeBlock nextDay() {
        int newDay = day + 1;
        Month newMonth = month;
        int newYear = year;

        if (newDay > DAYS_IN_MONTH.get(month) &&
                !(month == Month.FEB && isLeapYear(year) && newDay == DAYS_IN_MONTH.get(month) + 1)) {
            newDay -= DAYS_IN_MONTH.get(month);
            newMonth = nextMonth(month);
            if (newMonth == Month.JAN) {
                newYear++;
            }
        }

        return new TimeBlock(newYear, newMonth, newDay, 0, 0);
    }

    /**
     * calculates how many hours have passed before the start of a given year from
     * unix epoch
     *
     * @param year
     * @return the number of hours
     */
    private static int hoursBeforeYear(int year) {
        int sum = 0;
        for (int i = 1970; i < year; i++) {
            sum += (DAYS_IN_YEAR + (isLeapYear(i) ? 1 : 0)) * HOURS_IN_DAY;
        }
        return sum;
    }

    /**
     * calculates how many hours are in each month based on the year from unix epoch
     * 
     * @return the number of hours in that month
     */
    private static int hoursBeforeMonth(Month month, int year) {
        if (DAYS_BEFORE_MONTH.get(month) >= 59 && isLeapYear(year)) {
            return (DAYS_BEFORE_MONTH.get(month) + 1) * HOURS_IN_DAY;
        } else
            return DAYS_BEFORE_MONTH.get(month) * HOURS_IN_DAY;
    }

    /**
     * calculates how many hours have passed since JAN 1, 1970 00:00 until the
     * start of this block
     *
     * @return a double, the number of hours
     */
    private double hoursBeforeStart() {
        return hoursBeforeYear(year) + hoursBeforeMonth(month, year) + (day - 1) * HOURS_IN_DAY + startHour;
    }

    /**
     * calculates how many hours have passed since JAN 1, 1970 00:00 until the
     * end of this block
     *
     * @return a double, the number of hours
     */
    private double hoursBeforeEnd() {
        return hoursBeforeYear(year) + hoursBeforeMonth(month, year) + (day - 1) * HOURS_IN_DAY + endHour;
    }

    public boolean isValid() {
        if (year < 0) {
            return false;
        }
        if (month == null) {
            return false;
        }
        if (day <= 0) {
            return false;
        }
        if (day > DAYS_IN_MONTH.get(month) && !(month == Month.FEB && day == DAYS_IN_MONTH.get(month) + 1)) {
            return false;
        }
        if (startHour < 0) {
            return false;
        }
        if (startHour > 24) {
            return false;
        }
        if (duration() < 0) {
            return false;
        }
        return !(endHour > 24);
    }

    /**
     * compares the start time of this event and another
     * 
     * @param other
     * @return a positive number if this event starts first, negative if the other
     *         event starts first
     */
    public double compareToStart(TimeBlock other) {
        return other.hoursBeforeStart() - hoursBeforeStart();
    }

    /**
     * compares the end time of this event and another
     *
     * @param other
     * @return a positive number if this event ends first, negative if the other
     *         event end first
     */
    public double compareToEnd(TimeBlock other) {
        return other.hoursBeforeEnd() - hoursBeforeEnd();
    }

    /**
     * calculates the amount of time between the end of this TimeBlock and the start
     * of another
     * 
     * @param other
     * @return a double, the number of hours
     */
    public double hoursUntil(TimeBlock other) {
        return other.hoursBeforeStart() - hoursBeforeEnd();
    }

    /**
     * calculates the amount of time between the start of this time block and unix
     * epoch
     * 
     * @return a double, the number of hours
     */
    public double hoursSinceEpoch() {
        return hoursBeforeStart();
    }

    /**
     * checks whether two time blocks conflict each other
     * 
     * @param other the other TimeBlock
     * @return whether the two time blocks are conflicting
     */
    public boolean isConflicting(TimeBlock other) {
        if (year == other.year && month == other.month && day == other.day) {
            return (startHour < other.endHour && other.startHour < endHour) || (startHour > other.endHour
                    && other.startHour > endHour);
        }

        return false;
    }

    /**
     * checks whether a timeblock is ongoing within another
     * 
     * @param other the other TimeBlock
     * @return whether the explicit is ongoing within the implicit
     */
    public boolean checkOngoing(TimeBlock other) {
        if (year == other.year && month == other.month && day == other.day) {
            return (startHour <= other.endHour && other.startHour <= endHour) || (startHour >= other.endHour
                    && other.startHour >= endHour);
        }

        return false;
    }

    /**
     * accessor for duration
     * 
     * @return a double, the duration
     */
    public double duration() {
        return endHour - startHour;
    }

    /**
     * converts time block to a readable string
     * 
     * @return the string
     */
    @Override
    public String toString() {
        int startMinute = (int) Math.round(startHour % 1 * 60);
        int endMinute = (int) Math.round(endHour % 1 * 60);

        String s = String.format("%s %d, %d", month, day, year);

        if (duration() < 24) {
            s += String.format(" %02d:%02d", (int) startHour, startMinute);
            if (duration() > 0) {
                s += String.format("-%02d:%02d", (int) endHour, endMinute);
            }
        }

        return s;
    }

    /**
     * returns a TimeBlock representing the start
     * 
     * @return a TimeBlock with duration 0h with the same start
     */
    public TimeBlock getStartBlock() {
        return new TimeBlock(year, month, day, startHour);
    }

    /**
     * returns a TimeBlock representing the end
     * 
     * @return a TimeBlock with duration 0h where the start hour is the end
     */
    public TimeBlock getEndBlock() {
        return new TimeBlock(year, month, day, endHour);
    }

    /**
     * accessor for startHour
     * 
     * @return the start hour
     */
    public double getStartHour() {
        return startHour;
    }

    /**
     * accessor for endHour
     * 
     * @return the end hour
     */
    public double getEndHour() {
        return endHour;
    }

    /**
     * accessor for day
     * 
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * accessor for month
     * 
     * @return the month
     */
    public Month getMonth() {
        return month;
    }

    /**
     * accessor for year
     * 
     * @return the year
     */
    public int getYear() {
        return year;
    }
}

/**
 * TimeManager keeps track of the current time and controls time progression
 * 
 * @author Sean Yang
 * @since June 06, 2025
 */

package time;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import time.TimeBlock.Month;

public class TimeManager {
    private TimeBlock time;

    // default time manager constructor
    public TimeManager() {
        this.time = new TimeBlock(2025, Month.JUN, 1, 12);
    }

    /**
     * constructor for a time manager
     * 
     * @param time the current time
     */
    public TimeManager(TimeBlock time) {
        this.time = time;
    }

    /**
     * constructor for a time manager
     * 
     * @param filepath the filepath to load from
     */
    public TimeManager(String filepath) {
        this.time = new TimeBlock(2025, Month.JUN, 1, 0);

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            Month month = Month.valueOf(br.readLine().trim().toUpperCase());
            int day = Integer.parseInt(br.readLine().trim());
            int year = Integer.parseInt(br.readLine().trim());
            double hour = Double.parseDouble(br.readLine().trim());

            TimeBlock time = new TimeBlock(year, month, day, hour);

            if (!time.isValid()) {
                time = new TimeBlock(2025, Month.JUN, 1, 12);
            }

            this.time = time;
            br.close();
        } catch (IOException iox) {
            System.out.println("Error reading time file: " + iox.getMessage());
        }
    }

    /**
     * saves time to file
     * 
     * @param filepath the filepath to save to
     */
    public void save(String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write(time.getMonth() + "\n");
            bw.write(time.getDay() + "\n");
            bw.write(time.getYear() + "\n");
            bw.write(time.getStartHour() + "\n");
            bw.close();
        } catch (IOException iox) {
            System.out.println("Error writing to time file: " + iox.getMessage());
        }
    }

    /**
     * advances the current time by specified hours
     * 
     * @param hours a double, the number of hours
     */
    public void advanceHours(double hours) {
        double newHour = time.getEndHour() + hours;

        while (newHour >= 24) {
            Month prev_month = time.getMonth();
            int prev_year = time.getYear();

            time = time.nextDay();
            newHour -= 24;

            if (prev_month != time.getMonth()) {
                System.out.println("NEW MONTH: " + time.getMonth());
                // bill monthly members
                main.CommunityCentreRunner.getMemberManager().billMonthlyMembers();
                // pay part-time staff
                main.CommunityCentreRunner.getStaffManager().payPartTimeStaff();
                System.out.println(); // blank line
            }
            if (prev_year != time.getYear()) {
                System.out.println("NEW YEAR: " + time.getYear());
                // age members
                main.CommunityCentreRunner.getMemberManager().ageMembers();
                // bill yearly members
                main.CommunityCentreRunner.getMemberManager().billAnnualMembers();
                // increase years worked for full-time staff
                main.CommunityCentreRunner.getStaffManager().increaseYearsWorked();
                // pay full-time staff
                main.CommunityCentreRunner.getStaffManager().payFullTimeStaff();
                System.out.println(); // blank line
            }
        }

        this.time = new TimeBlock(time, newHour, 0);

        main.CommunityCentreRunner.getEventManager().advanceTime(time);
    }

    /**
     * advances the time by one hour
     */
    public void advanceHour() {
        advanceHours(1);
    }

    /**
     * advances the time until the start of a specified time block
     * 
     * @param timeBlock
     */
    public void advanceToTimeBlock(TimeBlock timeBlock) {
        advanceHours(time.hoursUntil(timeBlock));
    }

    /**
     * checks if a time block is currently ongoing
     * 
     * @param timeBlock
     * @return whether the time block coincides with the current time
     */
    public boolean isOngoing(TimeBlock timeBlock) {
        if (time.checkOngoing(timeBlock)) {
            return true;
        }

        return false;
    }

    /**
     * returns the current time as a time block
     * 
     * @return
     */
    public TimeBlock getCurrentTime() {
        return time;
    }
}

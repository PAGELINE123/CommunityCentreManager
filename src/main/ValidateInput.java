/**
 * ValidateInput
 * various methods to validate user inputs
 *
 * @author Sean Yang
 * @version 1.1
 * @since 2025-06-12
 */

package main;

import java.util.Scanner;

import member.Member.PlanType;
import time.TimeBlock;
import time.TimeBlock.Month;
import main.CommunityCentreRunner;

public class ValidateInput {
    // scanner
    public static Scanner scan = CommunityCentreRunner.scan;

    // validates input for positive integers
    public static int posInt() {
        int choice = -1;

        while (choice < 0) {
            // input
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                choice = Integer.parseInt(userInput);
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter an integer greater than or equal to 0.");
            }
        }

        return choice;
    }

    // validates input for start hour and duration
    public static double[] startDuration() {
        double startHour = -1;
        double duration = -1;

        while (startHour < 0) {
            // input
            System.out.println("Enter start hour (e.g. 14.5)");
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                startHour = Double.parseDouble(userInput);
                if (startHour < 0 || startHour > 24) {
                    System.out.println("Must be between 0 and 24 (inclusive).");
                    startHour = -1;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a number.");
            }
        }

        while (duration < 0) {
            // input
            System.out.println("Enter duration (hours)");
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                duration = Double.parseDouble(userInput);
                if (duration < 0 || startHour + duration > 24) {
                    System.out.println("Must be non-negative and end must be before midnight.");
                    duration = -1;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a number.");
            }
        }

        return new double[] { startHour, duration };
    }

    // validates input for positive doubles
    public static double posDouble() {
        double choice = -1;

        while (choice < 0) {
            // input
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                choice = Double.parseDouble(userInput);
                if (choice < 0) {
                    System.out.println("Must be non-negative.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a number");
            }
        }

        return choice;
    }

    // validates input for plan types
    public static PlanType planType() {
        PlanType planType = null;

        while (planType == null) {
            // input
            System.out.println("Enter the plan type (MONTHLY/ANNUAL)");
            System.out.print(" > ");
            String userInput = scan.nextLine().trim().toUpperCase();
            // validate the input to a plan type
            try {
                planType = PlanType.valueOf(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Must enter a valid plan type");
            }
        }

        return planType;
    }

    // validates input for dates, also disallows dates in the past
    public static TimeBlock date() {
        int year = -1;
        Month month = null;
        int day = 0;

        while (month == null) {
            // input
            System.out.println("Enter the month (3-letter abbreviation, e.g. JAN)");
            System.out.print(" > ");
            String userInput = scan.nextLine().trim().toUpperCase();
            if (userInput.length() != 3) {
                System.out.println("Must enter 3 characters");
                continue;
            }
            // validate the input to a month
            try {
                month = TimeBlock.Month.valueOf(userInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Must enter a valid month");
            }
        }

        while (day < 1 || day > 31) {
            // input
            System.out.println("Enter the day (1-31)");
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                day = Integer.parseInt(userInput);
                if (day < 1 || day > 31) {
                    System.out.println("Must enter a valid day");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a valid day");
            }
        }

        while (year < 0) {
            // input
            System.out.println("Enter the year (YYYY)");
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                year = Integer.parseInt(userInput);
                if (year < 0) {
                    System.out.println("Must enter a valid year");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a valid year");
            }
        }

        TimeBlock date = new TimeBlock(year, month, day, 0);
        if (!date.isValid()) {
            System.out.println("Invalid date. Please try again.");
            System.out.println(); // blank line
            return date();
        } else if (date.compareToStart(CommunityCentreRunner.getTimeManager().getCurrentTime())>0) {
            System.out.println("Date cannot be in the past. Please try again.");
            System.out.println(); // blank line
            return date();
        }

        return date;
    }

    /**
     * validates input for menu selections
     * 
     * @param max the maximum possible selection
     * @return the selection
     */
    public static int menu(int max) {
        int choice = -1;

        while (choice < 0) {
            // input
            System.out.print(" > ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                choice = Integer.parseInt(userInput);
                if (choice > max) {
                    choice = -1;
                    System.out.println("Not an option.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a number.");
            }
        }

        return choice;
    }
}

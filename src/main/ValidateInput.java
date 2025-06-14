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

import member.Member;
import member.Member.PlanType;
import time.TimeBlock;
import time.TimeBlock.Month;

public class ValidateInput {
    // scanner
    public static Scanner scan = CommunityCentreRunner.scan;

    // validates input for adult ages
    public static int adultAge() {
        int age = Member.ADULT_AGE - 1;

        while (age < Member.ADULT_AGE) {
            // input
            System.out.print(" >  ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                age = Integer.parseInt(userInput);
                if (age < Member.ADULT_AGE) {
                    System.out.println("Adult age must be at least " + Member.ADULT_AGE + ".");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter an integer.");
            }
        }

        return age;
    }

    // validates input for child age
    public static int childAge() {
        int age = -1;

        while (age < 0 || age >= Member.ADULT_AGE) {
            // input
            System.out.print(" >  ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                age = Integer.parseInt(userInput);
                if (age < 0 || age >= Member.ADULT_AGE) {
                    System.out.println("Youth age must be at between 0 and " + Member.ADULT_AGE + ".");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter an integer.");
            }
        }

        return age;
    }

    // validates input for positive integers
    public static int posInt() {
        int choice = -1;

        while (choice < 0) {
            // input
            System.out.print(" >  ");
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
        System.out.println("Enter start hour (e.g. 14.5)");

        double startHour = -1;
        double duration = -1;

        while (startHour < 0) {
            // input
            System.out.print(" >  ");
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
            System.out.print(" >  ");
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

    // validates input for a time block
    public static TimeBlock timeBlock() {
        TimeBlock date = date();
        double[] sd = startDuration();

        return new TimeBlock(date, sd[0], sd[1]);
    }

    // validates input for positive doubles
    public static double posDouble() {
        double choice = -1;

        while (choice < 0) {
            // input
            System.out.print(" >  ");
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

    // validates input for a rating /10
    public static double rating() {
        System.out.println("Enter a rating /10");

        double choice = -1;

        while (choice < 0 || choice > 10) {
            // input
            System.out.print(" >  ");
            String userInput = scan.nextLine().trim();
            // validate the input to a choice
            try {
                choice = Double.parseDouble(userInput);
                if (choice < 0) {
                    System.out.println("Must be at least 0/10.");
                } else if (choice > 10) {
                    System.out.println("Must be at most 10/10.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Must enter a number.");
            }
        }

        return choice;
    }

    // validates input for plan types
    public static PlanType planType() {
        System.out.println("Enter the plan type (MONTHLY/ANNUAL)");

        PlanType planType = null;

        while (planType == null) {
            // input
            System.out.print(" >  ");
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
        System.out.println("Enter the month (3-letter abbreviation, e.g. JAN)");

        int year = -1;
        Month month = null;
        int day = 0;

        while (month == null) {
            // input
            System.out.print(" >  ");
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
            System.out.print(" >  ");
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
            System.out.print(" >  ");
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

        TimeBlock date = new TimeBlock(year, month, day);
        if (!date.isValid()) {
            System.out.println("Invalid date. Please try again.");
            System.out.println(); // blank line
            return date();
        } else if (date.compareToStart(CommunityCentreRunner.getTimeManager().getCurrentTime()) > 0) {
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
            System.out.print(" >  ");
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

/**
 * This class represents a scheduled fundraiser.
 * It contains all the functions that a fundraiser needs.
 *
 * @author Mansour Abdelsalam
 * @version 1.1
 * @since 2025-06-06
 */

package event;

import java.util.Scanner;

import facility.Facility;
import main.ValidateInput;
import member.AdultMember;
import member.Member;
import time.TimeBlock;

public class Fundraiser extends Event {
    // fields
    private double goal;
    private double amountRaised;

    /**
     * Constructor for Fundraiser;
     * creates a fundraiser given information.
     * Host can be null to represent no host.
     * 
     * @param facility
     * @param timeBlock
     * @param host
     * @param goal
     */
    public Fundraiser(Facility facility, TimeBlock timeBlock, Member host, double goal) {
        super(facility, timeBlock, host);

        this.goal = goal;
    }

    // accessors
    public double getGoal() {
        return this.goal;
    }

    // mutators
    //

    /**
     * setCompleted
     * will ask the user for the amount each participant raised,
     */
    @Override
    public void setCompleted() {
        Scanner scan = new Scanner(System.in);

        isCompleted = true;
        double amount = 0;

        System.out
                .println("For each adult participant, enter the amount they raised (This does not affect their bill)");
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i) instanceof AdultMember) {
                amount = ValidateInput.posDouble();

                amountRaised += amount;
            }
        }
        System.out.println(); // blank line

        System.out.printf("Goal: $.2f\n", goal);
        System.out.printf("Total Amount Raised: $.2f\n", amountRaised);

        if (amountRaised > goal) {
            System.out.println("Goal exceeded!");
        } else if (amountRaised < goal) {
            System.out.println("Goal not reached.");
        } else {
            System.out.println("Goal matched!");
        }

        scan.close();
    }

    /*
     * toString
     */
    public String toString() {
        String s = "Fundraiser " + super.toString() + " | Goal: " + String.format("$%.2f", goal);
        if (isCompleted) {
            s += " | Total raised: " + String.format("$%.2f", amountRaised);
        }

        return s;
    }
}

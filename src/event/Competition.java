/**
 * This class represents a scheduled competition.
 * It contains all the functions that a competition needs.
 *
 * @author Mansour Abdelsalam
 * @version 1.1
 * @since 2025-06-04
 */

package event;

import facility.Facility;
import main.ValidateInput;
import member.Member;
import staff.PartTimeStaff;
import staff.Staff;
import time.TimeBlock;

public class Competition extends Event {
    // fields
    private double prize;
    private double participationCost;
    private Member winner;

    /**
     * Constructor for Competition;
     * creates a competition given information.
     * Host can be null to represent no host.
     * 
     * @param facility
     * @param timeBlock
     * @param host
     * @param prize
     * @param participationCost
     */
    public Competition(Facility facility, TimeBlock timeBlock, Member host, double prize, double participationCost) {
        super(facility, timeBlock, host);

        this.prize = prize;
        this.participationCost = participationCost;
        this.winner = null;
    }

    /**
     * Constructor for Competition;
     * creates a competition given information.
     * Host can be null to represent no host.
     * 
     * @param facility
     * @param timeBlock
     * @param host
     * @param prize
     * @param participationCost
     * @param completed
     * @param winner
     */
    public Competition(Facility facility, TimeBlock timeBlock, Member host, double prize, double participationCost,
            Member winner) {
        super(facility, timeBlock, host);

        this.prize = prize;
        this.participationCost = participationCost;
        this.winner = winner;
    }

    // accessors
    public Member getWinner() {
        return this.winner;
    }

    public double getPrize() {
        return this.prize;
    }

    public double getParticipationCost() {
        return this.participationCost;
    }

    // mutators
    public void setParticipationCost(double participationCost) {
        this.participationCost = participationCost;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    public void setWinner(Member member) {
        this.winner = member;
    }

    /**
     * setCompleted
     * will ask the user for the winner, affecting their balance in calculateBill
     * accordingly.
     */
    @Override
    public void setCompleted() {
        completed = true;
        boolean valid_winner = false;
        int winner_id = 0;

        System.out.println("Participating members");
        System.out.println(participants);
        System.out.println(); // blank line

        while (!valid_winner) {
            System.out.println("Enter the winner's member ID");
            winner_id = ValidateInput.posInt();

            Member winnerInput = main.CommunityCentreRunner.getMemberManager().searchById(winner_id);

            if (main.CommunityCentreRunner.getMemberManager().searchById(winner_id) != null
                    && participants.contains(winnerInput)) {
                valid_winner = true;
            } else {
                System.out.println("Please enter a valid registered member id.");
            }
        }

        this.winner = main.CommunityCentreRunner.getMemberManager().searchById(winner_id);
        System.out.println("Member has been set as the winner:");
        System.out.println(winner);
        System.out.println(); // blank line

        boolean partTimeStaffFound = false;

        for (Staff staff : supervising) {
            if (staff instanceof PartTimeStaff pts) {
                double duration = timeBlock.duration();
                pts.setHoursWorked(pts.getHoursWorked() + duration);
                System.out.println("Added " + duration + " to " + staff.getName() + "'s payroll.");
                partTimeStaffFound = true;
            }
        }

        if (partTimeStaffFound) {
            System.out.println();
        }
    }

    /*
     * toString
     */
    public String toString() {
        String s = "Competition " + super.toString() +
                " | Prize: " + prize +
                " | Participation cost: " + String.format("$%.2f", participationCost);
        if (winner != null) {
            s += " | Winner name: " + winner.getName();
        }

        return s;
    }
}

/**
 * ModifyCompetitionMenu
 * contains the menu to modify a competition event
 *
 * @author Sean Yang
 * @since June 13, 2025
 */

package main.submenu.modify;

import java.util.Scanner;

import event.Competition;
import facility.Facility;
import facility.FacilityManager;
import facility.SportsFacility;
import main.CommunityCentreRunner;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.Member;
import member.MemberManager;
import staff.Staff;
import staff.StaffManager;
import time.TimeBlock;

public class ModifyCompetitionMenu {
    /**
     * Show the “modify competition” submenu for a given Competition.
     * 
     * @param event the Competition to modify
     * @return BACK to return to the parent menu, CONTINUE to stay
     */
    public static MenuStatus show(Competition event) {
        Scanner scan = CommunityCentreRunner.scan;
        MemberManager memberManager = CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = CommunityCentreRunner.getFacilityManager();

        System.out.println("What would you like to modify about this event?");
        System.out.println("(1) Modify Facility");
        System.out.println("(2) Modify Time Block");
        System.out.println("(3) Modify Host");
        System.out.println("(4) Register Participant");
        System.out.println("(5) Assign Staff");
        System.out.println("(6) Modify Prize");
        System.out.println("(7) Modify Participation Cost");
        System.out.println("(0) Back");

        int choice = ValidateInput.menu(7);
        CommunityCentreRunner.separate();

        switch (choice) {
            case 1 -> {
                System.out.println("Enter new sports facility room number");
                System.out.print(" > ");
                int roomNum = ValidateInput.posInt();
                Facility fac = facilityManager.searchByRoomNum(roomNum);
                if (!(fac instanceof SportsFacility)) {
                    System.out.println("Sports facility with room num. " + roomNum + " not found.");
                } else if (event.setFacility(fac)) {
                    System.out.println("Facility updated to room " + roomNum +
                            " for event #" + event.getId());
                } else {
                    System.out.println("Unable to update facility due to booking conflict.");
                }
            }
            case 2 -> {
                System.out.println("Enter new date, then start and duration:");
                TimeBlock datePart = ValidateInput.date();
                double[] sd = ValidateInput.startDuration();
                TimeBlock tb = new TimeBlock(
                        datePart.getYear(),
                        datePart.getMonth(),
                        datePart.getDay(),
                        sd[0], sd[1]);
                if (event.setTimeBlock(tb)) {
                    System.out.println("Time block successfully updated to " + tb + ".");
                } else {
                    System.out.println("Unable to update time block due to conflicts.");
                }
            }
            case 3 -> {
                System.out.println("Enter new host (member) ID or name");
                System.out.print(" > ");
                String in = scan.nextLine().trim();
                Member host;
                try {
                    host = memberManager.searchById(Integer.parseInt(in));
                } catch (NumberFormatException e) {
                    host = memberManager.searchByName(in);
                }
                if (host == null) {
                    System.out.println("Host not found.");
                } else if (event.setHost(host)) {
                    System.out.println("Host updated to " + host.getName() +
                            " for event #" + event.getId());
                } else {
                    System.out.println("Unable to update host due to scheduling conflict.");
                }
            }
            case 4 -> {
                System.out.println("Enter participant ID or name to register");
                System.out.print(" > ");
                String in = scan.nextLine().trim();
                Member m;
                try {
                    m = memberManager.searchById(Integer.parseInt(in));
                } catch (NumberFormatException e) {
                    m = memberManager.searchByName(in);
                }
                if (m == null) {
                    System.out.println("Member not found.");
                } else if (event.registerParticipant(m)) {
                    System.out.println("Member " + m.getName() +
                            " registered for event #" + event.getId());
                } else {
                    System.out.println("Unable to register " + m.getName() +
                            " (full, conflict, or already registered).");
                }
            }
            case 5 -> {
                System.out.println("Enter staff ID or name to assign");
                System.out.print(" > ");
                String in = scan.nextLine().trim();
                Staff s;
                try {
                    s = staffManager.searchById(Integer.parseInt(in));
                } catch (NumberFormatException e) {
                    s = staffManager.searchByName(in);
                }
                if (s == null) {
                    System.out.println("Staff not found.");
                } else if (event.assignStaff(s)) {
                    System.out.println("Staff " + s.getName() +
                            " assigned to event #" + event.getId());
                } else {
                    System.out.println("Unable to assign " + s.getName() +
                            " (conflict or already assigned).");
                }
            }
            case 6 -> {
                System.out.println("Enter new prize amount ($)");
                System.out.print(" > ");
                double prize = ValidateInput.posDouble();
                event.setPrize(prize);
                System.out.println("Prize updated to $" + prize);
            }
            case 7 -> {
                System.out.println("Enter new participation cost ($)");
                System.out.print(" > ");
                double cost = ValidateInput.posDouble();
                event.setParticipationCost(cost);
                System.out.println("Participation cost updated to $" + cost);
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
            default -> {
                System.out.println("Invalid choice.");
            }
        }

        return MenuStatus.CONTINUE;
    }
}

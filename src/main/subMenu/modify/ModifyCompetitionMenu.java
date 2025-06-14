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
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.Member;
import member.MemberManager;
import staff.Staff;
import staff.StaffManager;
import time.TimeBlock;

public class ModifyCompetitionMenu {
    // show the menu
    public static MenuStatus show(Competition event) {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();

        System.out.println("What would you like to modify about this event?");
        System.out.println("(1) Modify Facility");
        System.out.println("(2) Modify Time Block");
        System.out.println("(3) Modify Host");
        System.out.println("(4) Register Participant");
        System.out.println("(5) Assign Staff");
        System.out.println("(6) Modify Prize");
        System.out.println("(7) Modify Participation Cost");
        System.out.println("<0> Back");

        int competitionChoice = ValidateInput.menu(7);
        main.CommunityCentreRunner.separate();

        switch (competitionChoice) {
            case 1 -> {
                System.out.println("Enter new sports facility room num.");
                int facilityId = ValidateInput.posInt();
                Facility facility = facilityManager.searchByRoomNum(facilityId);
                if (facility == null || !(facility instanceof SportsFacility)) {
                    System.out.println("Sports facility with room num. " + facilityId + " not found.");
                } else if (event.setFacility(facility)) {
                    System.out.println("Facility successfully updated to room num. " + facility.getRoomNum() +
                            " for event #" + event.getId());
                } else {
                    System.out.println("Unable to update facility to room num. " + facility.getRoomNum() +
                            " for event #" + event.getId());
                }
            }
            case 2 -> {
                TimeBlock tb = ValidateInput.timeBlock();
                System.out.println(tb);
                if (event.setTimeBlock(tb)) {
                    System.out.println("Time block successfully updated.");
                } else {
                    System.out.println("Unable to update time block due to conflicts.");
                }
            }
            case 3 -> {
                System.out.println("Enter new member host ID or name");
                System.out.print(" >  ");
                String hostIdOrName = scan.nextLine();
                Member host = memberManager.searchByIdOrName(hostIdOrName);
                if (host == null) {
                    System.out.println("Host not found.");
                    break;
                } else if (event.setHost(host)) {
                    System.out.println("Host successfully updated to " + host.getName() +
                            " for event #" + event.getId());
                } else {
                    System.out.println("Unable to update host to " + host.getName() +
                            " for event #" + event.getId());
                }
            }
            case 4 -> {
                System.out.println("Enter new participant ID or name");
                System.out.print(" >  ");
                String memberNameOrId = scan.nextLine();
                Member member = memberManager.searchByIdOrName(memberNameOrId);
                if (member == null) {
                    System.out.println("Member not found.");
                } else if (event.registerParticipant(member)) {
                    System.out.println("Member " + member.getName() +
                            " signed up for event #" + event.getId() + ".");
                } else {
                    System.out.println("Unable to register " +
                            member.getName() + " for event #" + event.getId() + ".");
                }
            }
            case 5 -> {
                System.out.println("Enter new staff ID or name");
                System.out.print(" >  ");
                String staffIdOrName = scan.nextLine();
                Staff staff = staffManager.searchByIdOrName(staffIdOrName);
                if (staff == null) {
                    System.out.println("Staff not found.");
                    break;
                } else if (event.assignStaff(staff)) {
                    System.out.println("Staff " + staff.getName() +
                            " assigned to event #" + event.getId() + ".");
                } else {
                    System.out.println("Staff " + staff.getName() +
                            " could not be assigned to event #" + event.getId() + ".");
                }
            }
            case 6 -> {
                System.out.println("Enter new prize amount ($)");
                double prize = ValidateInput.posDouble();
                event.setPrize(prize);
                System.out.println("Prize successfully updated.");
            }
            case 7 -> {
                System.out.println("Enter new participation cost ($)");
                double participationCost = ValidateInput.posDouble();
                event.setParticipationCost(participationCost);
                System.out.println("Participation cost successfully updated.");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

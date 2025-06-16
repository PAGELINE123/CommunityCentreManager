/**
 * ModifyFundraiserMenu
 * contains the menu to modify a fundraiser event
 *
 * @author Sean Yang
 * @since June 13, 2025
 */

package main.submenu.modify;

import java.util.Scanner;

import event.Fundraiser;
import facility.Facility;
import facility.FacilityManager;
import facility.MeetingFacility;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.Member;
import member.MemberManager;
import staff.Staff;
import staff.StaffManager;
import time.TimeBlock;

public class ModifyFundraiserMenu {
    // show the menu
    public static MenuStatus show(Fundraiser event) {
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
        System.out.println("(6) Modify Goal");
        System.out.println("<0> Back");

        int fundraiserChoice = ValidateInput.menu(6);
        main.CommunityCentreRunner.separate();

        switch (fundraiserChoice) {
            case 1 -> {
                System.out.println("Enter new meeting facility room num.");
                int facilityId = ValidateInput.posInt();
                Facility facility = facilityManager.searchByRoomNum(facilityId);
                if (facility == null || !(facility instanceof MeetingFacility)) {
                    System.out.println("Meeting facility with room num. " + facilityId + " not found.");
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
                System.out.println("Enter new goal amount ($)");
                double prize = ValidateInput.posDouble();
                event.setGoal(prize);
                System.out.println("Goal successfully updated.");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

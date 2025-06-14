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
                System.out.println("Enter facility room num.");
                int facilityId = ValidateInput.posInt();
                Facility facility = facilityManager.searchByRoomNum(facilityId);
                if (facility == null || !(facility instanceof MeetingFacility)) {
                    System.out.println("Meeting facility with ID #" + facilityId + " not found.");
                } else if (!facility.book(event)) {
                    System.out.println("This facility has already been booked for this time slot.");
                } else {
                    event.getFacility().getBookings().cancelEvent(event);
                    System.out.println(facility);
                    System.out.println("Facility successfully modified.");
                }
            }
            case 2 -> {
                TimeBlock tb = ValidateInput.timeBlock();
                System.out.println(tb);
                event.setTimeBlock(tb);
                System.out.println("Time block successfully updated.");
            }
            case 3 -> {
                System.out.println("Enter member host ID or name");
                System.out.print(" >  ");
                String hostIdOrName = scan.nextLine();
                Member host = memberManager.searchByIdOrName(hostIdOrName);
                if (host == null) {
                    System.out.println("Host not found.");
                    break;
                }

                System.out.println(host);
                event.setHost(host);
                System.out.println("Host successfully updated.");
            }
            case 4 -> {
                System.out.println("Enter member ID or name");
                System.out.print(" >  ");
                String memberNameOrId = scan.nextLine();
                Member member = memberManager.searchByIdOrName(memberNameOrId);
                if (member == null) {
                    System.out.println("Member not found.");
                    break;
                }

                event.registerParticipant(member);
                System.out.println("Member " + member.getName() +
                        " signed up for event #" + event.getId() + ".");
            }
            case 5 -> {
                System.out.println("Enter staff ID or name");
                System.out.print(" >  ");
                String staffIdOrName = scan.nextLine();
                Staff staff = staffManager.searchByIdOrName(staffIdOrName);
                if (staff == null) {
                    System.out.println("Staff not found.");
                    break;
                }

                event.assignStaff(staff);
                System.out.println("Staff " + staff.getName() +
                        " assigned to event #" + event.getId() + ".");
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

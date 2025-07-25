/**
 * DeleteMenu
 * contains the menu to delete objects
 *
 * @author Yubo Zhao
 * @since June 12, 2025
 */

package main.submenu;

import java.util.Scanner;

import event.EventManager;
import facility.Facility;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.AdultMember;
import member.Member;
import member.MemberManager;
import member.YouthMember;
import staff.Staff;
import staff.StaffManager;

public class DeleteMenu {
    // show the menu
    public static MenuStatus show() {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        System.out.println("What would you like to delete?");
        System.out.println("(1) Delete Member");
        System.out.println("(2) Delete Staff");
        System.out.println("(3) Delete Facility");
        System.out.println("(4) Delete Event");
        System.out.println("<0> Back");

        int deleteChoice = ValidateInput.menu(4);
        main.CommunityCentreRunner.separate();

        switch (deleteChoice) {
            case 1 -> {
                System.out.println("Member ID or name to delete");
                System.out.print(" >  ");
                String memberIdOrName = scan.nextLine().trim().toUpperCase();
                Member member = memberManager.searchByIdOrName(memberIdOrName);

                boolean removed = member != null && memberManager.removeMember(member.getId());

                if (removed && member instanceof AdultMember) {
                    System.out.println(
                            "Adult member " + member.getName() + " and their children have been deleted.");
                } else if (removed && member instanceof YouthMember) {
                    System.out.println("Youth member " + member.getName() + " has been deleted.");
                } else {
                    System.out.println("Member not found.");
                }
            }
            case 2 -> {
                System.out.println("Staff ID or name to delete");
                System.out.print(" >  ");
                String staffIdOrName = scan.nextLine().trim().toUpperCase();
                Staff staff = staffManager.searchByIdOrName(staffIdOrName);

                if (staff != null && staffManager.removeStaff(staff.getId())) {
                    System.out.println("Staff " + staff.getName() + " has been deleted.");
                } else {
                    System.out.println("Staff not found.");
                }
            }
            case 3 -> {
                System.out.println("Enter the facility room num. to delete");
                int roomNum = ValidateInput.posInt();
                Facility facility = facilityManager.searchByRoomNum(roomNum);

                if (facility != null && facilityManager.removeFacility(facility.getId())) {
                    System.out.println("Facility with room num. " + roomNum + " has been deleted.");
                } else {
                    System.out.println("Facility with room num. " + roomNum + " not found.");
                }
            }
            case 4 -> {
                System.out.println("Enter the event ID to delete");
                int eventId = ValidateInput.posInt();

                boolean cancelled = eventManager.cancelEvent(eventId); // uses cancelEvent to clean up
                // registrations
                if (cancelled) {
                    System.out.println("Event with ID #" + eventId + " has been deleted.");
                } else {
                    System.out.println("Event with ID #" + eventId + " not found.");
                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

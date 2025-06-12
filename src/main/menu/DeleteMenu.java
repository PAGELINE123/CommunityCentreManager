/**
 * DeleteMenu
 * contains the menu to delete objects
 *
 * @author Yubo Zhao
 * @since June 12, 2025
 */

package main.menu;

import event.EventManager;
import facility.FacilityManager;
import main.CommunityCentreRunner;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.MemberManager;
import staff.StaffManager;

public class DeleteMenu {
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show() {
        System.out.println("What would you like to delete?");
        System.out.println("(1) Delete Member");
        System.out.println("(2) Delete Staff");
        System.out.println("(3) Delete Facility");
        System.out.println("(4) Delete Event");
        System.out.println("-");
        System.out.println("(0) Back");

        int deleteChoice = ValidateInput.menu(4);
        main.CommunityCentreRunner.separate();

        switch (deleteChoice) {
            case 1 -> {
                System.out.print("Enter the member ID to delete: ");
                int memberId = ValidateInput.posInt();
                boolean removedMember = CommunityCentreRunner
                        .getMemberManager()
                        .removeMember(memberId);
                if (removedMember) {
                    System.out.println("Member with ID " + memberId + " has been deleted.");
                } else {
                    System.out.println("Member with ID " + memberId + " not found.");
                }
            }
            case 2 -> {
                System.out.print("Enter the staff ID to delete: ");
                int staffId = ValidateInput.posInt();
                boolean removedStaff = staffManager.removeStaff(staffId);
                if (removedStaff) {
                    System.out.println("Staff with ID " + staffId + " has been deleted.");
                } else {
                    System.out.println("Staff with ID " + staffId + " not found.");
                }
            }
            case 3 -> {
                System.out.print("Enter the facility ID to delete: ");
                int facilityId = ValidateInput.posInt();
                boolean removedFacility = facilityManager.removeFacility(facilityId);
                if (removedFacility) {
                    System.out.println("Facility with ID " + facilityId + " has been deleted.");
                } else {
                    System.out.println("Facility with ID " + facilityId + " not found.");
                }
            }
            case 4 -> {
                System.out.print("Enter the event ID to delete: ");
                int eventId = ValidateInput.posInt();
                boolean cancelled = eventManager.cancelEvent(eventId); // uses cancelEvent to clean up
                // registrations
                if (cancelled) {
                    System.out.println("Event with ID " + eventId + " has been deleted.");
                } else {
                    System.out.println("Event with ID " + eventId + " not found.");
                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

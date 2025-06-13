/**
 * ListMenu
 * contains the menu to list objects in various ways
 *
 * @author Sean Yang
 * @since June 11, 2025
 */

package main.subMenu;

import event.EventManager;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.MemberManager;
import staff.StaffManager;

public class ListMenu {
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show() {
        System.out.println("What would you like to view?");
        // all options for listing facilities
        System.out.println("(1) Facilities by ID");
        System.out.println("(2) Sports Facilities by Rating");
        System.out.println("(3) Meeting Facilities by Size");
        System.out.println("(4) Facilities by Cost to Rent");
        System.out.println("-");

        // list events
        System.out.println("(5) Events by ID");
        System.out.println("(6) Events in Chronological Order");
        System.out.println("(7) Future Events");
        System.out.println("(8) Past Events");
        System.out.println("-");

        // list members
        System.out.println("(9) Members by ID");
        System.out.println("(10) Members by Alphabet");
        System.out.println("(11) Members by Bill");
        System.out.println("-");

        // list staff
        System.out.println("(12) Staff by ID");
        System.out.println("(13) Staff by Alphabet");
        System.out.println("(14) Full-Time Staff by Pay");
        System.out.println("-");

        // back
        System.out.println("<0> Back");

        int viewChoice = ValidateInput.menu(14);
        main.CommunityCentreRunner.separate();

        switch (viewChoice) {
            case 1 -> {
                if (!facilityManager.printAllFacilities()) {
                    System.out.println("No facilities found.");
                }
            }
            case 2 -> {
                if (!facilityManager.printSportsFacilitiesByRating()) {
                    System.out.println("No sports facilities found.");
                }
            }
            case 3 -> {
                if (!facilityManager.printMeetingFacilitiesBySize()) {
                    System.out.println("No meeting facilities found.");
                }
            }
            case 4 -> {
                if (!facilityManager.printFacilitiesByCost()) {
                    System.out.println("No facilities found.");
                }
            }
            case 5 -> {
                if (!eventManager.printAllEvents()) {
                    System.out.println("No events found.");
                }
            }
            case 6 -> {
                if (!eventManager.printEventsChronologically()) {
                    System.out.println("No events found.");
                }
            }
            case 7 -> {
                if (!eventManager.printFutureEvents()) {
                    System.out.println("No future events found.");
                }
            }
            case 8 -> {
                if (!eventManager.printPastEvents()) {
                    System.out.println("No past events found.");
                }
            }
            case 9 -> {
                if (!memberManager.printAllMembers()) {
                    System.out.println("No members found");
                }
            }
            case 10 -> {
                if (!memberManager.printAlphabetical()) {
                    System.out.println("No members found.");
                }
            }
            case 11 -> {
                if (!memberManager.printAllBills()) {
                    System.out.println("No member bills found.");
                }
            }
            case 12 -> {
                if (!staffManager.printAllStaff()) {
                    System.out.println("No members found.");
                }
            }
            case 13 -> {
                if (!staffManager.printAlphabetical()) {
                    System.out.println("No staff found");
                }
            }
            case 14 -> {
                if (!staffManager.printAllPayrolls()) {
                    System.out.println("No payrolls found.");

                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

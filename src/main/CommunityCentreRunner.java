/**
 * CommunityCentreRunner
 * runs and implements all features of TimeManager, EventManager, MemberManager, FacilityManager,
 * and StaffManager, in a GUI for user interaction.
 *
 * @author Mansour Abdelsalam, Sean Yang, Yubo Zhao
 * @version 1.1
 * @since 2025-06-11
 */

package main;

// import scanner
import java.util.Scanner;

import event.EventManager;
import facility.FacilityManager;
import main.subMenu.AdvanceMenu;
import main.subMenu.CreateMenu;
import main.subMenu.DeleteMenu;
import main.subMenu.EventMenu;
import main.subMenu.ListMenu;
import main.subMenu.SearchMenu;
import member.MemberManager;
import staff.StaffManager;
import time.TimeBlock;
import time.TimeManager;

public class CommunityCentreRunner {
    // file paths
    public static final String EVENTS_FILEPATH = "data/events.txt";
    public static final String FACILITIES_FILEPATH = "data/facilities.txt";
    public static final String MEMBERS_FILEPATH = "data/members.txt";
    public static final String STAFFS_FILEPATH = "data/staffs.txt";
    public static final String TIME_FILEPATH = "data/time.txt";

    // initialize managers
    private static MemberManager memberManager = new MemberManager();
    private static TimeManager timeManager = new TimeManager();
    private static EventManager eventManager = new EventManager();
    private static FacilityManager facilityManager = new FacilityManager();
    private static StaffManager staffManager = new StaffManager();

    // scanner
    public static Scanner scan = new Scanner(System.in);

    // separator for formatting
    public static final String SEPARATOR = "------------------------------------------/------------------------------------------";

    // enum containing menu statuses
    public enum MenuStatus {
        QUIT, BACK, CONTINUE
    }

    // accessor method for member manager
    public static MemberManager getMemberManager() {
        return memberManager;
    }

    // accessor method for event manager
    public static EventManager getEventManager() {
        return eventManager;
    }

    // accessor method for time manager
    public static TimeManager getTimeManager() {
        return timeManager;
    }

    // accessor method for staff manager
    public static StaffManager getStaffManager() {
        return staffManager;
    }

    // accessor method for facility manager
    public static FacilityManager getFacilityManager() {
        return facilityManager;
    }

    // outputs separator and whitespace
    public static void separate() {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println();
    }

    /**
     * contains the main menu
     * 
     * @return the status of this loop of the menu (CONTINUE, EXIT, BACK)
     */
    public static MenuStatus menuLoop() {
        TimeBlock currentTime = timeManager.getCurrentTime(); // update current time

        // GUI main loop ----
        System.out.println(SEPARATOR);
        System.out.println("It is currently " + currentTime + ".");
        System.out.println(SEPARATOR);
        System.out.println(); // blank line

        // show events occurring soon
        System.out.println("Events occurring within this month:");
        TimeBlock next_month = new TimeBlock(currentTime.getYear(), TimeBlock.nextMonth(currentTime.getMonth()),
                currentTime.getDay());
        if (!eventManager.printFutureEventsBefore(next_month)) {
            System.out.println("No events.");
        }
        System.out.println(); // blank line
        System.out.println("Events ongoing now:");
        if (!eventManager.printOngoingEvents()) {
            System.out.println("No events.");
        }
        System.out.println(); // blank line

        System.out.println(SEPARATOR);
        System.out.println(); // blank line
        System.out.println("What would you like to do?");
        System.out.println("(1) List");
        System.out.println("(2) Search");
        System.out.println("(3) Create");
        System.out.println("(4) Delete");
        System.out.println("(5) Manage Event");
        System.out.println("(6) Advance Time");
        System.out.println("<0> Quit");

        int choice = ValidateInput.menu(6);
        separate();

        // if valid input
        switch (choice) {
            case 1 -> {
                return ListMenu.show();
            }
            case 2 -> {
                return SearchMenu.show();
            }
            case 3 -> {
                return CreateMenu.show();
            }
            case 4 -> {
                return DeleteMenu.show();
            }
            case 5 -> {
                return EventMenu.show();
            }
            case 6 -> {
                return AdvanceMenu.show();
            }
            case 0 -> {
                return MenuStatus.QUIT;
            }
        }

        return MenuStatus.CONTINUE;

    }

    public static void main(String[] args) {
        // load data from files=
        memberManager = new MemberManager(MEMBERS_FILEPATH);
        facilityManager = new FacilityManager(FACILITIES_FILEPATH);
        staffManager = new StaffManager(STAFFS_FILEPATH);
        eventManager = new EventManager(EVENTS_FILEPATH);
        timeManager = new TimeManager(TIME_FILEPATH);

        boolean quit = false;

        while (!quit) {
            // run the GUI loop
            switch (menuLoop()) {
                case CONTINUE -> {
                    System.out.println(); // blank line
                    // ask if user wants to continue
                    System.out.print("Enter (Q) to quit or enter to continue: ");
                    String continueChoice = scan.nextLine().toUpperCase();
                    if (continueChoice.equals("Q")) {
                        quit = true; // exit the loop
                        System.out.println(); // blank line
                        System.out.println(SEPARATOR);
                    }
                    System.out.println(); // blank line
                }
                case QUIT -> quit = true;
                case BACK -> System.out.println("Returning to main menu.\n");
            }

        }

        eventManager.save(EVENTS_FILEPATH);
        memberManager.save(MEMBERS_FILEPATH);
        staffManager.save(STAFFS_FILEPATH);
        facilityManager.save(FACILITIES_FILEPATH);
        timeManager.save(TIME_FILEPATH);

        scan.close();
    }
}

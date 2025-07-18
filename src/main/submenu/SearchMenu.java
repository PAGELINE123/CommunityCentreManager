/**
 * SearchMenu
 * contains the menu to search objects
 *
 * @author Yubo Zhao, Sean Yang
 * @since June 12, 2025
 */

package main.submenu;

import java.util.Scanner;

import event.Event;
import event.EventManager;
import facility.Facility;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.Member;
import member.MemberManager;
import staff.Staff;
import staff.StaffManager;
import time.TimeBlock;

public class SearchMenu {
    // show the menu
    public static MenuStatus show() {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        System.out.println("What would you like to search for?");
        // all options for searching
        // facilities
        System.out.println("(1) Facility using ID");
        System.out.println("(2) Facility using Room Num.");
        System.out.println("(3) Facilities above Capacity");
        System.out.println("(4) Facilities Available within Time Range");
        System.out.println("(5) Facilities Available within Time Range above Capacity");
        System.out.println("-");
        // search events
        System.out.println("(6) Event using ID");
        System.out.println("(7) Events within Time Range");
        System.out.println("-");
        // search members
        System.out.println("(8) Member using ID or name");
        System.out.println("-");
        // search staff
        System.out.println("(9) Staff using ID or name");
        System.out.println("-");
        // back
        System.out.println("<0> Back");

        int searchChoice = ValidateInput.menu(9);
        main.CommunityCentreRunner.separate();

        switch (searchChoice) {
            case 1 -> {
                System.out.println("Facility ID");
                int fid = ValidateInput.posInt();
                Facility fac = facilityManager.searchById(fid);

                if (fac != null) {
                    System.out.println(fac);
                    String bookings = fac.toBookingsString();
                    if (bookings != null) {
                        System.out.println(bookings);
                    }
                } else {
                    System.out.println("Facility with ID #" + fid + " not found.");
                }
            }
            case 2 -> {
                System.out.println("Room number");
                int roomNum = ValidateInput.posInt();
                Facility found = facilityManager.searchByRoomNum(roomNum);

                if (found != null)
                    System.out.println(found);
                else
                    System.out.println("Facility with room num. " + roomNum + " not found.");
            }
            case 3 -> {
                System.out.println("Enter minimum capacity");
                int minCap = ValidateInput.posInt();
                if (!facilityManager.printFacilitesWithCapacity(minCap)) {
                    System.out.println("No matching facilities found.");
                }
            }
            case 4, 5 -> {
                TimeBlock tb = ValidateInput.timeBlock();

                if (searchChoice == 4) {
                    System.out.println(); // blank line
                    System.out.println("Facilities available:");
                    if (!facilityManager.printAvailableFacilities(tb)) {
                        System.out.println("No matching facilities found.");
                    }
                } else {
                    System.out.println("Enter minimum capacity");
                    int minCap = ValidateInput.posInt();

                    System.out.println(); // blank line
                    System.out.println("Facilities available:");
                    if (!facilityManager.printAvailableFacilities(tb, minCap)) {
                        System.out.println("No matching facilities found.");
                    }
                }
            }
            case 6 -> {
                System.out.println("Event ID");
                int eid = ValidateInput.posInt();
                Event ev = eventManager.searchById(eid);
                if (ev != null) {
                    System.out.println(ev);
                    String details = ev.toSupervisingAndParticipatingString();
                    if (!details.isBlank()) {
                        System.out.println(details);
                    }
                } else
                    System.out.println("Event with ID #" + eid + " not found.");
            }
            case 7 -> {
                // build a TimeBlock
                TimeBlock tb = ValidateInput.timeBlock();
                eventManager.printEventsWithin(tb);
            }
            case 8 -> {
                System.out.println("Member ID or name");
                System.out.print(" >  ");
                String memberIdOrName = scan.nextLine().trim().toUpperCase();
                Member member = memberManager.searchByIdOrName(memberIdOrName);

                if (member != null) {
                    System.out.println(member);
                    System.out.println(member.personalInfo());
                    String registrations = member.toRegistrationString();
                    if (!registrations.isEmpty()) {
                        System.out.println(registrations);
                    }
                } else
                    System.out.println("Member not found.");
            }
            case 9 -> {
                System.out.println("Staff ID or name");
                System.out.print(" >  ");
                String staffIdOrName = scan.nextLine().trim().toUpperCase();
                Staff staff = staffManager.searchByIdOrName(staffIdOrName);

                if (staff != null) {
                    System.out.println(staff);
                    String shifts = staff.toShiftString();
                    if (!shifts.isBlank()) {
                        System.out.println(shifts);
                    }
                } else {
                    System.out.println("Staff not found.");
                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

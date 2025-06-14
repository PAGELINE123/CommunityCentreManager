/**
 * ModifyMenu
 * contains the menu to modify objects
 *
 * @author Sean Yang
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
import main.submenu.modify.ModifyAdultMenu;
import main.submenu.modify.ModifyEventMenu;
import main.submenu.modify.ModifyFacilityMenu;
import main.submenu.modify.ModifyStaffMenu;
import main.submenu.modify.ModifyYouthMenu;
import member.AdultMember;
import member.Member;
import member.MemberManager;
import member.YouthMember;
import staff.Staff;
import staff.StaffManager;

public class ModifyMenu {
    public static Scanner scan = main.CommunityCentreRunner.scan;
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show() {
        System.out.println("What would you like to modify?");
        System.out.println("(1) Modify Member");
        System.out.println("(2) Modify Staff");
        System.out.println("(3) Modify Facility");
        System.out.println("(4) Modify Event");
        System.out.println("<0> Back");

        int modifyChoice = ValidateInput.menu(4);
        main.CommunityCentreRunner.separate();

        switch (modifyChoice) {
            case 1 -> {
                System.out.println("Member ID or name to modify");
                System.out.print(" > ");
                String memberIdOrName = scan.nextLine().trim().toUpperCase();
                Member member = memberManager.searchByIdOrName(memberIdOrName);
                if (member != null) {
                    System.out.println(member);

                    main.CommunityCentreRunner.separate();

                    if (member instanceof YouthMember youth) {
                        MenuStatus status = ModifyYouthMenu.show(youth);
                        switch (status) {
                            case BACK -> {
                                return show();
                            }
                            default -> {
                                return status;
                            }
                        }
                    } else if (member instanceof AdultMember adult) {
                        MenuStatus status = ModifyAdultMenu.show(adult);
                        switch (status) {
                            case BACK -> {
                                return show();
                            }
                            default -> {
                                return status;
                            }
                        }
                    }
                } else {
                    System.out.println("Member not found.");
                }
            }
            case 2 -> {
                System.out.println("Staff ID or name to modify");
                System.out.print(" > ");
                String staffIdOrName = scan.nextLine().trim().toUpperCase();
                Staff staff = staffManager.searchByIdOrName(staffIdOrName);

                if (staff != null) {
                    System.out.println(staff);
                    main.CommunityCentreRunner.separate();

                    MenuStatus status = ModifyStaffMenu.show(staff);
                    switch (status) {
                        case BACK -> {
                            return show();
                        }
                        default -> {
                            return status;
                        }
                    }
                } else {
                    System.out.println("Staff not found.");
                }
            }
            case 3 -> {
                System.out.println("Enter facility ID");
                int fid = ValidateInput.posInt();

                Facility facility = facilityManager.searchById(fid);
                if (facility != null) {
                    System.out.println(facility);
                    main.CommunityCentreRunner.separate();

                    MenuStatus status = ModifyFacilityMenu.show(facility);
                    switch (status) {
                        case BACK -> {
                            return show();
                        }
                        default -> {
                            return status;
                        }
                    }
                } else
                    System.out.println("Facility with ID " + fid + " not found.");
            }
            case 4 -> {
                System.out.println("Enter event ID");
                int eid = ValidateInput.posInt();
                Event event = eventManager.searchById(eid);
                if (event != null) {
                    System.out.println(event);
                    main.CommunityCentreRunner.separate();

                    MenuStatus status = ModifyEventMenu.show(event);
                    switch (status) {
                        case BACK -> {
                            return show();
                        }
                        default -> {
                            return status;
                        }
                    }
                } else {
                    System.out.println("Event with ID #" + eid + " not found.");
                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

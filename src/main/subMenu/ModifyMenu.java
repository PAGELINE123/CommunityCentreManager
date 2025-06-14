/**
 * ModifyMenu
 * contains the menu to modify objects
 *
 * @author Sean Yang
 * @since June 12, 2025
 */

package main.subMenu;

import java.util.Scanner;

import event.Competition;
import event.Event;
import event.EventManager;
import event.Fundraiser;
import facility.Facility;
import facility.FacilityManager;
import facility.MeetingFacility;
import facility.SportsFacility;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import main.subMenu.modify.ModifyAdultMenu;
import main.subMenu.modify.ModifyCompetitionMenu;
import main.subMenu.modify.ModifyFullTimeStaffMenu;
import main.subMenu.modify.ModifyFundraiserMenu;
import main.subMenu.modify.ModifyMeetingFacilityMenu;
import main.subMenu.modify.ModifyPartTimeStaffMenu;
import main.subMenu.modify.ModifySportsFacilityMenu;
import main.subMenu.modify.ModifyYouthMenu;
import member.AdultMember;
import member.Member;
import member.MemberManager;
import member.YouthMember;
import staff.FullTimeStaff;
import staff.PartTimeStaff;
import staff.Staff;
import staff.StaffManager;

public class ModifyMenu {
    // show the menu
    public static MenuStatus show() {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

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
                System.out.print(" >  ");
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
                System.out.print(" >  ");
                String staffIdOrName = scan.nextLine().trim().toUpperCase();
                Staff staff = staffManager.searchByIdOrName(staffIdOrName);

                if (staff != null) {
                    System.out.println(staff);

                    main.CommunityCentreRunner.separate();

                    if (staff instanceof PartTimeStaff pts) {
                        MenuStatus status = ModifyPartTimeStaffMenu.show(pts);
                        switch (status) {
                            case BACK -> {
                                return show();
                            }
                            default -> {
                                return status;
                            }
                        }
                    } else if (staff instanceof FullTimeStaff fts) {
                        MenuStatus status = ModifyFullTimeStaffMenu.show(fts);
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
                    System.out.println("Staff not found.");
                }
            }
            case 3 -> {
                System.out.println("Enter facility room num.");
                int roomNum = ValidateInput.posInt();

                Facility facility = facilityManager.searchByRoomNum(roomNum);
                if (facility != null) {
                    System.out.println(facility);

                    main.CommunityCentreRunner.separate();

                    if (facility instanceof SportsFacility sf) {
                        MenuStatus status = ModifySportsFacilityMenu.show(sf);
                        switch (status) {
                            case BACK -> {
                                return show();
                            }
                            default -> {
                                return status;
                            }
                        }
                    } else if (facility instanceof MeetingFacility mf) {
                        MenuStatus status = ModifyMeetingFacilityMenu.show(mf);
                        switch (status) {
                            case BACK -> {
                                return show();
                            }
                            default -> {
                                return status;
                            }
                        }
                    }
                } else
                    System.out.println("Facility with room num. " + roomNum + " not found.");
            }
            case 4 -> {
                System.out.println("Enter event ID");
                int eid = ValidateInput.posInt();
                Event event = eventManager.searchById(eid);
                if (event != null) {
                    System.out.println(event);

                    main.CommunityCentreRunner.separate();

                    if (event instanceof Competition c) {
                        MenuStatus status = ModifyCompetitionMenu.show(c);
                        switch (status) {
                            case BACK -> {
                                return show();
                            }
                            default -> {
                                return status;
                            }
                        }
                    } else if (event instanceof Fundraiser f) {
                        MenuStatus status = ModifyFundraiserMenu.show(f);
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

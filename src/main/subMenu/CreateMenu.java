/**
 * CreateMenu
 * menu to create objects
 *
 * @author Yubo Zhao, Sean Yang
 * @since June 12, 2025
 */

package main.submenu;

import java.util.Scanner;

import event.Competition;
import event.Event;
import event.Fundraiser;
import facility.Facility;
import facility.FacilityManager;
import facility.MeetingFacility;
import facility.SportsFacility;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.AdultMember;
import member.Member;
import member.Member.PlanType;
import member.MemberManager;
import member.YouthMember;
import staff.FullTimeStaff;
import staff.PartTimeStaff;
import staff.Staff;
import time.TimeBlock;

public class CreateMenu {
    // show the menu
    public static MenuStatus show() {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();

        System.out.println("What would you like to create?");
        System.out.println("(1) Create Member");
        System.out.println("(2) Create Staff");
        System.out.println("(3) Create Facility");
        System.out.println("(4) Create Event");
        System.out.println("<0> Back");

        // now allow choice up to 4
        int createChoice = ValidateInput.menu(4);
        main.CommunityCentreRunner.separate();

        switch (createChoice) {
            case 1 -> {
                System.out.println("Age");
                int age = ValidateInput.posInt();
                System.out.println("Full name");
                System.out.print(" >  ");
                String name = scan.nextLine().trim().toUpperCase();
                PlanType planType = ValidateInput.planType();

                Member newMember;
                if (age >= Member.ADULT_AGE) {
                    System.out.println("Contact phone ###-###-####");
                    System.out.print(" >  ");
                    String contactPhone = scan.nextLine().trim();
                    System.out.println("Address");
                    System.out.print(" >  ");
                    String address = scan.nextLine().trim();

                    newMember = new AdultMember(age, name, planType, contactPhone, address);

                    main.CommunityCentreRunner.getMemberManager().addMember(newMember);
                    System.out.println(newMember);
                    System.out.println("Adult member created successfully.");
                } else {
                    System.out.println("Guardian ID or name");
                    System.out.print(" >  ");
                    String guardianIdOrName = scan.nextLine().trim().toUpperCase();
                    Member guardian;
                    try {
                        int id = Integer.parseInt(guardianIdOrName);
                        guardian = memberManager.searchById(id);
                    } catch (NumberFormatException nfe) {
                        guardian = memberManager.searchByName(guardianIdOrName);
                    }

                    if (guardian instanceof AdultMember adult) {
                        newMember = new YouthMember(age, name, planType, adult);

                        main.CommunityCentreRunner.getMemberManager().addMember(newMember);
                        System.out.println(newMember);
                        System.out.println("Youth member created successfully.");
                    } else {
                        System.out.println("No matching adult.");
                    }
                }
            }
            case 2 -> {
                // --- CREATE STAFF ---
                System.out.println("Full-time or Part-time?   (0) Full-time   (1) Part-time");
                int staffType = ValidateInput.menu(1);

                System.out.println("Full name");
                System.out.print(" >  ");
                String staffName = scan.nextLine().trim().toUpperCase();

                Staff newStaff;
                if (staffType == 0) {
                    System.out.println("Years worked");
                    int years = ValidateInput.posInt();
                    newStaff = new FullTimeStaff(staffName, years);
                } else {
                    System.out.println("Hours worked");
                    double hours = ValidateInput.posDouble();
                    System.out.println("Hourly rate ($)");
                    double rate = ValidateInput.posDouble();
                    System.out.println("Max weekly hours");
                    int maxH = ValidateInput.posInt();
                    newStaff = new PartTimeStaff(staffName, hours, rate, maxH);
                }
                main.CommunityCentreRunner.getStaffManager().addStaff(newStaff);
                System.out.println(newStaff);

                System.out.println("Staff created successfully.");
            }
            case 3 -> {
                // --- CREATE FACILITY ---
                System.out.println("Meeting or Sports?   (0) Meeting Room   (1) Sports Facility");
                int type = ValidateInput.menu(1);

                System.out.println("Enter room number");
                int room = ValidateInput.posInt();
                System.out.println("Enter max capacity");
                int cap = ValidateInput.posInt();

                Facility newFacility;
                if (type == 0) {
                    System.out.println("Enter room size");
                    double size = ValidateInput.posDouble();
                    newFacility = new MeetingFacility(room, cap, size);
                } else {
                    double rating = -1;
                    while (rating > 10 || rating < 0) {
                        System.out.println("Enter facility rating");
                        rating = ValidateInput.posDouble();

                        if (rating > 10 || rating < 0) {
                            System.out.println("Rating must be between 0 and 10.");
                        }
                    }

                    newFacility = new SportsFacility(room, cap, rating);
                }

                main.CommunityCentreRunner.getFacilityManager().addFacility(newFacility);
                System.out.println(newFacility);
                System.out.println("Facility created successfully.");
            }
            case 4 -> {
                System.out.println("Event type   (0) Competition   (1) Fundraiser");
                int eventType = ValidateInput.menu(1);

                double prize = 0;
                double goal = 0;
                double participationCost = 0;
                if (eventType == 0) {
                    System.out.println("Enter prize amount ($)");
                    prize = ValidateInput.posDouble();
                    System.out.println("Enter participation cost ($)");
                    participationCost = ValidateInput.posDouble();
                } else {
                    System.out.println("Enter goal amount ($)");
                    goal = ValidateInput.posDouble();
                }

                TimeBlock d = ValidateInput.date();

                System.out.println("Enter duration type   (0) Set duration   (1) All-day");
                int durationChoice = ValidateInput.menu(1);

                TimeBlock tb = new TimeBlock();
                switch (durationChoice) {
                    case 1 -> {
                        tb = d;
                    }
                    case 0 -> {
                        double[] sd = ValidateInput.startDuration();
                        double startHour = sd[0];
                        double duration = sd[1];
                        tb = new TimeBlock(d, startHour, duration);
                    }
                }

                Facility fac = null;
                boolean validFacility = false;
                while (!validFacility) {
                    System.out.println("Enter facility room num.");
                    int facId = ValidateInput.posInt();
                    fac = facilityManager.searchByRoomNum(facId);

                    if (fac == null) {
                        System.out.println("Facility with ID " + facId + " not found.");
                    } else {
                        switch (eventType) {
                            case 0 -> { // Competition
                                if (fac instanceof SportsFacility) {
                                    if (fac.getBookings().isBlockFree(tb)) {
                                        validFacility = true;
                                    } else {
                                        System.out.println("This facility has already been booked in the time block.");
                                    }
                                } else {
                                    System.out.println("Invalid facility for the event.");
                                }
                            }
                            case 1 -> { // Fundraiser
                                if (fac instanceof MeetingFacility) {
                                    if (fac.getBookings().isBlockFree(tb)) {
                                        validFacility = true;
                                    } else {
                                        System.out.println("This facility has already been booked in the time block.");
                                    }
                                } else {
                                    System.out.println("Invalid facility for the event.");
                                }
                            }
                        }
                    }
                }

                System.out.println("Enter hosting type   (0) Member ID   (1) None");
                int hostingChoice = ValidateInput.menu(2);
                Member host = null;
                switch (hostingChoice) {
                    case 0 -> {
                        while (host == null) {
                            System.out.println("Enter host (member) ID");
                            int hid = ValidateInput.posInt();
                            host = memberManager.searchById(hid);
                            if (host == null)
                                System.out.println("Member with ID " + host + " not found.");
                        }
                    }
                    case 1 -> {
                        System.out.println("No host.");
                    }
                }

                Event newEvent = (eventType == 0)
                        ? new Competition(fac, tb, host, prize, participationCost)
                        : new Fundraiser(fac, tb, host, goal);

                main.CommunityCentreRunner.getEventManager().book(newEvent);
                System.out.println(newEvent);
                System.out.println("Event created successfully.");
            }

            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

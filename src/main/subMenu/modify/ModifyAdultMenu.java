/**
 * AdultMenu
 * contains the menu to modify a adult member object
 *
 * @author Sean Yang
 * @since June 12, 2025
 */

package main.submenu.modify;

import java.util.Scanner;

import event.EventManager;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.AdultMember;
import member.Member;
import member.Member.PlanType;
import member.MemberManager;
import member.YouthMember;
import staff.StaffManager;

public class ModifyAdultMenu {
    public static Scanner scan = main.CommunityCentreRunner.scan;
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show(AdultMember adult) {
        System.out.println("What would you like to modify about this member?");
        System.out.println("(1) Modify Age");
        System.out.println("(2) Modify Name");
        System.out.println("(3) Modify Plan Type");
        System.out.println("(4) Modify Contact Phone");
        System.out.println("(5) Modify Address");
        System.out.println("(6) Add Child");
        System.out.println("(7) Remove Child");
        System.out.println("<0> Back");

        int adultChoice = ValidateInput.menu(7);
        main.CommunityCentreRunner.separate();

        switch (adultChoice) {
            case 1 -> {
                System.out.println("Enter new age");
                int age = ValidateInput.adultAge();
                adult.setAge(age);
                System.out.println("Age successfully update.");
            }
            case 2 -> {
                System.out.println("Enter new name");
                System.out.print(" > ");
                String name = scan.nextLine().trim().toUpperCase();
                adult.setName(name);
                System.out.println("Name successfully update.");

            }
            case 3 -> {
                System.out.println("Enter new plan type");
                PlanType planType = ValidateInput.planType();
                adult.setPlanType(planType);
                System.out.println("Plan type successfully update.");

            }
            case 4 -> {
                System.out.println("Enter new contact phone");
                System.out.print(" > ");
                String contactPhone = scan.nextLine().trim();
                adult.setContactPhone(contactPhone);
                System.out.println("Contact phone successfully update.");

            }
            case 5 -> {
                System.out.println("Enter new address");
                System.out.print(" > ");
                String address = scan.nextLine().trim();
                adult.setAddress(address);
                System.out.println("Address successfully update.");
            }
            case 6 -> {
                System.out.println("Enter child name or ID to add");
                System.out.print(" > ");
                String memberIdOrName = scan.nextLine().trim();
                Member member = memberManager.searchByIdOrName(memberIdOrName);

                if (member instanceof YouthMember youth) {
                    if (adult.addChild(youth)) {
                        System.out.println("Child with ID #" + youth.getId() + " successfully added.");
                    } else {
                        System.out.println("Youth member is already a child of this adult.");
                    }
                } else {
                    System.out.println("Youth member not found.");
                }
            }
            case 7 -> {
                System.out.println("Enter child name or ID to remove");
                System.out.print(" > ");
                String memberIdOrName = scan.nextLine().trim();
                Member member = memberManager.searchByIdOrName(memberIdOrName);

                if (member instanceof YouthMember youth) {
                    if (adult.removeChild(youth)) {
                        System.out.println("Child with ID #" + youth.getId() + " successfully removed.");
                    } else {
                        System.out.println("Youth member is not a child of this adult.");
                    }
                } else {
                    System.out.println("Youth member not found.");
                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

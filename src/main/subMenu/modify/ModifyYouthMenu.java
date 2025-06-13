/**
 * YouthMenu
 * contains the menu to modify a youth member object
 *
 * @author Sean Yang
 * @since June 12, 2025
 */

package main.subMenu.modify;

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

public class ModifyYouthMenu {
    public static Scanner scan = main.CommunityCentreRunner.scan;
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show(YouthMember youth) {
        System.out.println("What would you like to modify about this member?");
        System.out.println("(1) Modify Age");
        System.out.println("(2) Modify Name");
        System.out.println("(3) Modify Plan Type");
        System.out.println("(4) Modify Guardian");
        System.out.println("<0> Back");

        int youthChoice = ValidateInput.menu(7);
        main.CommunityCentreRunner.separate();

        switch (youthChoice) {
            case 1 -> {
                System.out.println("Enter new age");
                int age = ValidateInput.adultAge();
                youth.setAge(age);
                System.out.println("Age successfully update.");
            }
            case 2 -> {
                System.out.println("Enter new name");
                System.out.print(" > ");
                String name = scan.nextLine().trim().toUpperCase();
                youth.setName(name);
                System.out.println("Name successfully update.");

            }
            case 3 -> {
                System.out.println("Enter new plan type");
                PlanType planType = ValidateInput.planType();
                youth.setPlanType(planType);
                System.out.println("Plan type successfully update.");

            }
            case 7 -> {
                System.out.println("Enter new guardian name or ID");
                System.out.print(" > ");
                String memberIdOrName = scan.nextLine().trim();
                Member member = memberManager.searchByIdOrName(memberIdOrName);

                if (member instanceof AdultMember guardian) {
                    youth.setGuardian(guardian);
                    System.out.println("Guardian successfully set.");
                } else {
                    System.out.println("Adult member not found.");
                }
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

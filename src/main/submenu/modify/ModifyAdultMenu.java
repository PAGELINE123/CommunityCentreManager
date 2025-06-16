/**
 * AdultMenu
 * contains the menu to modify a adult member object
 *
 * @author Sean Yang
 * @since June 12, 2025
 */

package main.submenu.modify;

import java.util.Scanner;

import main.CommunityCentreRunner;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.AdultMember;
import member.Member;
import member.MemberManager;
import member.YouthMember;

/**
 * AdultMenu
 * contains the menu to modify an adult member object,
 * including reassigning a youth to this guardian.
 */
public class ModifyAdultMenu {
    public static MenuStatus show(AdultMember adult) {
        Scanner scan = CommunityCentreRunner.scan;
        MemberManager memberManager = CommunityCentreRunner.getMemberManager();

        System.out.println("What would you like to modify about this member?");
        System.out.println("(1) Modify Age");
        System.out.println("(2) Modify Name");
        System.out.println("(3) Modify Plan Type");
        System.out.println("(4) Modify Contact Phone");
        System.out.println("(5) Modify Address");
        System.out.println("(6) Reassign a Youth to This Guardian");
        System.out.println("(0) Back");

        int choice = ValidateInput.menu(6);
        CommunityCentreRunner.separate();

        switch (choice) {
            case 1: {
                System.out.println("Enter new age");
                System.out.print(" > ");
                int age = ValidateInput.posInt();
                adult.setAge(age);
                System.out.println("Age successfully updated.");
                break;
            }
            case 2: {
                System.out.println("Enter new name");
                System.out.print(" > ");
                String name = scan.nextLine().trim().toUpperCase();
                adult.setName(name);
                System.out.println("Name successfully updated.");
                break;
            }
            case 3: {
                System.out.println("Enter new plan type");
                System.out.print(" > ");
                var planType = ValidateInput.planType();
                adult.setPlanType(planType);
                System.out.println("Plan type successfully updated.");
                break;
            }
            case 4: {
                boolean valid = false;
                do {
                    System.out.println("Enter new contact phone");
                    System.out.print(" > ");
                    String contactPhone = scan.nextLine().trim();
                    try {
                        adult.setContactPhone(contactPhone);
                        valid = true;
                        System.out.println("Contact phone successfully updated.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } while (!valid);
                break;
            }
            case 5: {
                System.out.println("Enter new address");
                System.out.print(" > ");
                String address = scan.nextLine().trim();
                adult.setAddress(address);
                System.out.println("Address successfully updated.");
                break;
            }
            case 6: {
                System.out.println("Enter youth ID or name to reassign");
                System.out.print(" > ");
                String input = scan.nextLine().trim();
                Member m;
                try {
                    m = memberManager.searchById(Integer.parseInt(input));
                } catch (NumberFormatException e) {
                    m = memberManager.searchByName(input);
                }

                if (m instanceof YouthMember youth) {
                    AdultMember oldGuardian = youth.getGuardian();
                    if (oldGuardian != null) {
                        // unlink from old guardian without removing from system
                        oldGuardian.getChildren().remove(youth);
                    }
                    // assign to current adult
                    youth.setGuardian(adult);
                    adult.getChildren().add(youth);

                    System.out.println("Youth #" + youth.getId()
                            + " is now assigned to " + adult.getName() + ".");
                } else {
                    System.out.println("Youth member not found.");
                }
                break;
            }
            case 0:
                return MenuStatus.BACK;
            default:
                System.out.println("Invalid choice.");
        }

        return MenuStatus.CONTINUE;
    }
}

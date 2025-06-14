/**
 * ModifyFullTimeStaffMenu
 * contains the menu to modify a full time staff member object
 *
 * @author Sean Yang
 * @since June 13, 2025
 */

package main.submenu.modify;

import java.util.Scanner;

import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import staff.FullTimeStaff;

public class ModifyFullTimeStaffMenu {
    // show the menu
    public static MenuStatus show(FullTimeStaff staff) {
        Scanner scan = main.CommunityCentreRunner.scan;

        System.out.println("What would you like to modify about this employee?");
        System.out.println("(1) Modify Name");
        System.out.println("(2) Modify Years Worked");
        System.out.println("<0> Back");

        int fullTimeChoice = ValidateInput.menu(2);

        switch (fullTimeChoice) {
            case 1 -> {
                System.out.println("Enter new name");
                System.out.print(" >  ");
                String name = scan.nextLine().trim().toUpperCase();
                staff.setName(name);
                System.out.println("Name successfully updated.");
            }
            case 2 -> {
                System.out.println("Enter new number of years worked");
                int yearsWorked = ValidateInput.posInt();
                staff.setYearsWorked(yearsWorked);
                System.out.println("Years worked successfully updated.");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

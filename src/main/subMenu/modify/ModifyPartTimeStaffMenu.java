/**
 * ModifyPartTimeStaffMenu
 * contains the menu to modify a part time staff member object
 *
 * @author Sean Yang
 * @since June 13, 2025
 */

package main.subMenu.modify;

import java.util.Scanner;

import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import staff.PartTimeStaff;

public class ModifyPartTimeStaffMenu {
    // show the menu
    public static MenuStatus show(PartTimeStaff staff) {
        Scanner scan = main.CommunityCentreRunner.scan;

        System.out.println("What would you like to modify about this employee?");
        System.out.println("(1) Modify Name");
        System.out.println("(2) Modify Hours Worked this Month");
        System.out.println("(3) Modify Hourly Wage");
        System.out.println("(4) Modify Maximum Monthly Hours");
        System.out.println("(0) Back");

        // allow choices 0 through 4
        int choice = ValidateInput.menu(4);

        switch (choice) {
            case 1 -> {
                System.out.println("Enter new name");
                String name = scan.nextLine().trim().toUpperCase();
                staff.setName(name);
                System.out.println("Name successfully updated.");
            }
            case 2 -> {
                System.out.println("Enter new number of hours worked this month");
                double hoursWorked = ValidateInput.posDouble();
                staff.setHoursWorked((int)hoursWorked);
                System.out.println("Hours worked successfully updated.");
            }
            case 3 -> {
                System.out.println("Enter new hourly wage ($)");
                double hourlyWage = ValidateInput.posDouble();
                staff.setHourlySalary(hourlyWage);
                System.out.println("Hourly wage successfully updated.");
            }
            case 4 -> {
                System.out.println("Enter new maximum monthly hours");
                int maxMonthlyHours = ValidateInput.posInt();
                staff.setMaxMonthlyHours(maxMonthlyHours);
                System.out.println("Maximum monthly hours successfully updated.");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

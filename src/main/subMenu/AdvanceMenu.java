/**
 * AdvanceMenu
 * contains the menu to advance time
 *
 * @author Yubo Zhao
 * @since June 12, 2025
 */

package main.subMenu;

import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import time.TimeBlock;
import time.TimeManager;

public class AdvanceMenu {
    public static TimeManager timeManager = main.CommunityCentreRunner.getTimeManager();

    // show the menu
    public static MenuStatus show() {
        System.out.println("What time would you like to advance to?");
        // options to advance time
        System.out.println("(1) Advance by one hour");
        System.out.println("(2) Advance by specified hours");
        System.out.println("(3) Advance to a specific date");
        System.out.println("<0> Back");

        int advanceChoice = ValidateInput.menu(3);
        main.CommunityCentreRunner.separate();

        switch (advanceChoice) {
            case 1 -> {
                timeManager.advanceHour();
                System.out.println("Time advanced by one hour.");
            }
            case 2 -> {
                System.out.print("Enter the number of hours to advance: ");
                double hours = ValidateInput.posDouble();
                timeManager.advanceHours(hours);
                System.out.println("Time advanced by " + hours + " hours.");
            }
            case 3 -> {
                TimeBlock dateInput = ValidateInput.date();
                System.out.println(); // blank line
                timeManager.advanceToTimeBlock(dateInput.getStartBlock());
                TimeBlock currentTime = timeManager.getCurrentTime();
                System.out.println("Time set to " + currentTime + ".");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

/**
 * ModifySportsFacilityMenu
 * contains the menu to modify a sports facility object
 *
 * @author Sean Yang
 * @since June 13, 2025
 */

package main.subMenu.modify;

import facility.SportsFacility;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;

public class ModifySportsFacilityMenu {
    // show the menu
    public static MenuStatus show(SportsFacility facility) {
        System.out.println("What would you like to modify about this facility?");
        System.out.println("(1) Modify Room Number");
        System.out.println("(2) Modify Max Capacity");
        System.out.println("(3) Modify Rating");
        System.out.println("<0> Back");

        int facilityChoice = ValidateInput.menu(3);
        main.CommunityCentreRunner.separate();

        switch (facilityChoice) {
            case 1 -> {
                int roomNum;
                System.out.println("Enter new room number");
                roomNum = ValidateInput.posInt();
                facility.setRoomNum(roomNum);
                System.out.println("Room number successfully updated.");
            }
            case 2 -> {
                int maxCap;
                System.out.println("Enter new max capacity");
                maxCap = ValidateInput.posInt();

                if (facility.setMaxCapacity(maxCap)) {
                    System.out.println("Max capacity successfully updated.");
                } else {
                    System.out.println("The max capacity must be at least " +
                            facility.minMaxCapacity() + ".");
                }
            }
            case 3 -> {
                double rating;
                rating = ValidateInput.rating();
                facility.setRating(rating);
                System.out.println("Rating successfully updated.");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

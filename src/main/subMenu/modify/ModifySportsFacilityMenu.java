/**
 * ModifySportsFacilityMenu
 * contains the menu to modify a sports facility object
 *
 * @author
 * @since
 */

package main.submenu.modify;

import java.util.Scanner;

import event.EventManager;
import facility.FacilityManager;
import facility.SportsFacility;
import main.CommunityCentreRunner.MenuStatus;
import member.MemberManager;
import staff.StaffManager;

public class ModifySportsFacilityMenu {
    // show the menu
    public static MenuStatus show(SportsFacility facility) {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

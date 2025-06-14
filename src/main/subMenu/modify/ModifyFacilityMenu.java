/**
 * AdultMenu
 * contains the menu to modify a facility object
 *
 * @author
 * @since
 */

package main.submenu.modify;

import java.util.Scanner;

import event.EventManager;
import facility.Facility;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import member.MemberManager;
import staff.StaffManager;

public class ModifyFacilityMenu {
    // show the menu
    public static MenuStatus show(Facility facility) {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

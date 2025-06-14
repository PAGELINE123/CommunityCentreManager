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
    public static Scanner scan = main.CommunityCentreRunner.scan;
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show(Facility facility) {
        // code goes here
        return MenuStatus.CONTINUE;
    }
}

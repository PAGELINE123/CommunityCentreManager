/**
 * StaffMenu
 * contains the menu to modify a staff member object
 *
 * @author
 * @since
 */

package main.submenu.modify;

import java.util.Scanner;

import event.EventManager;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import member.MemberManager;
import staff.Staff;
import staff.StaffManager;

public class ModifyStaffMenu {
    public static Scanner scan = main.CommunityCentreRunner.scan;
    public static MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
    public static StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
    public static FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
    public static EventManager eventManager = main.CommunityCentreRunner.getEventManager();

    // show the menu
    public static MenuStatus show(Staff staff) {
        // code goes here
        return MenuStatus.CONTINUE;
    }
}

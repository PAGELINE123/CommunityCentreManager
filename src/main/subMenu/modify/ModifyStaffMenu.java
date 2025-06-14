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
    // show the menu
    public static MenuStatus show(Staff staff) {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

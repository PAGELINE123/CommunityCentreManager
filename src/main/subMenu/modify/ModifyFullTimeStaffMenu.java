/**
 * ModifyFullTimeStaffMenu
 * contains the menu to modify a full time staff member object
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
import staff.FullTimeStaff;
import staff.StaffManager;

public class ModifyFullTimeStaffMenu {
    // show the menu
    public static MenuStatus show(FullTimeStaff staff) {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

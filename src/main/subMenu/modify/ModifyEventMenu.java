/**
 * EventMenu
 * contains the menu to modify an event
 *
 * @author
 * @since
 */

package main.submenu.modify;

import java.util.Scanner;

import event.Event;
import event.EventManager;
import facility.FacilityManager;
import main.CommunityCentreRunner.MenuStatus;
import member.MemberManager;
import staff.StaffManager;

public class ModifyEventMenu {
    // show the menu
    public static MenuStatus show(Event event) {
        Scanner scan = main.CommunityCentreRunner.scan;
        MemberManager memberManager = main.CommunityCentreRunner.getMemberManager();
        StaffManager staffManager = main.CommunityCentreRunner.getStaffManager();
        FacilityManager facilityManager = main.CommunityCentreRunner.getFacilityManager();
        EventManager eventManager = main.CommunityCentreRunner.getEventManager();

        // code goes here

        return MenuStatus.CONTINUE;
    }
}

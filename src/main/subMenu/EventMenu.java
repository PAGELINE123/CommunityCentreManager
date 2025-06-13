/**
 * BookMenu
 * contains the menu to book an event
 *
 * @author Mansour Abdelsalam
 * @since 2025-06-12
 */

package main.subMenu;

import event.Event;
import main.CommunityCentreRunner;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.Member;
import staff.Staff;

public class EventMenu {
    public static MenuStatus show() {
        System.out.println("What would you like to do?");
        System.out.println("(1) Register member to an existing event");
        System.out.println("(2) Assign staff to an existing event");
        System.out.println("<0> Back");

        int choice = ValidateInput.menu(3);
        CommunityCentreRunner.separate();

        switch (choice) {
            case 1 -> {
                System.out.println("Enter event ID");
                int eventId = ValidateInput.posInt();
                Event event = CommunityCentreRunner.getEventManager().searchById(eventId);
                if (event == null) {
                    System.out.println("Event with ID #" + eventId + " not found.");
                    break;
                }

                System.out.println("Enter member ID");
                int memberId = ValidateInput.posInt();
                Member member = CommunityCentreRunner.getMemberManager().searchById(memberId);
                if (member == null) {
                    System.out.println("Member with ID #" + memberId + " not found.");
                    break;
                }

                event.registerParticipant(member);
                System.out.println("Member " + memberId + " signed up for event " + eventId + ".");
            }
            case 2 -> {
                System.out.println("Enter event ID");
                int eventId2 = ValidateInput.posInt();
                Event event2 = CommunityCentreRunner.getEventManager().searchById(eventId2);
                if (event2 == null) {
                    System.out.println("Event with ID #" + eventId2 + " not found.");
                    break;
                }

                System.out.println("Enter staff ID");
                int staffId = ValidateInput.posInt();
                Staff staff = CommunityCentreRunner.getStaffManager().searchById(staffId);
                if (staff == null) {
                    System.out.println("Staff with ID #" + staffId + " not found.");
                    break;
                }

                event2.assignStaff(staff);
                System.out.println("Staff " + staffId + " assigned to event " + eventId2 + ".");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

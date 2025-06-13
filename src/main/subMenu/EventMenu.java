/**
 * BookMenu
 * contains the menu to book an event
 *
 * @author Mansour Abdelsalam
 * @since 2025-06-12
 */

package main.subMenu;

import java.util.Scanner;

import event.Event;
import main.CommunityCentreRunner;
import main.CommunityCentreRunner.MenuStatus;
import main.ValidateInput;
import member.Member;
import staff.Staff;

public class EventMenu {
    public static MenuStatus show() {
        Scanner scan = CommunityCentreRunner.scan;

        System.out.println("What would you like to do?");
        System.out.println("(1) Register member to an existing event");
        System.out.println("(2) Assign staff to an existing event");
        System.out.println("<0> Back");

        int choice = ValidateInput.menu(3);
        CommunityCentreRunner.separate();

        switch (choice) {
            case 1 -> {
                System.out.println("Enter Event ID");
                int eventId = ValidateInput.posInt();
                Event event = CommunityCentreRunner.getEventManager().searchById(eventId);
                if (event == null) {
                    System.out.println("Event with ID #" + eventId + " not found.");
                    break;
                }

                System.out.println("Enter Member ID or Name");
                System.out.print(" >  ");
                String memberNameOrId = scan.nextLine();
                Member member = CommunityCentreRunner.getMemberManager().searchByIdOrName(memberNameOrId);
                if (member == null) {
                    System.out.println("Member not found.");
                    break;
                }

                event.registerParticipant(member);
                System.out.println("Member " + member.getName() + " signed up for event " + eventId + ".");
            }
            case 2 -> {
                System.out.println("Enter Event ID");
                int eventId2 = ValidateInput.posInt();
                Event event2 = CommunityCentreRunner.getEventManager().searchById(eventId2);
                if (event2 == null) {
                    System.out.println("Event with ID #" + eventId2 + " not found.");
                    break;
                }

                System.out.println("Enter Staff ID or Name");
                System.out.print(" >  ");
                String staffIdOrName = scan.nextLine();
                Staff staff = CommunityCentreRunner.getStaffManager().searchByIdOrName(staffIdOrName);
                if (staff == null) {
                    System.out.println("Staff not found.");
                    break;
                }

                event2.assignStaff(staff);
                System.out.println("Staff " + staff.getName() + " assigned to event " + eventId2 + ".");
            }
            case 0 -> {
                return MenuStatus.BACK;
            }
        }

        return MenuStatus.CONTINUE;
    }
}

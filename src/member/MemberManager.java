package member;

import event.Event;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import main.CommunityCentreRunner;
import java.util.Map;

/**
 * Manages a collection of Member objects: loading from file,
 * adding new members, searching by ID, printing bills,
 * and listing names alphabetically.
 * <p>
 * File format:
 * <num members>
 * <id>
 * <type>
 * <age>
 * <name>
 * <planType>
 * If adult:
 * <contactPhone>
 * <address>
 * <billAmount>
 * <billPaid>
 * <num children>
 * <child id>...
 * If youth:
 * <guardian id>
 * If adult:
 * <contactPhone>
 * <address>
 * <billAmount>
 * <billPaid>
 * <num children>
 * <child id>...
 * If youth:
 * <guardian id>
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-06
 */
public class MemberManager {
    /**
     * The list of all members managed by this class.
     */
    private ArrayList<Member> members = new ArrayList<>();

    public MemberManager() {
        members = new ArrayList<>();
    }

    /**
     * Constructs a MemberManager and immediately loads member data
     * from the specified file, wiring up parent/child relationships.
     *
     * @param filename the path to the member data file
     */
    public MemberManager(String filename) {
        members = new ArrayList<>();
        Map<Integer, Integer> youthGuardian = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int numMembers = Integer.parseInt(br.readLine().trim());

            for (int i = 0; i < numMembers; i++) {
                int id = Integer.parseInt(br.readLine().trim());
                int age = Integer.parseInt(br.readLine().trim());
                String name = br.readLine().trim();
                Member.PlanType pType = Member.PlanType.valueOf(br.readLine().trim().toUpperCase());

                if (age >= Member.ADULT_AGE) {
                    String phone = br.readLine().trim();
                    String address = br.readLine().trim();
                    double totalAmount = Double.parseDouble(br.readLine().trim());
                    double paidAmount = Double.parseDouble(br.readLine().trim());

                    int numChildren = Integer.parseInt(br.readLine().trim());
                    List<Integer> childIds = new ArrayList<>();
                    for (int j = 0; j < numChildren; j++) {
                        childIds.add(Integer.parseInt(br.readLine().trim()));
                    }

                    AdultMember adult = new AdultMember(age, name, pType, phone, address, totalAmount, paidAmount);
                    adult.setId(id);
                    members.add(adult);
                    // you can still wire children if you stored their IDs elsewhere
                } else { // youth
                    int guardianId = Integer.parseInt(br.readLine().trim());
                    YouthMember youth = new YouthMember(age, name, pType, null);
                    youth.setId(id);
                    members.add(youth);
                    youthGuardian.put(id, guardianId);
                }
            }

            // now hook up youth to guardian and guardian to children
            for (var e : youthGuardian.entrySet()) {
                YouthMember y = (YouthMember) searchById(e.getKey());
                AdultMember a = (AdultMember) searchById(e.getValue());
                y.setGuardian(a);
                a.addChild(y);
            }

            br.close();
        } catch (IOException iox) {
            System.out.println("Error reading member file: " + iox.getMessage());
        }
    }

    /**
     * saves to file
     * 
     * @param filepath
     */
    public void save(String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write(members.size() + "\n");

            for (Member member : members) {
                bw.write(member.id + "\n");
                bw.write(member.age + "\n");
                bw.write(member.name + "\n");
                bw.write(member.planType + "\n");

                if (member instanceof AdultMember adult) {
                    bw.write(adult.getContactPhone() + "\n");
                    bw.write(adult.getAddress() + "\n");
                    bw.write(adult.getTotalBillAmount() + "\n");
                    bw.write(adult.getPaidBillAmount() + "\n");

                    bw.write(adult.getChildren().size() + "\n");
                    for (YouthMember child : adult.getChildren()) {
                        bw.write(child.id + "\n");
                    }
                } else if (member instanceof YouthMember youth) {
                    bw.write(youth.getGuardian().id + "\n");
                }
            }

            bw.close();
        } catch (IOException iox) {
            System.out.println("Error writing to member file: " + iox.getMessage());
        }
    }

    /**
     * Adds a new member, assigning a unique ID automatically.
     *
     * @param member the Member to add
     */
    public void addMember(Member member) {
        member.setId(generateId());
        members.add(member);
    }

    /**
     * Removes a member by ID. If the member is an AdultMember,
     * also removes parent reference from any of their children.
     * If the member is a YouthMember, also removes the child from the guardian.
     *
     * @param id the ID of the member to remove
     * @return true if the member was found and removed, false otherwise
     */
    public boolean removeMember(int id) {
        Member target = searchById(id);
        if (target == null) {
            return false;
        }

        if (target instanceof AdultMember) {
            AdultMember adult = (AdultMember) target;
            for (YouthMember child : adult.getChildren()) {
                child.setGuardian(null);
                removeMember(child.getId());
            }
        } else if (target instanceof YouthMember) {
            YouthMember youth = (YouthMember) target;
            AdultMember guardian = youth.getGuardian();
            if (guardian != null) {
                guardian.getChildren().remove(youth);
            }
        }

        List<Event> allEvents = CommunityCentreRunner
                .getEventManager()
                .getEvents();
        for (Event e : allEvents) {
            List<Member> parts = e.getParticipants();
            for (int i = 0; i < parts.size(); i++) {
                if (parts.get(i).getId() == target.getId()) {
                    parts.remove(target);
                }
            }
        }

        return members.remove(target);
    }

    /**
     * Generates the next unique member ID (last ID + 1).
     *
     * @return the new unique ID
     */
    public int generateId() {
        int maxId = -1;

        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() > maxId) {
                maxId = members.get(i).getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Searches for a member by ID using binary search.
     * Assumes the members list is sorted by ID.
     *
     * @param id the ID to search for
     * @return the Member with matching ID, or null if not found
     */
    public Member searchById(int id) {
        return searchByIdRecursive(id, 0, members.size() - 1);
    }

    /**
     * Recursive helper for binary search.
     */
    private Member searchByIdRecursive(int id, int low, int high) {
        if (low > high) {
            return null;
        }
        int mid = (low + high) / 2;
        int midId = members.get(mid).getId();

        if (midId == id) {
            return members.get(mid);
        } else if (midId > id) {
            return searchByIdRecursive(id, low, mid - 1);
        } else {
            return searchByIdRecursive(id, mid + 1, high);
        }
    }

    /**
     * searches for member by their name or id, whichever is valid
     * 
     * @param idOrName
     * @return the member with the matching name or id
     */
    public Member searchByIdOrName(String idOrName) {
        Member member = null;

        try {
            int id = Integer.parseInt(idOrName);
            member = searchById(id);
        } catch (NumberFormatException ignored) {
            member = searchByName(idOrName);
        }

        return member;
    }

    /**
     * Prints all members' bills to standard output.
     * 
     * @return whether anything was printed
     */
    public boolean printAllBills() {
        if (members.isEmpty()) {
            return false;
        }

        for (Member m : members) {
            if (m instanceof AdultMember adult) {
                adult.printBill();
            }
        }

        return true;
    }

    /**
     * Prints all member names in alphabetical order.
     * 
     * @return whether anythign was printed
     */
    public boolean printAlphabetical() {
        if (members.isEmpty()) {
            return false;
        }

        ArrayList<String> sorted = new ArrayList<>();
        for (Member m : members) {
            sorted.add(m.getName());
        }
        Collections.sort(sorted);
        for (String name : sorted) {
            System.out.println(name);
        }

        return true;
    }

    /**
     * Prints all members in ID order.
     * 
     * @return whether anything was printed
     */
    public boolean printAllMembers() {
        if (members.isEmpty()) {
            return false;
        }

        for (Member member : members) {
            System.out.println(member);
        }

        return true;
    }

    /**
     * Searches for all members whose name matches the given string
     * (case-insensitive).
     *
     * @param name the full name to search for
     * @return the first person with the name
     */
    public Member searchByName(String name) {
        for (Member m : members) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Applies a billing cycle to all members on the MONTHLY plan.
     * For each AdultMember with a monthly plan, invokes payBill() using
     * the amount returned by calculateBill().
     */
    public void billMonthlyMembers() {
        for (Member m : members) {
            if (m.getPlanType() == Member.PlanType.MONTHLY && m instanceof AdultMember am) {
                am.addBillBase();
                System.out.printf("Member #" + am.getId() + " " + am.getName() + " was billed %.2f\n",
                        am.calculateBill());
                if (am.getPaidBillAmount()<am.getTotalBillAmount()) {
                    am.payBill(m.calculateBill()); // they pay off their bill
                }
            }
        }
    }

    /**
     * Applies a billing cycle to all members on the ANNUAL plan.
     * For each AdultMember with an annual plan, invokes payBill() using
     * the amount returned by calculateBill().
     */
    public void billAnnualMembers() {
        for (Member m : members) {
            if (m.getPlanType() == Member.PlanType.ANNUAL && m instanceof AdultMember am) {
                am.addBillBase();
                System.out.printf("Member #" + am.getId() + " " + am.getName() + " was billed %.2f\n",
                        am.calculateBill());
                if (am.getPaidBillAmount()<am.getTotalBillAmount()) {
                    am.payBill(m.calculateBill()); // they pay off their bill
                }
            }
        }
    }

    /**
     * ages all members by one year
     */
    public void ageMembers() {
        // snapshot so we can mutate members safely
        List<Member> snapshot = new ArrayList<>(members);

        for (Member m : snapshot) {
            if (m instanceof AdultMember adult) {
                adult.setAge(adult.getAge() + 1);

            } else if (m instanceof YouthMember youth) {
                youth.setAge(youth.getAge() + 1);

                if (youth.getAge() >= Member.ADULT_AGE) {
                    System.out.println(youth.getName() + " is now an adult member.");

                    // build new AdultMember
                    AdultMember grown = new AdultMember(
                            youth.getAge(),
                            youth.getName(),
                            youth.getPlanType(),
                            youth.getGuardian().getContactPhone(),
                            youth.getGuardian().getAddress());
                    grown.setPaidBillAmount(youth.calculateBill());

                    // now also replace in every Event’s participant list
                    for (Event e : youth.getRegistrations().getEventSchedule()) {
                        e.getParticipants().add(grown);
                    }

                    // replace in members list
                    removeMember(youth.getId());
                    youth.getGuardian().getChildren().remove(youth);
                    addMember(grown);
                }
            }
        }
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}

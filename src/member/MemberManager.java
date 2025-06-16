package member;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import event.Competition;
import event.Event;
import main.CommunityCentreRunner;

/**
 * manages member collection: load from file, add/remove, search, print bills,
 * list names.
 * file format: numMembers, id, age, name, planType, billingCycles, then for
 * adults phone,address,totalAmount,paidAmount,numChildren,childIds; for youth
 * guardianId.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-06
 */
public class MemberManager {
    /** list of all members */
    private ArrayList<Member> members = new ArrayList<>();

    /** create empty manager */
    public MemberManager() {
        members = new ArrayList<>();
    }

    /**
     * create manager from file and link guardians
     * 
     * @param filename member data file path
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
                int billingCycles = Integer.parseInt(br.readLine());
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
                    AdultMember adult = new AdultMember(age, name, pType, phone, address, totalAmount, paidAmount,
                            billingCycles);
                    adult.setId(id);
                    members.add(adult);
                } else {
                    int guardianId = Integer.parseInt(br.readLine().trim());
                    YouthMember youth = new YouthMember(age, name, pType, null, billingCycles);
                    youth.setId(id);
                    members.add(youth);
                    youthGuardian.put(id, guardianId);
                }
            }
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
     * save members to file
     * 
     * @param filepath output file path
     */
    public void save(String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write(members.size() + "\n");
            for (Member member : members) {
                bw.write(member.id + "\n");
                bw.write(member.age + "\n");
                bw.write(member.name + "\n");
                bw.write(member.planType + "\n");
                bw.write(member.billingCycles + "\n");
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
     * add member with unique id
     * 
     * @param member member to add
     */
    public void addMember(Member member) {
        member.setId(generateId());
        members.add(member);
    }

    /**
     * remove member by id, clean up relations and events
     * 
     * @param id member id
     * @return true if removed
     */
    public boolean removeMember(int id) {
        Member target = searchById(id);
        if (target == null)
            return false;
        if (target instanceof AdultMember adult) {
            for (YouthMember child : adult.getChildren()) {
                child.setGuardian(null);
                removeMember(child.getId());
            }
        } else if (target instanceof YouthMember youth) {
            AdultMember guardian = youth.getGuardian();
            if (guardian != null)
                guardian.getChildren().remove(youth);
        }
        List<Event> allEvents = CommunityCentreRunner.getEventManager().getEvents();
        for (Event e : allEvents) {
            e.getParticipants().removeIf(m -> m.getId() == target.getId());
        }
        return members.remove(target);
    }

    /**
     * generate next unique id
     * 
     * @return new id
     */
    public int generateId() {
        int maxId = -1;
        for (Member m : members)
            if (m.getId() > maxId)
                maxId = m.getId();
        return maxId + 1;
    }

    /**
     * search by id with binary search
     * 
     * @param id member id
     * @return member or null
     */
    public Member searchById(int id) {
        return searchByIdRecursive(id, 0, members.size() - 1);
    }

    /** recursive helper for searchById */
    private Member searchByIdRecursive(int id, int low, int high) {
        if (low > high)
            return null;
        int mid = (low + high) / 2;
        int midId = members.get(mid).getId();
        if (midId == id)
            return members.get(mid);
        else if (midId > id)
            return searchByIdRecursive(id, low, mid - 1);
        else
            return searchByIdRecursive(id, mid + 1, high);
    }

    /**
     * search by id or name
     * 
     * @param idOrName id string or full name
     * @return member or null
     */
    public Member searchByIdOrName(String idOrName) {
        try {
            return searchById(Integer.parseInt(idOrName));
        } catch (NumberFormatException ignored) {
            return searchByName(idOrName);
        }
    }

    /**
     * print bills for adult members
     * 
     * @return whether any printed
     */
    public boolean printAllBills() {
        if (members.isEmpty())
            return false;
        for (Member m : members)
            if (m instanceof AdultMember adult)
                adult.printBill();
        return true;
    }

    /**
     * print member names alphabetically
     * 
     * @return whether printed
     */
    public boolean printAlphabetical() {
        if (members.isEmpty())
            return false;
        ArrayList<String> sorted = new ArrayList<>();
        for (Member m : members)
            sorted.add(m.getName());
        Collections.sort(sorted);
        for (String name : sorted)
            System.out.println(name);
        return true;
    }

    /**
     * print all members
     * 
     * @return whether printed
     */
    public boolean printAllMembers() {
        if (members.isEmpty())
            return false;
        for (Member m : members)
            System.out.println(m);
        return true;
    }

    /**
     * search by full name
     * 
     * @param name full name
     * @return member or null
     */
    public Member searchByName(String name) {
        for (Member m : members)
            if (m.name.equalsIgnoreCase(name))
                return m;
        return null;
    }

    /** bill all monthly adult members */
    public void billMonthlyMembers() {
        for (Member m : members) {
            if (m.getPlanType() == Member.PlanType.MONTHLY && m instanceof AdultMember am) {
                System.out.printf("Member #%d %s was billed %.2f\n", am.getId(), am.getName(), am.calculateBill());
                am.incrementBillingCycles();
                if (am.getPaidBillAmount() < am.getTotalBillAmount()) {
                    am.payBill(am.calculateBill());
                }
            }
        }
    }

    /** bill all annual adult members */
    public void billAnnualMembers() {
        for (Member m : members) {
            if (m.getPlanType() == Member.PlanType.ANNUAL && m instanceof AdultMember am) {
                System.out.printf("Member #%d %s was billed %.2f\n", am.getId(), am.getName(), am.calculateBill());
                am.incrementBillingCycles();
                if (am.getPaidBillAmount() < am.getTotalBillAmount()) {
                    am.payBill(am.calculateBill());
                }
            }
        }
    }

    /** increase age by one year, promote youths to adults */
    public void ageMembers() {
        List<Member> snapshot = new ArrayList<>(members);
        for (Member m : snapshot) {
            if (m instanceof AdultMember adult) {
                adult.setAge(adult.getAge() + 1);
            } else if (m instanceof YouthMember youth) {
                youth.setAge(youth.getAge() + 1);
                if (youth.getAge() >= Member.ADULT_AGE) {
                    System.out.println(youth.getName() + " is now an adult member.");
                    AdultMember grown = new AdultMember(
                            youth.getAge(), youth.getName(), youth.getPlanType(),
                            youth.getGuardian().getContactPhone(), youth.getGuardian().getAddress());
                    grown.setBillingCycles(youth.getBillingCycles());
                    for (Event e : youth.getRegistrations().getEventSchedule()) {
                        e.registerParticipant(grown);
                        if (e instanceof Competition c && c.isCompleted() && c.getWinner().equals(grown)) {
                            c.setWinner(grown);
                        }
                    }
                    removeMember(youth.getId());
                    youth.getGuardian().getChildren().remove(youth);
                    addMember(grown);
                }
            }
        }
    }

    /** get member list */
    public ArrayList<Member> getMembers() {
        return members;
    }

    /** set member list */
    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }
}

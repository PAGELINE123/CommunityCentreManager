package member;

import java.util.ArrayList;
import java.util.List;

import event.Competition;
import event.Event;

/**
 * represents an adult member with contact info, billing details, and a list of
 * dependent youth members.
 * provides methods to calculate and pay bills, register for events, and manage
 * children.
 *
 * @author Yubo Zhao
 * @version 1.0
 * @since 2025-06-10
 */
public class AdultMember extends Member {
    private String contactPhone;
    private String address;
    private double totalBillAmount;
    private double paidBillAmount;
    private List<YouthMember> children = new ArrayList<>();

    /** create adult member with contact info */
    public AdultMember(int age, String name, PlanType planType, String contactPhone, String address) {
        super(age, name, planType);
        this.contactPhone = contactPhone;
        this.address = address;
        this.totalBillAmount = 0;
        this.paidBillAmount = 0;
        this.billingCycles = 0;
    }

    /** create adult member with initial bill amounts */
    public AdultMember(int age, String name, PlanType planType, String contactPhone, String address,
            double totalBillAmount, double paidBillAmount) {
        super(age, name, planType);
        this.contactPhone = contactPhone;
        this.address = address;
        this.totalBillAmount = totalBillAmount;
        this.paidBillAmount = paidBillAmount;
    }

    /** create adult member with billing cycles and bill amounts */
    public AdultMember(int age, String name, PlanType planType, String contactPhone, String address,
            double totalBillAmount, double paidBillAmount, int billingCycles) {
        super(age, name, planType, billingCycles);
        this.contactPhone = contactPhone;
        this.address = address;
        this.totalBillAmount = totalBillAmount;
        this.paidBillAmount = paidBillAmount;
    }

    /** calculate amount due (total bill minus paid amount) */
    @Override
    public double calculateBill() {
        totalBillAmount = calculateTotalBill();
        double bill = totalBillAmount - paidBillAmount;
        if (bill < 0)
            bill = 0;
        return bill;
    }

    /** increment billing cycles for this member and children */
    public void incrementBillingCycles() {
        this.billingCycles++;
        for (Member child : children) {
            child.setBillingCycles(child.getBillingCycles() + 1);
        }
    }

    /** calculate total bill (base fee + event costs + children bills) */
    public double calculateTotalBill() {
        double base = switch (planType) {
            case MONTHLY -> MONTHLY_BASE;
            case ANNUAL -> ANNUAL_BASE;
        };
        base = base + billingCycles * base;
        for (Event event : registrations.getEventSchedule()) {
            if (event instanceof Competition c) {
                base += c.getParticipationCost();
                if (this.equals(c.getWinner())) {
                    base -= c.getPrize();
                }
            }
        }
        if (!children.isEmpty()) {
            for (YouthMember child : children) {
                base += child.calculateBill();
            }
        }
        return base;
    }

    /**
     * apply payment toward bill
     * 
     * @param amount payment amount
     */
    public void payBill(double amount) {
        paidBillAmount += amount;
    }

    /** print bill details */
    public void printBill() {
        System.out.println(this + " | Total bill: " + calculateTotalBill() + " | Paid off: " + paidBillAmount);
    }

    /** personal info string with contact and address */
    public String personalInfo() {
        return "Personal Info" + " | Contact Phone: " + getContactPhone() + " | Address: " + getAddress();
    }

    /** member details string including children names */
    @Override
    public String toString() {
        String s = "Adult Member " + super.toString();
        if (!children.isEmpty()) {
            s += children.size() > 1 ? " | Children: " : " | Child: ";
            for (int i = 0; i < children.size(); i++) {
                if (i > 0)
                    s += ", ";
                s += children.get(i).getName();
            }
        }
        return s;
    }

    /**
     * register as host for event
     * 
     * @param event event to host
     */
    public void addHosting(Event event) {
        registerFor(event);
    }

    /**
     * add a dependent youth member
     * 
     * @param child youth member
     * @return true if added
     */
    public boolean addChild(YouthMember child) {
        if (!children.contains(child)) {
            children.add(child);
            return true;
        }
        return false;
    }

    /**
     * remove a dependent youth member
     * 
     * @param child youth member
     * @return true if removed
     */
    public boolean removeChild(YouthMember child) {
        if (children.contains(child)) {
            children.remove(child);
            main.CommunityCentreRunner.getMemberManager().removeMember(child.getId());
            return true;
        }
        return false;
    }

    /** get list of children */
    public List<YouthMember> getChildren() {
        return children;
    }

    /** get contact phone */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * set contact phone (format XXX-XXX-XXXX)
     * 
     * @param contactPhone phone string
     */
    public void setContactPhone(String contactPhone) {
        if (contactPhone == null) {
            throw new IllegalArgumentException("Phone number cannot be null.");
        }
        String digits = contactPhone.replaceAll("\\D", "");
        if (digits.length() != 10) {
            throw new IllegalArgumentException("Phone number must contain exactly 10 digits; got " + digits.length());
        }
        this.contactPhone = digits.replaceFirst("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
    }

    /** get residential address */
    public String getAddress() {
        return address;
    }

    /**
     * set residential address
     * 
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /** add base fee to total bill */
    public void BillBase() {
        double base = switch (planType) {
            case MONTHLY -> MONTHLY_BASE;
            case ANNUAL -> ANNUAL_BASE;
        };
        this.totalBillAmount += base;
    }

    /** get additional bill amount */
    public double getTotalBillAmount() {
        return totalBillAmount;
    }

    /**
     * set additional bill amount
     * 
     * @param totalBillAmount new amount
     */
    public void setTotalBillAmount(double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    /** get amount already paid */
    public double getPaidBillAmount() {
        return paidBillAmount;
    }

    /**
     * set amount already paid
     * 
     * @param paidBillAmount updated amount
     */
    public void setPaidBillAmount(double paidBillAmount) {
        this.paidBillAmount = paidBillAmount;
    }
}

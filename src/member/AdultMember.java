/**
 * Yubo
 */

package member;

import java.util.ArrayList;
import java.util.List;

import event.Competition;
import event.Event;

/**
 * Represents an adult member with contact information, billing details,
 * and a list of dependent youth members.
 * Provides methods to calculate and pay bills, register for events,
 * and manage children.
 *
 * @author Yubo Zhao
 * @version 1.0
 * @since 2025-06-10
 */
public class AdultMember extends Member {
    /** Contact phone number for this adult member. */
    private String contactPhone;

    /** Residential address of this adult member. */
    private String address;

    /** Amount due in addition to the base membership fee. */
    private double totalBillAmount;

    /** Amount already paid toward the total bill. */
    private double paidBillAmount;

    /** List of youth members (children) linked to this guardian. */
    private List<YouthMember> children = new ArrayList<>();

    /**
     * Constructs an AdultMember with the specified details.
     *
     * @param age          the member's age
     * @param name         the member's full name
     * @param planType     the membership billing plan
     * @param contactPhone phone number for contact
     * @param address      residential address
     */
    public AdultMember(int age, String name, PlanType planType, String contactPhone, String address) {
        super(age, name, planType); 
        this.contactPhone = contactPhone;
        this.address = address;
        this.totalBillAmount = 0;
        this.paidBillAmount = 0;
        this.billingCycles = 0;
    }

    /**
     * Constructs an AdultMember with the specified details.
     *
     * @param age             the member's age
     * @param name            the member's full name
     * @param planType        the membership billing plan
     * @param contactPhone    phone number for contact
     * @param address         residential address
     * @param totalBillAmount additional bill amount beyond base fee
     * @param paidBillAmount  amount already paid toward the bill
     */
    public AdultMember(int age, String name, PlanType planType,
            String contactPhone, String address,
            double totalBillAmount, double paidBillAmount) {
        super(age, name, planType);
        this.contactPhone = contactPhone;
        this.address = address;
        this.totalBillAmount = totalBillAmount;
        this.paidBillAmount = paidBillAmount;
    }

    /**
     * Constructs an AdultMember with the specified details.
     * @param age
     * @param name
     * @param planType
     * @param contactPhone
     * @param address
     * @param billingCycles
     */
    public AdultMember(int age, String name, PlanType planType,
            String contactPhone, String address,
            double totalBillAmount, double paidBillAmount, int billingCycles) {
        super(age, name, planType, billingCycles); 
        this.contactPhone = contactPhone;
        this.address = address;
        this.totalBillAmount = totalBillAmount;
        this.paidBillAmount = paidBillAmount;
    }

    /**
     * Calculates this member's bill to pay by subtracting
     * the total bill by the total amount of the bill they've paid
     *
     * @return the total amount due
     */
    @Override
    public double calculateBill() {
        totalBillAmount = calculateTotalBill();
        double bill = totalBillAmount - paidBillAmount;
        if (bill < 0)
            bill = 0;
        return bill;
    }

    /**
     * Calculates this member's total bill by adding the plan base fee
     * to any additional charges.
     * 
     * @return the total bill
     */
    public double calculateTotalBill() {
        double base = switch (planType) {
            case MONTHLY -> MONTHLY_BASE;
            case ANNUAL -> ANNUAL_BASE;
        };

        base = base + billingCycles*base;

        for (Event event : registrations.getEventSchedule()) {
            //System.out.println(registrations.getEventSchedule());
            if (event instanceof Competition c) {
                base += c.getParticipationCost();
                //System.out.println("ADULTMEMBER THIS IS WINNER: "+c.getWinner());
                if (c.getWinner()!=null) {
                    //System.out.println("ADULTMEMBER WINNER ID: "+c.getWinner().getId());
                    //System.out.println("ADULTMEMBER ID IS: "+this.getId());
                }
                if (this.equals(c.getWinner())) {
                    //System.out.println("ADULTMEMBER WINNER?? YES");
                    base -= c.getPrize();
                }
            }
        }

        if (!children.isEmpty()) {
            for (YouthMember child : children) {
                base += child.calculateBill();
            }
        }

        //System.out.println("ADULTMEMBER "+getName()+" BILL: "+base);
        //System.out.println("TOTAL BILL: "+totalBillAmount);

        return base;
    }

    /**
     * Applies a payment toward the bill.
     *
     * @param amount the payment amount
     */
    public void payBill(double amount) {
        paidBillAmount += amount;
    }

    /**
     * Prints this member's billing details to standard output.
     */
    public void printBill() {
        System.out.println(this + " | Total bill: " + calculateTotalBill() + " | Paid off: " + paidBillAmount);
    }

    /**
     * Returns the membership details string with personal info
     *
     * @return the toString representation
     */
    public String personalInfo() {
        return "Personal Info" +
                " | Contact Phone: " + getContactPhone() +
                " | Address: " + getAddress();
    }

    /**
     * Returns a string representation of this member's details.
     *
     * @return membership details string
     */
    @Override
    public String toString() {
        String s = "Adult Member " + super.toString();

        if (!children.isEmpty()) {
            if (children.size() > 1) {
                s += " | Children: ";

                for (int i = 0; i < children.size(); i++) {
                    YouthMember child = children.get(i);
                    if (i > 0) {
                        s += ", ";
                    }
                    s += child.getName();
                }
            } else {
                s += " | Child: ";
                s += children.get(0).getName();
            }
        }
        return s;
    }

    /**
     * Registers this member as host for an event.
     *
     * @param event the Event to host
     */
    public void addHosting(Event event) {
        registerFor(event);
    }

    /**
     * Adds a youth member as this guardian's child.
     *
     * @param child the YouthMember to add
     * @return whether the child was succesfully added (false -> already a child)
     */
    public boolean addChild(YouthMember child) {
        if (!children.contains(child)) {
            children.add(child);
            return true;
        }

        return false;
    }

    /**
     * Removes a youth member as this guardian's child.
     *
     * @param child the YouthMember to remove
     * @return whether the child was succesfully removed (false -> not a child)
     */
    public boolean removeChild(YouthMember child) {
        if (children.contains(child)) {
            children.remove(child);
            main.CommunityCentreRunner.getMemberManager().removeMember(child.getId());
            return true;
        }
        return false;
    }

    /**
     * Retrieves the list of this guardian's children.
     *
     * @return list of YouthMember
     */
    public List<YouthMember> getChildren() {
        return children;
    }

    /**
     * Gets the contact phone number.
     *
     * @return the phone number string
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * Sets and formats the contact phone number.
     * Strips out any non-digits, then requires exactly 10 digits.
     * Formats as "XXX-XXX-XXXX". Throws if invalid.
     */
    public void setContactPhone(String contactPhone) {
        if (contactPhone == null) {
            throw new IllegalArgumentException("Phone number cannot be null.");
        }
        String digits = contactPhone.replaceAll("\\D", "");
        if (digits.length() != 10) {
            throw new IllegalArgumentException(
                    "Phone number must contain exactly 10 digits; got " + digits.length());
        }
        this.contactPhone = digits.replaceFirst(
                "(\\d{3})(\\d{3})(\\d{4})",
                "$1-$2-$3");
    }

    /**
     * Gets the residential address.
     *
     * @return the address string
     */
    public String getAddress() {
        return address;
    }

    /**
     * Updates the residential address.
     *
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Adds the base to the total bill
     */
    public void BillBase() {
        double base = switch (planType) {
            case MONTHLY -> MONTHLY_BASE;
            case ANNUAL -> ANNUAL_BASE;
        };
        this.totalBillAmount += base;
    }

    /**
     * Gets the additional bill amount.
     *
     * @return the extra amount due
     */
    public double getTotalBillAmount() {
        return totalBillAmount;
    }

    /**
     * Sets the total bill amount.
     *
     * @param totalBillAmount the new amount
     */
    public void setTotalBillAmount(double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    /**
     * Gets the amount already paid.
     *
     * @return the paid amount
     */
    public double getPaidBillAmount() {
        return paidBillAmount;
    }

    /**
     * Sets the amount already paid.
     *
     * @param paidBillAmount the updated paid amount
     */
    public void setPaidBillAmount(double paidBillAmount) {
        this.paidBillAmount = paidBillAmount;
    }
}

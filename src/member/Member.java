/**
 * Yubo
 */

package member;

import java.util.ArrayList;
import java.util.Comparator;

import event.Event;
import time.Schedule;

/**
 * Abstract base class for all members.
 * Tracks common member information, billing plan, and event registrations.
 * Subclasses must implement calculateBill() to determine their specific fees.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-03
 */
public abstract class Member {
    /** Monthly base fee */
    public static final double MONTHLY_BASE = 35.00;
    /** Annual base fee */
    public static final double ANNUAL_BASE = 350.00;
    /** Minimum age for adult membership */
    public static final int ADULT_AGE = 18;

    /** Unique identifier for this member */
    protected int id;
    /** Age of the member */
    protected int age;
    /** Full name of the member */
    protected String name;
    /** Schedule of events this member is registered for */
    protected Schedule registrations;
    /** Billing plan type for this member */
    protected PlanType planType;
    /**Amount of billing cycles the member has been through */
    protected int billingCycles;

    /**
     * Enumeration of available billing plans.
     */
    public enum PlanType {
        MONTHLY,
        ANNUAL
    }

    /**
     * Constructs a Member with the given age, name, and plan.
     * Initializes an empty registration schedule.
     *
     * @param age      the member's age
     * @param name     the member's full name
     * @param planType the billing plan for this member
     */
    public Member(int age, String name, PlanType planType) {
        this.age = age;
        this.name = name;
        this.planType = planType;
        this.registrations = new Schedule();
        this.billingCycles = 0;
    }

    /**
     * Constructs a Member with the given age, name, and plan.
     * Initializes an empty registration schedule.
     *
     * @param age      the member's age
     * @param name     the member's full name
     * @param planType the billing plan for this member
     */
    public Member(int age, String name, PlanType planType, int billingCycles) {
        this.age = age;
        this.name = name;
        this.planType = planType;
        this.registrations = new Schedule();
        this.billingCycles = billingCycles;
    }


    /**
     * Calculates this member's total bill based on their plan and any
     * subclass-specific charges.
     *
     * @return the total amount owed
     */
    public abstract double calculateBill();

    /**
     * Returns personal info
     *
     * @return the string representation
     */
    public abstract String personalInfo();

    /**
     * Sets the amount of billing cycles of the member
     * 
     * @param billingCycles
     */
    public void setBillingCycles(int billingCycles) {
        this.billingCycles = billingCycles;
    }

    /**
     * returns the billing cycles of the member
     * @return the billing cycles of the member
     */
    public int getBillingCycles() {
        return this.billingCycles;
    }

    /**
     * Returns the membership details string
     * 
     * @return the toString representation
     */
    @Override
    public String toString() {
        return "#" + id
                + " | Age: " + age
                + " | Name: " + name
                + " | Plan: " + planType
                + " | Gross bill: "
                + String.format("$%.2f", calculateBill());
    }

    /**
     * returns a string containing events that this member is registered for
     * 
     * @return
     */
    public String toRegistrationString() {
        String s = "";
        if (!registrations.getEventSchedule().isEmpty()) {
            ArrayList<Event> events = new ArrayList<>();

            for (Event event : registrations.getEventSchedule()) {
                if (!event.isCompleted()) {
                    events.add(event);
                }
            }

            events.sort(Comparator.comparingDouble(Event::hoursSinceEpoch));

            s += "\nRegistered Events (Ongoing/Future):";

            for (Event event : events) {
                s += "\n - " + event;
            }
        }
        return s;
    }

    /**
     * Registers this member for the given event.
     *
     * @param event the event to register for
     */
    public void registerFor(Event event) {
        registrations.add(event);
    }

    /** @return the member's unique ID */
    public int getId() {
        return id;
    }

    /** Sets the member's unique ID */
    public void setId(int id) {
        this.id = id;
    }

    /** @return the member's age */
    public int getAge() {
        return age;
    }

    /** Sets the member's age */
    public void setAge(int age) {
        this.age = age;
    }

    /** @return the member's name */
    public String getName() {
        return name;
    }

    /** Sets the member's name */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the member's billing plan */
    public PlanType getPlanType() {
        return planType;
    }

    /** Sets the member's billing plan */
    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    /** @return the schedule of registered events */
    public Schedule getRegistrations() {
        return registrations;
    }

    /**
     * Determines equality based on ID and name.
     *
     * @param m another Member to compare
     * @return true if IDs and names match
     */
    public boolean equals(Member m) {
        if (m == null)
            return false;
        return m.getId() == this.getId();
    }
}

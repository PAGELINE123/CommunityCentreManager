/** yubo */
package member;

import java.util.ArrayList;
import java.util.Comparator;

import event.Event;
import time.Schedule;

/**
 * abstract base for members: common info, billing plan, event regs.
 * subclasses must implement calculateBill() and personalInfo().
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-03
 */
public abstract class Member {
    /** monthly base fee */
    public static final double MONTHLY_BASE = 35.00;
    /** annual base fee */
    public static final double ANNUAL_BASE = 350.00;
    /** age threshold for adult membership */
    public static final int ADULT_AGE = 18;

    /** unique member id */
    protected int id;
    /** member age */
    protected int age;
    /** full name */
    protected String name;
    /** schedule of registered events */
    protected Schedule registrations;
    /** billing plan */
    protected PlanType planType;
    /** number of billing cycles */
    protected int billingCycles;

    /** available billing plans */
    public enum PlanType { MONTHLY, ANNUAL }

    /**
     * create member with age, name, plan (starts at 0 billing cycles)
     *
     * @param age       member age
     * @param name      full name
     * @param planType  billing plan
     */
    public Member(int age, String name, PlanType planType) {
        this.age = age;
        this.name = name;
        this.planType = planType;
        this.registrations = new Schedule();
        this.billingCycles = 0;
    }

    /**
     * create member with age, name, plan, and billing cycles
     *
     * @param age            member age
     * @param name           full name
     * @param planType       billing plan
     * @param billingCycles  starting billing cycles
     */
    public Member(int age, String name, PlanType planType, int billingCycles) {
        this.age = age;
        this.name = name;
        this.planType = planType;
        this.registrations = new Schedule();
        this.billingCycles = billingCycles;
    }

    /** calculate total bill (plan + subclass charges) */
    public abstract double calculateBill();

    /** return personal info string */
    public abstract String personalInfo();

    /** set billing cycles */
    public void setBillingCycles(int billingCycles) {
        this.billingCycles = billingCycles;
    }

    /** get billing cycles */
    public int getBillingCycles() {
        return billingCycles;
    }

    /** member details including calculated bill */
    @Override
    public String toString() {
        return "#" + id
             + " | Age: " + age
             + " | Name: " + name
             + " | Plan: " + planType
             + " | Gross bill: " + String.format("$%.2f", calculateBill());
    }

    /** list upcoming/ongoing events */
    public String toRegistrationString() {
        String s = "";
        if (!registrations.getEventSchedule().isEmpty()) {
            ArrayList<Event> events = new ArrayList<>();
            for (Event event : registrations.getEventSchedule()) {
                if (!event.isCompleted()) events.add(event);
            }
            events.sort(Comparator.comparingDouble(Event::hoursSinceEpoch));
            s += "\nregistered events (ongoing/future):";
            for (Event event : events) {
                s += "\n - " + event;
            }
        }
        return s;
    }

    /** register for an event */
    public void registerFor(Event event) {
        registrations.add(event);
    }

    /** get member id */
    public int getId() {
        return id;
    }

    /** set member id */
    public void setId(int id) {
        this.id = id;
    }

    /** get age */
    public int getAge() {
        return age;
    }

    /** set age */
    public void setAge(int age) {
        this.age = age;
    }

    /** get name */
    public String getName() {
        return name;
    }

    /** set name */
    public void setName(String name) {
        this.name = name;
    }

    /** get billing plan */
    public PlanType getPlanType() {
        return planType;
    }

    /** set billing plan */
    public void setPlanType(PlanType planType) {
        this.planType = planType;
    }

    /** get registrations schedule */
    public Schedule getRegistrations() {
        return registrations;
    }

    /**
     * equality based on id
     *
     * @param m another member
     * @return true if same id
     */
    public boolean equals(Member m) {
        if (m == null) return false;
        return m.getId() == this.id;
    }
}

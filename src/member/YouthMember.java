package member;

import event.Competition;
import event.Event;

/**
 * represents a youth member with a discount rate, linked to one adult guardian.
 * automatically registers as a child with its guardian upon creation.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-03
 */
public class YouthMember extends Member {
    /** discount rate for youth members */
    public static final double DISCOUNT_RATE = 0.4;

    /** guardian for this youth member */
    private AdultMember guardian;

    /**
     * constructs a youth member with basic info and links guardian
     *
     * @param age      youth member's age
     * @param name     full name
     * @param planType billing plan type
     * @param guardian adult guardian
     */
    public YouthMember(int age, String name, PlanType planType, AdultMember guardian) {
        super(age, name, planType);
        if (guardian != null) {
            this.guardian = guardian;
            guardian.addChild(this);
        }
    }

    /**
     * constructs a youth member with billing cycles and links guardian
     *
     * @param age           youth member's age
     * @param name          full name
     * @param planType      billing plan type
     * @param guardian      adult guardian
     * @param billingCycles number of billing cycles
     */
    public YouthMember(int age, String name, PlanType planType, AdultMember guardian, int billingCycles) {
        super(age, name, planType, billingCycles);
        if (guardian != null) {
            this.guardian = guardian;
            guardian.addChild(this);
        }
    }

    /**
     * calculates bill applying discount to plan and events
     *
     * @return discounted bill amount
     */
    @Override
    public double calculateBill() {
        double base = switch (planType) {
            case MONTHLY -> MONTHLY_BASE;
            case ANNUAL -> ANNUAL_BASE;
        } * (1 - DISCOUNT_RATE);

        base = base + billingCycles * base;

        for (Event event : registrations.getEventSchedule()) {
            if (event instanceof Competition c) {
                base += c.getParticipationCost() * (1 - DISCOUNT_RATE);
                if (this.equals(c.getWinner())) {
                    // prize shouldn't be discounted
                    base -= c.getPrize();
                }
            }
        }

        return base;
    }

    /** returns guardian contact info and address */
    public String personalInfo() {
        return "Personal Info (of guardian)" + " | Contact Phone: " + guardian.getContactPhone() + " | Address: "
                + guardian.getAddress();
    }

    /** returns member details with guardian name */
    @Override
    public String toString() {
        return "Youth Member " + super.toString() + " | Guardian: " + guardian.getName();
    }

    /** gets guardian */
    public AdultMember getGuardian() {
        return guardian;
    }

    /** sets a new guardian and updates child list */
    public void setGuardian(AdultMember guardian) {
        if (this.guardian != null) {
            this.guardian.getChildren().remove(this);
        }
        this.guardian = guardian;
        if (guardian != null) {
            guardian.addChild(this);
        }
    }
}

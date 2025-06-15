/**
 * Yubo
 */

package member;

import event.Competition;
import event.Event;

/**
 * Represents a youth member who pays a discounted rate and is linked
 * to exactly one adult guardian. Automatically registers itself as a
 * child with its guardian upon creation.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-03
 */
public class YouthMember extends Member {
    /**
     * The discount rate applied to youth members.
     */
    public static final double DISCOUNT_RATE = 0.4;

    /**
     * The guardian responsible for this youth member.
     */
    private AdultMember guardian;

    /**
     * Constructs a YouthMember with the given details and wires up
     * the guardian-child relationship.
     *
     * @param age      the youth member's age
     * @param name     the youth member's full name
     * @param planType the billing plan type
     * @param guardian the adult guardian of this youth
     */
    public YouthMember(int age, String name, PlanType planType, AdultMember guardian) {
        super(age, name, planType);
        if (guardian != null) {
            this.guardian = guardian;
            guardian.addChild(this);
        }
    }

    /**
     * Constructs a YouthMember with the given details and wires up
     * the guardian-child relationship.
     *
     * @param age      the youth member's age
     * @param name     the youth member's full name
     * @param planType the billing plan type
     * @param guardian the adult guardian of this youth
     */
    public YouthMember(int age, String name, PlanType planType, AdultMember guardian, int billingCycles) {
        super(age, name, planType, billingCycles);
        if (guardian != null) {
            this.guardian = guardian;
            guardian.addChild(this);
        }
    }

    /**
     * Calculates the bill for a youth member by applying the
     * discount rate to the base plan amount.
     *
     * @return the discounted bill amount
     */
    @Override
    public double calculateBill() {
        double base = switch (planType) {
            case MONTHLY -> MONTHLY_BASE;
            case ANNUAL -> ANNUAL_BASE;
        } * (1 - DISCOUNT_RATE);

        base = base + billingCycles*base;

        for (Event event : registrations.getEventSchedule()) {
            if (event instanceof Competition c) {
                base += c.getParticipationCost() * (1 - DISCOUNT_RATE);
                if (this.equals(c.getWinner())) {
                    // prize shouldnt be discounted
                    base -= c.getPrize();
                }
            }
        }

        return base;
    }

    /**
     * Returns the personal info attached
     *
     * @return the string representation
     */
    public String personalInfo() {
        return "Personal Info (of guardian)" +
                " | Contact Phone: " + guardian.getContactPhone() +
                " | Address: " + guardian.getAddress();
    }

    /**
     * `
     * Returns a string representation of this youth member's billing details.
     *
     * @return the membership details string
     */
    @Override
    public String toString() {
        return "Youth Member " + super.toString() + " | Guardian: " + guardian.getName();
    }

    /**
     * Gets this youth member's guardian.
     *
     * @return the guardian AdultMember
     */
    public AdultMember getGuardian() {
        return guardian;
    }

    /**
     * Sets a new guardian for this youth member and updates
     * the guardian's child list.
     *
     * @param guardian the new adult guardian
     */
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

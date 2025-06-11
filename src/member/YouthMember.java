/**
 * Yubo
 */

package member;

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
        this.guardian = guardian;
        guardian.addChild(this);
    }

    /**
     * Calculates the bill for a youth member by applying the
     * discount rate to the base plan amount.
     *
     * @return the discounted bill amount
     */
    @Override
    public double calculateBill() {
        double base;
        switch(planType) {
            case MONTHLY:
                base = MONTHLY_BASE;
                break;
            case ANNUAL:
                base = ANNUAL_BASE;
                break;
            default:
                base = 0.00;
                break;
        }
        return base * (1 - DISCOUNT_RATE);
    }

    /**
     * Prints the calculated bill to standard output.
     */
    @Override
    public void printBill() {
        super.printBill();
    }

    /**
     * `
     * Returns a string representation of this youth member's billing details.
     *
     * @return the membership details string
     */
    @Override
    public String toString() {
        return super.toString()+" | guardian: "+guardian.getName();
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
        this.guardian = guardian;
        guardian.addChild(this);
    }
}

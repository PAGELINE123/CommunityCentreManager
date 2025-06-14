/**
 * Yubo
 */

package staff;

/**
 * Represents a full-time staff member with a base salary and annual raises.
 * Calculates monthly pay based on years worked.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-04
 */
public class FullTimeStaff extends Staff {
    /**
     * The base annual salary for all full-time staff.
     */
    public static final double BASE_SALARY = 60000;

    /**
     * The annual raise rate applied per year worked.
     */
    public static final double YEARLY_RAISE = 0.05;

    /**
     * Number of years this staff member has worked.
     */
    private int yearsWorked;

    /**
     * Constructs a FullTimeStaff with the given name and years worked.
     *
     * @param name        the full name of the staff member
     * @param yearsWorked the number of years the staff has been employed
     */
    public FullTimeStaff(String name, int yearsWorked) {
        super(name);
        this.yearsWorked = yearsWorked;
    }

    /**
     * Calculates the monthly pay by adding raises to the base salary
     * and dividing by 12 months.
     *
     * @return the monthly salary amount
     */
    @Override
    public double calculatePay() {
        double adjustedSalary = BASE_SALARY + (YEARLY_RAISE * yearsWorked * BASE_SALARY);
        return adjustedSalary / 12;
    }

    /**
     * Returns a payroll string
     */
    @Override
    public String toPayrollString() {
        return name +
                "'s monthly pay is: " + calculatePay() +
                " | Years worked: " + yearsWorked;
    }

    /**
     * Returns a string representation of this full-time staff member.
     *
     * @return a descriptive string including ID, name, and years worked
     */
    @Override
    public String toString() {
        return "Staff #" + id +
                " | Name: " + name +
                " | Years worked: " + yearsWorked;
    }

    /**
     * Gets the number of years worked by this staff member.
     *
     * @return the years worked
     */
    public int getYearsWorked() {
        return yearsWorked;
    }

    /**
     * Sets the number of years worked by this staff member.
     *
     * @param yearsWorked the new years worked value
     */
    public void setYearsWorked(int yearsWorked) {
        this.yearsWorked = yearsWorked;
    }
}

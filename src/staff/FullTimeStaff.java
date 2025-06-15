package staff;

/**
 * represents full-time staff with base salary and yearly raises.
 * calculates monthly pay based on years worked.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-04
 */
public class FullTimeStaff extends Staff {
    /** base annual salary */
    public static final double BASE_SALARY = 60000;
    /** annual raise rate per year worked */
    public static final double YEARLY_RAISE = 0.05;
    /** years worked */
    private int yearsWorked;

    /** create staff with name and years worked */
    public FullTimeStaff(String name, int yearsWorked) {
        super(name);
        this.yearsWorked = yearsWorked;
    }

    /** calculate monthly salary */
    @Override
    public double calculatePay() {
        double adjustedSalary = BASE_SALARY + (YEARLY_RAISE * yearsWorked * BASE_SALARY);
        return adjustedSalary / 12;
    }

    /** return payroll string */
    @Override
    public String toPayrollString() {
        return name + "'s monthly pay is: " + calculatePay() + " | Years worked: " + yearsWorked;
    }

    /** return staff details */
    @Override
    public String toString() {
        return "Staff #" + id + " | Name: " + name + " | Years worked: " + yearsWorked;
    }

    /** get years worked */
    public int getYearsWorked() {
        return yearsWorked;
    }

    /** set years worked */
    public void setYearsWorked(int yearsWorked) {
        this.yearsWorked = yearsWorked;
    }
}

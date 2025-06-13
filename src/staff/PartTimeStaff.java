/**
 * Yubo
 */

package staff;

/**
 * Represents a part-time staff member who is paid by the hour.
 * Calculates monthly pay up to a maximum number of hours.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-03
 */
public class PartTimeStaff extends Staff {
    /**
     * The number of hours this staff has worked in the month.
     */
    private double hoursWorked;

    /**
     * The hourly wage for this staff.
     */
    private double hourlySalary;

    /**
     * The maximum number of hours this staff is allowed to work per month.
     */
    private int maxMonthlyHours;

    /**
     * Constructs a new PartTimeStaff with the given name and pay parameters.
     *
     * @param name            the staff member's name
     * @param hoursWorked     the initial hours worked
     * @param hourlySalary    the wage per hour
     * @param maxMonthlyHours the cap on hours per month
     */
    public PartTimeStaff(String name, double hoursWorked, double hourlySalary, int maxMonthlyHours) {
        super(name);
        this.hoursWorked = hoursWorked;
        this.hourlySalary = hourlySalary;
        this.maxMonthlyHours = maxMonthlyHours;
    }

    /**
     * Calculates the monthly pay. If hoursWorked exceeds maxMonthlyHours,
     * it is capped before calculating pay.
     *
     * @return the total pay for this month
     */
    @Override
    public double calculatePay() {
        if (hoursWorked > maxMonthlyHours) {
            hoursWorked = maxMonthlyHours;
        }
        return hoursWorked * hourlySalary;
    }

    /**
     * Prints this staff member's payroll information to stdout.
     */
    @Override
    public void printPayroll() {
        System.out.println(name +
                "'s pay is: " + calculatePay() +
                " | Hours worked: " + hoursWorked);
    }

    /**
     * Returns a string representation of this PartTimeStaff.
     *
     * @return a string containing ID, name, salary info, and max hours
     */
    public String toString() {
        return "Staff #" + id +
                " | Name: " + name +
                " | Hourly salary: " + hourlySalary +
                " | Max monthly hours: " + maxMonthlyHours;
    }

    /**
     * Gets the number of hours worked.
     *
     * @return hours worked this month
     */
    public double getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Sets the number of hours worked.
     *
     * @param hoursWorked the new hours worked value
     */
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /**
     * Gets the hourly wage.
     *
     * @return the hourly salary
     */
    public double getHourlySalary() {
        return hourlySalary;
    }

    /**
     * Sets the hourly wage.
     *
     * @param hourlySalary the new hourly salary
     */
    public void setHourlySalary(double hourlySalary) {
        this.hourlySalary = hourlySalary;
    }

    /**
     * Gets the maximum monthly hours allowed.
     *
     * @return the max monthly hours
     */
    public int getMaxMonthlyHours() {
        return maxMonthlyHours;
    }

    /**
     * Sets the maximum monthly hours allowed.
     *
     * @param maxMonthlyHours the new max monthly hours
     */
    public void setMaxMonthlyHours(int maxMonthlyHours) {
        this.maxMonthlyHours = maxMonthlyHours;
    }
}

package staff;

/**
 * represents a part-time staff member paid by the hour.
 * calculates monthly pay up to a maximum number of hours.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-03
 */
public class PartTimeStaff extends Staff {
    /** hours worked this month */
    private double hoursWorked;
    /** hourly wage */
    private double hourlySalary;
    /** max allowed hours per month */
    private int maxMonthlyHours;

    /**
     * create part-time staff with initial hours, wage, and max hours
     *
     * @param name            staff member's name
     * @param hoursWorked     initial hours worked
     * @param hourlySalary    wage per hour
     * @param maxMonthlyHours cap on hours per month
     */
    public PartTimeStaff(String name, double hoursWorked, double hourlySalary, int maxMonthlyHours) {
        super(name);
        this.hoursWorked = hoursWorked;
        this.hourlySalary = hourlySalary;
        this.maxMonthlyHours = maxMonthlyHours;
    }

    /**
     * calculate monthly pay (capped at maxMonthlyHours)
     *
     * @return total pay for this month
     */
    @Override
    public double calculatePay() {
        if (hoursWorked > maxMonthlyHours) {
            hoursWorked = maxMonthlyHours;
        }
        return hoursWorked * hourlySalary;
    }

    /** return payroll details */
    public String toPayrollString() {
        return name + "'s pay is: " + calculatePay() + " | Hours worked: " + hoursWorked;
    }

    /** return staff details */
    @Override
    public String toString() {
        return "Staff #" + id
                + " | Name: " + name
                + " | Hourly salary: " + hourlySalary
                + " | Max monthly hours: " + maxMonthlyHours;
    }

    /** get hours worked */
    public double getHoursWorked() {
        return hoursWorked;
    }

    /** set hours worked */
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /** get hourly wage */
    public double getHourlySalary() {
        return hourlySalary;
    }

    /** set hourly wage */
    public void setHourlySalary(double hourlySalary) {
        this.hourlySalary = hourlySalary;
    }

    /** get max monthly hours */
    public int getMaxMonthlyHours() {
        return maxMonthlyHours;
    }

    /** set max monthly hours */
    public void setMaxMonthlyHours(int maxMonthlyHours) {
        this.maxMonthlyHours = maxMonthlyHours;
    }
}

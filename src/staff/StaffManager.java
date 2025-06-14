/**
 * Yubo
 */

package staff;

import static java.util.Collections.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import time.TimeBlock;

/**
 * Manages a collection of Staff members: loading from file, adding new staff,
 * searching by ID, printing payrolls, finding available staff for a timeslot,
 * and printing names alphabetically.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-09
 */
public class StaffManager {
    /**
     * The list of all staff members managed by this class.
     */
    private ArrayList<Staff> staffs;

    /**
     * Default constructor for StaffManager.
     */
    public StaffManager() {
        staffs = new ArrayList<>();
    }

    /**
     * Constructs a StaffManager and loads staff data from a text file.
     * The file format is:
     * <num staff>
     * <id>
     * <type> // "fulltime" or "parttime"
     * <name>
     * [fulltime only] <yearsWorked>
     * [parttime only] <hoursWorked>
     * <hourlySalary>
     * <maxWeeklyHours>
     *
     * @param filename the path to the staff data file
     */

    public StaffManager(String filename) {
        staffs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int numStaff = Integer.parseInt(br.readLine().trim());

            for (int i = 0; i < numStaff; i++) {
                int id = Integer.parseInt(br.readLine().trim());
                String type = br.readLine().trim().toLowerCase();
                String name = br.readLine().trim();

                if (type.equals("fulltime")) {
                    int yearsWorked = Integer.parseInt(br.readLine().trim());
                    FullTimeStaff full = new FullTimeStaff(name, yearsWorked);
                    full.setId(id);
                    staffs.add(full);

                } else if (type.equals("parttime")) {
                    double hoursWorked = Double.parseDouble(br.readLine().trim());
                    double hourlyRate = Double.parseDouble(br.readLine().trim());
                    int maxWeeklyHours = Integer.parseInt(br.readLine().trim());
                    PartTimeStaff part = new PartTimeStaff(name, hoursWorked, hourlyRate, maxWeeklyHours);
                    part.setId(id);
                    staffs.add(part);
                }
            }

            br.close();
        } catch (IOException iox) {
            System.out.println("Error reading staff file: " + iox.getMessage());
        }
    }

    /**
     * saves staff to file
     * 
     * @param filepath
     */
    public void save(String filepath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write(staffs.size() + "\n");

            for (Staff staff : staffs) {
                bw.write(staff.id + "\n");
                if (staff instanceof FullTimeStaff) {
                    bw.write("fulltime\n");
                } else {
                    bw.write("parttime\n");
                }
                bw.write(staff.name + "\n");

                if (staff instanceof FullTimeStaff fts) {
                    bw.write(fts.getYearsWorked() + "\n");
                } else if (staff instanceof PartTimeStaff pts) {
                    bw.write(pts.getHoursWorked() + "\n");
                    bw.write(pts.getHourlySalary() + "\n");
                    bw.write(pts.getMaxMonthlyHours() + "\n");
                }
            }

            bw.close();
        } catch (IOException iox) {
            System.out.println("Error writing to staff file: " + iox.getMessage());
        }
    }

    /**
     * Adds a new staff member, assigning them a unique ID.
     *
     * @param staff the Staff object to add
     */
    public void addStaff(Staff staff) {
        staff.setId(generateId());
        staffs.add(staff);
    }

    /**
     * Generates the next unique staff ID by taking the last assigned ID + 1.
     *
     * @return a new unique ID
     */
    public int generateId() {
        int maxId = -1;

        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).getId() > maxId) {
                maxId = staffs.get(i).getId();
            }
        }
        return maxId + 1;
    }

    /**
     * Searches for a staff member by their ID using binary search.
     * Assumes the list is sorted by ID.
     *
     * @param id the staff ID to search for
     * @return the Staff with the matching ID, or null if not found
     */
    public Staff searchById(int id) {
        return searchByIdRecursive(id, 0, staffs.size() - 1);
    }

    /**
     * Searches for a staff member by their name
     *
     * @param name the name to search for
     * @return the Staff with the matching name, or null if not found
     */
    public Staff searchByName(String name) {
        for (Staff staff : staffs) {
            if (staff.name.equalsIgnoreCase(name)) {
                return staff;
            }
        }

        return null;
    }

    /**
     * searches for staff member by their name or id, whichever is valid
     * 
     * @param idOrName
     * @return the staff with the matching name or id
     */
    public Staff searchByIdOrName(String idOrName) {
        Staff staff = null;

        try {
            int id = Integer.parseInt(idOrName);
            staff = searchById(id);
        } catch (NumberFormatException ignored) {
            staff = searchByName(idOrName);
        }

        return staff;
    }

    /**
     * Recursive helper for binary search by ID.
     *
     * @param id   the target ID
     * @param low  the lower index
     * @param high the upper index
     * @return the matching Staff, or null if not present
     */
    private Staff searchByIdRecursive(int id, int low, int high) {
        if (low > high) {
            return null;
        }
        int mid = (low + high) / 2;
        int midId = staffs.get(mid).getId();

        if (midId == id) {
            return staffs.get(mid);
        } else if (midId > id) {
            return searchByIdRecursive(id, low, mid - 1);
        } else {
            return searchByIdRecursive(id, mid + 1, high);
        }
    }

    /**
     * Prints all staff
     * 
     * @return whether anything was printed
     */
    public boolean printAllStaff() {
        if (staffs.isEmpty()) {
            return false;
        }

        for (Staff s : staffs) {
            System.out.println(s);
        }

        return true;
    }

    /**
     * Prints the payroll information for all staff to standard output.
     * 
     * @return whether anything was printed
     */
    public boolean printAllPayrolls() {
        if (staffs.isEmpty()) {
            return false;
        }

        for (Staff s : staffs) {
            System.out.println(s.toPayrollString());
        }

        return true;
    }

    /**
     * Finds all staff members available during the given time block.
     *
     * @param block the time block to check availability against
     * @return a list of Staff who are available
     */
    public ArrayList<Staff> availableStaff(TimeBlock block) {
        ArrayList<Staff> available = new ArrayList<>();
        for (Staff s : staffs) {
            if (s.isAvailable(block)) {
                available.add(s);
            }
        }
        return available;
    }

    /**
     * Prints all staff names in alphabetical order.
     * 
     * @return whether anything was printed
     */
    public boolean printAlphabetical() {
        if (staffs.isEmpty()) {
            return false;
        }

        ArrayList<String> sortedNames = new ArrayList<>();
        for (Staff s : staffs) {
            sortedNames.add(s.getName());
        }
        sort(sortedNames);
        for (String name : sortedNames) {
            System.out.println(name);
        }

        return true;
    }

    /**
     * Pays all full-time staff
     */
    public void payFullTimeStaff() {
        for (Staff s : staffs) {
            if (s instanceof FullTimeStaff fs) {
                System.out.printf("Staff #" + fs.getId() + " " + fs.getName() + " was paid %.2f\n",
                        fs.calculatePay() * 12);
            }
        }
    }

    /**
     * Pays all part-time staff
     */
    public void payPartTimeStaff() {
        for (Staff s : staffs) {
            if (s instanceof PartTimeStaff ps) {
                System.out.printf("Staff #" + ps.getId() + " " + ps.getName() + " was paid %.2f\n", ps.calculatePay());
            }
        }
    }

    /**
     * Resets part time staff hours
     */
    public void resetPartTimeStaffHours() {
        for (Staff s : staffs) {
            if (s instanceof PartTimeStaff ps) {
                ps.setHoursWorked(0);
            }
        }
        System.out.println("Hours reset for part-time staff.");
    }

    /**
     * Increases the years worked of all full-time staff by 1
     */
    public void increaseYearsWorked() {
        for (Staff s : staffs) {
            if (s instanceof FullTimeStaff fs) {
                fs.setYearsWorked(fs.getYearsWorked() + 1);
                System.out.println("Years of experience increased for full-time staff.");
            }
        }
    }

    /**
     * Sorts the staff list by descending pay, then by ascending name.
     */
    public void sortByPayThenName() {
        sort(staffs, Comparator.comparingDouble(Staff::calculatePay).reversed().thenComparing(Staff::getName,
                String.CASE_INSENSITIVE_ORDER));
    }

    public ArrayList<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(ArrayList<Staff> staffs) {
        this.staffs = staffs;
    }

    /**
     * Removes a staff member by ID.
     */
    public boolean removeStaff(int id) {
        for (int i = 0; i < staffs.size(); i++) {
            if (staffs.get(i).getId() == id) {
                staffs.remove(i);
                return true;
            }
        }
        return false;
    }

}

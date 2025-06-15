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
 * manages staff collection: load from file, add/remove, search, print payrolls,
 * find available staff, and list names alphabetically.
 * file format: numStaff, id, type (fulltime/parttime), name,
 * then yearsWorked for fulltime or hoursWorked, hourlySalary, maxWeeklyHours for parttime.
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-09
 */
public class StaffManager {
    /** list of all staff members */
    private ArrayList<Staff> staffs;

    /** create empty staff manager */
    public StaffManager() {
        staffs = new ArrayList<>();
    }

    /**
     * create manager from file and load staff data
     *
     * @param filename staff data file path
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
     * save staff to file
     *
     * @param filepath output file path
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
     * add staff with unique id
     *
     * @param staff staff object to add
     */
    public void addStaff(Staff staff) {
        staff.setId(generateId());
        staffs.add(staff);
    }

    /** generate next unique id */
    public int generateId() {
        int maxId = -1;
        for (Staff s : staffs) {
            if (s.getId() > maxId) {
                maxId = s.getId();
            }
        }
        return maxId + 1;
    }

    /**
     * search by id with binary search
     *
     * @param id staff id
     * @return matching staff or null
     */
    public Staff searchById(int id) {
        return searchByIdRecursive(id, 0, staffs.size() - 1);
    }

    /**
     * search by name
     *
     * @param name full name
     * @return matching staff or null
     */
    public Staff searchByName(String name) {
        for (Staff s : staffs) {
            if (s.name.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * search by id or name
     *
     * @param idOrName id string or full name
     * @return matching staff or null
     */
    public Staff searchByIdOrName(String idOrName) {
        try {
            return searchById(Integer.parseInt(idOrName));
        } catch (NumberFormatException ignored) {
            return searchByName(idOrName);
        }
    }

    /**
     * recursive helper for binary search by id
     *
     * @param id   target id
     * @param low  lower index
     * @param high upper index
     * @return matching staff or null
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
     * print all staff
     *
     * @return whether printed
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
     * print all payrolls
     *
     * @return whether printed
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
     * find available staff for a time block
     *
     * @param block time block to check
     * @return list of available staff
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
     * print staff names alphabetically
     *
     * @return whether printed
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

    /** pay all full-time staff */
    public void payFullTimeStaff() {
        for (Staff s : staffs) {
            if (s instanceof FullTimeStaff fs) {
                System.out.printf("Staff #%d %s was paid %.2f\n",
                                  fs.getId(), fs.getName(), fs.calculatePay() * 12);
            }
        }
    }

    /** pay all part-time staff */
    public void payPartTimeStaff() {
        for (Staff s : staffs) {
            if (s instanceof PartTimeStaff ps) {
                System.out.printf("Staff #%d %s was paid %.2f\n",
                                  ps.getId(), ps.getName(), ps.calculatePay());
            }
        }
    }

    /** reset hours for all part-time staff */
    public void resetPartTimeStaffHours() {
        for (Staff s : staffs) {
            if (s instanceof PartTimeStaff ps) {
                ps.setHoursWorked(0);
            }
        }
        System.out.println("Hours reset for part-time staff.");
    }

    /** increase years worked for all full-time staff */
    public void increaseYearsWorked() {
        for (Staff s : staffs) {
            if (s instanceof FullTimeStaff fs) {
                fs.setYearsWorked(fs.getYearsWorked() + 1);
                System.out.println("Years of experience increased for full-time staff.");
            }
        }
    }

    /** sort by descending pay then by name */
    public void sortByPayThenName() {
        sort(staffs, Comparator.comparingDouble(Staff::calculatePay)
                             .reversed()
                             .thenComparing(Staff::getName, String.CASE_INSENSITIVE_ORDER));
    }

    /** get staff list */
    public ArrayList<Staff> getStaffs() {
        return staffs;
    }

    /**
     * set staff list
     *
     * @param staffs new staff list
     */
    public void setStaffs(ArrayList<Staff> staffs) {
        this.staffs = staffs;
    }

    /**
     * remove staff by id
     *
     * @param id staff id
     * @return true if removed
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

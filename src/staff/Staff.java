package staff;

import java.util.ArrayList;
import java.util.Comparator;
import event.Event;
import time.Schedule;
import time.TimeBlock;

/**
 * abstract base for staff: tracks id, name, and shift schedule.
 * subclasses implement calculatePay() and toPayrollString().
 *
 * @author Yubo-Zhao
 * @version 1.0
 * @since 2025-06-04
 */
public abstract class Staff {
    /** unique staff identifier */
    protected int id = 0;
    /** full name of staff member */
    protected String name;
    /** schedule of assigned shifts */
    protected Schedule shifts;

    /** create staff with name and empty shift schedule */
    public Staff(String name) {
        this.name = name;
        this.shifts = new Schedule();
    }

    /** calculate pay for current period */
    public abstract double calculatePay();

    /**
     * schedule a shift; returns false if it conflicts
     * @param event the event to schedule
     */
    public boolean scheduleShift(Event event) {
        return shifts.add(event);
    }

    /** return payroll details string */
    public abstract String toPayrollString();

    /** list ongoing/future shifts */
    public String toShiftString() {
        String s = "";
        if (!shifts.getEventSchedule().isEmpty()) {
            ArrayList<Event> events = new ArrayList<>();
            for (Event event : shifts.getEventSchedule()) {
                if (!event.isCompleted()) events.add(event);
            }
            events.sort(Comparator.comparingDouble(Event::hoursSinceEpoch));
            s += "\nevents supervising (ongoing/future):";
            for (Event event : events) {
                s += "\n - " + event;
            }
        }
        return s;
    }

    /** check availability for a time block */
    public boolean isAvailable(TimeBlock block) {
        return shifts.isBlockFree(block);
    }

    /** get staff id */
    public int getId() {
        return id;
    }

    /** set staff id */
    public void setId(int id) {
        this.id = id;
    }

    /** get staff name */
    public String getName() {
        return name;
    }

    /** set staff name */
    public void setName(String name) {
        this.name = name;
    }

    /** get shift schedule */
    public Schedule getShifts() {
        return shifts;
    }
}

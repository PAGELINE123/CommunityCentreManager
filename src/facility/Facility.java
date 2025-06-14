/**
 * Facility implements an abstract facility and associated methods
 *
 * @author Sean Yang
 * @since June 9, 2025
 */

package facility;

import java.util.ArrayList;
import java.util.Comparator;

import event.Event;
import time.Schedule;
import time.TimeBlock;

public abstract class Facility {
    protected int id;
    protected int roomNum;
    protected int maxCapacity;
    protected Schedule bookings;
    public static final double BASE_COST = 100;
    public static final double HOURLY_COST = 25;

    // constructor for a new facility
    Facility(int roomNum, int maxCapacity) {
        id = main.CommunityCentreRunner.getFacilityManager().generateId();
        this.roomNum = roomNum;
        this.maxCapacity = maxCapacity;
        bookings = new Schedule();
    }

    /**
     * abstract method to calculate cost based on a time block
     * 
     * @param timeBlock
     * @return the cost
     */
    public abstract double calcCost(TimeBlock timeBlock);

    /**
     * abstract method to calculate cost for a one hour booking
     * 
     * @return the cost
     */
    public abstract double calcCostOneHour();

    /**
     * equals method compares two facilities
     * 
     * @param other
     * @return whether the two facilities are equal
     */
    public boolean equals(Facility other) {
        return other != null && id == other.id && roomNum == other.roomNum && maxCapacity == other.maxCapacity;
    }

    // accessor for max capacity
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // accessor for room num
    public int getRoomNum() {
        return roomNum;
    }

    // accessor for id
    public int getId() {
        return id;
    }

    // accessor for schedule
    public Schedule getBookings() {
        return bookings;
    }

    // mutator for id
    public void setId(int id) {
        this.id = id;
    }

    // mutator for room num
    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    /**
     * calculates the minimum which this facility's maximum capacity can be
     * 
     * @return the minimum
     */
    public int minMaxCapacity() {
        int max = 0;

        for (Event event : bookings.getEventSchedule()) {
            max = Math.max(max, event.getRegistrants().size());
        }

        return max;
    }

    // mutator for max capacity
    public boolean setMaxCapacity(int maxCapacity) {
        if (maxCapacity >= minMaxCapacity()) {
            this.maxCapacity = maxCapacity;
            return true;
        }

        return false;
    }

    // #<id>, rm<rm#>, capacity: <capacity>
    public String toString() {
        return String.format(" #%d | Room number: %d | Capacity: %d", id, roomNum, maxCapacity);
    }

    /**
     * returns a string containing events that this facility is booked for
     * 
     * @return
     */
    public String toBookingsString() {
        String s = "";
        if (!bookings.getEventSchedule().isEmpty()) {
            ArrayList<Event> events = new ArrayList<>();

            for (Event event : bookings.getEventSchedule()) {
                if (!event.isCompleted()) {
                    events.add(event);
                }
            }

            events.sort(Comparator.comparingDouble(Event::hoursSinceEpoch));

            s += "\nBooked Events (Ongoing/Future):";

            for (Event event : events) {
                s += "\n - " + event;
            }
        }
        return s;
    }
}

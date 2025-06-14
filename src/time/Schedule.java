/**
 * Schedule implements a chronologically ordered schedule preventing conflicts
 * 
 * @author Sean Yang
 * @since June 4, 2025
 */

package time;

import java.util.ArrayList;

import event.Event;

public class Schedule {
    private ArrayList<Event> eventSchedule;

    /**
     * constructor
     */
    public Schedule() {
        eventSchedule = new ArrayList<>();
    }

    public ArrayList<Event> getEventSchedule() {
        return this.eventSchedule;
    }

    /**
     * whether this block is free within the schedule
     * 
     * @param timeBlock
     * @return whether this block does not conflict with any other time blocks
     */
    public boolean isBlockFree(TimeBlock timeBlock) {
        for (Event event : eventSchedule) {
            if (event.getTimeBlock().isConflicting(timeBlock)) {
                return false;
            }
        }

        return true;
    }

    /**
     * returns the correct position to insert a target time block using binary
     * search
     * 
     * @param target
     * @return the index to insert at
     */
    private int binarySearch(TimeBlock target) {
        if (eventSchedule.size() <= 0) {
            return 0;
        }

        return binarySearch(0, eventSchedule.size() - 1, target);
    }

    /**
     * recursive method to serach for the correct position to insert
     * 
     * @param hi     the upper bound
     * @param lo     the lower bound
     * @param target
     * @return the index
     */
    private int binarySearch(int lo, int hi, TimeBlock target) {
        if (lo >= hi) {
            return (eventSchedule.get(lo).getTimeBlock().compareToStart(target) > 0) ? lo + 1 : lo;
        }

        int mid = lo + (hi - lo) / 2;

        double comp = eventSchedule.get(mid).getTimeBlock().compareToStart(target);

        if (comp == 0) {
            return mid;
        } else if (comp > 0) {
            lo = mid + 1;
        } else {
            hi = mid;
        }

        return binarySearch(lo, hi, target);
    }

    /**
     * finds all free blocks within a time range
     * 
     * @param range
     * @return an arraylist of free blocks
     */
    public ArrayList<TimeBlock> freeBlocksWithin(TimeBlock range) {
        ArrayList<TimeBlock> blocks = new ArrayList<>();

        int lo = binarySearch(range);
        int hi = binarySearch(range.getEndBlock()) - 1;

        if (lo > hi) {
            blocks.add(new TimeBlock(range, range.getStartHour(), range.duration()));
            return blocks;
        }

        double hoursUntilLo = range.getStartBlock().hoursUntil(eventSchedule.get(lo).getTimeBlock());

        if (hoursUntilLo > 0) {
            blocks.add(new TimeBlock(range, range.getStartHour(), hoursUntilLo));
        }

        for (int i = lo; i < hi; i++) {
            TimeBlock curr = eventSchedule.get(i).getTimeBlock();
            TimeBlock next = eventSchedule.get(i + 1).getTimeBlock();

            double hoursBetween = curr.hoursUntil(next);

            if (hoursBetween > 0) {
                blocks.add(new TimeBlock(curr, curr.getEndHour(), hoursBetween));
            }
        }

        double hoursAfterHi = eventSchedule.get(hi).getTimeBlock().hoursUntil(range.getEndBlock());

        if (hoursAfterHi > 0) {
            blocks.add(new TimeBlock(range, eventSchedule.get(hi).getTimeBlock().getEndHour(), hoursAfterHi));
        }

        return blocks;
    }

    /**
     * finds all events within a time range
     * 
     * @param range
     * @return an ArrayList of events which start and end within the range
     */
    public ArrayList<Event> eventsWithin(TimeBlock range) {
        ArrayList<Event> events = new ArrayList<>();

        int lo = binarySearch(range);
        int hi = binarySearch(range.getEndBlock()) - 1;

        for (int i = lo; i <= hi; i++) {
            events.add(eventSchedule.get(i));
        }

        return events;
    }

    /**
     * adds an event to the schedule if it is free
     * 
     * @param event
     * @return whether the addition succeeded
     */
    public boolean add(Event event) {
        if (eventSchedule.isEmpty()) {
            eventSchedule.add(event);
            return true;
        }

        if (isBlockFree(event.getTimeBlock())) {
            int idx = binarySearch(event.getTimeBlock());
            eventSchedule.add(idx, event);
            return true;
        }

        return false;
    }

    /**
     * removes from an event from the schedule if it exists
     * 
     * @param event
     * @return whether it was successfully cancelled
     */
    public boolean remove(Event event) {
        if (eventSchedule.get(binarySearch(event.getTimeBlock())) == event) {
            eventSchedule.remove(event);
            return true;
        }

        return false;
    }
}

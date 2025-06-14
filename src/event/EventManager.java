/**
 * This class manages and interacts with all events in the community centre's system, past present or future
 *
 * @author Mansour Abdelsalam
 * @version 1.0
 * @since 2025-06-10 
 */

package event;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import facility.Facility;
import member.Member;
import staff.Staff;
import time.TimeBlock;
import time.TimeBlock.Month;

public class EventManager {
    // fields
    private ArrayList<Event> events;

    /**
     * Constructor for EventManager;
     * creates an EventManager.
     * Schedule events using this class.
     */
    public EventManager() {
        events = new ArrayList<>();
    }

    /**
     * Constructor for EventManager;
     * creates an EventManager with information from a text file.
     * Schedule events using this class.
     * 
     * @param filePath
     */
    public EventManager(String filePath) {
        events = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            // read event information
            int numEvents = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numEvents; i++) {
                String eventType = reader.readLine().trim().toLowerCase();

                double prizeOrGoal = Double.parseDouble(reader.readLine().trim());
                double participationCost = 0;
                if (eventType.equals("competition")) {
                    participationCost = Double.parseDouble(reader.readLine().trim());
                }

                int facilityId = Integer.parseInt(reader.readLine().trim());
                Month month = TimeBlock.Month.valueOf(reader.readLine().trim().toUpperCase());
                int day = Integer.parseInt(reader.readLine().trim());
                int year = Integer.parseInt(reader.readLine().trim());
                double startHour = Double.parseDouble(reader.readLine().trim());
                double duration = Double.parseDouble(reader.readLine().trim());
                int host_id = Integer.parseInt(reader.readLine().trim());

                Facility facility = main.CommunityCentreRunner.getFacilityManager().searchById(facilityId);
                TimeBlock timeBlock = new TimeBlock(year, month, day, startHour, duration);

                if (!timeBlock.isValid()) {
                    timeBlock = new TimeBlock(2025, Month.JUN, 1, 0);
                }

                Member host = main.CommunityCentreRunner.getMemberManager().searchById(host_id);
                Event event;
                if (eventType.equals("competition")) {
                    event = new Competition(facility, timeBlock, host, prizeOrGoal, participationCost);
                } else {
                    event = new Fundraiser(facility, timeBlock, host, prizeOrGoal);
                }

                facility.getBookings().add(event);

                if (host != null)
                    host.getRegistrations().add(event);

                // check if the event has been completed
                if (timeBlock.compareToEnd(main.CommunityCentreRunner.getTimeManager().getCurrentTime()) > 0) {
                    event.setCompleted(true);
                }

                // read staff and member information registered to the event
                int numStaffSupervising = Integer.parseInt(reader.readLine().trim());
                for (int j = 0; j < numStaffSupervising; j++) {
                    Staff staff = main.CommunityCentreRunner.getStaffManager()
                            .searchById(Integer.parseInt(reader.readLine().trim()));
                    event.assignStaff(staff);
                }

                int numParticipants = Integer.parseInt(reader.readLine().trim());
                for (int k = 0; k < numParticipants; k++) {
                    Member member = main.CommunityCentreRunner.getMemberManager()
                            .searchById(Integer.parseInt(reader.readLine().trim()));
                    event.registerParticipant(member);
                }

                book(event);
            }

            reader.close();
        } catch (IOException iox) {
            System.out.println("Error reading event file: " + iox.getMessage());
        }
    }

    // accessors
    //

    // mutators
    //

    /**
     * save
     * saves all of EventManager's information to a text file.
     * 
     * @param filePath
     */
    public void save(String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            writer.write(events.size() + "\n");
            for (Event event : events) {
                if (event instanceof Competition c) {
                    writer.write("competition\n");
                    writer.write(c.getPrize() + "\n");
                    writer.write(c.getParticipationCost() + "\n");
                } else if (event instanceof Fundraiser f) {
                    writer.write("fundraiser\n");
                    writer.write(f.getGoal() + "\n");
                }

                writer.write(event.getFacility().getId() + "\n");
                writer.write(event.getTimeBlock().getMonth() + "\n");
                writer.write(event.getTimeBlock().getDay() + "\n");
                writer.write(event.getTimeBlock().getYear() + "\n");
                writer.write(event.getTimeBlock().getStartHour() + "\n");
                writer.write(event.getTimeBlock().duration() + "\n");

                if (event.getHost() != null) {
                    writer.write(event.getHost().getId() + "\n");
                } else {
                    writer.write("-1\n"); // no host
                }

                // write staff and member ids
                writer.write(event.getSupervising().size() + "\n");
                for (int j = 0; j < event.getSupervising().size(); j++) {
                    writer.write(event.getSupervising().get(j).getId() + "\n");
                }
                writer.write(event.getRegistrants().size() + "\n");
                for (int j = 0; j < event.getRegistrants().size(); j++) {
                    writer.write(event.getRegistrants().get(j).getId() + "\n");
                }
            }

            writer.close();
        } catch (IOException iox) {
            System.out.println("Error writing to event file: " + iox.getMessage());
        }
    }

    /**
     * book
     * adds a new event to the EventManager.
     */
    public void book(Event event) {
        event.setId(generateId());
        events.add(event);
    }

    /**
     * Removes the given event from the EventManager without cleanup.
     */
    public boolean removeEvent(Event event) {
        return events.remove(event);
    }

    /**
     * generateId
     * generates a unique integer ID to be assigned to an event.
     * Only called in the book method.
     * 
     * @return the unique ID
     */
    private int generateId() {
        int maxId = -1;

        for (Event event : events) {
            maxId = Math.max(maxId, event.getId());
        }
        return maxId + 1;
    }

    /**
     * printAllEvents
     * prints all events contained in the EventManager.
     * 
     * @return whether any events were printed
     */
    public boolean printAllEvents() {
        if (events.isEmpty()) {
            return false;
        }

        for (Event event : events) {
            System.out.println(event);
        }

        return true;
    }

    /**
     * printPastEvents
     * prints all events that have completed.
     * 
     * @return whether any events were printed
     */
    public boolean printPastEvents() {
        boolean found = false;

        for (Event event : events) {
            if (event.isCompleted()) {
                found = true;
                System.out.println(event);
            }
        }

        return found;
    }

    /**
     * printFutureEvents
     * prints all events that have not passed or are ongoing.
     * 
     * @return whether any events were printed
     */
    public boolean printFutureEvents() {
        boolean found = false;

        for (Event event : events) {
            if (!event.isCompleted()) {
                if (!main.CommunityCentreRunner.getTimeManager().isOngoing(event.getTimeBlock())) {
                    found = true;
                    System.out.println(event);
                }
            }
        }

        return found;
    }

    /**
     * printFutureEventsBefore
     * prints all events that have not passed or are ongoing
     * and will start before the specified time.
     * 
     * @param time
     * @return whether any events were printed
     */
    public boolean printFutureEventsBefore(TimeBlock time) {
        boolean found = false;

        for (Event event : events) {
            // print if the event starts before the time
            if (event.occursBefore(time)) {
                found = true;
                System.out.println(event);
            }
        }

        return found;
    }

    /**
     * printOngoingEvents
     * prints all events that are currently ongoing.
     * 
     * @return whether any events were printed
     */
    public boolean printOngoingEvents() {
        boolean found = false;

        for (Event event : events) {
            if (!event.isCompleted() && main.CommunityCentreRunner.getTimeManager().isOngoing(event.getTimeBlock())) {
                found = true;
                System.out.println(event);
            }
        }

        return found;
    }

    /**
     * prints all events chronologically
     * 
     * @return whether any events were printed
     */
    public boolean printEventsChronologically() {
        if (events.isEmpty()) {
            return false;
        }

        ArrayList<Event> sorted = new ArrayList<>(events);
        sorted.sort(Comparator.comparingDouble(Event::hoursSinceEpoch));

        for (Event event : sorted) {
            System.out.println(event);
        }

        return true;
    }

    /**
     * advanceTime
     * advances the time of all events within EventManager to a certain time block.
     * Is called within TimeManager.
     * 
     * @param newTime
     */
    public void advanceTime(TimeBlock newTime) {
        System.out.println("Checking for event completion...");
        main.CommunityCentreRunner.separate();

        for (Event event : events) {
            if (!event.isCompleted()) {
                if (event.getTimeBlock().compareToEnd(newTime) > 0) {
                    System.out.println("-------- Event has completed! --------");
                    System.out.println(event);
                    System.out.println(); // blank line
                    event.setCompleted();
                }
            }
        }
    }

    /**
     * searchById
     * searches for an event by ID
     * 
     * @param id
     * @return the event with the ID, null if not found
     */
    public Event searchById(int id) {
        return searchByIdRecursive(id, 0, events.size() - 1);
    }

    /**
     * searchByIdRecursive
     * helper event for searchById recursive binary search
     * 
     * @param id
     * @param low
     * @param high
     * @return the event with the ID, null if not found
     */
    private Event searchByIdRecursive(int id, int low, int high) {
        if (low > high) {
            return null;
        }
        int mid = (low + high) / 2;
        int midId = events.get(mid).getId();
        if (midId == id) {
            return events.get(mid);
        } else {
            if (midId > id) {
                return searchByIdRecursive(id, low, mid - 1);
            } else {
                return searchByIdRecursive(id, mid + 1, high);
            }
        }
    }

    /**
     * cancelEvent
     * cancels an event given its ID
     * 
     * @param id
     * @return whether the cancellation was successful
     */
    public boolean cancelEvent(int id) {
        Event event = searchById(id);
        if (event == null)
            return false;

        events.remove(event);

        // ← copy into ArrayList
        ArrayList<Member> participants = new ArrayList<>(event.getRegistrants());
        ArrayList<Staff> staffSupervising = new ArrayList<>(event.getSupervising());

        for (Member m : participants) {
            m.getRegistrations().remove(event);
        }
        for (Staff s : staffSupervising) {
            s.getShifts().remove(event);
        }
        return true;
    }

    /**
     * prints all events which happen within the time block
     * 
     * @param timeBlock the time block to search
     * @return whether anything was printed
     */
    public boolean printEventsWithin(TimeBlock timeBlock) {
        boolean found = false;

        for (Event event : events) {
            if (event.getTimeBlock().isConflicting(timeBlock)) {
                System.out.println(event);
                found = true;
            }
        }

        return found;
    }

    // accessor for events
    public ArrayList<Event> getEvents() {
        return events;
    }
}

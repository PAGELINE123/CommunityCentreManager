/**
 * This class represents a scheduled event.
 * It contains all the functions that an event needs.
 *
 * @author Mansour Abdelsalam
 * @version 1.0
 * @since 2025-05-30
 */

package event;

import java.util.ArrayList;

import facility.Facility;
import member.Member;
import staff.Staff;
import time.TimeBlock;

public abstract class Event {
    // fields
    protected Facility facility;
    protected TimeBlock timeBlock;
    protected Member host;
    protected ArrayList<Staff> supervising = new ArrayList<>();
    protected ArrayList<Member> participants = new ArrayList<>();
    protected int id;
    protected boolean completed;

    /**
     * Constructor for Event;
     * creates an event given information.
     * Host can be null to represent no host.
     * 
     * @param facility
     * @param timeBlock
     * @param host
     */
    public Event(Facility facility, TimeBlock timeBlock, Member host) {
        this.facility = facility;
        this.timeBlock = timeBlock;
        this.host = host;

        facility.getBookings().add(this);
        if (host != null) {
            host.getRegistrations().add(this);
        }

        this.completed = false;
        // id is set within eventManager, which will generate a unique ID for the event.
    }

    // accessors
    public int getId() {
        return this.id;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public TimeBlock getTimeBlock() {
        return this.timeBlock;
    }

    public ArrayList<Staff> getSupervising() {
        return this.supervising;
    }

    public ArrayList<Member> getParticipants() {
        return this.participants;
    }

    public Facility getFacility() {
        return this.facility;
    }

    public Member getHost() {
        return this.host;
    }

    // mutators
    public void setId(int id) {
        this.id = id;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean setFacility(Facility facility) {
        if (facility.getBookings().isBlockFree(timeBlock)) {
            this.facility.getBookings().remove(this);
            this.facility = facility;
            this.facility.getBookings().add(this);
            return true;
        }

        return false;
    }

    public boolean setTimeBlock(TimeBlock timeBlock) {
        if (!facility.getBookings().isBlockFree(timeBlock)) {
            return false;
        }
        if (!host.getRegistrations().isBlockFree(timeBlock)) {
            return false;
        }
        for (Staff staff : supervising) {
            if (!staff.getShifts().isBlockFree(timeBlock)) {
                return false;
            }
        }
        for (Member member : participants) {
            if (!member.getRegistrations().isBlockFree(timeBlock)) {
                return false;
            }
        }

        this.timeBlock = timeBlock;

        return true;
    }

    public boolean setHost(Member host) {
        if (host.getRegistrations().isBlockFree(timeBlock)) {
            this.host.getRegistrations().remove(this);
            this.host = host;
            this.host.getRegistrations().add(this);
            return true;
        }

        return false;
    }

    /**
     * equals
     * determines if two events are identical
     * 
     * @param other
     * @return whether the events are identical
     */
    public boolean equals(Event other) {
        return other != null && this.id == other.id && this.timeBlock.equals(other.timeBlock)
                && this.host.equals(other.host);
    }

    /**
     * isFull
     * determines if the event's facility has reached maximum capacity.
     * 
     * @return whether the event is full
     */
    public boolean isFull() {
        return (participants.size() >= this.facility.getMaxCapacity());
    }

    /**
     * occursBefore
     * determines if the event will occur before the given time and is not ongoing.
     * 
     * @param time
     * @return
     */
    public boolean occursBefore(TimeBlock time) {
        return (!isCompleted() && time.compareToStart(getTimeBlock()) < 0
                && !main.CommunityCentreRunner.getTimeManager().isOngoing(getTimeBlock()));
    }

    /**
     * registerParticipant
     * registers a member to the event.
     * 
     * @param member
     * @return whether the member was successfully added
     */
    public boolean registerParticipant(Member member) {
        // guard clauses
        if (member == null) {
            return false;
        }

        if (participants.contains(member)) {
            return false; // already registered for this event
        }

        if (isFull()) {
            return false; // event is full
        }

        if (!member.getRegistrations().isBlockFree(timeBlock)) {
            return false; // member has a conflicting time block
        }

        // all conditions are valid for member to be added now
        participants.add(member);
        member.getRegistrations().add(this);

        return true;
    }

    /**
     * assignStaff
     * assigns a staff member to supervise the event.
     * 
     * @param staff
     * @return whether or not the staff member was successfully added
     */
    public boolean assignStaff(Staff staff) {
        // guard clauses
        if (staff == null) {
            return false;
        }

        if (supervising.contains(staff)) {
            return false; // already supervising this event
        }

        if (!staff.getShifts().isBlockFree(timeBlock)) {
            return false; // the staff has a conflicting time block
        }

        // all conditions are valid for the staff member to be added now
        supervising.add(staff);
        staff.getShifts().add(this);

        return true;
    }

    /**
     * setCompleted
     * sets the event to completed and does any necessary functions
     */
    abstract public void setCompleted();

    /*
     * toString
     */
    public String toString() {
        String s = "#" + id +
                " | Room number: " + facility.getRoomNum() +
                " | Time: " + timeBlock;
        if (host != null) {
            s += " | Host: " + host.getName();
        }
        if (completed) {
            s += " | Completed";
        }

        return s;
    }

    public String toSupervisingAndParticipatingString() {
        String s = "";
        if (!supervising.isEmpty()) {
            s += "\nStaff Supervising:";

            for (Staff staff : supervising) {
                s += "\n - " + staff.getName();
            }
        }
        if (!participants.isEmpty()) {
            s += "\nRegistered Participants:";

            for (Member member : participants) {
                s += "\n - " + member.getName();
            }
        }
        return s;
    }

    // accessor for start hour
    public double hoursSinceEpoch() {
        return timeBlock.hoursSinceEpoch();
    }
}

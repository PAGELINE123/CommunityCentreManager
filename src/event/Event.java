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
    protected ArrayList<Staff> staffSupervising = new ArrayList<Staff>();
    protected ArrayList<Member> participants = new ArrayList<Member>();
    protected int id;
    protected boolean isCompleted;

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

        this.isCompleted = false;
        // id is set within eventManager, which will generate a unique ID for the event.
    }

    // accessors
    public int getId() {
        return this.id;
    }

    public boolean hasCompleted() {
        return this.isCompleted;
    }

    public TimeBlock getTimeBlock() {
        return this.timeBlock;
    }

    public ArrayList<Staff> getStaffSupervising() {
        return this.staffSupervising;
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

    /**
     * equals
     * determines if two events are identical
     * 
     * @param other
     * @return whether or not the events are identical
     */
    public boolean equals(Event other) {
        return other != null && this.id == other.id && this.timeBlock.equals(other.timeBlock)
                && this.host.equals(other.host);
    }

    /**
     * isFull
     * determines if the event's facility has reached maximum capacity.
     * 
     * @return whether or not the event is full
     */
    public boolean isFull() {
        return (participants.size() >= this.facility.getMaxCapacity());
    }

    /**
     * registerParticipant
     * registers a member to the event.
     * 
     * @param member
     * @return whether or not the member was member was successfully added
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
        member.registerFor(this);

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

        if (staffSupervising.contains(staff)) {
            return false; // already supervising this event
        }

        if (!staff.getShifts().isBlockFree(timeBlock)) {
            return false; // the staff has a conflicting time block
        }

        // all conditions are valid for the staff member to be added now
        staffSupervising.add(staff);

        return true;
    }

    /**
     * setCompleted
     * description
     */
    abstract public void setCompleted();

    /*
     * toString
     */
    public String toString() {
        String s = "Event ID: " + id + "\nFacility #" + facility.getRoomNum() + "\nScheduled for " + timeBlock
                + "\nHeld by " + host.getName();
        return s;
    }
}

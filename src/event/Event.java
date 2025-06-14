package event;

import java.util.ArrayList;
import java.util.List;

import facility.Facility;
import member.Member;
import staff.Staff;
import time.TimeBlock;

/**
 * This class represents a scheduled event.
 * It contains all the functions that an event needs.
 *
 * @author Mansour Abdelsalam
 * @version 1.0
 * @since 2025-05-30
 */
public abstract class Event {
    // fields
    protected Facility facility;
    protected TimeBlock timeBlock;
    protected Member host;
    protected ArrayList<Staff> supervising = new ArrayList<>();
    protected ArrayList<Member> registrants = new ArrayList<>();
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

    public List<Staff> getSupervising() {
        return this.supervising;
    }

    public List<Member> getRegistrants() {
        return this.registrants;
    }

    /** Alias for getRegistrants(), so code using getParticipants() will compile. */
    public List<Member> getParticipants() {
        return this.registrants;
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
     * @return whether the events are identical
     */
    public boolean equals(Event other) {
        return other != null
            && this.id == other.id
            && this.timeBlock.equals(other.timeBlock)
            && ((this.host == null && other.host == null)
                || (this.host != null && this.host.equals(other.host)));
    }

    /**
     * isFull
     * determines if the event's facility has reached maximum capacity.
     *
     * @return whether the event is full
     */
    public boolean isFull() {
        return registrants.size() >= this.facility.getMaxCapacity();
    }

    /**
     * occursBefore
     * determines if the event will occur before the given time and is not ongoing.
     *
     * @param time
     * @return
     */
    public boolean occursBefore(TimeBlock time) {
        return (!isCompleted()
            && time.compareToStart(getTimeBlock()) < 0
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
        if (member == null) return false;
        if (registrants.contains(member)) return false;
        if (isFull()) return false;
        if (!member.getRegistrations().isBlockFree(timeBlock)) return false;

        registrants.add(member);
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
        if (staff == null) return false;
        if (supervising.contains(staff)) return false;
        if (!staff.getShifts().isBlockFree(timeBlock)) return false;

        supervising.add(staff);
        staff.getShifts().add(this);
        return true;
    }

    /**
     * setCompleted
     * abstract: implement in subclasses to do any cleanup
     */
    abstract public void setCompleted();

    /**
     * mutator method for completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        String s = "#" + id
            + " | Room number: " + facility.getRoomNum()
            + " | Time: " + timeBlock;
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
            for (Staff st : supervising) {
                s += "\n - " + st.getName();
            }
        }
        if (!registrants.isEmpty()) {
            s += "\nRegistered Participants:";
            for (Member m : registrants) {
                s += "\n - " + m.getName();
            }
        }
        return s;
    }

    /**
     * accessor for hours since epoch
     */
    public double hoursSinceEpoch() {
        return timeBlock.hoursSinceEpoch();
    }

    /**
     * mutator for facility
     */
    public boolean setFacility(Facility facility) {
        if (facility.getBookings().isBlockFree(timeBlock)) {
            this.facility.getBookings().remove(this);
            this.facility = facility;
            this.facility.getBookings().add(this);
            return true;
        }
        return false;
    }

    /**
     * mutator for time block
     */
    public boolean setTimeBlock(TimeBlock newBlock) {
        if (!facility.getBookings().isBlockFree(newBlock)) return false;
        if (host != null && !host.getRegistrations().isBlockFree(newBlock)) return false;
        for (Staff st : supervising) {
            if (!st.getShifts().isBlockFree(newBlock)) return false;
        }
        for (Member m : registrants) {
            if (!m.getRegistrations().isBlockFree(newBlock)) return false;
        }
        this.timeBlock = newBlock;
        return true;
    }

    /**
     * mutator for host
     */
    public boolean setHost(Member newHost) {
        if (newHost != null && !newHost.getRegistrations().isBlockFree(timeBlock)) {
            return false;
        }
        if (host != null) {
            host.getRegistrations().remove(this);
        }
        host = newHost;
        if (host != null) {
            host.getRegistrations().add(this);
        }
        return true;
    }
}


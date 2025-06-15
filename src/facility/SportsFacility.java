/**
 * SportsFacility implements a sports facility and associated methods
 *
 * @author Sean Yang
 * @since June 9, 2025
 */

package facility;

import time.TimeBlock;

public class SportsFacility extends Facility {
    private double rating;

    public static final double COST_PER_RATING = 20;

    /**
     * constructor for a sports facility
     * 
     * @param roomNum
     * @param maxCapacity
     * @param rating
     */
    public SportsFacility(int roomNum, int maxCapacity, double rating) {
        super(roomNum, maxCapacity);
        this.rating = rating;
    }

    /**
     * calculates the cost associated with booking a sports facility for a specified
     * time
     * 
     * @param timeBlock
     * @return a double, the cost
     */
    @Override
    public double calcCost(TimeBlock timeBlock) {
        return Facility.BASE_COST + timeBlock.duration() * Facility.HOURLY_COST + rating * COST_PER_RATING;
    }

    /**
     * calculates the cost to book this sports facility for one hour
     * 
     * @return the cost
     */
    @Override
    public double calcCostOneHour() {
        return Facility.BASE_COST + Facility.HOURLY_COST + rating * COST_PER_RATING;
    }

    // converts object to string
    @Override
    public String toString() {
        return "Sports Facility" + super.toString()
                + String.format(" | Rating: %.1f/10 | Cost to Rent (1hr): $%.2f", rating, calcCostOneHour());
    }

    // accessor for rating
    public double getRating() {
        return rating;
    }

    // accessor for rating
    public void setRating(double rating) {
        this.rating = rating;
    }
}

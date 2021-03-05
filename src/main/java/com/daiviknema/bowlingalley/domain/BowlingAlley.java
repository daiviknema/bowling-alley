package com.daiviknema.bowlingalley.domain;

import com.daiviknema.bowlingalley.constant.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Bowling alley class. This is a singleton
 *
 * @author daivik
 */
public final class BowlingAlley {

    private List<Lane> lanes;

    private static BowlingAlley bowlingAlley;

    /**
     * Constructor to initialize the alley lanes
     *
     * @param numLanes
     */
    private BowlingAlley(Integer numLanes) {
        this.lanes = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) this.lanes.add(new Lane(i));
    }

    /**
     * getInstance method to retrieve the singleton
     *
     * @return
     */
    public static BowlingAlley getInstance() {
        return getInstance(Constants.NUM_LANES);
    }

    /**
     * Get the blowing alley instance. Synchronized to make sure that multiple threads calling
     * getInstance do not cause race conditions
     *
     * @param numLanes
     * @return
     */
    public static synchronized BowlingAlley getInstance(Integer numLanes) {
        if (bowlingAlley == null) {
            bowlingAlley = new BowlingAlley(numLanes);
        }
        return bowlingAlley;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }
}

package com.daiviknema.bowlingalley.service;

import com.daiviknema.bowlingalley.domain.BowlingAlley;
import com.daiviknema.bowlingalley.domain.Lane;
import com.daiviknema.bowlingalley.exception.LaneNotFoundException;

import java.util.List;

/**
 * Service to interact with lanes
 *
 * @author daivik
 */
public class LaneService {
    /**
     * Given a lane ID, fetch the corresponding lane if it is not occupied
     *
     * @param laneId
     * @return
     * @throws LaneNotFoundException
     */
    public Lane getLaneById(final Integer laneId) throws LaneNotFoundException {
        BowlingAlley bowlingAlley = BowlingAlley.getInstance();
        List<Lane> lanes = bowlingAlley.getLanes();
        if (laneId < 0 || laneId >= lanes.size()) {
            throw new LaneNotFoundException("Invalid laneId: " + laneId);
        }
        return lanes.get(laneId);
    }
}

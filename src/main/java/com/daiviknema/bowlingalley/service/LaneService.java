package com.daiviknema.bowlingalley.service;

import com.daiviknema.bowlingalley.domain.BowlingAlley;
import com.daiviknema.bowlingalley.domain.Lane;
import com.daiviknema.bowlingalley.exception.LaneNotFoundException;

import java.util.List;

public class LaneService {
    public Lane getLaneById(final Integer laneId) throws LaneNotFoundException {
        BowlingAlley bowlingAlley = BowlingAlley.getInstance();
        List<Lane> lanes = bowlingAlley.getLanes();
        if (laneId < 0 || laneId >= lanes.size()) {
            throw new LaneNotFoundException("Invalid laneId: " + laneId);
        }
        return lanes.get(laneId);
    }
}

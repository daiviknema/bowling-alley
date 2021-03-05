package com.daiviknema.bowlingalley.domain;

import com.daiviknema.bowlingalley.constant.Constants;

import java.util.ArrayList;
import java.util.List;

public final class BowlingAlley {

    private List<Lane> lanes;

    private static BowlingAlley bowlingAlley;

    private BowlingAlley(Integer numLanes) {
        this.lanes = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) this.lanes.add(new Lane(i));
    }

    public static BowlingAlley getInstance() {
        return getInstance(Constants.NUM_LANES);
    }

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

package com.daiviknema.bowlingalley.service;

import com.daiviknema.bowlingalley.domain.BowlingAlley;
import com.daiviknema.bowlingalley.domain.Game;
import com.daiviknema.bowlingalley.domain.Lane;
import com.daiviknema.bowlingalley.domain.Player;
import com.daiviknema.bowlingalley.exception.GameInstantiationFailedException;
import com.daiviknema.bowlingalley.exception.LaneNotFoundException;
import com.daiviknema.bowlingalley.exception.LaneOccupiedException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.daiviknema.bowlingalley.constant.Constants.MAX_FRAMES;

public class BowlingAlleyService {
    private final LaneService laneService = new LaneService();

    public synchronized List<Lane> showAvailableLanes() {
        BowlingAlley bowlingAlley = BowlingAlley.getInstance();
        return bowlingAlley.getLanes().stream()
                .filter(Objects::nonNull)
                .filter(lane -> !lane.getActive())
                .collect(Collectors.toList());
    }

    public synchronized Game bookLane(final Integer laneId, final List<Player> players)
            throws LaneNotFoundException, LaneOccupiedException, GameInstantiationFailedException {
        // Step 1: Fetch the lane that we want to book
        Lane lane = this.laneService.getLaneById(laneId);

        // Step 2: Check if the lane is available
        if (lane.getActive()) throw new LaneOccupiedException("Requested lane is occupied");

        // Step 3: Build a game
        Game game =
                new Game.GameBuilder().lane(lane).players(players).maxFrames(MAX_FRAMES).build();

        // Step 4: Mark lane as active
        lane.setActive(true);

        return game;
    }
}

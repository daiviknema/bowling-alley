package com.daiviknema.bowlingalley.domain;

import com.daiviknema.bowlingalley.exception.GameInstantiationFailedException;

import java.util.*;

/**
 * Domain class representing a bowling game
 *
 * @author daivik
 */
public class Game {
    // maximum number of frames in the game (defaults to MAX_FRAMES)
    private Integer maxFrames;

    // Unique game ID
    private String gameId;

    // List of players playing the game
    private List<Player> players;

    // Lane of bowling alley on which game is being played
    private Lane lane;

    // Map maintaining the list of Frames for each player
    private Map<Player, List<Frame>> playerToFramesMap;

    // Map to maintain the frame-wise scores for all players.
    // This serves as a history of the game (ie. how the scores changed throughout the duration of
    // the game)
    private List<Map<Player, Integer>> frameWisePlayerToScoreMaps;

    private Game(GameBuilder builder) {
        this.maxFrames = builder.maxFrames;
        this.gameId = builder.gameId;
        this.players = builder.players;
        this.lane = builder.lane;
        this.playerToFramesMap = builder.playerToFramesMap;
        this.frameWisePlayerToScoreMaps = builder.frameWisePlayerToScoreMaps;
    }

    /**
     * Since Game is a complex object, we have created a builder for ease of object instantiation
     */
    public static class GameBuilder {
        private Integer maxFrames;
        private String gameId;
        private List<Player> players;
        private Lane lane;
        private Map<Player, List<Frame>> playerToFramesMap;
        private List<Map<Player, Integer>> frameWisePlayerToScoreMaps;

        public GameBuilder() {
            this.gameId = UUID.randomUUID().toString();
            this.playerToFramesMap = new HashMap<>();
            this.frameWisePlayerToScoreMaps = new ArrayList<>();
        }

        public GameBuilder maxFrames(final Integer maxFrames) {
            this.maxFrames = maxFrames;
            return this;
        }

        public GameBuilder lane(final Lane lane) {
            this.lane = lane;
            return this;
        }

        public GameBuilder players(final List<Player> players) {
            this.players = players;
            for (Player player : this.players) {
                playerToFramesMap.put(player, new ArrayList<>());
            }
            return this;
        }

        public Game build() throws GameInstantiationFailedException {
            validateGame();
            return new Game(this);
        }

        private void validateGame() throws GameInstantiationFailedException {
            if (this.lane == null || this.lane.getActive()) {
                throw new GameInstantiationFailedException(
                        "Lane is null or currently occupied. Game instantiation failed.");
            }
        }
    }

    public Integer getMaxFrames() {
        return maxFrames;
    }

    public String getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Lane getLane() {
        return lane;
    }

    public Map<Player, List<Frame>> getPlayerToFramesMap() {
        return playerToFramesMap;
    }

    public List<Map<Player, Integer>> getFrameWisePlayerToScoreMaps() {
        return frameWisePlayerToScoreMaps;
    }
}

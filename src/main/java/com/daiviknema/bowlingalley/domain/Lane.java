package com.daiviknema.bowlingalley.domain;

/**
 * Domain class representing a Lane
 *
 * @author daivik
 */
public class Lane {

    private Integer laneId; // unique ID for lane
    private Boolean isActive; // true if there is currently a game being played on this lane
    private Game activeGame; // fetch the active game. If lane is empty, then returns null

    public Lane(final Integer laneId) {
        this.laneId = laneId;
        this.isActive = false;
        this.activeGame = null;
    }

    public Integer getLaneId() {
        return laneId;
    }

    public void setLaneId(Integer laneId) {
        this.laneId = laneId;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Game getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(Game activeGame) {
        this.activeGame = activeGame;
    }
}

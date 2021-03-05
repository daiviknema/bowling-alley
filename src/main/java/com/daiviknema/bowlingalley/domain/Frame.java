package com.daiviknema.bowlingalley.domain;

import com.daiviknema.bowlingalley.exception.InvalidFrameException;

import java.util.List;

import static com.daiviknema.bowlingalley.constant.Constants.NUM_PINS;

/**
 * Domain class representing a frame
 *
 * @author daivik
 */
public class Frame {
    public Frame(
            final List<Integer> pins, final Integer additionalBall1, final Integer additionalBall2)
            throws InvalidFrameException {
        if (pins == null || pins.size() != 2) {
            throw new InvalidFrameException("Incorrect number of pins in the frame");
        }
        this.pins = pins;
        this.isStrike = (pins.get(0) == NUM_PINS);
        this.isSpare = (pins.get(0) != NUM_PINS) && (pins.get(0) + pins.get(1) == NUM_PINS);
        this.additionalBall1 = additionalBall1;
        this.additionalBall2 = additionalBall2;
    }

    // represents the number of pins knocked down by a player (list is of size two - each list
    // element represents a ball)
    private List<Integer> pins;

    // whether the frame is a strike or not
    private Boolean isStrike;

    // whether the frame is a spare or not
    private Boolean isSpare;

    // In case of the final/pre-final frame, we might need to have additional balls
    private Integer additionalBall2;

    // In case of the final/pre-final frame, we might need to have additional balls
    private Integer additionalBall1;

    public List<Integer> getPins() {
        return pins;
    }

    public void setPins(List<Integer> pins) {
        this.pins = pins;
    }

    public Boolean getStrike() {
        return isStrike;
    }

    public void setStrike(Boolean strike) {
        isStrike = strike;
    }

    public Boolean getSpare() {
        return isSpare;
    }

    public void setSpare(Boolean spare) {
        isSpare = spare;
    }

    public Integer getAdditionalBall1() {
        return additionalBall1;
    }

    public void setAdditionalBall1(Integer additionalBall1) {
        this.additionalBall1 = additionalBall1;
    }
}

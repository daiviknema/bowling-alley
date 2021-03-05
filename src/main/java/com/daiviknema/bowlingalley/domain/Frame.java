package com.daiviknema.bowlingalley.domain;

import com.daiviknema.bowlingalley.exception.InvalidFrameException;

import java.util.List;

import static com.daiviknema.bowlingalley.constant.Constants.INVALID_BALL;
import static com.daiviknema.bowlingalley.constant.Constants.NUM_PINS;

public class Frame {
    public Frame(final List<Integer> pins, final Integer additionalBall1, final Integer additionalBall2) throws InvalidFrameException {
        if (pins == null || pins.size() != 2) {
            throw new InvalidFrameException("Incorrect number of pins in the frame");
        }
        this.pins = pins;
        this.isStrike = (pins.get(0) == NUM_PINS);
        this.isSpare = (pins.get(0) != NUM_PINS) && (pins.get(0) + pins.get(1) == NUM_PINS);
        this.additionalBall1 = additionalBall1;
        this.additionalBall2 = additionalBall2;
    }

    private List<Integer> pins;
    private Boolean isStrike;
    private Boolean isSpare;
    private Integer additionalBall1;
    private Integer additionalBall2;

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

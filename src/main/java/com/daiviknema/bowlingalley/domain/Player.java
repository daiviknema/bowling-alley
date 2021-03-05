package com.daiviknema.bowlingalley.domain;

import java.util.UUID;

public class Player {
    String playerId;
    String playerName;

    public Player(final String playerName) {
        this.playerId = UUID.randomUUID().toString();
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

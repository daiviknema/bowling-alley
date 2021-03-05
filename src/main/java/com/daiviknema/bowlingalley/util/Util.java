package com.daiviknema.bowlingalley.util;

import com.daiviknema.bowlingalley.domain.Game;
import com.daiviknema.bowlingalley.domain.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class (contains methods for i/o)
 *
 * @author daivik
 */
public class Util {
    /**
     * Given a game, prints the frame-wise score for all players
     *
     * @param game
     */
    public static void printPlayerWiseScores(final Game game) {
        Map<Player, Integer> playerToCumulativeScoreMap = new HashMap<>();
        game.getPlayers().forEach(player -> playerToCumulativeScoreMap.put(player, 0));
        game.getFrameWisePlayerToScoreMaps()
                .forEach(
                        playerToScoreMap -> {
                            playerToScoreMap
                                    .keySet()
                                    .forEach(
                                            player -> {
                                                playerToCumulativeScoreMap.put(
                                                        player,
                                                        playerToCumulativeScoreMap.get(player)
                                                                + playerToScoreMap.get(player));
                                            });
                        });

        System.out.println(" ======= Game ID: " + game.getGameId() + " ======= ");
        System.out.println(
                " ======= Frame No.: " + game.getFrameWisePlayerToScoreMaps().size() + " ======= ");
        playerToCumulativeScoreMap
                .keySet()
                .forEach(
                        player -> {
                            System.out.println(
                                    "Player "
                                            + player.getPlayerName()
                                            + ": "
                                            + playerToCumulativeScoreMap.get(player));
                        });
        System.out.println(" ================= ");
        System.out.println();
    }
}

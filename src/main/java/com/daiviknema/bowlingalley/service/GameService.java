package com.daiviknema.bowlingalley.service;

import com.daiviknema.bowlingalley.domain.Frame;
import com.daiviknema.bowlingalley.domain.Game;
import com.daiviknema.bowlingalley.domain.Player;
import com.daiviknema.bowlingalley.exception.InvalidFrameException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daiviknema.bowlingalley.constant.Constants.INVALID_BALL;

public class GameService {

    private void printPlayerWiseScores(final Game game) {
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

        System.out.println(" ======= ");
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
        System.out.println(" ======= ");
    }

    private Integer getPinsForPlayerAndFrame(
            final Game game, final Player player, final Integer frameIdx) {
        final Integer pins1 =
                game.getPlayerToFramesMap().get(player).get(frameIdx).getPins().get(0)
                                == INVALID_BALL
                        ? 0
                        : game.getPlayerToFramesMap().get(player).get(frameIdx).getPins().get(0);
        final Integer pins2 =
                game.getPlayerToFramesMap().get(player).get(frameIdx).getPins().get(1)
                                == INVALID_BALL
                        ? 0
                        : game.getPlayerToFramesMap().get(player).get(frameIdx).getPins().get(1);
        return pins1 + pins2;
    }

    public void playFrame(
            final Game game,
            final Map<Player, List<Integer>> playerToPinsMap,
            final Map<Player, Integer> additionalBall1Map,
            final Map<Player, Integer> additionalBall2Map)
            throws InvalidFrameException {
        if (game == null) return;

        HashMap<Player, Integer> playerToScoreMap = new HashMap<>();
        for (Player player : game.getPlayers()) {
            // Step 0: Figure out current frame number and whether it is the last frame
            final Integer currentFrameIdx = game.getPlayerToFramesMap().get(player).size();
            final Boolean isFinalFrame = (game.getMaxFrames() - 1 == currentFrameIdx);

            // Step 1: Update playerToFramesMap
            game.getPlayerToFramesMap()
                    .get(player)
                    .add(
                            new Frame(
                                    playerToPinsMap.get(player),
                                    additionalBall1Map.get(player),
                                    additionalBall2Map.get(player)));

            // Make a note of the number of pins knocked down in ball 1 and ball 2
            final Integer ball1Pins = playerToPinsMap.get(player).get(0);
            final Boolean ball1Valid = ball1Pins != INVALID_BALL;

            final Integer ball2Pins = playerToPinsMap.get(player).get(1);
            final Boolean ball2Valid = ball2Pins != INVALID_BALL;

            // Step 2: Update the score for the current frame with the sum of pins knocked over
            playerToScoreMap.put(
                    player, (ball1Valid ? ball1Pins : 0) + (ball2Valid ? ball2Pins : 0));

            // Step 3: Update the score of previous to previous frame if it was strike/spare
            if (currentFrameIdx > 1) {
                Frame prevPrevFrame =
                        game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 2);
                Frame prevFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 1);
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);
                List<Integer> validPins = new ArrayList<>();
                if (prevFrame != null && prevFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(prevFrame.getPins().get(0));
                if (prevFrame != null && prevFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(prevFrame.getPins().get(1));
                if (currFrame != null && currFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(0));
                if (currFrame != null && currFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(1));

                Integer oldScore = getPinsForPlayerAndFrame(game, player, currentFrameIdx - 2);
                if (prevPrevFrame.getStrike()) {
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 2)
                            .put(player, oldScore + validPins.get(0) + validPins.get(1));
                } else if (prevPrevFrame.getSpare()) {
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 2)
                            .put(player, oldScore + validPins.get(0));
                }
            }

            if (currentFrameIdx > 0) {
                Frame prevFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 1);
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);
                List<Integer> validPins = new ArrayList<>();
                if (currFrame != null && currFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(0));
                if (currFrame != null && currFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(1));

                Integer oldScore = getPinsForPlayerAndFrame(game, player, currentFrameIdx - 1);
                if (prevFrame.getStrike()) {
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(
                                    player,
                                    oldScore
                                            + validPins.get(0)
                                            + (validPins.size() > 1 ? validPins.get(1) : 0));
                } else if (prevFrame.getSpare()) {
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(player, oldScore + validPins.get(0));
                }
            }

            if (isFinalFrame) {
                Frame prevFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 1);
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);
                List<Integer> validPins = new ArrayList<>();
                if (currFrame != null && currFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(0));
                if (currFrame != null && currFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(1));
                if (additionalBall1Map.get(player) != INVALID_BALL)
                    validPins.add(additionalBall1Map.get(player));

                Integer oldScore = getPinsForPlayerAndFrame(game, player, currentFrameIdx - 1);
                if (prevFrame.getStrike()) {
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(player, oldScore + validPins.get(0) + validPins.get(1));
                } else if (prevFrame.getSpare()) {
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(player, oldScore + validPins.get(0));
                }
            }

            if (isFinalFrame) {
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);
                List<Integer> validPins = new ArrayList<>();
                if (additionalBall1Map.get(player) != INVALID_BALL)
                    validPins.add(additionalBall1Map.get(player));
                if (additionalBall2Map.get(player) != INVALID_BALL)
                    validPins.add(additionalBall2Map.get(player));

                Integer oldScore = playerToScoreMap.get(player);
                if (currFrame.getStrike()) {
                    playerToScoreMap.put(player, oldScore + validPins.get(0) + validPins.get(1));
                } else if (currFrame.getSpare()) {
                    playerToScoreMap.put(player, oldScore + validPins.get(0));
                }
            }
        }
        game.getFrameWisePlayerToScoreMaps().add(playerToScoreMap);
        printPlayerWiseScores(game);
    }
}

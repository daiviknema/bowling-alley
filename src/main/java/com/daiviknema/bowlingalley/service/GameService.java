package com.daiviknema.bowlingalley.service;

import com.daiviknema.bowlingalley.domain.Frame;
import com.daiviknema.bowlingalley.domain.Game;
import com.daiviknema.bowlingalley.domain.Player;
import com.daiviknema.bowlingalley.exception.InvalidFrameException;
import com.daiviknema.bowlingalley.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daiviknema.bowlingalley.constant.Constants.INVALID_BALL;

/**
 * Service to run through games
 *
 * @author daivik
 */
public class GameService {

    /**
     * For a given frame number, computes the score of the frame without considering any bonus
     * points
     *
     * @param game
     * @param player
     * @param frameIdx
     * @return
     */
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

    /**
     * Public method to step through a game frame by frame and update player-wise scores
     *
     * @param game
     * @param playerToPinsMap
     * @param additionalBall1Map
     * @param additionalBall2Map
     * @throws InvalidFrameException
     */
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
            // Note that, ball 2 can be invalid in case of a strike
            final Integer ball1Pins = playerToPinsMap.get(player).get(0);
            final Boolean ball1Valid = ball1Pins != INVALID_BALL;

            final Integer ball2Pins = playerToPinsMap.get(player).get(1);
            final Boolean ball2Valid = ball2Pins != INVALID_BALL;

            // Step 2: Update the score for the current frame with the sum of pins knocked over
            // We will consider the bonus points (in case of strike/spare) later
            playerToScoreMap.put(
                    player, (ball1Valid ? ball1Pins : 0) + (ball2Valid ? ball2Pins : 0));

            // Step 3: Update the score of previous to previous frame in case it was strike/spare
            if (currentFrameIdx > 1) {
                Frame prevPrevFrame =
                        game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 2);
                Frame prevFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 1);
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);

                // Valid pins list will save the balls that can be counted for bonus (we need to
                // ignore invalid balls for bonus)
                List<Integer> validPins = new ArrayList<>();
                if (prevFrame != null && prevFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(prevFrame.getPins().get(0));
                if (prevFrame != null && prevFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(prevFrame.getPins().get(1));
                if (currFrame != null && currFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(0));
                if (currFrame != null && currFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(1));

                // Calculate base score of previous to previous frame (without any bonus points)
                Integer baseScoreWithoutBonus =
                        getPinsForPlayerAndFrame(game, player, currentFrameIdx - 2);

                if (prevPrevFrame.getStrike()) {
                    // add bonus points in case the previous to previous frame was a strike
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 2)
                            .put(
                                    player,
                                    baseScoreWithoutBonus + validPins.get(0) + validPins.get(1));
                } else if (prevPrevFrame.getSpare()) {
                    // add bonus points in case the previous to previous frame was a spare
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 2)
                            .put(player, baseScoreWithoutBonus + validPins.get(0));
                }
            }

            // Step 3: Update the score of previous frame in case it was strike/spare
            if (currentFrameIdx > 0) {
                Frame prevFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 1);
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);

                // Valid pins list will save the balls that can be counted for bonus (we need to
                // ignore invalid balls for bonus)
                List<Integer> validPins = new ArrayList<>();
                if (currFrame != null && currFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(0));
                if (currFrame != null && currFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(1));

                // Calculate base score of previous to previous frame (without any bonus points)
                Integer baseScoreWithoutBonus =
                        getPinsForPlayerAndFrame(game, player, currentFrameIdx - 1);
                if (prevFrame.getStrike()) {
                    // add bonus points in case the previous frame was a strike
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(
                                    player,
                                    baseScoreWithoutBonus
                                            + validPins.get(0)
                                            + (validPins.size() > 1 ? validPins.get(1) : 0));
                } else if (prevFrame.getSpare()) {
                    // add bonus points in case the previous frame was a strike
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(player, baseScoreWithoutBonus + validPins.get(0));
                }
            }

            // Step 4: handle the case of pre-final frame:
            // We need to update the preFinal frame by considering the additional balls (if
            // applicable)
            if (isFinalFrame) {
                // Update the score for prefinal frame by considering additional balls
                Frame prevFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx - 1);
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);

                // Valid pins list will save the balls that can be counted for bonus (we need to
                // ignore invalid balls for bonus)
                List<Integer> validPins = new ArrayList<>();
                if (currFrame != null && currFrame.getPins().get(0) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(0));
                if (currFrame != null && currFrame.getPins().get(1) != INVALID_BALL)
                    validPins.add(currFrame.getPins().get(1));
                if (additionalBall1Map.get(player) != INVALID_BALL)
                    validPins.add(additionalBall1Map.get(player));

                // Base score of prefinal frame (without any bonus)
                Integer baseScoreWithoutBonus =
                        getPinsForPlayerAndFrame(game, player, currentFrameIdx - 1);
                if (prevFrame.getStrike()) {
                    // in case prefinal frame is a strike
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(
                                    player,
                                    baseScoreWithoutBonus + validPins.get(0) + validPins.get(1));
                } else if (prevFrame.getSpare()) {
                    // in case prefinal frame is a spare
                    game.getFrameWisePlayerToScoreMaps()
                            .get(currentFrameIdx - 1)
                            .put(player, baseScoreWithoutBonus + validPins.get(0));
                }
            }

            // Step 5: handle the case of final frame:
            // We need to update the final frame score by considering the additional balls (if
            // applicable)
            if (isFinalFrame) {
                Frame currFrame = game.getPlayerToFramesMap().get(player).get(currentFrameIdx);
                List<Integer> validPins = new ArrayList<>();
                if (additionalBall1Map.get(player) != INVALID_BALL)
                    validPins.add(additionalBall1Map.get(player));
                if (additionalBall2Map.get(player) != INVALID_BALL)
                    validPins.add(additionalBall2Map.get(player));

                Integer baseScoreWithoutBonus = playerToScoreMap.get(player);
                if (currFrame.getStrike()) {
                    playerToScoreMap.put(
                            player, baseScoreWithoutBonus + validPins.get(0) + validPins.get(1));
                } else if (currFrame.getSpare()) {
                    playerToScoreMap.put(player, baseScoreWithoutBonus + validPins.get(0));
                }
            }
        }

        // Step 6: update the framewise scores in the game object
        game.getFrameWisePlayerToScoreMaps().add(playerToScoreMap);

        // Step 7: print player-wise scores at the end of the frame
        Util.printPlayerWiseScores(game);
    }
}

package com.daiviknema.bowlingalley;

import com.daiviknema.bowlingalley.runner.BowlingAlleyRunner;

/**
 * App class. Calls the bowling alley runner. When we receive instructions/game data over REST calls
 * or from some external source, we can easily switch out the runner for a different one
 *
 * @author daivik
 */
public class App {
    private static BowlingAlleyRunner bowlingAlleyRunner = new BowlingAlleyRunner();

    public static void main(String[] args) throws Exception {
        bowlingAlleyRunner.run();
    }
}

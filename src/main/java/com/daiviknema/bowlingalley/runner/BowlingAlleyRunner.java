package com.daiviknema.bowlingalley.runner;

import com.daiviknema.bowlingalley.domain.Game;
import com.daiviknema.bowlingalley.domain.Player;
import com.daiviknema.bowlingalley.service.BowlingAlleyService;
import com.daiviknema.bowlingalley.service.GameService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.daiviknema.bowlingalley.constant.Constants.MAX_FRAMES;

public class BowlingAlleyRunner {

    private static final File inputFile = new File("input1.txt");
    private static Scanner scanner = null;
    private final BowlingAlleyService bowlingAlleyService = new BowlingAlleyService();
    private final GameService gameService = new GameService();

    static {
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void run() throws Exception {
        List<Player> players =
                Arrays.asList(scanner.nextLine().split(" ")).stream()
                        .map(playerName -> new Player(playerName))
                        .collect(Collectors.toList());
        Integer numPlayers = players.size();

        Integer laneId = scanner.nextInt();

        Game game = bowlingAlleyService.bookLane(laneId, players);

        for (int i = 0; i < MAX_FRAMES; i++) {
            Map<Player, List<Integer>> playerToPinsMap = new HashMap<>();
            Map<Player, Integer> additionalBall1Map = new HashMap<>();
            Map<Player, Integer> additionalBall2Map = new HashMap<>();
            for (int j = 0; j < 2 * numPlayers; j += 2) {
                playerToPinsMap.put(players.get(j / 2), new ArrayList<>());
                playerToPinsMap.get(players.get(j / 2)).add(scanner.nextInt());
                playerToPinsMap.get(players.get(j / 2)).add(scanner.nextInt());
            }
            if (i == MAX_FRAMES - 1) {
                for (int j = 0; j < numPlayers; j++) {
                    additionalBall1Map.put(players.get(j), scanner.nextInt());
                    additionalBall2Map.put(players.get(j), scanner.nextInt());
                }
            }
            gameService.playFrame(game, playerToPinsMap, additionalBall1Map, additionalBall2Map);
        }
    }
}

# Bowling Alley

### Building and Running the code in this Repo:

1. You will need the JDK and maven installed on your machine. This code has been tested with OpenJDK version 11 and Maven 3.6.3 - most recent versions of JDK and Maven should be able to build and run this code without any issues
2. Steps to build:
```
cd <project root>
mvn clean install
```
3. Steps to run the code:
```
java -classpath target/classes com.daiviknema.bowlingalley.App
```

### Input file format:

You can edit the input file `input1.txt` present in the root of the project to try out different testcases. Alternatively, one could write their own runner. The format of the input file is as follows:
```
<number-of-bowling-games>
<space-separated-list-of-player-names-for-first-game>
<lane-id>
<player-1-frame-1-ball-1> <player-1-frame-1-ball2> <player-2-frame-1-ball-1> <player-2-frame-1-ball-2> ...
<player-1-frame-2-ball-1> <player-1-frame-2-ball2> <player-2-frame-2-ball-1> <player-2-frame-2-ball-2> ...
<player-1-frame-3-ball-1> <player-1-frame-3-ball2> <player-2-frame-3-ball-1> <player-2-frame-3-ball-2> ...
.
.
.
<space-separated-list-of-player-names-for-second-game>
<lane-id>
<player-1-frame-1-ball-1> <player-1-frame-1-ball2> <player-2-frame-1-ball-1> <player-2-frame-1-ball-2> ...
<player-1-frame-2-ball-1> <player-1-frame-2-ball2> <player-2-frame-2-ball-1> <player-2-frame-2-ball-2> ...
<player-1-frame-3-ball-1> <player-1-frame-3-ball2> <player-2-frame-3-ball-1> <player-2-frame-3-ball-2> ...
.
.
.
<new-line>
```

### Scope for improvement:
- There is currently no DB connectivity
- There are no unit tests currently
- I/O format is weak. There should probebly be a REST API to access the business logic layer
- Currently hasn't been tested to be thread-safe

package src.kol_2.zadnji_20;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class FootballGame {
    String homeTeam;
    String awayTeam;
    int homeGoals;
    int awayGoals;

    public FootballGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }
}

class FootballTeam {
    String name;
    int played;
    int wins;
    int draws;
    int loses;
    int goalsGiven;
    int goalsTaken;

    public FootballTeam(String name) {
        this.name = name;
        this.played = 0;
        this.wins = 0;
        this.draws = 0;
        this.loses = 0;
        this.goalsGiven = 0;
        this.goalsTaken = 0;
    }

    public String getName() {
        return name;
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLoses() {
        return loses;
    }

    public int getGoalsGiven() {
        return goalsGiven;
    }

    public int getGoalsTaken() {
        return goalsTaken;
    }

    public void updateStats(int homeGoals, int awayGoals) {
        this.played++;
        if (homeGoals > awayGoals) {
            this.wins++;
        } else if (homeGoals < awayGoals) {
            this.loses++;
        } else {
            this.draws++;
        }
        goalsGiven += homeGoals;
        goalsTaken += awayGoals;
    }

    public int calculateTotalPoints() {
        return wins * 3 + draws;
    }

    public int goalDifference() {
        return goalsGiven - goalsTaken;
    }

}

class FootballTable {

    Map<String, FootballTeam> footballTeams;
    Set<FootballGame> footballGames;

    public FootballTable() {
        this.footballTeams = new HashMap<>();
        this.footballGames = new HashSet<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        FootballGame footballGame = new FootballGame(homeTeam, awayTeam, homeGoals, awayGoals);

       footballTeams.putIfAbsent(homeTeam, new FootballTeam(homeTeam));
       footballTeams.putIfAbsent(awayTeam, new FootballTeam(awayTeam));

        FootballTeam home = footballTeams.get(homeTeam);
        FootballTeam away = footballTeams.get(awayTeam);
            home.updateStats(homeGoals, awayGoals);
            away.updateStats(awayGoals, homeGoals);
    }

    public void printTable() {
        Comparator<FootballTeam> comparator = Comparator.comparing(FootballTeam::calculateTotalPoints).reversed()
                      .thenComparing(FootballTeam::goalDifference, Comparator.reverseOrder())
                      .thenComparing(FootballTeam::getName);
        List<FootballTeam> listToPrint = footballTeams.values().stream().sorted(comparator).collect(Collectors.toList());

        for (int i = 0; i < listToPrint.size(); i++) {
            FootballTeam team = listToPrint.get(i);
            System.out.printf("%2d. %-15s%5d%5d%5d%5d%5d\n", i + 1, team.name, team.played, team.wins, team.draws, team.loses, team.calculateTotalPoints());
        }
    }

}

// 29
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                      .map(line -> line.split(";"))
                      .forEach(parts -> table.addGame(parts[0], parts[1],
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}


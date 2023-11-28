package src.kol_1.risk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Move {
    Integer[] attacker;
    Integer[] defender;

    public Move(Integer[] attacker, Integer[] defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public static Move create(String line) {
        String[] parts = line.split(";");

        String[] attMoves = parts[0].split("\\s+");
        String[] defMoves = parts[1].split("\\s+");
        Integer[] attackerNumbers = new Integer[3];
        int i = 0;
        for (String move : attMoves) {
            attackerNumbers[i++] = Integer.parseInt(move);
        }

        Integer[] defenderNumbers = new Integer[3];
        i = 0;
        for (String move : defMoves) {
            defenderNumbers[i++] = Integer.parseInt(move);
        }

        return new Move(attackerNumbers, defenderNumbers);
    }

}

class Risk {
    List<Move> moves;

    public Risk() {
        moves = new ArrayList<>();
    }

    public int processAttacksData(InputStream in) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        moves = bf.lines().map(Move::create).collect(Collectors.toList());

        List<Move> attackerWon = moves.stream().filter(it -> {
            List<Integer> sortedAttackerMoves = Arrays.stream(it.attacker).sorted().collect(Collectors.toList());
            List<Integer> sortedDefenderMoves = Arrays.stream(it.defender).sorted().collect(Collectors.toList());

            for (int i = 0; i < sortedAttackerMoves.size(); i++) {
                if (sortedDefenderMoves.get(i) >= sortedAttackerMoves.get(i)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        bf.close();
        return attackerWon.size();
    }
}

public class RiskTester {
    public static void main(String[] args) throws IOException {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}
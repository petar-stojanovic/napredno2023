package src.kol_1.quiz_trial;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

class InvalidOperationException extends Exception {

    public InvalidOperationException(char c) {
        super(String.format("%c is not allowed option for this question", c));
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Question implements Comparable<Question> {
    String text;
    int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    //TF;text;points;answer - bool
    //MC;text;points;answer - char
    public static Question createQuestion(String line) throws InvalidOperationException {
        String[] parts = line.split(";");
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        if (parts[0].equals("TF")) {
            return new TFQuestion(text, points, Boolean.parseBoolean(parts[3]));
        }

        char charAnswer = parts[3].charAt(0);
        if (!isAnswerAllowed(charAnswer)) {
            throw new InvalidOperationException(charAnswer);
        }

        return new MCQuestion(text, points, charAnswer);
    }

    public static boolean isAnswerAllowed(char answer) {
        List<Character> allowedAnswers = Arrays.asList('A', 'B', 'C', 'D', 'E');
        return allowedAnswers.contains(answer);
    }

    @Override
    public int compareTo(Question o) {
        return Integer.compare(points, o.points);
    }

    abstract Double answerQuestion(String answer);
}

class TFQuestion extends Question {
    boolean answer;

    public TFQuestion(String text, int points, boolean answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", text, points, answer);
    }

    @Override
    Double answerQuestion(String answer) {
        boolean correctAnswer = this.answer == Boolean.parseBoolean(answer);
        return (double) (correctAnswer ? points : 0);
    }
}

class MCQuestion extends Question {
    char answer;

    public MCQuestion(String text, int points, char answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", text, points, answer);
    }

    @Override
    Double answerQuestion(String answer) {
        boolean correctQuestion = this.answer == answer.charAt(0);
        return correctQuestion ? points : points * -0.2;
    }
}

class Quiz {

    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String s) {
        try {
            this.questions.add(Question.createQuestion(s));
        } catch (InvalidOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printQuiz(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);

        questions.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);

        pw.flush();
    }

    public void answerQuiz(List<String> answers, OutputStream out) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(out);

        if (answers.size() != questions.size()) {
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }

        double totalPoints = 0;
        for (int i = 0; i < questions.size(); i++) {
            double points = questions.get(i).answerQuestion(answers.get(i));
            pw.printf("%d. %.02f\n", i + 1, points);
            totalPoints += points;
        }

        pw.format("Total points: %.02f", totalPoints);

        pw.flush();
    }

}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}

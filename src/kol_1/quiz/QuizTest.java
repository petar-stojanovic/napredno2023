package src.kol_1.quiz;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

abstract class Question implements Comparable<Question> {
    String text;
    int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    abstract double answerQuestionAndGetPoints(String answer);

    @Override
    public int compareTo(Question other) {
        return Integer.compare(points, other.points);
    }
}

class TrueFalseQuestion extends Question {
    boolean answer;

    public TrueFalseQuestion(String text, int points, boolean answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    double answerQuestionAndGetPoints(String answer) {
        if (this.answer == Boolean.parseBoolean(answer)) {
            return points;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", text, points, answer);
    }
}

class MultipleChoiceQuestion extends Question {
    char answer;

    public MultipleChoiceQuestion(String text, int points, char answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    double answerQuestionAndGetPoints(String answer) {
        if (this.answer == answer.charAt(0)) {
            return points;
        }
        return points * -0.2;
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", text, points, answer);
    }
}

class InvalidOperationException extends Exception {

    public InvalidOperationException(String message) {
        super(message);
    }
}

class QuestionFactory {

    public static Question createQuestion(String questionData) throws InvalidOperationException {
        String[] parts = questionData.split(";");
        String questionType = parts[0];
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        if (parts[0].equals("TF")) {
            return new TrueFalseQuestion(text, points, Boolean.parseBoolean(parts[3]));
        }
        checkIfMCQuestionHasInvalidAnswer(parts[3]);
        return new MultipleChoiceQuestion(text, points, parts[3].charAt(0));
    }

    private static void checkIfMCQuestionHasInvalidAnswer(String answer) throws InvalidOperationException {
        List<String> allowedAnswers = Arrays.asList("A", "B", "C", "D", "E");
        if (!allowedAnswers.contains(answer)) {
            throw new InvalidOperationException(String.format("%s is not allowed option for this question", answer));
        }
    }
}

class Quiz {

    List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    void addQuestion(String questionData) throws InvalidOperationException {
        questions.add(QuestionFactory.createQuestion(questionData));
    }

    void printQuiz(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        questions.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);

        pw.flush();
    }

    void answerQuiz(List<String> answers, OutputStream os) throws InvalidOperationException {
        PrintWriter pw = new PrintWriter(os);

        if (answers.size() != questions.size()) {
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }

        double totalPoints = 0;
        for (int i = 0; i < questions.size(); i++) {
            double answeredQuestionPoints = questions.get(i).answerQuestionAndGetPoints(answers.get(i));
            totalPoints += answeredQuestionPoints;
            pw.format("%d. %.2f\n", i + 1, answeredQuestionPoints);
        }

        pw.format("Total points: %.2f\n", totalPoints);
        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
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

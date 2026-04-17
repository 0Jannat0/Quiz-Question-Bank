package com.quizbank;

import java.util.Scanner;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    //используем интерфейс QuestionStorage
    private static final QuestionStorage storage = new InMemoryQuestionStorage();

    public static void main(String[] args) {

        addSampleQuestions();

        System.out.println("=== QUIZ QUESTION BANK ===");
        System.out.println("Welcome! You have "
                + storage.size() + " sample questions.");

        while (true) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addQuestion();
                    break;
                case "2":
                    listAllQuestions();
                    break;
                case "3":
                    deleteQuestion();
                    break;
                case "4":
                    takeQuiz();
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add a new question");
        System.out.println("2. List all questions");
        System.out.println("3. Delete a question");
        System.out.println("4. Take a quiz");
        System.out.println("0. Exit");
    }

    private static void addSampleQuestions() {
        storage.add(new Question(
                "What is 2 + 2?",
                Arrays.asList("3", "4", "5"),
                1,
                Topic.MATH
        ));

        storage.add(new Question(
                "What is the capital of France?",
                Arrays.asList("London", "Berlin", "Paris", "Madrid"),
                2,
                Topic.GENERAL
        ));

        storage.add(new Question(
                "Which keyword is used to define a class in Java?",
                Arrays.asList("def", "class", "struct", "type"),
                1,
                Topic.JAVA
        ));
    }

    // Adds a new question from user input.
    private static void addQuestion() {
        System.out.println("\n--- ADD NEW QUESTION ---");

        // 1. Text
        System.out.print("Enter question text: ");
        String text = scanner.nextLine();

        // 2. Options
        System.out.print("Enter options separated by comma (e.g., Apple,Banana,Cherry): ");
        String optionsInput = scanner.nextLine();
        List<String> options = Arrays.asList(optionsInput.split(","));

        // 3. Correct answer
        System.out.print("Enter the number of the CORRECT answer (1-" + options.size() + "): ");
        int correctNumber = Integer.parseInt(scanner.nextLine());
        int correctIndex = correctNumber - 1;

        // 4. Topic
        System.out.println("Available topics:");
        for (Topic t : Topic.values()) {
            System.out.println("  - " + t.getDisplayName());
        }
        System.out.print("Enter topic: ");
        String topicInput = scanner.nextLine();
        Topic topic = Topic.fromString(topicInput);

        // 5. Create and add
        Question newQuestion = new Question(text, options, correctIndex, topic);
        storage.add(newQuestion);

        System.out.println("Question added successfully!");
    }

    // Displays all questions in the bank.
    private static void listAllQuestions() {
        List<Question> all = storage.getAll();

        System.out.println("\n--- ALL QUESTIONS (" + all.size() + ") ---");

        if (all.isEmpty()) {
            System.out.println("The question bank is empty.");
            return;
        }

        for (int i = 0; i < all.size(); i++) {
            Question q = all.get(i);
            System.out.println((i + 1) + ". " + q.getText() + " [" + q.getTopic().getDisplayName() + "]");
        }
    }

    // Deletes a question by its number in the list.
    private static void deleteQuestion() {
        List<Question> all = storage.getAll();

        if (all.isEmpty()) {
            System.out.println("\nThe question bank is empty. Nothing to delete.");
            return;
        }

        System.out.println("\n--- DELETE QUESTION ---");
        for (int i = 0; i < all.size(); i++) {
            System.out.println((i + 1) + ". " + all.get(i).getText());
        }

        System.out.print("\nEnter the number of the question to delete (or 0 to cancel): ");
        int number = Integer.parseInt(scanner.nextLine());

        if (number == 0) {
            System.out.println("Deletion cancelled.");
            return;
        }

        // storage.delete() expects 0-based index
        storage.delete(number - 1);
    }

    // Starts a quiz with all questions in random order.
    private static void takeQuiz() {
        List<Question> all = storage.getAll();

        if (all.isEmpty()) {
            System.out.println("\nNo questions available. Add some questions first!");
            return;
        }

        // Create a shuffled copy of the questions
        List<Question> quizQuestions = new ArrayList<>(all);
        Collections.shuffle(quizQuestions);

        System.out.println("\n=== QUIZ STARTED ===");
        System.out.println("Answer by entering the number of your choice.\n");

        int score = 0;

        for (int i = 0; i < quizQuestions.size(); i++) {
            Question q = quizQuestions.get(i);

            System.out.println("Q" + (i + 1) + ": " + q.getText());
            System.out.println("Topic: " + q.getTopic().getDisplayName());

            List<String> options = q.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println("  " + (j + 1) + ". " + options.get(j));
            }

            System.out.print("Your answer (1-" + options.size() + "): ");
            int userAnswer = Integer.parseInt(scanner.nextLine());

            if (q.isCorrect(userAnswer)) {
                System.out.println("Correct!\n");
                score++;
            } else {
                int correctNumber = q.getCorrectAnswerIndex() + 1;
                System.out.println("Wrong! The correct answer was: " + correctNumber + "\n");
            }
        }

        System.out.println("=== QUIZ FINISHED ===");
        System.out.println("Your score: " + score + " out of " + quizQuestions.size());

        double percentage = (double) score / quizQuestions.size() * 100;
        System.out.printf("Percentage: %.1f%%\n", percentage);
    }
}
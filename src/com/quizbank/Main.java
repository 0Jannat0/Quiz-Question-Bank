package com.quizbank;

import java.io.*;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    // Switch between storage types easily:
    // private static final QuestionStorage storage = new InMemoryQuestionStorage();
    // private static final QuestionStorage storage = new FileQuestionStorage();
    private static final QuestionStorage storage = new SQLQuestionStorage();

    public static void main(String[] args) {
        addSampleQuestionsIfEmpty();

        System.out.println("=== QUIZ QUESTION BANK ===");
        System.out.println("Welcome! You have " + storage.size() + " questions in bank.");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addQuestion();
                case "2" -> listAllQuestions();
                case "3" -> deleteQuestion();
                case "4" -> editQuestion();
                case "5" -> takeQuiz();
                case "6" -> filterByTopic();
                case "7" -> exportToCSV();
                case "8" -> importFromCSV();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add new question");
        System.out.println("2. List all questions");
        System.out.println("3. Delete a question");
        System.out.println("4. Edit a question");
        System.out.println("5. Take a quiz");
        System.out.println("6. Filter by topic");
        System.out.println("7. Export to CSV");
        System.out.println("8. Import from CSV");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }

    private static void addSampleQuestionsIfEmpty() {
        if (storage.size() == 0) {
            storage.add(new Question("What is 2+2?",
                    Arrays.asList("3", "4", "5"), 1, Topic.MATH));
            storage.add(new Question("Capital of France?",
                    Arrays.asList("London", "Berlin", "Paris", "Madrid"), 2, Topic.GENERAL));
            storage.add(new Question("Keyword for class in Java?",
                    Arrays.asList("def", "class", "struct", "type"), 1, Topic.JAVA));
        }
    }

    private static void addQuestion() {
        System.out.println("\n--- ADD QUESTION ---");
        String text = readNonEmptyString("Enter question text: ");

        List<String> options = null;
        while (options == null) {
            System.out.print("Enter options separated by commas (min 2): ");
            String input = scanner.nextLine();
            String[] parts = input.split(",");
            if (parts.length >= 2) {
                options = Arrays.asList(parts);
            } else {
                System.out.println("Please provide at least 2 options.");
            }
        }

        int correctNumber = readInt("Enter number of correct answer (1-" + options.size() + "): ", 1, options.size());
        int correctIndex = correctNumber - 1;

        System.out.println("Available topics:");
        for (Topic t : Topic.values()) {
            System.out.println("  - " + t.getDisplayName());
        }
        Topic topic = null;
        while (topic == null) {
            System.out.print("Enter topic: ");
            String t = scanner.nextLine();
            topic = Topic.fromString(t);
            if (topic == Topic.GENERAL && !t.equalsIgnoreCase("General Knowledge")) {
                System.out.println("Topic not recognized, defaulting to General Knowledge.");
            }
        }

        storage.add(new Question(text, options, correctIndex, topic));
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) return value;
                System.out.println("Enter number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    private static void listAllQuestions() {
        List<Question> all = storage.getAll();
        System.out.println("\n--- ALL QUESTIONS (" + all.size() + ") ---");
        if (all.isEmpty()) {
            System.out.println("Bank is empty.");
            return;
        }
        for (int i = 0; i < all.size(); i++) {
            Question q = all.get(i);
            System.out.printf("%d. %s [%s]%n", i + 1, q.getText(), q.getTopic().getDisplayName());
        }
    }

    private static void deleteQuestion() {
        List<Question> all = storage.getAll();
        if (all.isEmpty()) {
            System.out.println("No questions to delete.");
            return;
        }
        listAllQuestions();
        int num = readInt("Enter number to delete (0 to cancel): ", 0, all.size());
        if (num == 0) return;
        storage.delete(num - 1);
    }

    private static void editQuestion() {
        List<Question> all = storage.getAll();
        if (all.isEmpty()) {
            System.out.println("No questions to edit.");
            return;
        }
        listAllQuestions();
        int num = readInt("Enter number to edit (0 to cancel): ", 0, all.size());
        if (num == 0) return;
        Question old = all.get(num - 1);
        System.out.println("Editing: " + old.getText());

        System.out.print("New text (Enter to keep): ");
        String text = scanner.nextLine();
        if (text.trim().isEmpty()) text = old.getText();

        System.out.print("New options comma-separated (Enter to keep): ");
        String opts = scanner.nextLine();
        List<String> options = old.getOptions();
        if (!opts.trim().isEmpty()) {
            String[] parts = opts.split(",");
            if (parts.length >= 2) options = Arrays.asList(parts);
        }

        int correctIndex = old.getCorrectAnswerIndex();
        System.out.print("New correct answer number (1-" + options.size() + ", Enter to keep): ");
        String ansStr = scanner.nextLine();
        if (!ansStr.trim().isEmpty()) {
            try {
                int ans = Integer.parseInt(ansStr);
                if (ans >= 1 && ans <= options.size()) correctIndex = ans - 1;
            } catch (NumberFormatException ignored) {}
        }

        System.out.print("New topic (Enter to keep): ");
        String topicStr = scanner.nextLine();
        Topic topic = old.getTopic();
        if (!topicStr.trim().isEmpty()) topic = Topic.fromString(topicStr);

        storage.update(num - 1, new Question(text, options, correctIndex, topic));
    }

    private static void takeQuiz() {
        List<Question> all = storage.getAll();
        if (all.isEmpty()) {
            System.out.println("Add questions first.");
            return;
        }
        List<Question> quiz = new ArrayList<>(all);
        Collections.shuffle(quiz);
        int score = 0;
        System.out.println("\n=== QUIZ STARTED ===");
        for (int i = 0; i < quiz.size(); i++) {
            Question q = quiz.get(i);
            System.out.printf("%nQ%d: %s%n", i + 1, q.getText());
            List<String> opts = q.getOptions();
            for (int j = 0; j < opts.size(); j++) {
                System.out.println((j + 1) + ". " + opts.get(j));
            }
            int ans = readInt("Your answer: ", 1, opts.size());
            if (q.isCorrect(ans)) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Wrong. Correct: " + (q.getCorrectAnswerIndex() + 1));
            }
        }
        System.out.printf("%n=== QUIZ FINISHED ===%nScore: %d/%d (%.1f%%)%n",
                score, quiz.size(), 100.0 * score / quiz.size());
    }

    private static void filterByTopic() {
        System.out.println("\n--- FILTER BY TOPIC ---");
        Topic[] topics = Topic.values();
        for (int i = 0; i < topics.length; i++) {
            System.out.println((i + 1) + ". " + topics[i].getDisplayName());
        }
        int choice = readInt("Select topic: ", 1, topics.length);
        Topic selected = topics[choice - 1];
        List<Question> filtered = storage.getByTopic(selected);
        System.out.println("\nQuestions in " + selected.getDisplayName() + ":");
        if (filtered.isEmpty()) {
            System.out.println("None.");
        } else {
            for (int i = 0; i < filtered.size(); i++) {
                System.out.println((i + 1) + ". " + filtered.get(i).getText());
            }
        }
    }

    private static void exportToCSV() {
        List<Question> all = storage.getAll();
        try (PrintWriter writer = new PrintWriter("export.csv")) {
            writer.println("Text,Options,CorrectIndex,Topic");
            for (Question q : all) {
                writer.printf("\"%s\",\"%s\",%d,\"%s\"%n",
                        q.getText().replace("\"", "\"\""),
                        String.join(";", q.getOptions()),
                        q.getCorrectAnswerIndex(),
                        q.getTopic().getDisplayName());
            }
            System.out.println("Exported " + all.size() + " questions to export.csv");
        } catch (FileNotFoundException e) {
            System.err.println("Export failed: " + e.getMessage());
        }
    }

    private static void importFromCSV() {
        System.out.print("Enter CSV file name (default: import.csv): ");
        String fileName = scanner.nextLine();
        if (fileName.trim().isEmpty()) fileName = "import.csv";
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return;
        }
        int imported = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // Very simple CSV parser for this demo (assumes no commas inside quoted fields)
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                String text = parts[0].replace("\"", "");
                String[] optsArray = parts[1].replace("\"", "").split(";");
                List<String> options = Arrays.asList(optsArray);
                int correctIndex = Integer.parseInt(parts[2].trim());
                Topic topic = Topic.fromString(parts[3].replace("\"", ""));
                storage.add(new Question(text, options, correctIndex, topic));
                imported++;
            }
            System.out.println("Imported " + imported + " questions from " + fileName);
        } catch (IOException e) {
            System.err.println("Import error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("CSV format error: " + e.getMessage());
        }
    }
}
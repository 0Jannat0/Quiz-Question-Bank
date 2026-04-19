package com.quizbank;

import java.io.*;
import java.util.Arrays;
import java.util.List;

//Stores questions in a text file.
public class FileQuestionStorage extends BaseQuestionStorage {
    private static final String FILE_NAME = "questions_db.txt";

    public FileQuestionStorage() {
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing file. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                try {
                    Question q = parseLine(line);
                    questions.add(q);
                } catch (Exception e) {
                    System.err.println("Skipping invalid line " + lineNumber + ": " + line);
                }
            }
            System.out.println("Loaded " + questions.size() + " questions from " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private Question parseLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Expected 4 parts separated by |");
        }
        String text = parts[0];
        List<String> options = Arrays.asList(parts[1].split(","));
        int correctIndex = Integer.parseInt(parts[2]);
        Topic topic = Topic.fromString(parts[3]);
        return new Question(text, options, correctIndex, topic);
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Question q : questions) {
                String line = formatQuestion(q);
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Saved " + questions.size() + " questions to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    private String formatQuestion(Question q) {
        return q.getText() + "|" +
                String.join(",", q.getOptions()) + "|" +
                q.getCorrectAnswerIndex() + "|" +
                q.getTopic().getDisplayName();
    }

    @Override
    public void add(Question q) {
        questions.add(q);
        saveToFile();
        System.out.println("Question added and saved.");
    }

    @Override
    public void delete(int index) {
        validateIndex(index);
        Question removed = questions.remove(index);
        saveToFile();
        System.out.println("Deleted: " + removed.getText());
    }

    @Override
    public void update(int index, Question newQuestion) {
        validateIndex(index);
        questions.set(index, newQuestion);
        saveToFile();
        System.out.println("Question updated and saved.");
    }
}
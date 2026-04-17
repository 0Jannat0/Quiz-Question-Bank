package com.quizbank;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Stores questions in a text file.
//Data PERSISTS after program exits.

public class FileQuestionStorage implements QuestionStorage {

    private static final String FILE_NAME = "questions_db.txt";

    private final List<Question> questions;

    //Constructor - loads questions from file when object is created
    public FileQuestionStorage() {
        this.questions = new ArrayList<>();
        loadFromFile();
    }

    //Reads the file and populates the questions list
    private void loadFromFile() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No existing database file found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Question q = parseQuestion(line);
                    questions.add(q);
                } catch (Exception e) {
                    System.out.println("Skipping invalid line " + lineNumber + ": " + line);
                }
            }

            System.out.println("Loaded " + questions.size() + " question from " + FILE_NAME);


        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    //Parses a single line into a Question object
    private Question parseQuestion(String line) {

        String[] parts = line.split("\\|");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Line must have 4 parts separated by |");
        }

        String text = parts[0];

        // Options are separated by comma
        List<String> options = Arrays.asList(parts[1].split(","));

        int correctIndex = Integer.parseInt(parts[2]);

        // Topic is stored as display name, convert to Topic enum
        Topic topic = Topic.fromString(parts[3]);

        return new Question(text, options, correctIndex, topic);
    }

    //Saves all questions to the file (overwrites existing content).
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

    //Formats a Question object into a string for file storage.
    private String formatQuestion(Question q) {
        String text = q.getText();
        String options = String.join(",", q.getOptions());
        int correctIndex = q.getCorrectAnswerIndex();
        String topic = q.getTopic().getDisplayName();

        return text + "|" + options + "|" + correctIndex + "|" + topic;
    }

    //INTERFACE METHODS

    @Override
    public void add(Question q) {
        questions.add(q);
        saveToFile();  // Immediately save to disk
        System.out.println("Question added and saved to file.");
    }

    @Override
    public void delete(int index) {
        if (index >= 0 && index < questions.size()) {
            Question removed = questions.remove(index);
            saveToFile();  // Update file after deletion
            System.out.println("Deleted: " + removed.getText());
        } else {
            System.out.println("Invalid index: " + index);
        }
    }

    @Override
    public List<Question> getAll() {
        // Return a copy to protect internal list
        return new ArrayList<>(questions);
    }

    @Override
    public List<Question> getByTopic(Topic topic) {
        List<Question> result = new ArrayList<>();
        for (Question q : questions) {
            if (q.getTopic() == topic) {
                result.add(q);
            }
        }
        return result;
    }

    @Override
    public int size() {
        return questions.size();
    }
}



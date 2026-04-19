package com.quizbank;

import java.util.List;
import java.util.Objects;

//Represents a single quiz question.

public class Question {
    private String text;
    private List<String> options;
    private int correctAnswerIndex;
    private Topic topic;

    public Question(String text, List<String> options, int correctAnswerIndex, Topic topic) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        if (options == null || options.size() < 2) {
            throw new IllegalArgumentException("At least two options are required.");
        }
        if (correctAnswerIndex < 0 || correctAnswerIndex >= options.size()) {
            throw new IllegalArgumentException("Correct answer index out of bounds.");
        }
        this.text = text;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.topic = topic;
    }

    // Getters
    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public Topic getTopic() {
        return topic;
    }

    // Setters with validation
    public void setText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty.");
        }
        this.text = text;
    }

    public void setOptions(List<String> options) {
        if (options == null || options.size() < 2) {
            throw new IllegalArgumentException("At least two options are required.");
        }
        this.options = options;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        if (correctAnswerIndex < 0 || correctAnswerIndex >= options.size()) {
            throw new IllegalArgumentException("Correct answer index out of bounds.");
        }
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    //Checks if the user's answer (1-based number) is correct.
    public boolean isCorrect(int userAnswerNumber) {
        int userIndex = userAnswerNumber - 1;
        return userIndex == correctAnswerIndex;
    }

    //Displays the question and options nicely in the console.
    public void display() {
        System.out.println("Topic: " + topic.getDisplayName());
        System.out.println("Question: " + text);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }

    @Override
    public String toString() {
        return text + " (" + topic.getDisplayName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return correctAnswerIndex == question.correctAnswerIndex &&
                Objects.equals(text, question.text) &&
                Objects.equals(options, question.options) &&
                topic == question.topic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, options, correctAnswerIndex, topic);
    }
}
package com.quizbank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Stores questions in an SQLite database.

public class SQLQuestionStorage extends BaseQuestionStorage {
    private static final String DB_URL = "jdbc:sqlite:quizbank.db";

    public SQLQuestionStorage() {
        initializeDatabase();
        loadQuestions();
    }

    private void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT NOT NULL,
                options TEXT NOT NULL,
                correct_index INTEGER NOT NULL,
                topic TEXT NOT NULL
            );
            """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    private void loadQuestions() {
        questions.clear();
        String sql = "SELECT id, text, options, correct_index, topic FROM questions";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String text = rs.getString("text");
                String optionsStr = rs.getString("options");
                List<String> options = List.of(optionsStr.split(","));
                int correctIndex = rs.getInt("correct_index");
                Topic topic = Topic.fromString(rs.getString("topic"));
                questions.add(new Question(text, options, correctIndex, topic));
            }
            System.out.println("Loaded " + questions.size() + " questions from SQLite.");
        } catch (SQLException e) {
            System.err.println("Error loading questions: " + e.getMessage());
        }
    }

    @Override
    public void add(Question q) {
        String sql = "INSERT INTO questions (text, options, correct_index, topic) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, q.getText());
            pstmt.setString(2, String.join(",", q.getOptions()));
            pstmt.setInt(3, q.getCorrectAnswerIndex());
            pstmt.setString(4, q.getTopic().getDisplayName());
            pstmt.executeUpdate();
            questions.add(q);
            System.out.println("Question added to SQLite.");
        } catch (SQLException e) {
            System.err.println("Failed to add: " + e.getMessage());
        }
    }

    @Override
    public void delete(int index) {
        validateIndex(index);
        Question q = questions.get(index);
        String sql = "DELETE FROM questions WHERE text = ? AND topic = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, q.getText());
            pstmt.setString(2, q.getTopic().getDisplayName());
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                questions.remove(index);
                System.out.println("Question deleted from SQLite.");
            } else {
                System.out.println("Question not found in database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to delete: " + e.getMessage());
        }
    }

    @Override
    public void update(int index, Question newQuestion) {
        validateIndex(index);
        Question old = questions.get(index);
        String sql = "UPDATE questions SET text = ?, options = ?, correct_index = ?, topic = ? WHERE text = ? AND topic = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newQuestion.getText());
            pstmt.setString(2, String.join(",", newQuestion.getOptions()));
            pstmt.setInt(3, newQuestion.getCorrectAnswerIndex());
            pstmt.setString(4, newQuestion.getTopic().getDisplayName());
            pstmt.setString(5, old.getText());
            pstmt.setString(6, old.getTopic().getDisplayName());
            pstmt.executeUpdate();
            questions.set(index, newQuestion);
            System.out.println("Question updated in SQLite.");
        } catch (SQLException e) {
            System.err.println("Failed to update: " + e.getMessage());
        }
    }
}
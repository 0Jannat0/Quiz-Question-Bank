package com.quizbank;

import java.util.List;

//Any class that implements this interface MUST provide these methods.

public interface QuestionStorage {

     /* Adds a new question to the storage.
     q the question to add */
    void add(Question q);

    //Deletes a question by its index.
    void delete(int index);

    void update(int index, Question newQuestion);

    //Returns ALL questions in the storage.
    List<Question> getAll();

    //Returns questions filtered by topic.
    List<Question> getByTopic(Topic topic);

    int size();
}
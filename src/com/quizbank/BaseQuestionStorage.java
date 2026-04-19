package com.quizbank;

import java.util.ArrayList;
import java.util.List;

//Abstract base class providing common validation and list management.
//Concrete storage classes must implement persistence-specific logic.

public abstract class BaseQuestionStorage implements QuestionStorage {
    protected final List<Question> questions = new ArrayList<>();

    protected void validateIndex(int index) {
        if (index < 0 || index >= questions.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    @Override
    public int size() {
        return questions.size();
    }

    @Override
    public List<Question> getAll() {
        return new ArrayList<>(questions); // return copy
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
}
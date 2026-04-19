package com.quizbank;
import java.util.ArrayList;
import java.util.List;

//Stores questions in RAM. Data lost on exit.

public class InMemoryQuestionStorage extends BaseQuestionStorage {

    @Override
    public void add(Question q) {
        questions.add(q);
        System.out.println("Question added to memory.");
    }

    @Override
    public void delete(int index) {
        validateIndex(index);
        Question removed = questions.remove(index);
        System.out.println("Deleted: " + removed.getText());
    }

    @Override
    public void update(int index, Question newQuestion) {
        validateIndex(index);
        questions.set(index, newQuestion);
        System.out.println("Question updated in memory.");
    }
}
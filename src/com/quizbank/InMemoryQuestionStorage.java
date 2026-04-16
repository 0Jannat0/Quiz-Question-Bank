package com.quizbank;
import java.util.ArrayList;
import java.util.List;

//Stores questions in memory. Data is Lost when the program exits.

import java.util.ArrayList;

public class InMemoryQuestionStorage implements QuestionStorage{

    private final List<Question> questions;

    public InMemoryQuestionStorage(){
        this.questions = new ArrayList<>();
    }

    @Override
    public void add(Question q){
        questions.add(q);
        System.out.println("Question added to memory");
    }

    @Override
    public void delete(int index){
        if (index >= 0 && index < questions.size()){
            Question removed = questions.remove(index);
            System.out.println("Deleted: "+ removed.getText());
        }else{
            System.out.println("Invalid index: "+ index);
        }
    }

    @Override
    public List<Question> getAll(){
        return new ArrayList<>(questions);
    }

    @Override
    public List<Question> getByTopic(Topic topic){
        List<Question> result = new ArrayList<>();
        for (Question q: questions){
            if (q.getTopic() == topic){
                result.add(q);
            }
        }
        return result;
    }

    @Override
    public int size(){
        return questions.size();
    }
}
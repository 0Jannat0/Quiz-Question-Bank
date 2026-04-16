package com.quizbank;
import java.util.List;
import java.util.Objects;

public class Question {

    private String text;
    private List<String> options;
    private int correctAnswerIndex;
    private Topic topic;

    public Question(String text, List<String> options, int correctAnswerIndex, Topic topic){
        this.text = text;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.topic = topic;
    }

    public String getText(){
        return text;
    }

    public List<String> getOptions(){
        return options;
    }

    public int getCorrectAnswerIndex(){
        return correctAnswerIndex;
    }

    public Topic getTopic(){
        return topic;
    }


    public boolean isCorrect(int userAnswer){
        int userIndex = userAnswer - 1;
        return userIndex == this.correctAnswerIndex;
    }

    public void display(){
        System.out.println("Topic: "+topic);
        System.out.println("Question: "+text);
        System.out.println("Options:");
        for (int i = 0; i < options.size(); i++){
            System.out.println(" "+(i+1)+". "+options.get(i));
        }
    }

    @Override
    public String toString(){
        return text + " ("+topic+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return correctAnswerIndex == question.correctAnswerIndex &&
                Objects.equals(text, question.text) &&
                Objects.equals(options, question.options) &&
                Objects.equals(topic, question.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, options, correctAnswerIndex, topic);
    }




}

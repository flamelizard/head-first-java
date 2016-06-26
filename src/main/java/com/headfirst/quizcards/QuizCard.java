package com.headfirst.quizcards;

import java.io.Serializable;

/**
 * Created by Tom on 6/19/2016.
 */
public class QuizCard implements Serializable {

    private String question;
    private String answer;

    public QuizCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString() {
        return question + " -- " + answer;
    }
}

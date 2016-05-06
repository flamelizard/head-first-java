package com.headfirst.student.grading;

/**
 * Created by Tom on 3/28/2016.
 */
public class Undergraduate extends Student {
    public Undergraduate(String name) {
        super(name);
    }

    public void computeCourseGrade() {
        int accum = 0;
        for (int grade: getGrades()) {
            accum += grade;
        }
        courseGrade = (accum/getGrades().size()) >= 70 ? "Pass" : "Fail";
    }
}

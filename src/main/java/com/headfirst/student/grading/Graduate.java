package com.headfirst.student.grading;

/**
 * Created by Tom on 3/28/2016.
 */
public class Graduate extends Student {
    public Graduate(String name) {
        super(name);
    }

    public void computeCourseGrade() {
        int accum = 0;
        for (int grade: getGrades()) {
            accum += grade;
        }
        courseGrade = (accum/getGrades().size()) >= 80 ? "Pass" : "Fail";
    }
}

package com.headfirst.student.grading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 3/28/2016.
 */
public abstract class Student {
    private String name;
    private List<Integer> grades = new ArrayList<>();
    protected String courseGrade = "---";

    public Student(String name) {
        this.name = name;
    }
    public void addGrade(int grade) {
        grades.add(grade);
    }
    public void addGrade(int[] grades) {
        for (int grade: grades) {
            this.grades.add(grade);
        }
    }
    public List<Integer> getGrades() {
        return grades;
    }
    public String getCourseGrade() {
        return courseGrade;
    }

    abstract public void computeCourseGrade();

    public String toString() {
        return String.format("%s: %s", getName(), grades);
    }

    public String getName() {
        return name;
    }
}

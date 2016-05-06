package com.headfirst.student;

import com.headfirst.student.grading.Graduate;
import com.headfirst.student.grading.Student;
import com.headfirst.student.grading.Undergraduate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Tom on 4/7/2016.
 */
public class TeacherCalc {
//    List<Student> students = new ArrayList<>();
    URI scoresFile;

    public TeacherCalc() {
        try {
            scoresFile = getClass().getResource("/studentGrades.txt").toURI();
        }catch (URISyntaxException e) {}
    }

    public static void main(String[] args) throws IOException{
        TeacherCalc cal = new TeacherCalc();
        cal.printGrades();
    }

/*
Student file has format: U|G first last score1 score2
where U is undergraduate
*/
public List<Student> readScoreFromFile(URI file) throws IOException {
        List<Student> students = new ArrayList<>();
        Scanner scan = new Scanner(new File(file));

        String token;
        Student stud = null;
        while (scan.hasNext()) {
            token = scan.next();
            if (token.equals("U")) {
                String name = scan.next() + " " + scan.next();
                stud = new Undergraduate(name);
            } else if (token.equals("G")) {
                String name = scan.next() + " " + scan.next();
                stud = new Graduate(name);
            } else {
                continue;
            }

            while(scan.hasNextInt()) {
                stud.addGrade(scan.nextInt());
            }
            students.add(stud);
            stud = null;
        }
        System.out.println("[students] " + students);
        return students;
    }

    public void printGrades() throws IOException{
        System.out.println("== Student's results ==");
        for (Student stud: readScoreFromFile(scoresFile)) {
            stud.computeCourseGrade();
            System.out.printf("%s - %s\n", stud.getName(),
                    stud.getCourseGrade());
        }
    }


}

package com.headfirst.student.grading;

import com.headfirst.student.TeacherCalc;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.junit.Assert.assertThat;

/**
 * Created by Tom on 3/28/2016.
 */
public class TeacherCalcTest {
    List<Student> students = new ArrayList<>();
    String testFile = "/studentGrades.txt";
    URI gradesFile;

    @Before
    public void buildStudents() throws IOException, URISyntaxException {
        gradesFile = this.getClass().getResource(testFile).toURI();
        TeacherCalc calc = new TeacherCalc();
//        System.out.println("[file] " + gradesFile);
        students = calc.readScoreFromFile(gradesFile);
    }

    @Test
    public void studentScoreCheck() {
        assertThat("Data file has records for 3 students",
                students, hasSize(3));

        assertThat("Student is parent class",
                students, everyItem(instanceOf(Student.class)));

        for (Student stud: students) {
            stud.computeCourseGrade();
            System.out.println(String.format("%s [ %s ]", stud.getName(),
                    stud.getCourseGrade()));
        }

    }

    @Test
    public void studentDataParsing() {
        Student stud = students.get(0);
        assertThat(stud, is(Matchers.instanceOf(Undergraduate.class)));
        assertThat(stud.getName(), is("mike smith"));
        System.out.println(stud.getGrades());
        assertThat(stud.getGrades(), Matchers.contains(50, 60, 80));

        stud = students.get(2);
        assertThat(stud, is(Matchers.instanceOf(Graduate.class)));
//        shows overloaded method "contains"
        assertThat(stud.getGrades(),
                contains(Arrays.asList(is(40), is(40), is(70))));
    }
}

class GradeGenerator {
    Random rand = new Random();
    final int max = 100;

    int[] generate() {
        return generate(3);
    }
    int[] generate(int len) {
        int[] grades = new int[len];
        for (int i = 0; i < len; i++) {
            grades[i] = rand.nextInt(max + 1);
        }
        return grades;
    }
}

package org.pg4200.exam2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

class Program {
    public String programName;
    public ArrayList<Course> courses;
    public ArrayList<Course> getCourses(){
        return courses;
    }
}

class Course {
    public String courseName;
    public String courseCode;
    public HashMap<Integer, Student> students;
}

class Student {
    public String firstName;
    public String lastName;
    public Integer studentId;
    public Student(String firstName, String lastName, Integer studentId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
    }
}

public class Ex04 {


    public ArrayList<String> ex04 (Program program) {
        ArrayList<Course> courses = program.getCourses();

        return courses.stream()
                .flatMap(s -> s.students.entrySet().stream())
                .map(s -> s.getValue())
                .distinct()
                .map(s -> s.firstName.substring(0, 1)
                        + s.lastName.substring(0, 1)
                        + s.studentId.toString().substring(s.studentId.toString().length()-2)
                        + "@hk.no")
                .collect(Collectors.toCollection(ArrayList::new))
                ;
    }

}

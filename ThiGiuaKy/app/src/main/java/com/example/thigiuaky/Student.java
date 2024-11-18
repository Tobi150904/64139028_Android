package com.example.thigiuaky;

public class Student {
    private String name;
    private int age;
    private String studentClass;
    private String skills;

    public Student(String name, int age, String studentClass, String skills) {
        this.name = name;
        this.age = age;
        this.studentClass = studentClass;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getSkills() {
        return skills;
    }
}
package com.yomplex.tests.model;

public class Course {

    private String coursename;
    private int courseexist;


    public Course(){

    }

    public Course(String coursename, int courseexist) {
        this.coursename = coursename;
        this.courseexist = courseexist;

    }
    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public int getCourseexist() {
        return courseexist;
    }

    public void setCourseexist(int courseexist) {
        this.courseexist = courseexist;
    }
}

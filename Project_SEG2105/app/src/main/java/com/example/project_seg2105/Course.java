package com.example.project_seg2105;

public class Course {
    private String courseName;
    public String courseCode;
    private String id;

    public Course(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Course(String id, String courseCode, String courseName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.id = id;
    }

    public Course(String courseCode, String courseName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }
}

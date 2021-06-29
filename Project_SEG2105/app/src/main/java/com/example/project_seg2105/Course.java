package com.example.project_seg2105;

public class Course {
    public String courseName;
    public String courseCode;
    public String courseDes;

    public String getCourseDes() {
        return courseDes;
    }

    public void setCourseDes(String courseDes) {
        this.courseDes = courseDes;
    }

    public String id;
    public String iName;

    public Course(String courseName, String courseCode, String iName, String day, String time, String capacity, String courseDes) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.iName = iName;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.courseDes = courseDes;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String day;
    public String time;
    public String capacity;

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public Course(String id, String courseName, String courseCode, String iName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.id = id;
        this.iName = iName;
    }

    public Course(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Course(String courseName, String courseCode, String iName) {
        this.iName = iName;
        this.courseCode = courseCode;
        this.courseName = courseName;
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

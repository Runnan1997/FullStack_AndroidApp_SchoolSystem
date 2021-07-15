package com.example.project_seg2105;

public class CourseForStudent {
    public String courseName;

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

    public String getCourseDes() {
        return courseDes;
    }

    public void setCourseDes(String courseDes) {
        this.courseDes = courseDes;
    }

    public String day;
    public String time;
    public String capacity;
    public String courseDes;

    public CourseForStudent(String id, String courseCode, String sName) {
        this.id = id;
        this.courseCode = courseCode;
        this.sName = sName;
    }

    public String id;
    public String courseCode;
    public String sName;


    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public String iName;

    public CourseForStudent(String courseName, String courseCode, String sName, String iName, String studentday, String studenttime, String day, String time, String capacity, String courseDes) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.sName = sName;
        this.iName = iName;
        this.studentday = studentday;
        this.studenttime = studenttime;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.courseDes = courseDes;
    }

    public CourseForStudent(String courseName, String courseCode, String sName, String iName, String day, String time, String capacity, String courseDes) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.sName = sName;
        this.iName = iName;
        this.day = day;
        this.time = time;
        this.capacity = capacity;
        this.courseDes = courseDes;
    }

    public CourseForStudent(String studentday){
        this.studentday = studentday;
    }

    public CourseForStudent(){}

    public CourseForStudent(String courseCode, String courseName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public CourseForStudent(String courseName, String courseCode, String sName, String iName) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.sName = sName;
        this.iName = iName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getstudentday() {
        return studentday;
    }

    public void setstudentday(String studentday) {
        this.studentday = studentday;
    }

    public String getstudenttime() {
        return studenttime;
    }

    public void setstudenttime(String studenttime) {
        this.studenttime = studenttime;
    }


    public String studentday;
    public String studenttime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

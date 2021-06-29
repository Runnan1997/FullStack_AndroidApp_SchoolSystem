package com.example.project_seg2105;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void coursegetCourseName_isCorrect() {
        Course c = new Course("ITI1100", "Digital System");
        assertEquals("Digital System", c.getCourseName());
    }

    @Test
    public void coursegetCourseCode_isCorrect() {
        Course c = new Course("ITI1100", "Digital System");
        assertEquals("ITI1100", c.getCourseCode());
    }

    @Test
    public void matchCourseNameformat_True() {
        assertTrue(searchCourse.matchformat("History"));
    }

    @Test
    public void matchCourseNameformat_False() {
        assertFalse(searchCourse.matchformat("1234ERv"));
    }

    @Test
    public void emailValidaiton_isTrue() {
        assertTrue(SignUp.emailValidaiton("rguo100@gmail.com"));
    }
}
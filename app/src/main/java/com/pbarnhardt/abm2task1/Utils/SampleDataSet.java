package com.pbarnhardt.abm2task1.Utils;

import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.Status;
import com.pbarnhardt.abm2task1.Enums.Types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleDataSet {
    /**
     * Placeholder strings.
     * Constants for string values in the sample.
     */
    private static final String SAMPLE_TITLE = "Winter 2023";
    private static final String SAMPLE_COURSE_TITLE = "C196: Mobile Application Development";
    private static final String SAMPLE_COURSE_DESCRIPTION = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
    private static final String SAMPLE_COURSE_NOTE = "This is a note!";
    private static final String SAMPLE_ASSESSMENT_TITLE = "ABM2 Task 1 - Student Tracker";
    private static final String SAMPLE_ASSESSMENT_DESCRIPTION = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
    private static final String SAMPLE_MENTOR_NAME = "Bart Simpson";
    private static final String SAMPLE_MENTOR_EMAIL = "bsimpson@email.edu";
    private static final String SAMPLE_MENTOR_PHONE = "111-867-5309";

    /**
     * Gets date/time for sample data.
     */
    private static Date getDate(int diff) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, diff);
        return cal.getTime();
    }

    /**
     * Sets sample term.
     */
    public static List<Terms> getTerms() {
        List<Terms> term = new ArrayList<>();
        term.add(new Terms(1, SAMPLE_TITLE, getDate(0), (getDate(6))));
        return term;
    }

    /**
     * Sets sample course.
     */
    public static List<Courses> getCourses() {
        List<Courses> course = new ArrayList<>();
        course.add(new Courses(1, SAMPLE_COURSE_TITLE, SAMPLE_COURSE_DESCRIPTION, false, getDate(0),true, getDate(3), Status.PROGRESS, SAMPLE_COURSE_NOTE, 1));
        return course;
    }

    /**
     * Sets sample assessment.
     */
    public static List<Assessments> getAssessments() {
        List<Assessments> assessment = new ArrayList<>();
        assessment.add(new Assessments(1, SAMPLE_ASSESSMENT_TITLE, Types.OBJECTIVE, SAMPLE_ASSESSMENT_DESCRIPTION, getDate(0), getDate(3), false, true, 1));
        return assessment;
    }

    /**
     * Sets sample mentor.
     */
    public static List<Mentors> getMentors() {
        List<Mentors> mentor = new ArrayList<>();
        mentor.add(new Mentors(1, SAMPLE_MENTOR_NAME, SAMPLE_MENTOR_PHONE, SAMPLE_MENTOR_EMAIL, 1));
        return mentor;
    }
}

package com.pbarnhardt.abm2task1.Utils;

public class Constants {
    public static final String APP_NAME = "Student Tracker";
    public static final int NUMBER_OF_THREADS = 4;
    public static final String TERM_TABLE_NAME = "terms";
    public static final String COURSE_TABLE_NAME = "courses";
    public static final String ASSESSMENT_TABLE_NAME = "assessments";
    public static final String MENTOR_TABLE_NAME = "mentors";
    public static final String TERM_KEY = "term_key";
    public static final String EDIT_KEY = "edit_key";
    public static final String COURSE_KEY = "course_id_key";
    public static final String ASSESSMENT_KEY = "assessment_key";
    public static final String MENTOR_KEY = "mentor_key";
    public static final String TERM_COLUMN_ID = "termId";
    public static final String COURSE_COLUMN_ID = "courseId";
    public static final String ASSESSMENT_COLUMN_ID = "assessmentId";
    public static final String MENTOR_COLUMN_ID = "mentorId";
    public static final int DATABASE_VERSION = 4;

    //constants for courses
    public static final String COURSE_ID = "courseId";
    public static final String COURSE_NAME = "courseName";
    public static final String COURSE_DESCRIPTION = "courseDescription";
    public static final String COURSE_START_DATE = "courseStartDate";
    public static final String COURSE_END_DATE = "courseEndDate";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_NOTES = "courseNotes";
    public static final String COURSE_TERM_ID = "courseTermId";
    public static final String COURSE_START_ALARM = "courseStartAlarm";
    public static final String COURSE_END_ALARM = "courseEndAlarm";

    //constants for terms
    public static final String TERM_ID = "termId";
    public static final String TERM_TITLE = "termTitle";
    public static final String TERM_START_DATE = "termStartDate";
    public static final String TERM_END_DATE = "termEndDate";

    //constants for assessments
    public static final String ASSESSMENT_ID = "assessmentId";
    public static final String ASSESSMENT_TITLE = "assessmentTitle";
    public static final String ASSESSMENT_TYPE = "assessmentType";
    public static final String ASSESSMENT_DUE_DATE = "assessmentDueDate";
    public static final String ASSESSMENT_COURSE_ID = "assessmentCourseId";

    //constants for mentors
    public static final String MENTOR_ID = "mentorId";
    public static final String MENTOR_NAME = "mentorName";
    public static final String MENTOR_PHONE = "mentorPhone";
    public static final String MENTOR_EMAIL = "mentorEmail";
    public static final String MENTOR_COURSE_ID = "mentorCourseId";
}

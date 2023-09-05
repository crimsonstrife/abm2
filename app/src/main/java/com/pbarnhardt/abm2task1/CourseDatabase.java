package com.pbarnhardt.abm2task1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// This class creates the database for the app, including tables for terms, the courses within each term, and the assessments within each course.
public class CourseDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentTracker.db"; // The name of the database file.
    private static final int DATABASE_VERSION = 1; // Increment this number to force the database to update.

    // Constructor for the database.
    public CourseDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Strings for creating the terms table.
    private static final class TermTable {
        private static final String TABLE_NAME = "terms";
        private static final String COL_ID = "_id";
        private static final String COL_TITLE = "title";
        private static final String COL_START = "start_date";
        private static final String COL_END = "end_date";
        private static final String COL_ACTIVE = "active";
        private static final String COL_COURSE_COUNT = "course_count";
    }

    // Strings for creating the courses table.
    private static final class CourseTable {
        private static final String TABLE_NAME = "courses";
        private static final String COL_ID = "_id";
        private static final String COL_TERM_ID = "term_id";
        private static final String COL_TITLE = "title";
        private static final String COL_DESC = "description";
        private static final String COL_START = "start_date";
        private static final String COL_END = "end_date";
        private static final String COL_STATUS = "status";
        private static final String COL_MENTOR_NAME = "mentor_name";
        private static final String COL_MENTOR_PHONE = "mentor_phone";
        private static final String COL_MENTOR_EMAIL = "mentor_email";
    }

    // Strings for the Course Notes table.
    private static final class CourseNotesTable {
        private static final String TABLE_NAME = "course_notes";
        private static final String COL_ID = "_id";
        private static final String COL_COURSE_ID = "course_id";
        private static final String COL_NOTE = "note";
    }

    // Strings for creating the assessments table.
    private static final class AssessmentTable {
        private static final String TABLE_NAME = "assessments";
        private static final String COL_ID = "_id";
        private static final String COL_COURSE_ID = "course_id";
        private static final String COL_TITLE = "title";
        private static final String COL_DESC = "description";
        private static final String COL_DUE_DATE = "due_date";
        private static final String COL_TYPE = "type";
    }

    //OnCreate method for the database
    @Override
    public void onCreate(SQLiteDatabase db) {
    // Create the terms table.
        db.execSQL("CREATE TABLE " + TermTable.TABLE_NAME + " (" +
                TermTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TermTable.COL_TITLE + " TEXT, " +
                TermTable.COL_START + " TEXT, " +
                TermTable.COL_END + " TEXT, " +
                TermTable.COL_ACTIVE + " INTEGER, " +
                TermTable.COL_COURSE_COUNT + " INTEGER" +
                ")");

        // Create the courses table.
        db.execSQL("CREATE TABLE " + CourseTable.TABLE_NAME + " (" +
                CourseTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CourseTable.COL_TERM_ID + " INTEGER, " +
                CourseTable.COL_TITLE + " TEXT, " +
                CourseTable.COL_DESC + " TEXT, " +
                CourseTable.COL_START + " TEXT, " +
                CourseTable.COL_END + " TEXT, " +
                CourseTable.COL_STATUS + " TEXT, " +
                CourseTable.COL_MENTOR_NAME + " TEXT, " +
                CourseTable.COL_MENTOR_PHONE + " TEXT, " +
                CourseTable.COL_MENTOR_EMAIL + " TEXT" +
                ")");

        // Create the course notes table.
        db.execSQL("CREATE TABLE " + CourseNotesTable.TABLE_NAME + " (" +
                CourseNotesTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CourseNotesTable.COL_COURSE_ID + " INTEGER, " +
                CourseNotesTable.COL_NOTE + " TEXT" +
                ")");

        // Create the assessments table.
        db.execSQL("CREATE TABLE " + AssessmentTable.TABLE_NAME + " (" +
                AssessmentTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AssessmentTable.COL_COURSE_ID + " INTEGER, " +
                AssessmentTable.COL_TITLE + " TEXT, " +
                AssessmentTable.COL_DESC + " TEXT, " +
                AssessmentTable.COL_DUE_DATE + " DATE, " +
                AssessmentTable.COL_TYPE + " TEXT" +
                ")");
    }

    //OnUpgrade method for the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the tables.
        db.execSQL("DROP TABLE IF EXISTS " + TermTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CourseNotesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AssessmentTable.TABLE_NAME);

        // Recreate the tables.
        onCreate(db);
    }

    //Insert methods for the database
    public void insertTerm(SQLiteDatabase db, String title, String start, String end, int active) {
        // Insert a new term into the terms table, course count is set to 0 by default since there are no courses in the term yet.
        db.execSQL("INSERT INTO " + TermTable.TABLE_NAME + " (" +
                TermTable.COL_TITLE + ", " +
                TermTable.COL_START + ", " +
                TermTable.COL_END + ", " +
                TermTable.COL_ACTIVE + ", " +
                TermTable.COL_COURSE_COUNT +
                ") VALUES ('" +
                title + "', '" +
                start + "', '" +
                end + "', " +
                active + ", " +
                0 +
                ")");
    }

    public void insertCourse(SQLiteDatabase db, int termId, String title, String desc, String start, String end, String status, String mentorName, String mentorPhone, String mentorEmail) {
        // Insert a new course into the courses table.
        db.execSQL("INSERT INTO " + CourseTable.TABLE_NAME + " (" +
                CourseTable.COL_TERM_ID + ", " +
                CourseTable.COL_TITLE + ", " +
                CourseTable.COL_DESC + ", " +
                CourseTable.COL_START + ", " +
                CourseTable.COL_END + ", " +
                CourseTable.COL_STATUS + ", " +
                CourseTable.COL_MENTOR_NAME + ", " +
                CourseTable.COL_MENTOR_PHONE + ", " +
                CourseTable.COL_MENTOR_EMAIL +
                ") VALUES (" +
                termId + ", '" +
                title + "', '" +
                desc + "', '" +
                start + "', '" +
                end + "', '" +
                status + "', '" +
                mentorName + "', '" +
                mentorPhone + "', '" +
                mentorEmail +
                "')");

        // Increment the course count for the term.
        db.execSQL("UPDATE " + TermTable.TABLE_NAME + " SET " +
                TermTable.COL_COURSE_COUNT + " = " + TermTable.COL_COURSE_COUNT + " + 1 " +
                "WHERE " + TermTable.COL_ID + " = " + termId);
    }

    public void insertCourseNotes(SQLiteDatabase db, int courseId, String note) {
        // Insert a new course note into the course notes table.
        db.execSQL("INSERT INTO " + CourseNotesTable.TABLE_NAME + " (" +
                CourseNotesTable.COL_COURSE_ID + ", " +
                CourseNotesTable.COL_NOTE +
                ") VALUES (" +
                courseId + ", '" +
                note +
                "')");
    }

    public void insertAssessment(SQLiteDatabase db, int courseId, String title, String desc, String dueDate, String type) {
        // Insert a new assessment into the assessments table.
        db.execSQL("INSERT INTO " + AssessmentTable.TABLE_NAME + " (" +
                AssessmentTable.COL_COURSE_ID + ", " +
                AssessmentTable.COL_TITLE + ", " +
                AssessmentTable.COL_DESC + ", " +
                AssessmentTable.COL_DUE_DATE + ", " +
                AssessmentTable.COL_TYPE +
                ") VALUES (" +
                courseId + ", '" +
                title + "', '" +
                desc + "', '" +
                dueDate + "', '" +
                type +
                "')");
    }

    //Update methods for the database
    public void updateTerm(SQLiteDatabase db, int id, String title, String start, String end, int active) {
        // Update a term in the terms table.
        db.execSQL("UPDATE " + TermTable.TABLE_NAME + " SET " +
                TermTable.COL_TITLE + " = '" + title + "', " +
                TermTable.COL_START + " = '" + start + "', " +
                TermTable.COL_END + " = '" + end + "', " +
                TermTable.COL_ACTIVE + " = " + active + " " +
                "WHERE " + TermTable.COL_ID + " = " + id);
    }

    public void updateCourse(SQLiteDatabase db, int id, int termId, String title, String desc, String start, String end, String status, String mentorName, String mentorPhone, String mentorEmail) {
        // Update a course in the courses table.
        db.execSQL("UPDATE " + CourseTable.TABLE_NAME + " SET " +
                CourseTable.COL_TERM_ID + " = " + termId + ", " +
                CourseTable.COL_TITLE + " = '" + title + "', " +
                CourseTable.COL_DESC + " = '" + desc + "', " +
                CourseTable.COL_START + " = '" + start + "', " +
                CourseTable.COL_END + " = '" + end + "', " +
                CourseTable.COL_STATUS + " = '" + status + "', " +
                CourseTable.COL_MENTOR_NAME + " = '" + mentorName + "', " +
                CourseTable.COL_MENTOR_PHONE + " = '" + mentorPhone + "', " +
                CourseTable.COL_MENTOR_EMAIL + " = '" + mentorEmail + "' " +
                "WHERE " + CourseTable.COL_ID + " = " + id);
    }

    public void updateCourseNotes(SQLiteDatabase db, int id, int courseId, String note) {
        // Update a course note in the course notes table.
        db.execSQL("UPDATE " + CourseNotesTable.TABLE_NAME + " SET " +
                CourseNotesTable.COL_COURSE_ID + " = " + courseId + ", " +
                CourseNotesTable.COL_NOTE + " = '" + note + "' " +
                "WHERE " + CourseNotesTable.COL_ID + " = " + id);
    }

    public void updateAssessment(SQLiteDatabase db, int id, int courseId, String title, String desc, String dueDate, String type) {
        // Update an assessment in the assessments table.
        db.execSQL("UPDATE " + AssessmentTable.TABLE_NAME + " SET " +
                AssessmentTable.COL_COURSE_ID + " = " + courseId + ", " +
                AssessmentTable.COL_TITLE + " = '" + title + "', " +
                AssessmentTable.COL_DESC + " = '" + desc + "', " +
                AssessmentTable.COL_DUE_DATE + " = '" + dueDate + "', " +
                AssessmentTable.COL_TYPE + " = '" + type + "' " +
                "WHERE " + AssessmentTable.COL_ID + " = " + id);
    }
}

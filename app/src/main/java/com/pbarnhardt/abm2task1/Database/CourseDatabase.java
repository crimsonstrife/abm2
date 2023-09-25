package com.pbarnhardt.abm2task1.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.pbarnhardt.abm2task1.DAO.AssessmentsDAO;
import com.pbarnhardt.abm2task1.DAO.CourseDAO;
import com.pbarnhardt.abm2task1.DAO.MentorsDAO;
import com.pbarnhardt.abm2task1.DAO.NotesDAO;
import com.pbarnhardt.abm2task1.DAO.StatusDAO;
import com.pbarnhardt.abm2task1.DAO.TermsDAO;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Notes;
import com.pbarnhardt.abm2task1.Entity.Status;
import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Course (StudentTracker) database.
 */
@Database(entities = {Assessments.class, Courses.class, Notes.class, Terms.class, Mentors.class, Status.class}, version = 1, exportSchema = false)
public abstract class CourseDatabase extends RoomDatabase {
    /**
     * The constants.
     */
    public static final String DATABASE_NAME = "TrackerDatabase.db";

    /**
     * The abstract method for the CourseDao.
     *
     * @return the course dao
     */
    public abstract CourseDAO courseDao();

    /**
     * The abstract method for the TermDao.
     *
     * @return the term dao
     */
    public abstract TermsDAO termDao();

    /**
     * The abstract method for the AssessmentDao.
     *
     * @return the assessment dao
     */
    public abstract AssessmentsDAO assessmentDao();

    /**
     * The abstract method for the NotesDao.
     *
     * @return the notes dao
     */
    public abstract NotesDAO notesDao();

    /**
     * The abstract method for the MentorsDao.
     *
     * @return the mentors dao
     */
    public abstract MentorsDAO mentorsDao();

    /**
     * The abstract method for the StatusDao.
     */
    public abstract StatusDAO statusDao();

    private static volatile CourseDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    /**
     * Database write executor.
     */
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets database.
     *
     * @param context the context
     * @return the database
     */
    public static CourseDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CourseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CourseDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);

            //comment out the following line to prevent the database from being cleared during app restarts
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                CourseDAO cdao = INSTANCE.courseDao();
                cdao.deleteAllCourses();
                TermsDAO tdao = INSTANCE.termDao();
                tdao.deleteAllTerms();
                AssessmentsDAO adao = INSTANCE.assessmentDao();
                adao.deleteAllAssessments();
                NotesDAO ndao = INSTANCE.notesDao();
                ndao.deleteAllNotes();
                MentorsDAO mdao = INSTANCE.mentorsDao();
                mdao.deleteAllMentors();
                StatusDAO sdao = INSTANCE.statusDao();
                sdao.deleteAllStatus();

                Terms term = new Terms("Term 1", "2020-01-01", "2020-06-30", "In Progress");
                tdao.insertTerms(term);

                Courses course = new Courses("Course 1", "Course 1 Description", "2020-01-01", "2020-01-31", 1, 1);
                cdao.insertCourses(course);

                Assessments assessment = new Assessments("Assessment 1", "Objective", "Assessment 1 Description", "2020-01-15", 1);
                adao.insertAssessments(assessment);

                Notes note = new Notes("Note 1", 1);
                ndao.insertNotes(note);

                Mentors mentor = new Mentors("John Doe", "555-555-5555", "jdoe@school.edu", 1);
                mdao.insertMentors(mentor);

                //Status status1 = new Status("In Progress");
                //sdao.insertStatus(status1);
                //Status status2 = new Status("Completed");
                //sdao.insertStatus(status2);
                //Status status3 = new Status("Dropped");
                //sdao.insertStatus(status3);
                //Status status4 = new Status("Plan to Take");
                //sdao.insertStatus(status4);
            });
        }
    };


    //Changed to an Array, so the below is no longer required.
    /**
     * Populate the Status table with the 4 default values.
     *
     */
    //public static void populateStatus() {
        //databaseWriteExecutor.execute(() -> {
            //StatusDAO sdao = INSTANCE.statusDao();
            //Status status1 = new Status("In Progress");
            //sdao.insertStatus(status1);
            //Status status2 = new Status("Completed");
            //sdao.insertStatus(status2);
            //Status status3 = new Status("Dropped");
            //sdao.insertStatus(status3);
            //Status status4 = new Status("Plan to Take");
            //sdao.insertStatus(status4);
        //});
    //}
}

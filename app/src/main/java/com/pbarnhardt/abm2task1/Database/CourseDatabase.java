package com.pbarnhardt.abm2task1.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pbarnhardt.abm2task1.DAO.AssessmentsDAO;
import com.pbarnhardt.abm2task1.DAO.CourseDAO;
import com.pbarnhardt.abm2task1.DAO.MentorsDAO;
import com.pbarnhardt.abm2task1.DAO.NotesDAO;
import com.pbarnhardt.abm2task1.DAO.TermsDAO;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Notes;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Utils.Converters;

/**
 * The type Course (StudentTracker) database.
 */
@Database(entities = {Assessments.class, Courses.class, Notes.class, Terms.class, Mentors.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
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
    private static volatile CourseDatabase INSTANCE;
    private static final Object LOCK = new Object();

    public static CourseDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CourseDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}

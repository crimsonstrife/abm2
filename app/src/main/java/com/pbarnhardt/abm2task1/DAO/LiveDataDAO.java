package com.pbarnhardt.abm2task1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Entity.Notes;
import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

@Dao
public interface LiveDataDAO {

    //Get Live Data Lists.
    @Query("SELECT * FROM Terms")
    LiveData<List<Terms>> getLiveTermsList();

    @Query("SELECT * FROM Courses")
    LiveData<List<Courses>> getLiveCoursesList();

    @Query("SELECT * FROM Assessments")
    LiveData<List<Assessments>> getLiveAssessmentsList();

    @Query("SELECT * FROM CourseMentors")
    LiveData<List<Mentors>> getLiveMentorsList();

    @Query("SELECT * FROM CourseNotes")
    LiveData<List<Notes>> getLiveNotesList();

    /**
     * Get filtered live data lists.
     */

    //Get courses by term ID.
    @Query("SELECT * FROM Courses WHERE termID = :termID")
    LiveData<List<Courses>> getLiveCoursesByTermID(int termID);

    //Get assessments by course ID.
    @Query("SELECT * FROM Assessments WHERE courseID = :courseID")
    LiveData<List<Assessments>> getLiveAssessmentsByCourseID(int courseID);

    //Get mentors by course ID.
    @Query("SELECT * FROM CourseMentors WHERE courseID = :courseID")
    LiveData<List<Mentors>> getLiveMentorsByCourseID(int courseID);

    //Get notes by course ID.
    @Query("SELECT * FROM CourseNotes WHERE courseID = :courseID")
    LiveData<List<Notes>> getLiveNotesByCourseID(int courseID);

    /**
     * Inserts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(Terms term);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Courses course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(Assessments assessment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(Mentors mentor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote (Notes note);

    /**
     * Deletes
     */
    @Delete
    void deleteTerm(Terms term);

    @Delete
    void deleteCourse(Courses course);

    @Delete
    void deleteAssessment(Assessments assessment);

    @Delete
    void deleteMentor(Mentors mentor);

    @Delete
    void deleteNote (Notes note);

    /**
     * Individual Gets and Filters
     */
    //Get mentor information by course ID.
    @Query("SELECT courseMentor.courseMentorName, courseMentor.courseMentorPhone, courseMentor.courseMentorEmail, courseMentor.courseID FROM CourseMentors courseMentor " +
            "LEFT OUTER JOIN (SELECT * FROM CourseMentors sub WHERE sub.courseID = :courseID) mc ON mc.courseMentorName = courseMentor.courseMentorName")
    LiveData<List<Mentors>> getLiveMentorInfoByCourseID(int courseID);

    //Get mentor information by mentor ID.
    @Query("SELECT courseMentor.courseMentorName, courseMentor.courseMentorPhone, courseMentor.courseMentorEmail, courseMentor.courseID FROM CourseMentors courseMentor " +
            "LEFT OUTER JOIN (SELECT * FROM CourseMentors sub WHERE sub.mentorID = :mentorID) mc ON mc.courseMentorName = courseMentor.courseMentorName")
    LiveData<List<Mentors>> getLiveMentorInfoByMentorID(int mentorID);

    //Get assessment information by course ID.
    @Query("SELECT assessment.assessmentName, assessment.assessmentType, assessment.assessmentDueDate, assessment.courseID FROM Assessments assessment " +
            "LEFT OUTER JOIN (SELECT * FROM Assessments sub WHERE sub.courseID = :courseID) a ON a.assessmentName = assessment.assessmentName")
    LiveData<List<Assessments>> getLiveAssessmentInfoByCourseID(int courseID);

    /**
     * Check if exists
     */
    //Check if mentor is assigned to a specific course.
    @Query("SELECT * FROM CourseMentors WHERE courseID = :courseID AND courseMentorName = :mentorName")
    LiveData<List<Mentors>> checkIfMentorExists(int courseID, String mentorName);
}

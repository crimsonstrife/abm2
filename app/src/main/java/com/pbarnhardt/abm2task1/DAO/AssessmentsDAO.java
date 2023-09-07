package com.pbarnhardt.abm2task1.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pbarnhardt.abm2task1.Entity.Assessments;

import java.util.List;

/**
 * The interface Assessments dao.
 */
@Dao
public interface AssessmentsDAO {
    /**
     * Gets Assessments.
     *
     * @return the Assessments
     */
    // @Query("SELECT * FROM Assessments ORDER BY courseId")
    @Query("SELECT * FROM Assessments")
    public List<Assessments> getAssessments();

    /**
     * Insert Assessments.
     *
     * @param assessment the Assessment
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAssessments(Assessments assessment);

    /**
     * Delete Assessments.
     *
     * @param assessment the Assessment
     */
    @Delete
    public void deleteAssessments(Assessments assessment);

    /**
     * Update Assessments.
     *
     * @param assessment the Assessment
     */
    @Update
    public void updateAssessments(Assessments assessment);

    /**
     * Delete all Assessments.
     */
    @Query("DELETE FROM Assessments")
    public void deleteAllAssessments();

    /**
     * Gets Assessments by course id.
     *
     * @param courseId the course id
     * @return the Assessments by course id
     */
    @Query("SELECT * FROM Assessments WHERE courseId = :courseId")
    public List<Assessments> getAssessmentsByCourseId(int courseId);
}

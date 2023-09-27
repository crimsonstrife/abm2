package com.pbarnhardt.abm2task1.DAO;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_TABLE_NAME;

import androidx.lifecycle.LiveData;
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
     * Insert Assessments.
     *
     * @param assessment the Assessment
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAssessments(Assessments assessment);

    /**
     * Insert all Assessments.
     *
     * @param assessments the Assessments
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Assessments> assessments);

    /**
     * Delete Assessments.
     *
     * @param assessment the Assessment
     */
    @Delete
    public void deleteAssessments(Assessments assessment);

    /**
     * Queries
     */
    @Query("SELECT * FROM " + ASSESSMENT_TABLE_NAME + " WHERE assessmentId = :id")
    public Assessments getAssessmentsById(int id);
    @Query("SELECT * FROM " + ASSESSMENT_TABLE_NAME + " ORDER BY assessmentDueDate DESC")
    LiveData<List<Assessments>> getAllAssessments();
    @Query("SELECT * FROM " + ASSESSMENT_TABLE_NAME + " WHERE courseId = :courseId")
    LiveData<List<Assessments>> getAssessmentsByCourse(final int courseId);
    @Query("DELETE FROM " + ASSESSMENT_TABLE_NAME)
    int deleteAll();
    @Query("SELECT COUNT(*) FROM " + ASSESSMENT_TABLE_NAME)
    int getCount();
}

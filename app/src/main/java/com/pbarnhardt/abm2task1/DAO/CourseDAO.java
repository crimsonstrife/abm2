package com.pbarnhardt.abm2task1.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pbarnhardt.abm2task1.Entity.Courses;

import java.util.List;

/**
 * The interface Course dao.
 */
@Dao
public interface CourseDAO {
    /**
     * Gets Courses
     *
     * @return the Courses
     */
    // @Query("SELECT * FROM Courses" ORDER BY courseID ASC)
    @Query("SELECT * FROM Courses")
    public List<Courses> getCourses();

    /**
     * Insert Courses
     *
     * @param courses the Courses
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCourses(Courses courses);

    /**
     * Delete Courses
     *
     * @param courses the Courses
     */
    @Delete
    public void deleteCourses(Courses courses);

    /**
     * Update Courses
     *
     * @param courses the Courses
     */
    @Update
    public void updateCourses(Courses courses);

    /**
     * Delete all Courses
     */
    @Query("DELETE FROM Courses")
    public void deleteAllCourses();

    /**
     * Gets Courses by term id
     *
     * @param termId the term id
     * @return the Courses
     */
    @Query("SELECT * FROM Courses WHERE termId = :termId")
    public List<Courses> getCoursesByTermId(int termId);
}

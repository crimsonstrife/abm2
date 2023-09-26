package com.pbarnhardt.abm2task1.DAO;

import androidx.lifecycle.LiveData;
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
     * Insert Courses
     *
     * @param courses the Courses
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCourses(Courses courses);

    /**
     * Insert all Courses
     *
     * @param courses the Courses
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Courses> courses);


    /**
     * Delete Courses
     *
     * @param courses the Courses
     */
    @Delete
    public void deleteCourses(Courses courses);

    /**
     * Queries
     */
    @Query("SELECT * FROM courses WHERE id = :id")
    public Courses getCourseById(int id);
    @Query("SELECT * FROM courses ORDER BY courseStartDate DESC")
    LiveData<List<Courses>> getAllCourses();
    @Query("SELECT * FROM courses WHERE termId = :termId")
    LiveData<List<Courses>> getCoursesByTerm(final int termId);
    @Query("DELETE FROM courses")
    int deleteAll();
    @Query("SELECT COUNT(*) FROM courses")
    int getCount();
}

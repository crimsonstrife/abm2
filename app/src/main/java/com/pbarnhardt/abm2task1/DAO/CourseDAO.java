package com.pbarnhardt.abm2task1.DAO;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_TABLE_NAME;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
    void insertCourses(Courses courses);

    /**
     * Insert all Courses
     *
     * @param courses the Courses
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Courses> courses);


    /**
     * Delete Courses
     *
     * @param courses the Courses
     */
    @Delete
    void deleteCourses(Courses courses);

    /**
     * Queries
     */
    @Query("SELECT * FROM " + COURSE_TABLE_NAME + " WHERE courseId = :id")
    Courses getCourseById(int id);
    @Query("SELECT * FROM " + COURSE_TABLE_NAME + " ORDER BY courseName DESC")
    LiveData<List<Courses>> getAllCourses();
    @Query("SELECT * FROM " + COURSE_TABLE_NAME + " WHERE termId = :termId")
    LiveData<List<Courses>> getCoursesByTerm(final int termId);
    @Query("DELETE FROM " + COURSE_TABLE_NAME)
    int deleteAll();
    @Query("SELECT COUNT(*) FROM " + COURSE_TABLE_NAME)
    int getCount();
}

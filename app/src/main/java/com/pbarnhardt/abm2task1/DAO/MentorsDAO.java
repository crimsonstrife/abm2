package com.pbarnhardt.abm2task1.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pbarnhardt.abm2task1.Entity.Mentors;

import java.util.List;

/**
 * The interface Mentors dao.
 */
@Dao
public interface MentorsDAO {
    /**
     * Gets mentors.
     *
     * @return the mentors
     */
    // @Query("SELECT * FROM CourseMentors ORDER BY courseId")
    @Query("SELECT * FROM CourseMentors")
    public List<Mentors> getMentors();

    /**
     * Insert mentors.
     *
     * @param mentor the mentor
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMentors(Mentors mentor);

    /**
     * Delete mentors.
     *
     * @param mentor the mentor
     */
    @Delete
    public void deleteMentors(Mentors mentor);

    /**
     * Update mentors.
     *
     * @param mentor the mentor
     */
    @Update
    public void updateMentors(Mentors mentor);

    /**
     * Delete all mentors.
     */
    @Query("DELETE FROM CourseMentors")
    public void deleteAllMentors();

    /**
     * Gets mentors by course id.
     *
     * @param courseId the course id
     * @return the mentors by course id
     */
    @Query("SELECT * FROM CourseMentors WHERE courseId = :courseId")
    public List<Mentors> getMentorsByCourseId(int courseId);
}

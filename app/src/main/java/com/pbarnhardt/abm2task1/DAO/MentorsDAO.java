package com.pbarnhardt.abm2task1.DAO;

import static com.pbarnhardt.abm2task1.Utils.Constants.MENTOR_TABLE_NAME;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pbarnhardt.abm2task1.Entity.Mentors;

import java.util.List;

/**
 * The interface Mentors dao.
 */
@Dao
public interface MentorsDAO {
    /**
     * Insert mentors.
     *
     * @param mentor the mentor
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentors(Mentors mentor);

    /**
     * Insert all.
     *
     * @param mentors the mentors
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Mentors> mentors);

    /**
     * Delete mentors.
     *
     * @param mentor the mentor
     */
    @Delete
    void deleteMentors(Mentors mentor);

    /**
     * The Queries
     */
    @Query("SELECT * FROM " + MENTOR_TABLE_NAME + " WHERE mentorId = :id")
    Mentors getMentorsById(int id);
    @Query("SELECT * FROM " + MENTOR_TABLE_NAME + " ORDER BY courseMentorName DESC")
    LiveData<List<Mentors>> getAllMentors();
    @Query("SELECT * FROM " + MENTOR_TABLE_NAME + " WHERE courseId = :courseId")
    LiveData<List<Mentors>> getMentorsByCourse(final int courseId);
    @Query("DELETE FROM " + MENTOR_TABLE_NAME)
    int deleteAll();
    @Query("SELECT COUNT(*) FROM " + MENTOR_TABLE_NAME)
    int getCount();
}

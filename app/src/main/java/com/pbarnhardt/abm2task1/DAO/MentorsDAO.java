package com.pbarnhardt.abm2task1.DAO;

import androidx.lifecycle.LiveData;
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
     * Insert mentors.
     *
     * @param mentor the mentor
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMentors(Mentors mentor);

    /**
     * Insert all.
     *
     * @param mentors the mentors
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Mentors> mentors);

    /**
     * Delete mentors.
     *
     * @param mentor the mentor
     */
    @Delete
    public void deleteMentors(Mentors mentor);

    /**
     * The Queries
     */
    @Query("SELECT * FROM coursementors WHERE mentorId = :id")
    public Mentors getMentorsById(int id);
    @Query("SELECT * FROM coursementors ORDER BY courseMentorName DESC")
    LiveData<List<Mentors>> getAllMentors();
    @Query("SELECT * FROM coursementors WHERE courseId = :courseId")
    LiveData<List<Mentors>> getMentorsByCourse(final int courseId);
    @Query("DELETE FROM coursementors")
    int deleteAll();
    @Query("SELECT COUNT(*) FROM coursementors")
    int getCount();
}

package com.pbarnhardt.abm2task1.DAO;

import androidx.room.Dao;
import androidx.room.Query;

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

}

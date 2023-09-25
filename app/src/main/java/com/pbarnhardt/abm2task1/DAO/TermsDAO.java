package com.pbarnhardt.abm2task1.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

/**
 * The interface Terms dao.
 */
@Dao
public interface TermsDAO {
    /**
     * Gets terms.
     *
     * @return the terms
     */
    // @Query("SELECT * FROM Terms ORDER BY termId")
    @Query("SELECT * FROM Terms")
    public List<Terms> getTerms();

    /**
     * Insert terms.
     *
     * @param term the term
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTerms(Terms term);

    /**
     * Delete terms.
     *
     * @param term the term
     */
    @Delete
    public void deleteTerms(Terms term);

    /**
     * Update terms.
     *
     * @param term the term
     */
    @Update
    public void updateTerms(Terms term);

    /**
     * Delete all terms.
     */
    @Query("DELETE FROM Terms")
    public void deleteAllTerms();
}

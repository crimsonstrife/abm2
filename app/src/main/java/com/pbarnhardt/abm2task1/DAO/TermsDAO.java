package com.pbarnhardt.abm2task1.DAO;

import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_TABLE_NAME;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pbarnhardt.abm2task1.Entity.Terms;

import java.util.List;

/**
 * The interface Terms dao.
 */
@Dao
public interface TermsDAO {
    /**
     * Insert terms.
     *
     * @param term the term
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerms(Terms term);

    /**
     * Insert all.
     *
     * @param terms the terms
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Terms> terms);

    /**
     * Delete terms.
     *
     * @param term the term
     */
    @Delete
    void deleteTerms(Terms term);

    /**
     * Queries
     */
    @Query("SELECT * FROM " + TERM_TABLE_NAME + " WHERE termId = :id")
    Terms getTermById(int id);
    @Query("SELECT * FROM " + TERM_TABLE_NAME + " ORDER BY termStartDate DESC")
    LiveData<List<Terms>> getAll();
    @Query("DELETE FROM " + TERM_TABLE_NAME)
    int deleteAll();
    @Query("SELECT COUNT(*) FROM " + TERM_TABLE_NAME)
    int getCount();
}

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
     * Insert terms.
     *
     * @param term the term
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertTerms(Terms term);

    /**
     * Insert all.
     *
     * @param terms the terms
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Terms> terms);

    /**
     * Queries
     */
    @Query("SELECT * FROM terms WHERE id = :id")
    public Terms getTermById(int id);
    @Query("SELECT * FROM terms ORDER BY startDate DESC")
    LiveData<List<Terms>> getAll();
    @Query("DELETE FROM terms")
    int deleteAll();
    @Query("SELECT COUNT(*) FROM terms")
    int getCount();
}

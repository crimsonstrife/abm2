package com.pbarnhardt.abm2task1.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pbarnhardt.abm2task1.Entity.Status;

import java.util.List;

/**
 * The interface Status dao.
 */
@Dao
public interface StatusDAO {
    /**
     * Gets Status.
     *
     * @return the Status
     */
    @Query("SELECT * FROM CourseStatus")
    public List<Status> getStatus();

    /**
     * Insert Status.
     *
     * @param status the Status
     */
    // @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertStatus(Status status);

    /**
     * Delete Status.
     *
     * @param status the Status
     */
    @Delete
    public void deleteStatus(Status status);

    /**
     * Update Status.
     *
     * @param status the Status
     */
    @Update
    public void updateStatus(Status status);

    /**
     * Delete all Status.
     */
    @Query("DELETE FROM CourseStatus")
    public void deleteAllStatus();
}

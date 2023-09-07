package com.pbarnhardt.abm2task1.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pbarnhardt.abm2task1.Entity.Notes;

import java.util.List;

/**
 * The interface Notes dao.
 */
@Dao
public interface NotesDAO {
    /**
     * Gets Notes.
     *
     * @return the Notes
     */
    // @Query("SELECT * FROM CourseNotes ORDER BY courseId")
    @Query("SELECT * FROM CourseNotes")
    public List<Notes> getNotes();

    /**
     * Insert Notes.
     *
     * @param note the Note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNotes(Notes note);

    /**
     * Delete Notes.
     *
     * @param note the Note
     */
    @Delete
    public void deleteNotes(Notes note);

    /**
     * Update Notes.
     *
     * @param note the Note
     */
    @Update
    public void updateNotes(Notes note);

    /**
     * Delete all Notes.
     */
    @Query("DELETE FROM CourseNotes")
    public void deleteAllNotes();

    /**
     * Gets Notes by course id.
     *
     * @param courseId the course id
     * @return the Notes by course id
     */
    @Query("SELECT * FROM CourseNotes WHERE courseId = :courseId")
    public List<Notes> getNotesByCourseId(int courseId);
}

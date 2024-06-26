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
    List<Notes> getNotes();

    /**
     * Insert Notes.
     *
     * @param note the Note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(Notes note);

    /**
     * Delete Notes.
     *
     * @param note the Note
     */
    @Delete
    void deleteNotes(Notes note);

    /**
     * Update Notes.
     *
     * @param note the Note
     */
    @Update
    void updateNotes(Notes note);

    /**
     * Delete all Notes.
     */
    @Query("DELETE FROM CourseNotes")
    void deleteAllNotes();

    /**
     * Gets Notes by course id.
     *
     * @param courseId the course id
     * @return the Notes by course id
     */
    @Query("SELECT * FROM CourseNotes WHERE courseId = :courseId")
    List<Notes> getNotesByCourseId(int courseId);
}

package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The type Notes for courses in student tracker.
 */
@Entity(tableName = "CourseNotes")
public class Notes {
    @PrimaryKey(autoGenerate = true)
    private int noteId;

    private String noteContent;
    private int courseId;

    /**
     * Instantiates a new Note.
     *
     * @param noteContent the note content
     * @param courseId    the course id
     */
    public Notes(String noteContent, int courseId) {
        this.noteContent = noteContent;
        this.courseId = courseId;
    }

    /**
     * Gets note id.
     *
     * @return the note id
     */
    public int getNoteId() {
        return noteId;
    }

    /**
     * Gets note content.
     *
     * @return the note content
     */
    public String getNoteContent() {
        return noteContent;
    }

    /**
     * Get private int courseId
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Set private int noteId
     */
    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}

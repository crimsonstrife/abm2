package com.pbarnhardt.abm2task1.Entity;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_COLUMN_ID;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

/**
 * The type Notes for courses in student tracker.
 */
@Entity(tableName = "CourseNotes", foreignKeys = @ForeignKey(entity = Courses.class,
        parentColumns = COURSE_COLUMN_ID,
        childColumns = COURSE_COLUMN_ID,
        onDelete = ForeignKey.RESTRICT))
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
     * Gets course id.
     *
     * @return the course id
     */
    public int getNoteCourseId() {
        return courseId;
    }

    /**
     * Sets note content.
     *
     * @param noteContent the note content
     */
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    /**
     * Sets course id.
     *
     * @param courseId the course id
     */
    public void setNoteCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * Get all notes for a course.
     *
     * @param courseId the course id
     * @return array of notes
     */
    public String[] getNotesForCourse(int courseId) {
        String[] notes = new String[2];
        notes[0] = Integer.toString(this.noteId);
        notes[1] = this.noteContent;
        return notes;
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

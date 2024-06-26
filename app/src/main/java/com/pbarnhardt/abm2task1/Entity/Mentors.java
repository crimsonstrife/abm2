package com.pbarnhardt.abm2task1.Entity;

import static com.pbarnhardt.abm2task1.Utils.Constants.MENTOR_TABLE_NAME;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * The type Mentors for courses in student tracker.
 */
@Entity(tableName = MENTOR_TABLE_NAME)
public class Mentors {
    @PrimaryKey(autoGenerate = true)
    private int mentorId;

    private String courseMentorName;
    private String courseMentorPhone;
    private String courseMentorEmail;

    //only one mentor per course
    private int courseId;

    /**
     * Instantiates a new Mentor.
     *
     * @param courseMentorName the mentor name
     * @param courseMentorPhone the mentor phone
     * @param courseMentorEmail the mentor email
     * @param courseId the course id
    */
    @Ignore
    public Mentors(int mentorId, String courseMentorName, String courseMentorPhone, String courseMentorEmail, int courseId){
        this.mentorId = mentorId;
        this.courseMentorName = courseMentorName;
        this.courseMentorPhone = courseMentorPhone;
        this.courseMentorEmail = courseMentorEmail;
        this.courseId = courseId;
    }
    public Mentors(String courseMentorName, String courseMentorPhone, String courseMentorEmail, int courseId) {
        this.courseMentorName = courseMentorName;
        this.courseMentorPhone = courseMentorPhone;
        this.courseMentorEmail = courseMentorEmail;
        this.courseId = courseId;
    }

    /**
     * Gets mentor ID.
     *
     * @return the mentor ID
     */
    public int getMentorId() {
        return mentorId;
    }

    /**
     * Gets mentor name.
     *
     * @return the mentor name
     */
    public String getMentorName() {
        return courseMentorName;
    }

    /**
     * Gets mentor phone.
     *
     * @return the mentor phone
     */
    public String getMentorPhone() {
        return courseMentorPhone;
    }

    /**
     * Gets mentor email.
     *
     * @return the mentor email
     */
    public String getMentorEmail() {
        return courseMentorEmail;
    }

    /**
     * Gets course ID.
     *
     * @return the course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Sets mentor name.
     *
     * @param name the mentor name
     */
    public void setCourseMentorName(String name) {
        this.courseMentorName = name;
    }

    /**
     * Sets mentor phone.
     *
     * @param phone the mentor phone
     */
    public void setCourseMentorPhone(String phone) {
        this.courseMentorPhone = phone;
    }

    /**
     * Sets mentor email.
     *
     * @param email the mentor email
     */
    public void setCourseMentorEmail(String email) {
        this.courseMentorEmail = email;
    }

    /**
     * Get mentor name string.
     */
    public String getCourseMentorName() {
        return courseMentorName;
    }

    /**
     * Get mentor phone string.
     */
    public String getCourseMentorPhone() {
        return courseMentorPhone;
    }

    /**
     * Get mentor email string.
     */
    public String getCourseMentorEmail() {
        return courseMentorEmail;
    }

    /**
     * Set mentor Id
     */
    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    /**
     * Set course Id
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}

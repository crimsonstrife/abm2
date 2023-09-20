package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * The type Mentors for courses in student tracker.
 */
@Entity(tableName = "CourseMentors", foreignKeys = @ForeignKey(entity = Courses.class,
        parentColumns = "courseId",
        childColumns = "courseId",
        onDelete = ForeignKey.RESTRICT))
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
     * @param mentorName
     * @param mentorPhone
     * @param mentorEmail
     * @param courseId
    */
    public Mentors(String mentorName, String mentorPhone, String mentorEmail, int courseId) {
        this.courseMentorName = mentorName;
        this.courseMentorPhone = mentorPhone;
        this.courseMentorEmail = mentorEmail;
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
    public void setMentorName(String name) {
        this.courseMentorName = name;
    }

    /**
     * Sets mentor phone.
     *
     * @param phone the mentor phone
     */
    public void setMentorPhone(String phone) {
        this.courseMentorPhone = phone;
    }

    /**
     * Sets mentor email.
     *
     * @param email the mentor email
     */
    public void setMentorEmail(String email) {
        this.courseMentorEmail = email;
    }

}

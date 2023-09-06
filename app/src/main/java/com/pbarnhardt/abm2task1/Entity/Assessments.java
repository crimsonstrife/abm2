package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * The type Assessments for courses in student tracker.
 */
@Entity(tableName = "Assessments", foreignKeys = @ForeignKey(entity = Courses.class,
        parentColumns = "courseId",
        childColumns = "courseId",
        onDelete = ForeignKey.RESTRICT))
public class Assessments {
    @PrimaryKey(autoGenerate = true)
    private int assessmentId;

    private String assessmentName;
    private String assessmentDescription;
    private String assessmentType;
    private String assessmentDueDate;
    private int courseId;

    /**
     * Instantiates a new Assessments.
     *
     * @param assessmentName     the assessment name
     * @param assessmentDescription the assessment description
     * @param assessmentType     the assessment type
     * @param assessmentDueDate  the assessment due date
     * @param courseId           the course id
     */
    public Assessments(String assessmentName, String assessmentType, String assessmentDescription, String assessmentDueDate, int courseId) {
        this.assessmentName = assessmentName;
        this.assessmentDescription = assessmentDescription;
        this.assessmentType = assessmentType;
        this.assessmentDueDate = assessmentDueDate;
        this.courseId = courseId;
    }

    /**
     * Gets assessment id.
     *
     * @return the assessment id
     */
    public int getAssessmentId() {
        return assessmentId;
    }

    /**
     * Gets assessment name.
     *
     * @return the assessment name
     */
    public String getAssessmentName() {
        return assessmentName;
    }

    /**
     * Gets assessment type.
     *
     * @return the assessment type
     */
    public String getAssessmentType() {
        return assessmentType;
    }

    /**
     * Gets assessment description.
     *
     * @return the assessment description
     */
    public String getAssessmentDescription() {
        return assessmentDescription;
    }

    /**
     * Gets assessment due date.
     *
     * @return the assessment due date
     */
    public String getAssessmentDueDate() {
        return assessmentDueDate;
    }

    /**
     * Gets course id.
     *
     * @return the course id
     */
    public int getAssessmentCourseId() {
        return courseId;
    }

    /**
     * Sets assessment name.
     *
     * @param assessmentName the assessment name
     */
    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    /**
     * Sets assessment type.
     *
     * @param assessmentType the assessment type
     */
    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    /**
     * Sets assessment description.
     *
     * @param assessmentDescription the assessment description
     */
    public void setAssessmentDescription(String assessmentDescription) {
        this.assessmentDescription = assessmentDescription;
    }

    /**
     * Sets assessment due date.
     *
     * @param assessmentDueDate the assessment due date
     */
    public void setAssessmentDueDate(String assessmentDueDate) {
        this.assessmentDueDate = assessmentDueDate;
    }

    /**
     * Sets course id.
     *
     * @param courseId the course id
     */
    public void setAssessmentCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * Get all assessments for a course.
     *
     * @param courseId the course id
     * @return array of Assessments with name, type, description, and due date
     */
    public String[] getAssessmentsForCourse(int courseId) {
        String[] assessments = new String[5];
        assessments[0] = Integer.toString(this.assessmentId);
        assessments[1] = this.assessmentName;
        assessments[2] = this.assessmentType;
        assessments[3] = this.assessmentDescription;
        assessments[4] = this.assessmentDueDate;
        return assessments;
    }
}

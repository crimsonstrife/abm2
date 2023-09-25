package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The type Terms(semesters) for student tracker.
 */
@Entity(tableName = "Terms")
public class Terms {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termName;
    private String termStartDate;
    private String termEndDate;
    private String termStatus;
    private int termCourseCount;

    /**
     * Instantiates a new Terms.
     *
     * @param termName       the term name
     * @param termStartDate  the term start date
     * @param termEndDate    the term end date
     * @param termStatus     the term status
     * Description: This is the constructor for the Terms class. It is used to create a new term. Course count is set to 0 by default.
     */
    public Terms(String termName, String termStartDate, String termEndDate, String termStatus) {
        this.termName = termName;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termStatus = termStatus;
        this.termCourseCount = 0;
    }

    /**
     * Gets term id.
     *
     * @return the term id
     */
    public int getTermId() {
        return termId;
    }

    /**
     * Gets the Status of the term.
     *
     * @return the term status
     */
    public String getTermStatus() {
        return termStatus;
    }

    /**
     * Gets term name.
     *
     * @return the term name
     */
    public String getTermName() {
        return termName;
    }

    /**
     * Gets term start date.
     *
     * @return the term start date
     */
    public String getTermStartDate() {
        return termStartDate;
    }

    /**
     * Gets term end date.
     *
     * @return the term end date
     */
    public String getTermEndDate() {
        return termEndDate;
    }

    /**
     * Gets term course count.
     *
     * @return the term course count
     */
    public int getTermCourseCount() {
        return termCourseCount;
    }

    /**
     * Sets the term title.
     *
     * @param termName the term name
     */
    public void setTermName(String termName) {
        this.termName = termName;
    }

    /**
     * Sets term start date.
     *
     * @param termStartDate the term start date
     */
    public void setTermStartDate(String termStartDate) {
        this.termStartDate = termStartDate;
    }

    /**
     * Sets term end date.
     *
     * @param termEndDate the term end date
     */
    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }

    /**
     * Sets term status.
     *
     * @param termStatus the term status
     */
    public void setTermStatus(String termStatus) {
        this.termStatus = termStatus;
    }

    /**
     * Increment term course count.
     *
     * Description: This method is used to increase the course count for a term.
     */
    public void incrementCourseCount() {
        this.termCourseCount++;
    }

    /**
     * Decrement term course count.
     *
     * Description: This method is used to decrease the course count for a term.
     */
    public void decrementCourseCount() {
        // only decrement if the count is greater than 0
        if (this.termCourseCount > 0) {
            this.termCourseCount--;
        }
    }

    /**
     * Get a list of all terms and their IDs.
     *
     * @return an array of terms
     */
    public String[] getTermsList() {
        String[] termsList = new String[2];
        termsList[0] = Integer.toString(this.termId);
        termsList[1] = this.termName;
        return termsList;
    }

    /**
     * Set term id.
     *
     * @param termId the term id
     */
    public void setTermId(int termId) {
        this.termId = termId;
    }

    /**
     * Set term course count.
     *
     * @param termCourseCount the term course count
     */
    public void setTermCourseCount(int termCourseCount) {
        this.termCourseCount = termCourseCount;
    }
}

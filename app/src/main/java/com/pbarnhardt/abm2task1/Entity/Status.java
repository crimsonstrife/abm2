package com.pbarnhardt.abm2task1.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * The type Statuses for courses.
 */
@Entity(tableName = "Status")
public class Status {
    @PrimaryKey(autoGenerate = true)
    private int statusId;

    private String statusText;

    /**
     * Instantiates a new Status.
     *
     * @param statusText the status text
     */
    public Status(String statusText) {
        this.statusText = statusText;
    }

    /**
     * Gets status id.
     *
     * @return the status id
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Gets status text.
     *
     * @return the status text
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Sets status text.
     *
     * @param statusText the status text
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return statusText;
    }

    /**
     * Sets status id.
     *
     * @param statusId the status id
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}

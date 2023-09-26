package com.pbarnhardt.abm2task1.Enums;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * The enum Statuses for courses.
 *
 * originally attempted to handle as a database table, but it was convoluted.
 */
public enum Status {
    PROGRESS {
        @Override
        public String toString() {
            return "In Progress";
        }
    },

    COMPLETED {
        @Override
        public String toString() {
            return "Completed";
        }
    },

    DROPPED {
        @Override
        public String toString() {
            return "Dropped";
        }
    },

    PLANNED {
        @Override
        public String toString() {
            return "Plan To Take";
        }
    }
}

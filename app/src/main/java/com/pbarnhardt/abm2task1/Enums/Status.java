package com.pbarnhardt.abm2task1.Enums;

import androidx.annotation.NonNull;

/**
 * The enum Statuses for courses.
 * originally attempted to handle as a database table, but it was convoluted.
 */
public enum Status {
    PROGRESS {
        @NonNull
        @Override
        public String toString() {
            return "In Progress";
        }
    },

    COMPLETED {
        @NonNull
        @Override
        public String toString() {
            return "Completed";
        }
    },

    DROPPED {
        @NonNull
        @Override
        public String toString() {
            return "Dropped";
        }
    },

    PLANNED {
        @NonNull
        @Override
        public String toString() {
            return "Plan To Take";
        }
    }
}

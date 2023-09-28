package com.pbarnhardt.abm2task1.Enums;

import androidx.annotation.NonNull;

public enum Types {
    OBJECTIVE {
        @NonNull
        @Override
        public String toString() {
            return "Objective Assessment";
        }
    },

    PERFORMANCE {
        @NonNull
        @Override
        public String toString() {
            return "Performance Assessment";
        }
    }
}

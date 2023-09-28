package com.pbarnhardt.abm2task1.Enums;

import androidx.annotation.NonNull;

public enum RecyclerAdapter {
    MAIN {
        @NonNull
        @Override
        public String toString() {
            return "Parent";
        }
    },
    CHILD {
        @NonNull
        @Override
        public String toString() {
            return "Child";
        }
    },
    ADD {
        @NonNull
        @Override
        public String toString() {
            return "Add";
        }
    }
}

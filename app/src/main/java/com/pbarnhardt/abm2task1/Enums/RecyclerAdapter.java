package com.pbarnhardt.abm2task1.Enums;

public enum RecyclerAdapter {
    MAIN {
        @Override
        public String toString() {
            return "Parent";
        }
    },
    CHILD {
        @Override
        public String toString() {
            return "Child";
        }
    },
    ADD {
        @Override
        public String toString() {
            return "Add";
        }
    }
}

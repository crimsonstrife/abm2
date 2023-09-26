package com.pbarnhardt.abm2task1.Enums;

public enum Types {
    OBJECTIVE {
        @Override
        public String toString() {
            return "Objective Assessment";
        }
    },

    PERFOREMANCE {
        @Override
        public String toString() {
            return "Performance Assessment";
        }
    }
}

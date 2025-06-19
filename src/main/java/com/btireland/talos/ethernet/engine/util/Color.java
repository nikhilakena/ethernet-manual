package com.btireland.talos.ethernet.engine.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Color {

        RED("red"),
        CLEAR("clear");

        private String label;

        private Color(String label) {
            this.label = label;
        }

        @JsonValue
        public String getLabel() {
            return label;
        }

}

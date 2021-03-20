/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

public enum AvailabilityEnum {
    AVAILABLE("AVAILABLE") {
        @Override
        public String toString() {
            return "Available";
        }
    },
    PROCESSING("PROCESSING") {
        @Override
        public String toString() {
            return "Reserved";
        }
    },
    RENTED_OUT("RENTED_OUT") {
        @Override
        public String toString() {
            return "Rented";
        }
    };

    private final String state;

    private AvailabilityEnum(String state) {
        this.state = state;
    }

    public abstract String toString();

}

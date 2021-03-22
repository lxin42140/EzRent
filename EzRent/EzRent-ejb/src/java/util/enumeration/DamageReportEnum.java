/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

/**
 *
 * @author Yuxin
 */
public enum DamageReportEnum {
    PENDING("PENDING") {
        @Override
        public String toString() {
            return "Pending";
        }
    },
    RESOLVED("RESOLVED") {
        @Override
        public String toString() {
            return "Resolved";
        }
    };

    private final String state;

    private DamageReportEnum(String state) {
        this.state = state;
    }

    public abstract String toString();
}

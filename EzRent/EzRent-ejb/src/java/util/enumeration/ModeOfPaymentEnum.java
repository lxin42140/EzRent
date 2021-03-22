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
public enum ModeOfPaymentEnum {
    CREDIT_CARD("CREDIT_CARD") {
        @Override
        public String toString() {
            return "Credit Card";
        }
    },
    CASH_ON_DELIVERY("CASH_ON_DELIVERY") {
        @Override
        public String toString() {
            return "Cash on delivery";
        }
    };

    private final String state;

    private ModeOfPaymentEnum(String state) {
        this.state = state;
    }

    public abstract String toString();
}

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
public enum DeliveryStatusEnum {
    PENDING("PENDING") {
        @Override
        public String toString() {
            return "Pending delivery";
        }
    },
    SHIPPED("SHIPPED") {
        @Override
        public String toString() {
            return "Shipped";
        }
    },
    LOST("LOST") {
        @Override
        public String toString() {
            return "Lost";
        }
    },
    DELIVERED("DELIVERED") {
        @Override
        public String toString() {
            return "Delivered";
        }
    };

    private final String state;

    private DeliveryStatusEnum(String state) {
        this.state = state;
    }

    public abstract String toString();
}

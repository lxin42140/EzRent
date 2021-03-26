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
public enum DeliveryOptionEnum {
    MAIL("MAIL"){
        @Override
        public String toString() {
            return "Mail";
        }
        
    },
    MEETUP("MEETUP"){
        @Override
        public String toString() {
            return "Meet-up";
        }
        
    };
    
    private final String state;

    private DeliveryOptionEnum(String state) {
        this.state = state;
    }

    public abstract String toString();
}

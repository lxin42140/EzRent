/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Yuxin
 */
public class PaymentNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PaymentNotFoundException</code> without
     * detail message.
     */
    public PaymentNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PaymentNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PaymentNotFoundException(String msg) {
        super(msg);
    }
}

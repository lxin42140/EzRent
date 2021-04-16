/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Ziyue
 */
public class UpdateCustomerFailException extends Exception {

    /**
     * Creates a new instance of <code>UpdateCustomerFailException</code>
     * without detail message.
     */
    public UpdateCustomerFailException() {
    }

    /**
     * Constructs an instance of <code>UpdateCustomerFailException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCustomerFailException(String msg) {
        super(msg);
    }
}

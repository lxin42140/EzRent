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
public class UpdateTransactionStatusException extends Exception {

    /**
     * Creates a new instance of <code>UpdateTransactionStatusException</code>
     * without detail message.
     */
    public UpdateTransactionStatusException() {
    }

    /**
     * Constructs an instance of <code>UpdateTransactionStatusException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateTransactionStatusException(String msg) {
        super(msg);
    }
}

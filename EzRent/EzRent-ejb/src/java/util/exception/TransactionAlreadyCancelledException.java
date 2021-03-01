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
public class TransactionAlreadyCancelledException extends Exception {

    /**
     * Creates a new instance of
     * <code>TransactionAlreadyCancelledException</code> without detail message.
     */
    public TransactionAlreadyCancelledException() {
    }

    /**
     * Constructs an instance of
     * <code>TransactionAlreadyCancelledException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public TransactionAlreadyCancelledException(String msg) {
        super(msg);
    }
}

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
public class CreateNewCreditCardException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewCreditCardException</code>
     * without detail message.
     */
    public CreateNewCreditCardException() {
    }

    /**
     * Constructs an instance of <code>CreateNewCreditCardException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewCreditCardException(String msg) {
        super(msg);
    }
}

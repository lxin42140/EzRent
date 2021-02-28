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
public class CreateNewReviewException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewReviewException</code> without
     * detail message.
     */
    public CreateNewReviewException() {
    }

    /**
     * Constructs an instance of <code>CreateNewReviewException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewReviewException(String msg) {
        super(msg);
    }
}

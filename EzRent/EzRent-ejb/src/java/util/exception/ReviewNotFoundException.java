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
public class ReviewNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ReviewNotFoundException</code> without
     * detail message.
     */
    public ReviewNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ReviewNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ReviewNotFoundException(String msg) {
        super(msg);
    }
}

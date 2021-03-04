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
public class UpdateRequestException extends Exception {

    /**
     * Creates a new instance of <code>UpdateRequestException</code> without
     * detail message.
     */
    public UpdateRequestException() {
    }

    /**
     * Constructs an instance of <code>UpdateRequestException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateRequestException(String msg) {
        super(msg);
    }
}

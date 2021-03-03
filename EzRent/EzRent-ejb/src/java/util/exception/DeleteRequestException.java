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
public class DeleteRequestException extends Exception {

    /**
     * Creates a new instance of <code>DeleteRequestException</code> without
     * detail message.
     */
    public DeleteRequestException() {
    }

    /**
     * Constructs an instance of <code>DeleteRequestException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteRequestException(String msg) {
        super(msg);
    }
}

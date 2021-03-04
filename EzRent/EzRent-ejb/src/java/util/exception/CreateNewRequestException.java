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
public class CreateNewRequestException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewRequestException</code> without
     * detail message.
     */
    public CreateNewRequestException() {
    }

    /**
     * Constructs an instance of <code>CreateNewRequestException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewRequestException(String msg) {
        super(msg);
    }
}

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
public class UpdateAdminFailException extends Exception {

    /**
     * Creates a new instance of <code>UpdateAdminFailException</code> without
     * detail message.
     */
    public UpdateAdminFailException() {
    }

    /**
     * Constructs an instance of <code>UpdateAdminFailException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateAdminFailException(String msg) {
        super(msg);
    }
}

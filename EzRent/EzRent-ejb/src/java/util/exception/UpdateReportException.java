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
public class UpdateReportException extends Exception {

    /**
     * Creates a new instance of <code>UpdateReportException</code> without
     * detail message.
     */
    public UpdateReportException() {
    }

    /**
     * Constructs an instance of <code>UpdateReportException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateReportException(String msg) {
        super(msg);
    }
}

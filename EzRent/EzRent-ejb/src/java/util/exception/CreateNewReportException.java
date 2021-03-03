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
public class CreateNewReportException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewReportException</code> without
     * detail message.
     */
    public CreateNewReportException() {
    }

    /**
     * Constructs an instance of <code>CreateNewReportException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewReportException(String msg) {
        super(msg);
    }
}

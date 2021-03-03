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
public class DeleteDamageReportException extends Exception {

    /**
     * Creates a new instance of <code>DeleteDamageReportException</code>
     * without detail message.
     */
    public DeleteDamageReportException() {
    }

    /**
     * Constructs an instance of <code>DeleteDamageReportException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteDamageReportException(String msg) {
        super(msg);
    }
}

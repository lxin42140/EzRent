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
public class DamageReportNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>DamageReportNotFoundException</code>
     * without detail message.
     */
    public DamageReportNotFoundException() {
    }

    /**
     * Constructs an instance of <code>DamageReportNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DamageReportNotFoundException(String msg) {
        super(msg);
    }
}

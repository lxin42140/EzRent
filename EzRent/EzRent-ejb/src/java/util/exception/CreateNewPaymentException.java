/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author kiyon
 */
public class CreateNewPaymentException extends Exception {
    public CreateNewPaymentException() {
    }
    
    public CreateNewPaymentException(String msg) {
        super(msg);
    }
}

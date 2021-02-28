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
public class CreateNewOfferException extends Exception {
    public CreateNewOfferException() {   
    }
    
    public CreateNewOfferException(String msg) {
        super(msg);
    }
    
}

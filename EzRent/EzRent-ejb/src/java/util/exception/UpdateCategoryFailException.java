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
public class UpdateCategoryFailException extends Exception {
    public UpdateCategoryFailException() {
    }
    
    public UpdateCategoryFailException(String msg) {
        super(msg);
    }
    
}

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
public class DeleteCategoryException extends Exception {
    public DeleteCategoryException() {
    }
    
    public DeleteCategoryException(String msg) {
        super(msg);
    }
}

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
public class CreateNewConversationException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewConversationException</code>
     * without detail message.
     */
    public CreateNewConversationException() {
    }

    /**
     * Constructs an instance of <code>CreateNewConversationException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewConversationException(String msg) {
        super(msg);
    }
}

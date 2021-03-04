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
public class ConversationNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ConversationNotFoundException</code>
     * without detail message.
     */
    public ConversationNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ConversationNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConversationNotFoundException(String msg) {
        super(msg);
    }
}

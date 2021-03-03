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
public class DeleteConversationException extends Exception {

    /**
     * Creates a new instance of <code>DeleteConversationException</code>
     * without detail message.
     */
    public DeleteConversationException() {
    }

    /**
     * Constructs an instance of <code>DeleteConversationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteConversationException(String msg) {
        super(msg);
    }
}

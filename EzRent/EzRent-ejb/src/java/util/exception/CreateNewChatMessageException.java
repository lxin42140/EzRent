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
public class CreateNewChatMessageException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewChatMessageException</code>
     * without detail message.
     */
    public CreateNewChatMessageException() {
    }

    /**
     * Constructs an instance of <code>CreateNewChatMessageException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewChatMessageException(String msg) {
        super(msg);
    }
}

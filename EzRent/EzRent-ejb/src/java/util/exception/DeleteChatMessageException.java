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
public class DeleteChatMessageException extends Exception {

    /**
     * Creates a new instance of <code>DeleteChatMessageException</code> without
     * detail message.
     */
    public DeleteChatMessageException() {
    }

    /**
     * Constructs an instance of <code>DeleteChatMessageException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteChatMessageException(String msg) {
        super(msg);
    }
}

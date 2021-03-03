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
public class ChatMessageNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ChatMessageNotFoundException</code>
     * without detail message.
     */
    public ChatMessageNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ChatMessageNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ChatMessageNotFoundException(String msg) {
        super(msg);
    }
}

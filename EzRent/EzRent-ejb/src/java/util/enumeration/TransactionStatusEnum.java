/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

/**
 *
 * @author Yuxin
 */
public enum TransactionStatusEnum {

    PENDING_PAYMENT,// for cash on delivery
    PAID,
    RECEIVED, // received and ongoing are the same
    COMPLETED,
    CANCELLED
}

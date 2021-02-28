/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaymentEntity;
import entity.TransactionEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.TransactionStatusEnum;
import util.exception.CreateNewTransactionException;
import util.exception.TransactionAlreadyCancelledException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateTransactionStatusException;

/**
 *
 * @author Yuxin
 */
@Local
public interface TransactionEntitySessionBeanLocal {
    
    public TransactionEntity createNewTransaction(Long offerId, PaymentEntity newPayment, Long creditCardId, Long deliveryId, Long reviewId, TransactionEntity newTransaction) throws CreateNewTransactionException;
    
    public List<TransactionEntity> retrieveAllTransactions();

    public List<TransactionEntity> retrieveAllValidTransactions();

    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException;

    public void cancelTransaction(Long transactionId) throws TransactionNotFoundException, TransactionAlreadyCancelledException;

    public void automateTransactionStatus();

    public void updateTransactionStatus(Long transactionId, TransactionStatusEnum transactionStatus) throws UpdateTransactionStatusException, TransactionNotFoundException;

}

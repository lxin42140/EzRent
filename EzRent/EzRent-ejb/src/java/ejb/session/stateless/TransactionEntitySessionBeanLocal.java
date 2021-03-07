/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransactionEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTransactionException;
import util.exception.OfferNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateTransactionStatusException;

/**
 *
 * @author Yuxin
 */
@Local
public interface TransactionEntitySessionBeanLocal {

    public Long markTransactionPaid(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException;

    public Long markTransactionReceived(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException;

    public Long markTransactionCompleted(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException;

    public Long markTransactionCancelled(Long transactionId) throws TransactionNotFoundException, UpdateTransactionStatusException;

    public TransactionEntity retrieveTransactionByTransactionId(Long transactionId) throws TransactionNotFoundException;

    public List<TransactionEntity> retrieveAllActiveTransactions();

    public Long createNewTransaction(Long offerId, TransactionEntity newTransaction) throws CreateNewTransactionException, OfferNotFoundException;
   
}

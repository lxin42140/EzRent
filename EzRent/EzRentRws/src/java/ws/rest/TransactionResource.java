/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.TransactionEntitySessionBeanLocal;
import entity.TransactionEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author kiyon
 */
@Path("Transaction")
public class TransactionResource {

    TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal = lookupTransactionEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TransactionResource
     */
    public TransactionResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPendingDeliveryTransactions() {
        try {
            List<TransactionEntity> transactions = transactionEntitySessionBeanLocal.retrieveAllPendingDeliveryTransactions();
            for(TransactionEntity transactionEntity : transactions) {
                transactionEntity.setDelivery(null);
                transactionEntity.setOffer(null);
                transactionEntity.setPayment(null);
                transactionEntity.setReviews(null);
            }
            GenericEntity<List<TransactionEntity>> genericEntity = new GenericEntity<List<TransactionEntity>>(transactions) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
			

    private TransactionEntitySessionBeanLocal lookupTransactionEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TransactionEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/TransactionEntitySessionBean!ejb.session.stateless.TransactionEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

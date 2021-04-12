/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CreateNewDeliveryException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.TransactionNotFoundException;
import ws.datamodel.CreateDeliveryReq;

/**
 * REST Web Service
 *
 * @author Li Xin
 */
@Path("Delivery")
public class DeliveryResource {

    DeliveryEntitySessionBeanLocal deliveryEntitySessionBean = lookupDeliveryEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public DeliveryResource() {
    }

    @Path("createDelivery")
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDelivery(CreateDeliveryReq createDelivery
    ) {
        try {
            Long deliveryEntityId = deliveryEntitySessionBean.createNewDelivery(createDelivery.getNewDeliveryEntity(), createDelivery.getTransactionId(), createDelivery.getDeliveryCompanyId());
            return Response.status(Status.OK).entity(deliveryEntityId).build();
        } catch (CreateNewDeliveryException | DeliveryCompanyNotFoundException | TransactionNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    private DeliveryEntitySessionBeanLocal lookupDeliveryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DeliveryEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/DeliveryEntitySessionBean!ejb.session.stateless.DeliveryEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

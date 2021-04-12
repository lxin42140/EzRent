/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.DeliveryCompanyEntitySessionBeanLocal;
import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import entity.DeliveryCompanyEntity;
import entity.DeliveryEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.enumeration.DeliveryStatusEnum;
import util.exception.CreateNewDeliveryException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.DeliveryNotFoundException;
import util.exception.TransactionNotFoundException;
import util.exception.UpdateDeliveryException;
import ws.datamodel.CreateDeliveryReq;

/**
 * REST Web Service
 *
 * @author Li Xin
 */
@Path("Delivery")
public class DeliveryResource {

    DeliveryCompanyEntitySessionBeanLocal deliveryCompanyEntitySessionBean = lookupDeliveryCompanyEntitySessionBeanLocal();

    DeliveryEntitySessionBeanLocal deliveryEntitySessionBean = lookupDeliveryEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public DeliveryResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDelivery(CreateDeliveryReq createDelivery) {
        try {
            Long deliveryEntityId = deliveryEntitySessionBean.createNewDelivery(createDelivery.getNewDeliveryEntity(), createDelivery.getTransactionId(), createDelivery.getDeliveryCompanyId());
            return Response.status(Status.OK).entity(deliveryEntityId).build();
        } catch (CreateNewDeliveryException | DeliveryCompanyNotFoundException | TransactionNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDeliveryStatus(@QueryParam("deliveryId") Long deliveryId, @QueryParam("deliveryStatus") String deliveryStatus) {
        try {
            DeliveryStatusEnum newDeliveryEnum = null;
            switch (deliveryStatus) {
                case "SHIPPED":
                    newDeliveryEnum = DeliveryStatusEnum.SHIPPED;
                    break;
                case "LOST":
                    newDeliveryEnum = DeliveryStatusEnum.LOST;
                    break;
                case "DELIVERED":
                    newDeliveryEnum = DeliveryStatusEnum.DELIVERED;
                    break;
                default:
                    break;
            }

            DeliveryEntity updatedDelivery = deliveryEntitySessionBean.updateDeliveryStatus(deliveryId, newDeliveryEnum);
            updatedDelivery.setDeliveryCompany(null);
            updatedDelivery.setTransaction(null);
            return Response.status(Status.OK).entity(updatedDelivery).build();
        } catch (DeliveryNotFoundException | UpdateDeliveryException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllDeliveries(@QueryParam("deliveryCompanyId") Long deliveryCompanyId) {
        try {
            List<DeliveryEntity> deliveryEntities = deliveryEntitySessionBean.retrieveAllDeliveriesByCompanyId(deliveryCompanyId);
            for (DeliveryEntity deliveryEntity : deliveryEntities) {
                deliveryEntity.setDeliveryCompany(null);
                deliveryEntity.setTransaction(null);
            }
            GenericEntity<List<DeliveryEntity>> genericEntity = new GenericEntity<List<DeliveryEntity>>(deliveryEntities) {
            };
            return Response.status(Status.OK).entity(deliveryEntities).build();
        } catch (Exception ex) {
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

    private DeliveryCompanyEntitySessionBeanLocal lookupDeliveryCompanyEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DeliveryCompanyEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/DeliveryCompanyEntitySessionBean!ejb.session.stateless.DeliveryCompanyEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

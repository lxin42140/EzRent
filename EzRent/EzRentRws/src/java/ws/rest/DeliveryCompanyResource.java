/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.DeliveryCompanyEntitySessionBeanLocal;
import entity.DeliveryCompanyEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginException;
import ws.datamodel.LoginHelper;

/**
 * REST Web Service
 *
 * @author kiyon
 */
@Path("DeliveryCompany")
public class DeliveryCompanyResource {

    DeliveryCompanyEntitySessionBeanLocal deliveryCompanyEntitySessionBeanLocal = lookupDeliveryCompanyEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DeliveryCompanyResource
     */
    public DeliveryCompanyResource() {
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deliveryCompanyLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            DeliveryCompanyEntity deliveryCompanyEntity = deliveryCompanyEntitySessionBeanLocal.retrieveDeliveryCompanyByUsernameAndPassword(username, password);
            deliveryCompanyEntity.setPassword(null);
            deliveryCompanyEntity.setDeliveries(null);
            deliveryCompanyEntity.setSalt(null);
            deliveryCompanyEntity.setAccessRight(null);

            return Response.status(Status.OK).entity(deliveryCompanyEntity).build();
        } catch (InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.AdminstratorEntitySessionBeanLocal;
import entity.AdministratorEntity;
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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.AdminNotFoundException;
import util.exception.InvalidLoginException;
import ws.datamodel.CreateAdminReq;

/**
 * REST Web Service
 *
 * @author Yuxin
 */
@Path("Admin")
public class AdminResource {

    AdminstratorEntitySessionBeanLocal adminstratorEntitySessionBeanLocal = lookupAdminstratorEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public AdminResource() {
    }

    @Path("adminLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response adminLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            return Response.status(Status.OK).entity(admin).build();
        } catch (AdminNotFoundException | InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

//    @Path("adminLogout")
//    @GET
//    @Consumes(MediaType.TEXT_PLAIN)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response adminLogout(@QueryParam("username") String username,
//            @QueryParam("password") String password) {
//
//        try {
//            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
//            return Response.status(Status.OK).entity(admin).build();
//        } catch (AdminNotFoundException | InvalidLoginException ex) {
//            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//        }
//    }
    @Path("createAdminAcc")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAdmin(CreateAdminReq createAdmin) {
        if (createAdmin != null) {
            try {
                AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(createAdmin.getUsername(), createAdmin.getPassword());
                System.out.println("********** AdminResource.createNewAdmin(): Staff " + admin.getUserName() + " login remotely via web service");

                Long adminId = adminstratorEntitySessionBeanLocal.createNewAdminstrator(createAdmin.getNewAdmin());

                return Response.status(Status.OK).entity(createAdmin).build();
            } catch (InvalidLoginException | AdminNotFoundException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity("Invalid create new Admin request").build();
        }
    }

    @Path("updateAdminStatus/{adminId}/{newAdminStatus}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAdminStatus(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("adminId") Long adminId,
            @PathParam("newAdminStatus") Boolean newAdminStatus) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            System.out.println("********** AdminResource.updateAdminStatus(): Staff " + admin.getUserName() + " login remotely via web service");
            Long updatedAdminId = adminstratorEntitySessionBeanLocal.updateAdminStatus(adminId, newAdminStatus);

            return Response.status(Status.OK).entity(updatedAdminId).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private AdminstratorEntitySessionBeanLocal lookupAdminstratorEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (AdminstratorEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/AdminstratorEntitySessionBean!ejb.session.stateless.AdminstratorEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

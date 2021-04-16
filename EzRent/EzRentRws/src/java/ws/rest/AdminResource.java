/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.AdminstratorEntitySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.DeliveryCompanyEntitySessionBeanLocal;
import entity.AdministratorEntity;
import entity.CustomerEntity;
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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.enumeration.UserAccessRightEnum;
import util.exception.AdminNotFoundException;
import util.exception.CreateNewDeliveryCompanyException;
import util.exception.DeliveryCompanyNotFoundException;
import util.exception.InvalidLoginException;
import ws.datamodel.CreateAdminReq;
import ws.datamodel.CreateDeliveryCompanyReq;

/**
 * REST Web Service
 *
 * @author Yuxin
 */
@Path("Admin")
public class AdminResource {

    CustomerEntitySessionBeanLocal customerEntitySessionBean = lookupCustomerEntitySessionBeanLocal();

    DeliveryCompanyEntitySessionBeanLocal deliveryCompanyEntitySessionBeanLocal = lookupDeliveryCompanyEntitySessionBeanLocal();

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
            admin.setPassword(null);
            admin.setSalt(null);
            return Response.status(Status.OK).entity(admin).build();
        } catch (AdminNotFoundException | InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllAdmin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllAdmin(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            List<AdministratorEntity> admins = adminstratorEntitySessionBeanLocal.retrieveAllAdminstrators();
            GenericEntity<List<AdministratorEntity>> genericEntity = new GenericEntity<List<AdministratorEntity>>(admins) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (AdminNotFoundException | InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }
    
    @Path("retrieveAllCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCustomer(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            List<CustomerEntity> customers = customerEntitySessionBean.retrieveAllCustomers();
            GenericEntity<List<CustomerEntity>> genericEntity = new GenericEntity<List<CustomerEntity>>(customers) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (AdminNotFoundException | InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveAllDeliveryCompanies")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllDeliveryCompanies(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            List<DeliveryCompanyEntity> deliveryCompanies = deliveryCompanyEntitySessionBeanLocal.retrieveAllDeliveryCompanies();
            for (DeliveryCompanyEntity deliveryCompanyEntity : deliveryCompanies) {
                deliveryCompanyEntity.setDeliveries(null);
                deliveryCompanyEntity.setPassword(null);
                deliveryCompanyEntity.setSalt(null);
            }
            GenericEntity<List<DeliveryCompanyEntity>> genericEntity = new GenericEntity<List<DeliveryCompanyEntity>>(deliveryCompanies) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (AdminNotFoundException | InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }
    }

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
                
                AdministratorEntity returnAdmin = adminstratorEntitySessionBeanLocal.retrieveAdminByAdminId(adminId);

                return Response.status(Status.OK).entity(returnAdmin).build();
            } catch (InvalidLoginException | AdminNotFoundException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity("Invalid create new Admin request").build();
        }
    }

    @Path("createDeliveryAcc")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewDeliveryCompany(CreateDeliveryCompanyReq createDeliveryCompany) {
        if (createDeliveryCompany != null) {
            try {
                AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(createDeliveryCompany.getUsername(), createDeliveryCompany.getPassword());
                System.out.println("********** AdminResource.createDeliveryAcc(): Staff " + admin.getUserName() + " login remotely via web service");
                
                DeliveryCompanyEntity beforeDeliveryCompany = createDeliveryCompany.getNewDeliveryCompany();
                beforeDeliveryCompany.setAccessRight(UserAccessRightEnum.DELIVERY_COMPANY);

                DeliveryCompanyEntity deliveryCompany = deliveryCompanyEntitySessionBeanLocal.createNewDeliveryCompany(beforeDeliveryCompany);
                deliveryCompany.setDeliveries(null);
                return Response.status(Status.OK).entity(deliveryCompany).build();
            } catch (InvalidLoginException | AdminNotFoundException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (CreateNewDeliveryCompanyException ex) {
                return Response.status(Status.BAD_REQUEST).entity("Invalid create new Delivery Company request").build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity("Invalid create new Delivery Company request").build();
        }
    }

    @Path("updateAdminStatus")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAdminStatus(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("adminId") Long adminId,
            @QueryParam("newAdminStatus") Boolean newAdminStatus) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            AdministratorEntity updatedAdmin = adminstratorEntitySessionBeanLocal.updateAdminStatus(adminId, newAdminStatus);
            updatedAdmin.setPassword(null);
            updatedAdmin.setSalt(null);
            return Response.status(Status.OK).entity(updatedAdmin).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    @Path("updateCustomerStatus")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerStatus(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("customerId") Long customerId,
            @QueryParam("newCustomerStatus") Boolean newCustomerStatus) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            CustomerEntity updatedCustomer = customerEntitySessionBean.updateCustomerStatus(customerId, newCustomerStatus);
            updatedCustomer.setPassword(null);
            updatedCustomer.setSalt(null);
            return Response.status(Status.OK).entity(updatedCustomer).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("updateDeliveryCompanyStatus")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDeliveryCompanyStatus(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("deliveryCompanyId") Long deliveryCompanyId,
            @QueryParam("isDisabled") Boolean isDisabled
    ) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBeanLocal.retrieveAdminByUsernameAndPassword(username, password);
            DeliveryCompanyEntity deliveryCompanyEntity = deliveryCompanyEntitySessionBeanLocal.updateDeliveryCompanyAccountStatus(deliveryCompanyId, isDisabled);
            deliveryCompanyEntity.setDeliveries(null);
            deliveryCompanyEntity.setPassword(null);
            deliveryCompanyEntity.setSalt(null);
            return Response.status(Status.OK).entity(deliveryCompanyEntity).build();
        } catch (DeliveryCompanyNotFoundException | AdminNotFoundException | InvalidLoginException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
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

    private DeliveryCompanyEntitySessionBeanLocal lookupDeliveryCompanyEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DeliveryCompanyEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/DeliveryCompanyEntitySessionBean!ejb.session.stateless.DeliveryCompanyEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomerEntitySessionBeanLocal lookupCustomerEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/CustomerEntitySessionBean!ejb.session.stateless.CustomerEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}

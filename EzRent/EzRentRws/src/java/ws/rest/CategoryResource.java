/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.AdminstratorEntitySessionBeanLocal;
import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import entity.AdministratorEntity;
import entity.CategoryEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.AdminNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InvalidLoginException;
import ws.datamodel.CreateCategoryReq;
import ws.datamodel.CreateCategoryWithParentReq;

/**
 * REST Web Service
 *
 * @author Li Xin
 */
@Path("Category")
public class CategoryResource {

    AdminstratorEntitySessionBeanLocal adminstratorEntitySessionBean = lookupAdminstratorEntitySessionBeanLocal();

    CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal = lookupCategoryEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public CategoryResource() {
    }

    @Path("retrieveAllCategories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
            System.out.println("********** CategoryResource.retrieveAllCategories(): Staff " + admin.getUserName() + " login remotely via web service");
            List<CategoryEntity> categories = categoryEntitySessionBeanLocal.retrieveAllCategory();

            for (CategoryEntity category : categories) {
                if (category.getParentCategory() != null) {
                    category.getParentCategory().getSubCategories().clear();
                }
                category.getSubCategories().clear();
            }

            GenericEntity<List<CategoryEntity>> genericEntity = new GenericEntity<List<CategoryEntity>>(categories) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("retrieveCategory/{categoryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCategory(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("categoryId") Long categoryId) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
            System.out.println("********** CategoryResource.retrieveCategory(): Staff " + admin.getUserName() + " login remotely via web service");
            CategoryEntity category = categoryEntitySessionBeanLocal.retrieveCategoryById(categoryId);
            if (category.getParentCategory() != null) {
                category.getParentCategory().getSubCategories().clear();
            }
            return Response.status(Status.OK).entity(category).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CategoryNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("createNewRootCategory")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRootCategory(CreateCategoryReq createCategory) {
        if (createCategory != null) {
            try {
                AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(createCategory.getUsername(), createCategory.getPassword());
                System.out.println("********** CategoryResource.createNewRootCategory(): Staff " + admin.getUserName() + " login remotely via web service");
                Long categoryId = categoryEntitySessionBeanLocal.createNewCategoryWithoutParentCategory(createCategory.getNewCategoryEntity());

                return Response.status(Response.Status.OK).entity(categoryId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new category request").build();
        }
    }

    @Path("createNewRootCategoryWithParentCategory")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewRootCategoryWithParentCategory(CreateCategoryWithParentReq createCategoryWithParent) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(createCategoryWithParent.getUsername(), createCategoryWithParent.getPassword());
            System.out.println("********** CategoryResource.createNewRootCategoryWithParentCategory(): Staff " + admin.getUserName() + " login remotely via web service");
            Long categoryId = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(createCategoryWithParent.getNewCategoryEntity(), createCategoryWithParent.getParentCategoryId());

            return Response.status(Response.Status.OK).entity(categoryId).build();
        } catch (CategoryNotFoundException | CreateNewCategoryException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("updateRootCategoryName/{categoryId}/{newCategoryName}")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategoryName(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("categoryId") Long categoryId,
            @PathParam("newCategoryName") String newCategoryName) {
        if (newCategoryName != null || newCategoryName.length() == 0) {
            try {
                AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
                System.out.println("********** CategoryResource.updateCategoryName(): Staff " + admin.getUserName() + " login remotely via web service");
                Long updatedCategoryId = categoryEntitySessionBeanLocal.updateCategoryName(categoryId, newCategoryName);

                return Response.status(Response.Status.OK).entity(updatedCategoryId).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update of Category request").build();
        }
    }

    @Path("{categoryId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("categoryId") Long categoryId) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
            System.out.println("********** CategoryResource.updateCategoryName(): Staff " + admin.getUserName() + " login remotely via web service");
            categoryEntitySessionBeanLocal.deleteLeafCategory(categoryId);
            return Response.status(Status.OK).build();
        } catch (InvalidLoginException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (CategoryNotFoundException | DeleteCategoryException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private CategoryEntitySessionBeanLocal lookupCategoryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategoryEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/CategoryEntitySessionBean!ejb.session.stateless.CategoryEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
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

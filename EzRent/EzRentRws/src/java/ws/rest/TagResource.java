/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.AdminstratorEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.AdministratorEntity;
import entity.CategoryEntity;
import entity.TagEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import util.exception.AdminNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.DeleteTagException;
import util.exception.InvalidLoginException;
import util.exception.TagNotFoundException;
import ws.datamodel.CreateTagReq;

/**
 * REST Web Service
 *
 * @author Yuxin
 */
@Path("Tag")
public class TagResource {

    AdminstratorEntitySessionBeanLocal adminstratorEntitySessionBean = lookupAdminstratorEntitySessionBeanLocal();

    TagEntitySessionBeanLocal tagEntitySessionBean = lookupTagEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    public TagResource() {
    }

    @Path("retrieveAllTags")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTags(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
            System.out.println("********** TagResource.retrieveAllTags(): Staff " + admin.getUserName() + " login remotely via web service");

            List<TagEntity> tags = tagEntitySessionBean.retrieveAllTags();

            GenericEntity<List<TagEntity>> genericEntity = new GenericEntity<List<TagEntity>>(tags) {
            };

            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

//    @Path("retrieveTag/{tagId}")
//    @GET
//    @Consumes(MediaType.TEXT_PLAIN)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response retrieveTag(@QueryParam("username") String username,
//            @QueryParam("password") String password,
//            @PathParam("tagId") Long tagId) {
//        try {
//            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
//            System.out.println("********** TagResource.retrieveTag(): Staff " + admin.getUserName() + " login remotely via web service");
//            TagEntity tag = tagEntitySessionBean.retrieveTagByTagId(tagId);
//            return Response.status(Status.OK).entity(tag).build();
//        } catch (InvalidLoginException | AdminNotFoundException ex) {
//            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
//        } catch (TagNotFoundException ex) {
//            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
//        }
//    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewTag(CreateTagReq createTag) {
        if (createTag != null) {
            try {
                AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(createTag.getUsername(), createTag.getPassword());
                System.out.println("********** TagResource.retrieveTag(): Staff " + admin.getUserName() + " login remotely via web service");

                TagEntity tag = tagEntitySessionBean.createNewTag(createTag.getNewTagEntity());

                return Response.status(Status.OK).entity(tag).build();
            } catch (InvalidLoginException | AdminNotFoundException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity("Invalid create new tag request").build();
        }
    }

    @Path("updateTagName")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTagName(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("tagId") Long tagId,
            @QueryParam("newTagName") String newTagName) {
        if (newTagName != null || newTagName.length() == 0) {
            try {
                AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
                System.out.println("********** TagResource.updateTagName(): Staff " + admin.getUserName() + " login remotely via web service");
                TagEntity tagEntity = tagEntitySessionBean.updateTagName(tagId, newTagName);

                return Response.status(Status.OK).entity(tagEntity).build();
            } catch (InvalidLoginException | AdminNotFoundException ex) {
                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Status.BAD_REQUEST).entity("Invalid update of Tag request").build();
        }
    }
    
    @Path("deleteTag")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTag(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @QueryParam("tagId") Long tagId) {
        try {
            AdministratorEntity admin = adminstratorEntitySessionBean.retrieveAdminByUsernameAndPassword(username, password);
            System.out.println("********** TagResource.deleteTag(): Staff " + admin.getUserName() + " login remotely via web service");

            tagEntitySessionBean.deleteTag(tagId);
            return Response.status(Status.OK).build();
        } catch (InvalidLoginException | AdminNotFoundException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (TagNotFoundException | DeleteTagException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    private TagEntitySessionBeanLocal lookupTagEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TagEntitySessionBeanLocal) c.lookup("java:global/EzRent/EzRent-ejb/TagEntitySessionBean!ejb.session.stateless.TagEntitySessionBeanLocal");
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

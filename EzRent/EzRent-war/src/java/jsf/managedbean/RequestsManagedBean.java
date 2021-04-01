/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RequestEntitySessionBeanLocal;
import entity.CustomerEntity;
import entity.RequestEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.RequestUrgencyEnum;
import util.exception.CreateNewRequestException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteRequestException;
import util.exception.FavouriteRequestException;
import util.exception.RequestNotFoundException;

/**
 *
 * @author Li Xin
 */
@Named(value = "requestsManagedBean")
@ViewScoped
public class RequestsManagedBean implements Serializable {

    @EJB
    private RequestEntitySessionBeanLocal requestEntitySessionBeanLocal;

    //view all request
    private List<RequestEntity> requests;

    //create new request
    private RequestEntity newRequestEntity;
    private final List<String> requestUrgencyStates = Arrays.asList("Urgent", "Medium", "Low");
    private String selectedUrgencyState;
    private CustomerEntity currentCustomer;

    public RequestsManagedBean() {
        this.newRequestEntity = new RequestEntity();
        this.selectedUrgencyState = "";
    }

    @PostConstruct
    public void postConstruct() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer") != null) {
            this.currentCustomer = (CustomerEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        }
        this.requests = requestEntitySessionBeanLocal.retrieveAllRequests();
    }

    public void createNewRequest(ActionEvent event) {
        try {
            if (this.currentCustomer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            switch (selectedUrgencyState) {
                case "Urgent":
                    this.newRequestEntity.setRequestUrgencyEnum(RequestUrgencyEnum.URGENT);
                    break;
                case "Medium":
                    this.newRequestEntity.setRequestUrgencyEnum(RequestUrgencyEnum.MEDIUM);
                    break;
                case "Low":
                    this.newRequestEntity.setRequestUrgencyEnum(RequestUrgencyEnum.LOW);
                    break;
                default:
                    break;
            }

            if (!this.validateNewRequestDates()) {
                return;
            }
            this.newRequestEntity.setDatePosted(new Date());

            RequestEntity createdRequest = requestEntitySessionBeanLocal.createNewRequest(this.currentCustomer.getUserId(), newRequestEntity);
            this.requests.add(createdRequest);

            //reset
            this.selectedUrgencyState = "";
            this.newRequestEntity = new RequestEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New request successfully created!", null));
        } catch (IOException | CreateNewRequestException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new request: " + ex.getMessage(), null));
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();
            this.newRequestEntity.setFilePathName(event.getFile().getFileName());

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void deleteRequest(ActionEvent event) {
        try {
            if (this.currentCustomer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            RequestEntity requestToDelete = (RequestEntity) event.getComponent().getAttributes().get("requestToDelete");
            requestEntitySessionBeanLocal.deleteRequest(requestToDelete.getRequestId());
            this.requests.remove(requestToDelete);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request successfully deleted!", null));
        } catch (IOException | DeleteRequestException | RequestNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the request: " + ex.getMessage(), null));
        }
    }

    public void toogleLikeRequest(ActionEvent event) {
        try {
            if (this.currentCustomer == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileAdmin/loginPage.xhtml");
            }

            RequestEntity requestToLikeDislike = (RequestEntity) event.getComponent().getAttributes().get("requestToLikeDislike");

            if (requestToLikeDislike.getCustomer().equals(currentCustomer)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unable to like own request!", null));
                return;
            }

            requestEntitySessionBeanLocal.toggleRequestLikeDislike(this.currentCustomer.getUserId(), requestToLikeDislike.getRequestId());

            //update list
            this.requests.remove(requestToLikeDislike);
            this.requests.add(requestEntitySessionBeanLocal.retrieveRequestByRequestId(requestToLikeDislike.getRequestId()));
        } catch (IOException | RequestNotFoundException | CustomerNotFoundException | FavouriteRequestException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong while trying to like the request! " + ex.getMessage(), null));
        }
    }

    private boolean validateNewRequestDates() {
        if (newRequestEntity.getRequiredStartDate().after(newRequestEntity.getRequiredEndDate())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request end date needs to be after the request start date!", null));
            return false;
        } else if (newRequestEntity.getRequiredEndDate().before(new Date())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request end date needs to be after today!", null));
            return false;
        }
        return true;
    }

    public RequestEntity getNewRequestEntity() {
        return newRequestEntity;
    }

    public void setNewRequestEntity(RequestEntity newRequestEntity) {
        this.newRequestEntity = newRequestEntity;
    }

    public List<String> getRequestUrgencyStates() {
        return requestUrgencyStates;
    }

    public List<RequestEntity> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestEntity> requests) {
        this.requests = requests;
    }

    public CustomerEntity getCurrentCustomer() {
        return currentCustomer;
    }

    public String getSelectedUrgencyState() {
        return selectedUrgencyState;
    }

    public void setSelectedUrgencyState(String selectedUrgencyState) {
        this.selectedUrgencyState = selectedUrgencyState;
    }

}

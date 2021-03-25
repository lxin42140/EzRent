package jsf.managedbean;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.CustomerEntity;
import java.io.IOException;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.UserAccessRightEnum;
import util.exception.CreateNewCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginException;

/**
 *
 * @author Yuxin
 */
@Named(value = "loginManagedBean")
@RequestScoped
public class LoginManagedBean {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @NotNull
    @Size(min = 4, max = 32)
    private String username;
    @NotNull
    @Size(min = 4, max = 32)
    private String password;
    
    private CustomerEntity newCustomer;
    
    @NotNull(message = "First name is required")
    @Size(max = 32)
    private String firstName;
    
    @NotNull(message = "Last name is required")
    @Size(max = 32)
    private String lastName;
    
    @NotNull(message = "Email is required")
    @Email
    private String email;
    
    @NotNull(message = "Address is required")
    @Size(max = 128)
    private String streetAdd;
    
    @NotNull(message = "Postal code is required")
    @Size(max = 64)
    private String postal;
    
    @NotNull(message = "Username is required")
    @Size(min = 4, max = 32)
    private String newUsername;
    
    @NotNull(message = "Password is required")
    @Size(min = 4, max = 32)
    private String newPassword;
    

    public LoginManagedBean() {
    }
    
    

    public void login(ActionEvent event) throws IOException {
        try {

            CustomerEntity currentCustomer = customerEntitySessionBeanLocal.retrieveCustomerByUsernameAndPassword(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomer", currentCustomer);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Successfully!", null));
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            facesContext.getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (InvalidLoginException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));

        }
    }

    public void logout() throws IOException {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout Successfully!", null));
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }
    
    public void createCustomer(ActionEvent event) throws IOException{
        try {
            newCustomer = new CustomerEntity(streetAdd, postal, new Date(), "", 0.0, newUsername, email, firstName, lastName, UserAccessRightEnum.CUSTOMER, false, false, newPassword);
            Long customerId = customerEntitySessionBeanLocal.createNewCustomer(newCustomer);
            newCustomer = new CustomerEntity();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Flash flash = facesContext.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered successfully, please kindly login to activate your account (Customer ID: " + customerId + ")", null));
            facesContext.getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch(CreateNewCustomerException ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while registering: " + ex.getMessage(), null));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreetAdd() {
        return streetAdd;
    }

    public void setStreetAdd(String streetAdd) {
        this.streetAdd = streetAdd;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    

}
//package jsf.managedbean;
//
//import ejb.session.stateless.StaffEntitySessionBeanLocal;
//import entity.StaffEntity;
//import java.io.IOException;
//import javax.ejb.EJB;
//import javax.inject.Named;
//import javax.enterprise.context.RequestScoped;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//import javax.servlet.http.HttpSession;
//import util.exception.InvalidLoginCredentialException;
//
//
//
//@Named(value = "loginManagedBean")
//@RequestScoped
//
//public class LoginManagedBean 
//{
//    @EJB
//    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
//    
//    private String username;
//    private String password;
//    
//    
//    
//    public LoginManagedBean() 
//    {
//    }
//    
//    
//    
//    public void login(ActionEvent event) throws IOException
//    {
//        try
//        {
//            StaffEntity currentStaffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentStaffEntity", currentStaffEntity);
//            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
//        }
//        catch(InvalidLoginCredentialException ex)
//        {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
//        }
//    }
//    
//    
//    
//    public void logout(ActionEvent event) throws IOException
//    {
//        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
//        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
//    }
//
//    
//    
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//}

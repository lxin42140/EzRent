package jsf.managedbean;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.CustomerEntity;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    
    @NotNull(message = "Username is required")
    @Size(min = 4, max = 32)
    private String username;
    @NotNull(message = "Password is required")
    @Size(min = 4, max = 32)
    private String password;
    
    
    public LoginManagedBean() {
    }
    
    public void login(ActionEvent event) throws IOException {
        try {
            CustomerEntity currentCustomer = customerEntitySessionBeanLocal.retrieveCustomerByUsernameAndPassword(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomer", currentCustomer);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
            
        } catch(InvalidLoginException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));

        }
    }
    public void logout(ActionEvent event) throws IOException {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
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
    
    
}

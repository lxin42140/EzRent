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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

//import ejb.session.stateless.RecordEntitySessionBeanLocal;
//import entity.RecordEntity;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Li Xin
 */
@Named(value = "createNewRecordManagedBean")
@RequestScoped
public class CreateNewRecordManagedBean {

    //@EJB
    //private RecordEntitySessionBeanLocal recordEntitySessionBeanLocal;
    
    //private RecordEntity newRecord;
    

    /**
     * Creates a new instance of CreateNewRecordManagedBean
     */
    public CreateNewRecordManagedBean() {
        
        //newRecord = new RecordEntity();
    }
    
    // if nothing is being passed from ajax, can remove AjaxBehaviorEvent
    // however, cannot use actionevent as parameter
    public void createNewRecord(AjaxBehaviorEvent event)
    {
        //Long newRecordId = recordEntitySessionBeanLocal.createNewRecord(newRecord);
        
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New record created successfully", "New Record ID: " + newRecordId));
        
        System.out.println("********** CreateNewRecordManagedBean.createNewRecord: " + event.getComponent().getAttributes().get("test"));
    }
    
//    public RecordEntity getNewRecord() {
//        return newRecord;
//    }
//
//    public void setNewRecord(RecordEntity newRecord) {
//        this.newRecord = newRecord;
//    }
    
}

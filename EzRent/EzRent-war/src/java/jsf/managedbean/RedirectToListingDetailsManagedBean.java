/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Li Xin
 */
@Named(value = "redirectToListingDetailsManagedBean")
@RequestScoped
public class RedirectToListingDetailsManagedBean {

    public RedirectToListingDetailsManagedBean() {
    }

    public void viewListingDetails(ActionEvent event) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedListingIdToView", (Long) event.getComponent().getAttributes().get("selectedListingIdToView"));
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/listingOperations/listingDetails.xhtml");
        } catch (IOException ex) {
        }
    }

}

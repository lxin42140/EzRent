/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Li Xin
 */
@Named(value = "searchManagedBean")
@RequestScoped
public class SearchManagedBean {

    /**
     * Creates a new instance of SearchManagedBean
     */
    public SearchManagedBean() {
    }

}

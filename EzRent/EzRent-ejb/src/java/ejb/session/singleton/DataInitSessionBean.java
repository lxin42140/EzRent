/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.UserEntitySessionBeanLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Li Xin
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    // to create simulated user
    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    @PostConstruct
    public void postConstruct() {
         
    }
}

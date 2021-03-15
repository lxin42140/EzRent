/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.CustomerEntity;
import entity.UserEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.UserAccessRightEnum;

/**
 *
 * @author Li Xin
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            if (em.find(UserEntity.class, 1L) == null) {
                initData();
            }     
        } catch(ParseException ex) {
        }
    }

    private void initData() throws ParseException {
        UserEntity user1 = new UserEntity("user1", "anc@email.com", "john", "smith", UserAccessRightEnum.CUSTOMER, true, true, "password");
        String date = "10022021";
        Date joinedDate = new SimpleDateFormat("ddMMyyyy").parse(date);
        UserEntity user2 = new CustomerEntity("testing test 123", "123456", joinedDate, "N/A", 0.0, "customer1", "cust@mail.com", "John", "Doe", UserAccessRightEnum.CUSTOMER, true, true, "password");
        
        em.persist(user1);
        em.persist(user2);
        em.flush();
    }
}

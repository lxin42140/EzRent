/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.CategoryEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AvailabilityEnum;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.ModeOfPaymentEnum;
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
        } catch (ParseException ex) {
        }
    }

    private void initData() throws ParseException {
        /*INIT USER*/
        UserEntity user1 = new UserEntity("user1", "anc@email.com", "john", "smith", UserAccessRightEnum.CUSTOMER, true, true, "password");
        String date = "10022021";
        Date joinedDate = new SimpleDateFormat("ddMMyyyy").parse(date);
        em.persist(user1);
        em.flush();

        /*INIT Customer*/
        CustomerEntity user2 = new CustomerEntity("testing test 123", "123456", joinedDate, "N/A", 0.0, "customer1", "cust@mail.com", "John", "Doe", UserAccessRightEnum.CUSTOMER, true, true, "password");
        CustomerEntity user3 = new CustomerEntity("testing test 1234", "1234567", joinedDate, "N/A", 0.0, "customer2", "cust2@mail.com", "Johnny", "Doey", UserAccessRightEnum.CUSTOMER, true, true, "password");
        em.persist(user2);
        em.persist(user3);
        em.flush();

        /*INIT CATEGORY*/
        CategoryEntity categoryEntity = new CategoryEntity("Category A");
        em.persist(categoryEntity);
        em.flush();

        /*INIT TAG*/
        TagEntity tag = new TagEntity("Popular");
        em.persist(tag);
        em.flush();

        /*INIT LISTING*/
        List<TagEntity> tags = Arrays.asList(tag);
        ListingEntity listing = new ListingEntity("Test Listing", 10.0, "This is a test listing", "Singapore", joinedDate, 1, 2, 10, DeliveryOptionEnum.DELIVERY, AvailabilityEnum.AVAILABLE, ModeOfPaymentEnum.CREDIT_CARD, user2, categoryEntity, tags);
        em.persist(listing);
        em.flush();

        ListingEntity listing2 = new ListingEntity("Test Listing 2", 10.0, "This is a test listing 2", "Singapore", joinedDate, 1, 2, 10, DeliveryOptionEnum.MEETUP, AvailabilityEnum.RENTED_OUT, ModeOfPaymentEnum.CASH_ON_DELIVERY, user2, categoryEntity, tags);
        em.persist(listing2);
        em.flush();

        /*INIT OFFER*/
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, 5);
        Date startDate = calendar.getTime();
        Date endDate = calendar2.getTime();
        OfferEntity offer = new OfferEntity(startDate, startDate, startDate, endDate, listing, user3);
        em.persist(offer);
        em.flush();
    }
}

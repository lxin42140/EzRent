/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.CategoryEntity;
import entity.CommentEntity;
import entity.CustomerEntity;
import entity.ListingEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
        CustomerEntity user2 = new CustomerEntity("testing test 123", "123456", joinedDate, "Hello everyone, welcome! I rent all sorts of things, PM me for more information :)", 0.0, "customer1", "cust@mail.com", "John", "Doe", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user2);
        em.flush();
        CustomerEntity user3 = new CustomerEntity("testing test 123", "123456", joinedDate, "Hello everyone, welcome! I rent all sorts of things, PM me for more information :)", 0.0, "customer2", "cust2@mail.com", "John", "Doe", UserAccessRightEnum.CUSTOMER, false, false, "password");
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
        ListingEntity listing = new ListingEntity("T-shirt", 50.20, "This is a test listing", "Singapore", joinedDate, 1, 2, 10, DeliveryOptionEnum.MAIL, AvailabilityEnum.AVAILABLE, ModeOfPaymentEnum.CREDIT_CARD, user2, categoryEntity, tags);
        em.persist(listing);
        em.flush();

        ListingEntity listing2 = new ListingEntity("Gameboy", 10.0, "This is a test listing 2", "Singapore", joinedDate, 1, 2, 10, DeliveryOptionEnum.MEETUP, AvailabilityEnum.RENTED_OUT, ModeOfPaymentEnum.CASH_ON_DELIVERY, user2, categoryEntity, tags);
        em.persist(listing2);
        em.flush();

        /*INIT COMMENT*/
        CommentEntity comment1 = new CommentEntity("This is comment 1", new Date(), user2);
        comment1.setListing(listing);
        listing.getComments().add(comment1);
        em.persist(comment1);
        em.flush();

        CommentEntity comment2 = new CommentEntity("This is a reply to comment 1", new Date(), user3);
        comment2.setParentComment(comment1);
        comment1.getReplies().add(comment2);
        em.persist(comment2);
        em.flush();
    }
}

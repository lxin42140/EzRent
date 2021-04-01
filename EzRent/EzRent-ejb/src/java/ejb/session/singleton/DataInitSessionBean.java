/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.CategoryEntity;
import entity.CreditCardEntity;
import entity.CommentEntity;
import entity.CustomerEntity;
import entity.DeliveryCompanyEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.RequestEntity;
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
import util.enumeration.RequestUrgencyEnum;
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

        CustomerEntity user3 = new CustomerEntity("testing test 123", "123456", joinedDate, "Hello everyone, welcome! I rent all sorts of things, PM me for more information :)", 0.0, "customer2", "cust2@mail.com", "John", "Doey", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user3);
        em.flush();

        CustomerEntity user4 = new CustomerEntity("testing test 12345", "12345678", joinedDate, "N/A", 0.0, "customer3", "cust3@mail.com", "Johnna", "Doet", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user4);
        em.flush();

        CustomerEntity user5 = new CustomerEntity("testing test 12345", "12345678", joinedDate, "N/A", 0.0, "customer4", "cust3@mail.com", "Johnna", "Doetttt", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user5);
        em.flush();

        /*INIT CATEGORY*/
        CategoryEntity categoryEntity = new CategoryEntity("Category A");
        em.persist(categoryEntity);
        em.flush();

        /*INIT TAG*/
        TagEntity tag = new TagEntity("Popular");
        em.persist(tag);
        em.flush();

        TagEntity tag1 = new TagEntity("Camera");
        em.persist(tag1);
        em.flush();

        /*INIT LISTING*/
        ListingEntity listing = new ListingEntity("Test listing 1", 50.20, "This is a test listing", DeliveryOptionEnum.MAIL, "image1.jpg", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CREDIT_CARD);
        listing.getTags().add(tag1);
        listing.getTags().add(tag);
        tag.getListings().add(listing);
        tag1.getListings().add(listing);

        listing.setCategory(categoryEntity);
        categoryEntity.getListings().add(listing);

        user2.getListings().add(listing);
        listing.setListingOwner(user2);
        
        em.persist(listing);
        em.flush();

        ListingEntity listing2 = new ListingEntity("Test listing 2", 50.20, "This is a test listing", DeliveryOptionEnum.MEETUP, "image2.jpg", "Singapore", joinedDate, 1, 3, 5, ModeOfPaymentEnum.CREDIT_CARD);
        listing2.getTags().add(tag1);
        listing2.getTags().add(tag);
        tag.getListings().add(listing2);
        tag1.getListings().add(listing2);
        listing2.setCategory(categoryEntity);
        categoryEntity.getListings().add(listing2);

        user2.getListings().add(listing2);
        listing2.setListingOwner(user2);
        em.persist(listing2);
        em.flush();

        ListingEntity listing3 = new ListingEntity("Test listing 3", 50.20, "This is a test listing", DeliveryOptionEnum.MAIL, "image3.png", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CREDIT_CARD);
        listing3.getTags().add(tag1);
        listing3.getTags().add(tag);
        tag.getListings().add(listing3);
        tag1.getListings().add(listing3);

        listing3.setCategory(categoryEntity);
        categoryEntity.getListings().add(listing3);

        user2.getListings().add(listing3);
        listing3.setListingOwner(user2);
        em.persist(listing3);
        em.flush();

        ListingEntity listing4 = new ListingEntity("Test listing 4", 50.20, "This is a test listing", DeliveryOptionEnum.MAIL, "image4.png", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CREDIT_CARD);
        listing4.getTags().add(tag1);
        listing4.getTags().add(tag);
        tag.getListings().add(listing4);
        tag1.getListings().add(listing4);

        listing4.setCategory(categoryEntity);
        categoryEntity.getListings().add(listing4);

        user2.getListings().add(listing4);
        listing4.setListingOwner(user2);
        em.persist(listing4);
        em.flush();

        ListingEntity listing5 = new ListingEntity("Test listing 5", 50.20, "This is a test listing", DeliveryOptionEnum.MAIL, "image5.png", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CREDIT_CARD);
        listing5.getTags().add(tag1);
        listing5.getTags().add(tag);
        tag.getListings().add(listing5);
        tag1.getListings().add(listing5);

        listing5.setCategory(categoryEntity);
        categoryEntity.getListings().add(listing5);

        user2.getListings().add(listing5);
        listing5.setListingOwner(user2);

        em.persist(listing5);
        em.flush();

        /*INIT OFFER*/
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, 5);
        Date startDate = calendar.getTime();
        Date endDate = calendar2.getTime();

        OfferEntity offer1 = new OfferEntity(startDate, startDate, startDate, endDate, listing, user3);
        em.persist(offer1);
        em.flush();

        OfferEntity offer2 = new OfferEntity(startDate, startDate, startDate, endDate, listing2, user3);
        em.persist(offer2);
        em.flush();

        OfferEntity offer3 = new OfferEntity(startDate, startDate, startDate, endDate, listing3, user3);
        em.persist(offer3);
        em.flush();

        OfferEntity offer4 = new OfferEntity(startDate, startDate, startDate, endDate, listing4, user3);
        em.persist(offer4);
        em.flush();

        OfferEntity offer5 = new OfferEntity(startDate, startDate, startDate, endDate, listing5, user3);
        em.persist(offer5);
        em.flush();

        user3.getOffers().add(offer1);
        user3.getOffers().add(offer5);
        user3.getOffers().add(offer5);
        user3.getOffers().add(offer5);
        user3.getOffers().add(offer5);

        OfferEntity offer6 = new OfferEntity(startDate, startDate, startDate, endDate, listing, user4);
        em.persist(offer6);
        em.flush();

        OfferEntity offer7 = new OfferEntity(startDate, startDate, startDate, endDate, listing2, user4);
        em.persist(offer7);
        em.flush();

        OfferEntity offer8 = new OfferEntity(startDate, startDate, startDate, endDate, listing3, user4);
        em.persist(offer8);
        em.flush();

        OfferEntity offer9 = new OfferEntity(startDate, startDate, startDate, endDate, listing4, user4);
        em.persist(offer9);
        em.flush();

        OfferEntity offer10 = new OfferEntity(startDate, startDate, startDate, endDate, listing5, user4);
        em.persist(offer10);
        em.flush();

        user4.getOffers().add(offer6);
        user4.getOffers().add(offer7);
        user4.getOffers().add(offer8);
        user4.getOffers().add(offer9);
        user4.getOffers().add(offer10);

        /*INIT CREDIT CARD FOR CUSTOMER */
        CreditCardEntity creditCard = new CreditCardEntity("Johnny", "1234567812345678", endDate, 111);
        creditCard.setCustomer(user3);
        user3.getCreditCards().add(creditCard);
        em.persist(creditCard);
        em.flush();

        /* INIT DELIVERY COMPANY*/
        DeliveryCompanyEntity deliveryCompany = new DeliveryCompanyEntity("Compnay1", "111111", "12345678", "company1", "company@company.com", "company", "1", UserAccessRightEnum.DELIVERY_COMPANY, false, false, "password");
        em.persist(deliveryCompany);

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

        /*INIT REQUEST*/
    }
}

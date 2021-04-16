/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.TransactionEntitySessionBeanLocal;
import entity.AdministratorEntity;
import entity.CategoryEntity;
import entity.CreditCardEntity;
import entity.CommentEntity;
import entity.CustomerEntity;
import entity.DeliveryCompanyEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.PaymentEntity;
import entity.RequestEntity;
import entity.TagEntity;
import entity.TransactionEntity;
import entity.UserEntity;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.DeliveryOptionEnum;
import util.enumeration.ModeOfPaymentEnum;
import util.enumeration.PaymentStatusEnum;
import util.enumeration.RequestUrgencyEnum;
import util.enumeration.TransactionStatusEnum;
import util.enumeration.UserAccessRightEnum;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.CreateNewTransactionException;
import util.exception.OfferNotFoundException;

/**
 *
 * @author Li Xin
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB(name = "CategoryEntitySessionBeanLocal")
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;

    @EJB(name = "TransactionEntitySessionBeanLocal")
    private TransactionEntitySessionBeanLocal transactionEntitySessionBeanLocal;
    
    

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

        /*INIT Admin*/
        AdministratorEntity admin1 = new AdministratorEntity("admin1", "admin@ezrent.com", "Jessica", "Loh", UserAccessRightEnum.ADMINSTRATOR, false, false, "password");
        em.persist(admin1);
        em.flush();

        /*INIT Customer*/
        CustomerEntity user2 = new CustomerEntity("Blk 534, Jurong West St 2, #03-55", "123456", joinedDate, "Hello everyone, welcome! I rent all sorts of things, PM me for more information :)", 0.0, "john_doe99", "cust@mail.com", "John", "Doe", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user2);
        em.flush();

        CustomerEntity user3 = new CustomerEntity("4 Jalan Pesawat Jurong Town", "123456", joinedDate, "Hello everyone, welcome! I rent all sorts of things, PM me for more information :)", 0.0, "kimpossible", "cust2@mail.com", "Kim", "Lee", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user3);
        em.flush();

        CustomerEntity user4 = new CustomerEntity("3 Shenton way, Shenton House, #14-05", "12345678", joinedDate, "N/A", 0.0, "minho_89", "cust3@mail.com", "Xiao Jun", "Pei", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user4);
        em.flush();

        CustomerEntity user5 = new CustomerEntity("3012 Bedok Industrial Park E 03-2034", "12345678", joinedDate, "N/A", 0.0, "ardwolf", "cust3@mail.com", "Lauren", "Loh", UserAccessRightEnum.CUSTOMER, false, false, "password");
        em.persist(user5);
        em.flush();

        /*INIT CATEGORY*/
        CategoryEntity categoryEntity = new CategoryEntity("Clothing");
        em.persist(categoryEntity);
        em.flush();
        CategoryEntity categoryEntity1 = new CategoryEntity("Electronics");
        em.persist(categoryEntity1);
        em.flush();
        CategoryEntity categoryEntity2 = new CategoryEntity("Furniture");
        em.persist(categoryEntity2);
        em.flush();
        CategoryEntity categoryEntity3 = new CategoryEntity("Home Appliances");
        em.persist(categoryEntity3);
        em.flush();
        CategoryEntity categoryEntity4 = new CategoryEntity("Entertainment");
        em.persist(categoryEntity4);
        em.flush();
        CategoryEntity categoryEntity5 = new CategoryEntity("Books & Stationary");
        em.persist(categoryEntity5);
        em.flush();
        
            CategoryEntity child = new CategoryEntity("Women's Fashion");
            CategoryEntity child2 = new CategoryEntity("Men's Fashion");
            CategoryEntity child3 = new CategoryEntity("Phone");
            CategoryEntity child4 = new CategoryEntity("Tablet");
            CategoryEntity child5 = new CategoryEntity("Laptop");
            CategoryEntity child6 = new CategoryEntity("Audio");
            CategoryEntity child7 = new CategoryEntity("Chair");
            CategoryEntity child8 = new CategoryEntity("Table");
            CategoryEntity child9 = new CategoryEntity("Movies");
            CategoryEntity child10 = new CategoryEntity("Game Cartridge");
        try {
            Long childCategory = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child, categoryEntity.getCategoryId());
            Long childCategory2 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child2, categoryEntity.getCategoryId());
            Long childCategory3 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child3, categoryEntity1.getCategoryId());
            Long childCategory4 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child4, categoryEntity1.getCategoryId());
            Long childCategory5 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child5, categoryEntity1.getCategoryId());
            Long childCategory6 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child6, categoryEntity1.getCategoryId());
            Long childCategory7 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child7, categoryEntity2.getCategoryId());
            Long childCategory8 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child8, categoryEntity2.getCategoryId());
            Long childCategory9 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child9, categoryEntity4.getCategoryId());
            Long childCategory10 = categoryEntitySessionBeanLocal.createNewCategoryWithParentCategory(child10, categoryEntity4.getCategoryId());
        }catch (CategoryNotFoundException | CreateNewCategoryException ex){
        }

        /*INIT TAG*/
        TagEntity tag = new TagEntity("Popular");
        em.persist(tag);
        em.flush();

        TagEntity tag1 = new TagEntity("New");
        em.persist(tag1);
        em.flush();
        
        TagEntity tag2 = new TagEntity("Sales");
        em.persist(tag2);
        em.flush();
        

        /*INIT LISTING*/
        ListingEntity listing = new ListingEntity("T-Shirt Mickey Mouse", 2.20, "Only worn it once, good condition", DeliveryOptionEnum.MEETUP, "image1.jpg", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CASH_ON_DELIVERY);
        listing.getTags().add(tag1);
        listing.getTags().add(tag);

        listing.setCategory(child2);

        user2.getListings().add(listing);
        listing.setListingOwner(user2);

        em.persist(listing);
        em.flush();

        ListingEntity listing2 = new ListingEntity("iPhone 9s in Black", 50.90, "Phone is used for 1 year, still functioning good", DeliveryOptionEnum.MEETUP, "image2.jpg", "Singapore", joinedDate, 1, 3, 5, ModeOfPaymentEnum.CREDIT_CARD);
        listing2.getTags().add(tag1);
        listing2.setCategory(child3);

        user2.getListings().add(listing2);
        listing2.setListingOwner(user2);
        em.persist(listing2);
        em.flush();

        ListingEntity listing3 = new ListingEntity("iPad Air 2nd Gen", 66.00, "Not in use for 2 years. Kept in box.", DeliveryOptionEnum.MAIL, "image3.jpg", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CASH_ON_DELIVERY);
        listing3.getTags().add(tag);
        listing3.setCategory(child4);

        user2.getListings().add(listing3);
        listing3.setListingOwner(user2);
        em.persist(listing3);
        em.flush();

        ListingEntity listing4 = new ListingEntity("Samsung Galaxy S10", 45.20, "Still in Pristine condition!", DeliveryOptionEnum.MAIL, "image4.jpg", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CREDIT_CARD);
        listing4.getTags().add(tag1);
        listing4.getTags().add(tag);

        listing4.setCategory(child3);

        user2.getListings().add(listing4);
        listing4.setListingOwner(user2);
        em.persist(listing4);
        em.flush();

        ListingEntity listing5 = new ListingEntity("Nintendo Overcooked Cartridge", 1.70, "Cheap rental!", DeliveryOptionEnum.MAIL, "image5.jpg", "Singapore", joinedDate, 1, 2, 10, ModeOfPaymentEnum.CREDIT_CARD);
        listing5.getTags().add(tag1);
        listing5.getTags().add(tag);

        listing5.setCategory(child10);

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

        /*INIT TRANSACTION*/
        try {
            PaymentEntity payment = new PaymentEntity(endDate, BigDecimal.ZERO);
            TransactionEntity transaction = new TransactionEntity(startDate, joinedDate, TransactionStatusEnum.PENDING_PAYMENT);
            transactionEntitySessionBeanLocal.createNewTransaction(offer8.getOfferId(), transaction);
        } catch (CreateNewTransactionException | OfferNotFoundException ex) {
        }
        
//        try {
//            PaymentEntity payment = new PaymentEntity(endDate, BigDecimal.ZERO);
//            TransactionEntity transaction = new TransactionEntity(startDate, joinedDate, TransactionStatusEnum.PAID);
//            transaction.setPayment(payment);
//            payment.setTransaction(transaction);
//            payment.setModeOfPayment(ModeOfPaymentEnum.CREDIT_CARD);
//            payment.setPaymentStatus(PaymentStatusEnum.PAID);
//            transactionEntitySessionBeanLocal.createNewTransaction(offer3.getOfferId(), transaction);
//        } catch (CreateNewTransactionException | OfferNotFoundException ex) {
//        }
        
        
        /* INIT DELIVERY COMPANY*/
        DeliveryCompanyEntity deliveryCompany = new DeliveryCompanyEntity("Compnay1", "111111", "12345678", "company1", "company@company.com", "company", "1", UserAccessRightEnum.DELIVERY_COMPANY, false, false, "password");
        em.persist(deliveryCompany);

        /*INIT COMMENT*/
        CommentEntity comment1 = new CommentEntity("Is this still available?", new Date(), user3);
        comment1.setListing(listing);
        listing.getComments().add(comment1);
        em.persist(comment1);
        em.flush();

        CommentEntity comment2 = new CommentEntity("Yes, of course", new Date(), user2);
        comment2.setParentComment(comment1);
        comment1.getReplies().add(comment2);
        em.persist(comment2);
        em.flush();

        /*INIT REQUEST*/
        RequestEntity requestEntity = new RequestEntity("Canon Camera Stand", RequestUrgencyEnum.URGENT, new Date(), startDate, endDate, "I need this by next Thursday", "request1.jpg");
        requestEntity.setCustomer(user2);
        em.persist(requestEntity);
        em.flush();
    }

    public void persist(Object object) {
        em.persist(object);
    }
}

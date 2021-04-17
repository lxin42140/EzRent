/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ChatMessageEntity;
import entity.ConversationEntity;
import entity.CustomerEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.ConversationNotFoundException;
import util.exception.CreateNewConversationException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteConversationException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Ziyue
 */
@Stateless
public class ConversationEntitySessionBean implements ConversationEntitySessionBeanLocal {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewConversation(Long senderId, Long receiverId, ConversationEntity conversationEntity) throws CustomerNotFoundException, CreateNewConversationException {

        if (senderId == null) {
            throw new CreateNewConversationException("CreateNewConversationException: Sender ID can't be null!");
        }

        if (receiverId == null) {
            throw new CreateNewConversationException("CreateNewConversationException: Receiver ID can't be null!");
        }

        if (conversationEntity == null) {
            throw new CreateNewConversationException("CreateNewConversationException: Conversation can't be null!");
        }

        CustomerEntity sender = customerEntitySessionBeanLocal.retrieveCustomerById(senderId);

        CustomerEntity receiver = customerEntitySessionBeanLocal.retrieveCustomerById(receiverId);

        if (sender.equals(receiver)) {
            throw new CreateNewConversationException("CreateNewConversationException: Customer cannot be sender and receiver simultaneously!");
        }

        //Associate with customers
        conversationEntity.getChatMembers().add(sender);
        conversationEntity.getChatMembers().add(receiver);

        sender.getConversations().add(conversationEntity);
        receiver.getConversations().add(conversationEntity);

        try {
            validate(conversationEntity);

            em.persist(conversationEntity);
            em.flush();
            return conversationEntity.getConversationId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewConversationException("CreateNewConversationException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewConversationException("CreateNewConversationException: Conversation with same Conversation ID already exists!");
            } else {
                throw new CreateNewConversationException("CreateNewConversationException: " + ex.getMessage());
            }
        }
    }

//    Create conversation along with initial message
//    public Long createNewConversationWithMessage(Long senderId, Long receiverId, Long chatMessageId, ConversationEntity conversationEntity) throws CustomerNotFoundException, CreateNewConversationException, ChatMessageNotFoundException {
//        
//        if (senderId == null) {
//            throw new CreateNewConversationException("CreateNewConversationException: Sender ID can't be null!");
//        }
//        
//        if (receiverId == null) {
//            throw new CreateNewConversationException("CreateNewConversationException: Receiver ID can't be null!");
//        }
//        
//        if (chatMessageId == null) {
//            throw new CreateNewConversationException("CreateNewConversationException: Chat message ID can't be null!");
//        }
//        
//        if (conversationEntity == null) {
//            throw new CreateNewConversationException("CreateNewConversationException: Conversation can't be null!");
//        }
//        
//        CustomerEntity sender = customerEntitySessionBeanLocal.retrieveCustomerById(senderId);
//        
//        CustomerEntity receiver = customerEntitySessionBeanLocal.retrieveCustomerById(receiverId);
//        
//        ChatMessageEntity chatMessageEntity = chatMessageEntitySessionBeanLocal.retrieveChatMessageById(chatMessageId);        
//        
//        if (sender.equals(receiver)) {
//            throw new CreateNewConversationException("CreateNewConversationException: Customer cannot be sender and receiver simultaneously!");
//        }
//        
//        //Associate conversation with customers
//        conversationEntity.getChatMembers().add(sender);
//        conversationEntity.getChatMembers().add(receiver);
//        
//        
//        sender.getConversations().add(conversationEntity);
//        receiver.getConversations().add(conversationEntity);
//        
//        //Associate conversation with message
//        conversationEntity.getChatMessages().add(chatMessageEntity);
//        chatMessageEntity.setConversation(conversationEntity);
//        
//        //Associate message with sender
//        chatMessageEntity.setMessageSender(sender);
//        
//        try {
//            validate(conversationEntity);
//            chatMessageEntitySessionBeanLocal.validate(chatMessageEntity);
//            
//            em.persist(conversationEntity);
//            em.persist(chatMessageEntity);
//            em.flush();
//            return conversationEntity.getConversationId();
//        } catch (ValidationFailedException ex) {
//            throw new CreateNewConversationException("CreateNewConversationException: " + ex.getMessage());         
//        } catch (PersistenceException ex) {
//            if (isSQLIntegrityConstraintViolationException(ex)) {
//                throw new CreateNewConversationException("CreateNewConversationException: Conversation with same Conversation ID already exists!");
//            } else {
//                throw new CreateNewConversationException("CreateNewConversationException: " + ex.getMessage());         
//            } 
//        }        
//    }
    @Override
    public List<ConversationEntity> retrieveAllConversations() {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c");
        return query.getResultList();
    }

    @Override
    public List<ConversationEntity> retrieveAllConversationsByCustomer(Long customerId) {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c WHERE :customerId MEMBER OF c.chatMembers");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
    
    @Override
    public ConversationEntity retrieveAllConversationsBySenderReceiver(Long senderId, Long receiverId) throws ConversationNotFoundException {
        Query query = em.createQuery("SELECT c FROM ConversationEntity c WHERE :senderId MEMBER OF c.chatMembers AND :receiverId MEMBER OF c.chatMembers");
        query.setParameter("senderId", senderId);
        query.setParameter("receiverId", receiverId);
        
        ConversationEntity conversation = (ConversationEntity) query.getSingleResult();
        
        if (conversation == null) {
            throw new ConversationNotFoundException("ConversationNotFoundException: conversation between these 2 does not exist!");
        }
        
        return conversation;
    }

    @Override
    public ConversationEntity retrieveConversationByConversationId(Long conversationId) throws ConversationNotFoundException {
        if (conversationId == null) {
            throw new ConversationNotFoundException("ConversationNotFoundException: Conversation Id is null!");
        }

        ConversationEntity conversationEntity = em.find(ConversationEntity.class, conversationId);

        if (conversationEntity == null) {
            throw new ConversationNotFoundException("ConversationNotFoundException: Conversation Id " + conversationId + " does not exist!");
        }

        return conversationEntity;
    }

    @Override
    public void deleteConversation(Long conversationId) throws ConversationNotFoundException, DeleteConversationException {
        ConversationEntity conversationEntity = retrieveConversationByConversationId(conversationId);

        for (CustomerEntity customer : conversationEntity.getChatMembers()) {
            customer.getConversations().remove(conversationEntity);
        }
        conversationEntity.getChatMembers().clear();

        for (ChatMessageEntity chatMessage : conversationEntity.getChatMessages()) {
            chatMessage.setMessageSender(null);
            chatMessage.setConversation(null);
        }
        conversationEntity.getChatMessages().clear();

        try {
            em.remove(conversationEntity);
        } catch (PersistenceException ex) {
            throw new DeleteConversationException("DeleteDamageReportException: " + ex.getMessage());
        }
    }

    @Override
    public void clearEmptyConversations() throws DeleteConversationException {
        List<ConversationEntity> conversations = this.retrieveAllConversations();
        for (ConversationEntity conversation : conversations) {
            if (conversation.getChatMessages().isEmpty()) { // empty conversation
                //diassociate customer and conversation
                for (CustomerEntity customer : conversation.getChatMembers()) {
                    customer.getConversations().remove(conversation);
                }
                conversation.setChatMembers(null);

                try {
                    em.remove(conversation);
                } catch (PersistenceException ex) {
                    throw new DeleteConversationException("DeleteDamageReportException: " + ex.getMessage());
                }
            }
        }
    }

    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }

    private void validate(ConversationEntity conversationEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ConversationEntity>> errors = validator.validate(conversationEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
}

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
import util.exception.ChatMessageNotFoundException;
import util.exception.ConversationNotFoundException;
import util.exception.CreateNewChatMessageException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteChatMessageException;
import util.exception.ValidationFailedException;

/**
 *
 * @author Ziyue
 */
@Stateless
public class ChatMessageEntitySessionBean implements ChatMessageEntitySessionBeanLocal {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "ConversationEntitySessionBeanLocal")
    private ConversationEntitySessionBeanLocal conversationEntitySessionBeanLocal;

    @PersistenceContext(unitName = "EzRent-ejbPU")
    private EntityManager em;
    
    

    @Override
    public Long createnewChatMessage(Long conversationId, Long senderId, ChatMessageEntity chatMessageEntity) throws CreateNewChatMessageException, ConversationNotFoundException, CustomerNotFoundException {
        
        if (conversationId == null) {
            throw new CreateNewChatMessageException("CreateNewChatMessageException: Conversation Id is null!");
        }
        
        if (senderId == null) {
            throw new CreateNewChatMessageException("CreateNewChatMessageException: Sender Id is null!");
        }
        
        if (chatMessageEntity == null) {
            throw new CreateNewChatMessageException("CreateNewChatMessageException: Chat message is null!");
        }
        
        ConversationEntity conversationEntity = conversationEntitySessionBeanLocal.retrieveConversationByConversationId(conversationId);
        
        CustomerEntity sender = customerEntitySessionBeanLocal.retrieveCustomerById(senderId);
        
        //Check if said sender is part of conversation
        if (!conversationEntity.getChatMembers().contains(sender)) {
            throw new CreateNewChatMessageException("CreateNewChatMessageException: Sender is not part of this conversation!");
        }
        
        //Associate message with conversation
        conversationEntity.getChatMessages().add(chatMessageEntity);
        chatMessageEntity.setConversation(conversationEntity);
        
        //Associate message with sender
        chatMessageEntity.setMessageSender(sender);
        
        try {
            validate(chatMessageEntity);
            
            em.persist(chatMessageEntity);
            em.flush();
            return chatMessageEntity.getChatMessageId();
        } catch (ValidationFailedException ex) {
            throw new CreateNewChatMessageException("CreateNewChatMessageException: " + ex.getMessage());
        } catch (PersistenceException ex) {
            if (isSQLIntegrityConstraintViolationException(ex)) {
                throw new CreateNewChatMessageException("CreateNewChatMessageException: Damage Report with same Damage Report ID already exists!");
            } else {
                throw new CreateNewChatMessageException("CreateNewChatMessageException: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public List<ChatMessageEntity> retrieveAllChatMessages() {
        Query query = em.createQuery("SELECT c FROM ChatMessageEntity c");
        return query.getResultList();
    }
    
    @Override
    public List<ChatMessageEntity> retrieveAllChatMessagesBySender(Long senderId) {
        Query query = em.createQuery("SELECT c FROM ChatMessageEntity c WHERE c.messageSender = :inSenderId");
        query.setParameter("inSenderId", senderId);
        
        return query.getResultList();
    }
    
    @Override
    public List<ChatMessageEntity> retrieveAllChatMessagesByConversation(Long conversationId) {
        Query query = em.createQuery("SELECT c FROM ChatMessageEntity c WHERE c.conversation = :inConversationId");
        query.setParameter("inConversationId", conversationId);
        
        return query.getResultList();
    }
    
    @Override
    public ChatMessageEntity retrieveChatMessageByChatMessageId(Long chatMessageId) throws ChatMessageNotFoundException {
        if (chatMessageId == null) {
            throw new ChatMessageNotFoundException("ChatMessageNotFoundException: ChatMessage Id is null!");
        }
        
        ChatMessageEntity chatMessageEntity = em.find(ChatMessageEntity.class, chatMessageId);
        
        if (chatMessageEntity == null) {
            throw new ChatMessageNotFoundException("ChatMessageNotFoundException: ChatMessage Id " + chatMessageId + " does not exist!");
        }
        
        return chatMessageEntity;
    }
    
    @Override
    public void deleteChatMessage(Long chatMessageid) throws ChatMessageNotFoundException, DeleteChatMessageException {
        ChatMessageEntity chatMessageEntity = retrieveChatMessageByChatMessageId(chatMessageid);
        
        chatMessageEntity.setMessageSender(null);
        
        chatMessageEntity.getConversation().getChatMessages().remove(chatMessageEntity);
        chatMessageEntity.setConversation(null);
        
        try {
            em.remove(chatMessageEntity);
        } catch (PersistenceException ex) {
            throw new DeleteChatMessageException("DeleteChatMessageException: " + ex.getMessage());
        }
    }
    
    private void validate(ChatMessageEntity chatMessageEntity) throws ValidationFailedException {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<ChatMessageEntity>> errors = validator.validate(chatMessageEntity);

        String errorMessage = "";

        for (ConstraintViolation error : errors) {
            errorMessage += "\n\t" + error.getPropertyPath() + " - " + error.getInvalidValue() + "; " + error.getMessage();
        }

        if (errorMessage.length() > 0) {
            throw new ValidationFailedException("ValidationFailedException: Invalid inputs!\n" + errorMessage);
        }
    }
    
    private boolean isSQLIntegrityConstraintViolationException(PersistenceException ex) {
        return ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException");
    }
}

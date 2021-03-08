/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ConversationEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ConversationNotFoundException;
import util.exception.CreateNewConversationException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteConversationException;

/**
 *
 * @author Ziyue
 */
@Local
public interface ConversationEntitySessionBeanLocal {

    public Long createNewConversation(Long senderId, Long receiverId, ConversationEntity conversationEntity) throws CustomerNotFoundException, CreateNewConversationException;

    public List<ConversationEntity> retrieveAllConversations();

    public List<ConversationEntity> retrieveAllConversationsByCustomer(Long customerId);

    public ConversationEntity retrieveConversationByConversationId(Long conversationId) throws ConversationNotFoundException;

    public void deleteConversation(Long conversationId) throws ConversationNotFoundException, DeleteConversationException;

    public void clearEmptyConversations() throws DeleteConversationException;
    
}

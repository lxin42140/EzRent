/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ChatMessageEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ChatMessageNotFoundException;
import util.exception.ConversationNotFoundException;
import util.exception.CreateNewChatMessageException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteChatMessageException;

/**
 *
 * @author Ziyue
 */
@Local
public interface ChatMessageEntitySessionBeanLocal {

    public Long createnewChatMessage(Long conversationId, Long senderId, ChatMessageEntity chatMessageEntity) throws CreateNewChatMessageException, ConversationNotFoundException, CustomerNotFoundException;

    public List<ChatMessageEntity> retrieveAllChatMessages();

    public List<ChatMessageEntity> retrieveAllChatMessagesBySender(Long senderId);

    public List<ChatMessageEntity> retrieveAllChatMessagesByConversation(Long conversationId);

    public ChatMessageEntity retrieveChatMessageByChatMessageId(Long chatMessageId) throws ChatMessageNotFoundException;

    public void deleteChatMessage(Long chatMessageid) throws ChatMessageNotFoundException, DeleteChatMessageException;
    
}

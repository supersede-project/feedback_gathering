package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.ChatUnread;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Aydinli on 20.02.2018.
 */
public interface ChatUnreadService {
    List<ChatUnread> findAll();
    ChatUnread save(ChatUnread chatUnread);
    ChatUnread find(long id);
    void delete(long id);
    void delete(List<ChatUnread> chatUnreads);
    ChatUnread findByFeedbackChatInformationFeedbackChatId(long chatMessageId);
    List<ChatUnread> findByFeedbackId(long feedbackId);
    List<ChatUnread> findByEnduserId(long userId);
    List<ChatUnread> findByFeedbackIdAndEnduserId(long feedbackId,long userId);
}

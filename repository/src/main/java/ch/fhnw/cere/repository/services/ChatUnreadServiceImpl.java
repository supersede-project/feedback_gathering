package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.ChatUnread;
import ch.fhnw.cere.repository.repositories.ChatUnreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aydinli on 20.02.2018.
 */
@Service
public class ChatUnreadServiceImpl implements ChatUnreadService{

    @Autowired
    private ChatUnreadRepository chatUnreadRepository;

    @Override
    public List<ChatUnread> findAll() {
        return chatUnreadRepository.findAll();
    }

    @Override
    public ChatUnread save(ChatUnread chatUnread) {
        return chatUnreadRepository.save(chatUnread);
    }

    @Override
    public ChatUnread find(long id) {
        return chatUnreadRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        chatUnreadRepository.delete(id);
    }

    @Override
    public void delete(List<ChatUnread> chatUnreads) {
        chatUnreadRepository.delete(chatUnreads);
    }

    @Override
    public ChatUnread findByFeedbackChatInformationFeedbackChatId(long chatMessageId) {
        return chatUnreadRepository.findByFeedbackChatInformationFeedbackChatId(chatMessageId);
    }

    @Override
    public List<ChatUnread> findByFeedbackId(long feedbackId) {
        return chatUnreadRepository.findByFeedbackId(feedbackId);
    }

    @Override
    public List<ChatUnread> findByEnduserId(long userId) {
        return chatUnreadRepository.findByEnduserId(userId);
    }

    @Override
    public List<ChatUnread> findByFeedbackIdAndEnduserId(long feedbackId, long userId) {
        return chatUnreadRepository.findByFeedbackIdAndEnduserId(feedbackId,userId);
    }
}

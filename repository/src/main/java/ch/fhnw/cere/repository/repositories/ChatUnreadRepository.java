package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.ChatUnread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Aydinli on 20.02.2018.
 */
public interface ChatUnreadRepository extends JpaRepository<ChatUnread,Long>{
    ChatUnread findByFeedbackChatInformationFeedbackChatId(@Param("chatMessageId") long chatMessageId);
    List<ChatUnread> findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<ChatUnread> findByEnduserId(@Param("userId") long userId);
    List<ChatUnread> findByFeedbackIdAndEnduserId(@Param("feedbackId") long feedbackId,
                                                  @Param("userId") long userId);
}

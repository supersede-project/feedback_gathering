package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Aydinli on 31.01.2018.
 */
public interface EmailUnsubscribedService {
    public List<EmailUnsubscribed> findAll();
    public EmailUnsubscribed save(EmailUnsubscribed emailUnsubscribed);
    public EmailUnsubscribed find(long id);
    public void delete(long id);
    EmailUnsubscribed findByEnduserId(long userId);
    EmailUnsubscribed findByEmail(String email);
}

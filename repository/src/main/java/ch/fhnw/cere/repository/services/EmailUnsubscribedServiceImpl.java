package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import ch.fhnw.cere.repository.repositories.EmailUnsubscribedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aydinli on 31.01.2018.
 */
@Service
public class EmailUnsubscribedServiceImpl implements EmailUnsubscribedService {

    @Autowired
    private EmailUnsubscribedRepository emailUnsubscribedRepository;

    @Override
    public List<EmailUnsubscribed> findAll() {
        return emailUnsubscribedRepository.findAll();
    }

    @Override
    public EmailUnsubscribed save(EmailUnsubscribed emailUnsubscribed) {
        return emailUnsubscribedRepository.save(emailUnsubscribed);
    }

    @Override
    public EmailUnsubscribed find(long id) {
        return emailUnsubscribedRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        emailUnsubscribedRepository.delete(id);
    }

    @Override
    public EmailUnsubscribed findByEnduserId(long userId) {
        return emailUnsubscribedRepository.findByEnduserId(userId);
    }

    @Override
    public EmailUnsubscribed findByEmail(String email) {
        return emailUnsubscribedRepository.findByEmail(email);
    }
}

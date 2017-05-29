package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.ApiUser;
import ch.fhnw.cere.orchestrator.repositories.ApiUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ApiUserDetailsService implements UserDetailsService {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApiUser user = this.apiUserRepository.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }

}
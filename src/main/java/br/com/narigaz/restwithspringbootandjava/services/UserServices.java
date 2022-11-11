package br.com.narigaz.restwithspringbootandjava.services;

import br.com.narigaz.restwithspringbootandjava.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServices implements UserDetailsService {
    UserRepository userRepository;

    @Autowired
    public UserServices(UserRepository repository) {
        this.userRepository = repository;
    }

    private final Logger logger = Logger.getLogger(UserServices.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name " + username + "!");
        var user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username" + username + " not found!");
        }
    }
}

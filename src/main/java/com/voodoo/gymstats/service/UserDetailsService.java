package com.voodoo.gymstats.service;

import com.voodoo.gymstats.model.User;
import com.voodoo.gymstats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;



@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден с логином: " + login);
        }

        user.setLastVisitDate(LocalDate.now());
        userRepository.save(user);

        return user;
    }
}

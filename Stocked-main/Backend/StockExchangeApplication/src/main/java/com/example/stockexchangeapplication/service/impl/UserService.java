package com.example.stockexchangeapplication.service.impl;

import com.example.stockexchangeapplication.model.AppUser;
import com.example.stockexchangeapplication.repository.UserRepository;
import com.example.stockexchangeapplication.service.IUserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public AppUser updateUser(AppUser user, String email) throws NotFoundException {
        System.out.println("Emailllllll"+email);
        Optional<AppUser> userOptional = userRepository.findUser(email);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User does not exist");
        }
        AppUser existingUser = userOptional.get();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }

    @Override
    public AppUser getUserById(Long id) throws Exception {
        if (! userRepository.existsById(id)) throw new Exception("User Not Found");

        return userRepository.findById(id).get();
    }

    @Override
    public AppUser getUser(String email, String password) {
        Optional<AppUser> userOptional = userRepository.findUser(email, password);
        if(userOptional.isEmpty()) {
            return null;
        }
        return userOptional.get();
    }

    @Override
    public AppUser getUserByMail(String mail) {
        Optional<AppUser> user = userRepository.findUser(mail);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User doesn't exist");
        }
        return user.get();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = getUserByMail(email);
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}

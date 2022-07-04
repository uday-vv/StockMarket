package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.repository.UserRepository;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.AppUser;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.IUserService;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtility jwtUtility;

    @Value("${mail.username}")
    String mailId;

    @Value("${mail.password}")
    String password;

    private RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Override
    public String saveUser(AppUser user) throws Exception {
        user.setAdmin(false);
        AppUser savedUser = userRepository.save(user);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            savedUser.getEmail(),
                            savedUser.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final AppUser subject = getUserByMail(user.getEmail());

        final String token =
                jwtUtility.generateToken(subject);

        ResponseEntity<AppUser> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity entity = new HttpEntity<>(savedUser, headers);
            response = restTemplate.exchange("https://stockexchangeapp.herokuapp.com/api/v1/setUser", HttpMethod.POST,entity,AppUser.class);
        } catch (Exception ex) {
            throw new Exception("Error occurred");
        }

        if (response.getStatusCode().value() != 200) {
            userRepository.delete(savedUser);
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Responded","UserController");
        headers.add("Access-Control-Allow-Origin","*");
        sendEmail(savedUser);

        return token;
    }


//    private void sendEmail_(AppUser user) {
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//        try {
//
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//
//            mimeMessageHelper.setSubject("User Confirmation");
//            mimeMessageHelper.setFrom(new InternetAddress(mailId));
//            mimeMessageHelper.setTo(user.getEmail());
//            mimeMessageHelper.setText(
//                    "<h1><a href=\"https://stockprice-app.herokuapp.com/api/v1/auth/confirmUser/"+user.getId()+"\">Click to Confirm</a></h1>",
//                    true
//            );
//
//            mailSender.send(mimeMessageHelper.getMimeMessage());
//            System.out.println("Mail Sent");
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }

    private void sendEmail(AppUser user) throws Exception {

        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.starttls.required","true");
        properties.put("mail.smtp.ssl.protocols","TLSv1.2");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailId, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailId));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("User Confirmation Email");
            message.setContent(
                    "<h1><a href=\"https://stockexchangeapp.herokuapp.com/api/v1/confirmUser/"+user.getId()+"\">Click to Confirm</a></h1>",
                    "text/html"
            );
            Transport.send(message);
            System.out.println("Done Sending Mail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AppUser getUserById(Long id) throws Exception {
        if (! userRepository.existsById(id)) throw new Exception("User Not Found");

        return userRepository.findById(id).get();
    }

    @Override
    public AppUser getUser(String email, String password) {
        Optional<AppUser> user = userRepository.findUser(email, password);
        if(user.isEmpty()) {
            return null;
        }
        return user.get();
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
    public String updateUser(AppUser user, String token) throws Exception {
        Long id = user.getId();
        if (! userRepository.existsById(id)) {
            throw new Exception("User does not exist");
        }
        AppUser existingUser = userRepository.getById(id);
        String existingEmail = existingUser.getEmail();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        if(user.getPassword() != "") {
            existingUser.setPassword(user.getPassword());
        }
         existingUser = userRepository.save(existingUser);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            existingUser.getEmail(),
                            existingUser.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final String newToken =
                jwtUtility.generateToken(existingUser);

        ResponseEntity<AppUser> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity<>(existingUser, headers);
            response = restTemplate.exchange("https://stockexchangeapp.herokuapp.com/api/v1/updateUser?email="+existingEmail, HttpMethod.PUT,entity,AppUser.class);
        } catch (Exception ex) {
            userRepository.delete(existingUser);
            throw new Exception("Error occurred");
        }

        if (response.getStatusCode().value() != 200) {
            userRepository.delete(existingUser);
            return null;
        }
        return newToken;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = getUserByMail(email);
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}

package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.controller;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.AppUser;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.io.JwtRequest;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.io.JwtResponse;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.repository.UserRepository;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl.UserService;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtility jwtUtility;

    @GetMapping("/hello")
    public String hello() {
        return "Hello there!";
    }

    @PostMapping(
            path = "/auth/authenticate")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final AppUser userPayload
                = userService.getUserByMail(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userPayload);

        return new ResponseEntity<>(new JwtResponse(token),HttpStatus.OK);
    }

    @PostMapping(
            path = "/auth/setUser",
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<JwtResponse> createUser(@RequestBody AppUser user) throws Exception {
        return new ResponseEntity<>(new JwtResponse(userService.saveUser(user)), HttpStatus.OK);
    }

    @PutMapping(
            path = "/updateUser",
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<JwtResponse> updateUser(@RequestBody AppUser user, @RequestHeader("Authorization") String token) throws Exception {
        return new ResponseEntity<>(new JwtResponse(userService.updateUser(user, token)), HttpStatus.OK);
    }

    @GetMapping(
            value = "/auth/confirmUser/{userId}"
    )
    public String welcomeUser(@PathVariable Long userId) throws Exception {
        AppUser user = userService.getUserById(userId);
        user.setConfirmed(true);
        userRepository.save(user);
        return "User Verified";
    }

    @GetMapping(
            value = "/user/{userId}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<AppUser> getUserById(@PathVariable Long userId) throws Exception {
        AppUser user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(
            value = "user/login",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<AppUser> loginUser(@PathParam("email") String email, @PathParam("password") String password) {
        AppUser user = userService.getUser(email, password);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

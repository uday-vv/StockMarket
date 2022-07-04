package com.example.stockexchangeapplication.controller;

import com.example.stockexchangeapplication.io.request.JwtRequest;
import com.example.stockexchangeapplication.io.response.JwtResponse;
import com.example.stockexchangeapplication.model.AppUser;
import com.example.stockexchangeapplication.service.impl.UserService;
import com.example.stockexchangeapplication.util.JwtUtility;
import javassist.NotFoundException;
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
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtility jwtUtility;

    @GetMapping("/hello")
    public String hello() {
        return "Hello there!";
    }

    @PostMapping(
            path = "/authenticate")
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
            path = "/setUser",
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser userDetails) throws Exception {
        System.out.println("Inside exchange app controller");
        AppUser user = userService.saveUser(userDetails);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(
            path="/updateUser",
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser userDetails, @PathParam("email") String email) throws NotFoundException {
        return new ResponseEntity<>(userService.updateUser(userDetails, email),HttpStatus.OK);
    }

    @GetMapping(
            value = "/confirmUser/{userId}"
    )
    public String welcomeUser(@PathVariable Long userId) throws Exception {
        AppUser user = userService.getUserById(userId);
        user.setConfirmed(true);
        userService.saveUser(user);
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

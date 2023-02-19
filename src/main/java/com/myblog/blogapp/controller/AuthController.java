package com.myblog.blogapp.controller;

import com.myblog.blogapp.entities.Role;
import com.myblog.blogapp.entities.User;
import com.myblog.blogapp.payload.JWTAuthResponse;
import com.myblog.blogapp.payload.LoginDto;
import com.myblog.blogapp.payload.SignUpDto;
import com.myblog.blogapp.repository.RoleRepository;
import com.myblog.blogapp.repository.UserRepository;
import com.myblog.blogapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtTokenProvider tokenProvider;

    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;//this bean will not be automatically create like password encoder
    //we have to define a method in security config file for bean creation.(this class used for verification purpose)
    //localhost:8080/api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto)
    {
        // if username and password is true it will generate tokens
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

       // return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);

       String token= tokenProvider.generateToken(authentication);
       return ResponseEntity.ok(new JWTAuthResponse(token));


    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto)
    {
        //add check for username exists in DB
        if(userRepository.existsByUsername(signUpDto.getUsername()))
        {
            return new ResponseEntity<>("Username is already taken!",HttpStatus.BAD_REQUEST);
        }

        //add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail()))
        {
            return new ResponseEntity<>("Email is already taken!",HttpStatus.BAD_REQUEST);
        }
        // create user object for copy the data of signup to user.
        User user=new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
         //here we find the role of user form role table based on name it will return the role object.
        //Will set Admin role for user by default.
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));//convert the role obj to set by using collections class.

        userRepository.save(user);
       return new ResponseEntity<>("User register successfully",HttpStatus.OK);

    }

}

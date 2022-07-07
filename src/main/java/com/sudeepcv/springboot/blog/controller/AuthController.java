package com.sudeepcv.springboot.blog.controller;

import com.sudeepcv.springboot.blog.entity.Role;
import com.sudeepcv.springboot.blog.entity.User;
import com.sudeepcv.springboot.blog.payload.JWTAuthResponse;
import com.sudeepcv.springboot.blog.payload.LoginDto;
import com.sudeepcv.springboot.blog.payload.SignUpDto;
import com.sudeepcv.springboot.blog.repository.RoleRepository;
import com.sudeepcv.springboot.blog.repository.UserRepository;
import com.sudeepcv.springboot.blog.security.JwtTokenProvider;
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

//import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("login")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {

        if (userRepository.existsByUsername(signUpDto.getUsername()) ) {
            return new ResponseEntity<>("username already taken", HttpStatus.BAD_REQUEST);
        }

        if ( userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("email already taken", HttpStatus.BAD_REQUEST);
        }

        User user=new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role role=roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(role));

        User newUser=userRepository.save(user);
        return new ResponseEntity<>("user saved", HttpStatus.CREATED);

    }
}

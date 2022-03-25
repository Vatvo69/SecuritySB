package com.example.demo.controller;

import com.example.demo.dto.request.SignInForm;
import com.example.demo.dto.request.SignUpForm;
import com.example.demo.dto.response.ResponseJwt;
import com.example.demo.dto.response.ResponseMessage;
import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.userprincipal.UserPrincipal;
import com.example.demo.service.impl.RoleService;
import com.example.demo.service.impl.UserService;
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

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignUpForm signUpForm){
        if(userService.existsByUsername(signUpForm.getUsername()))
            return new ResponseEntity<>(new ResponseMessage("Username is existed!"), HttpStatus.OK);
        if(userService.existsByEmail(signUpForm.getEmail()))
            return new ResponseEntity<>(new ResponseMessage("Email is existed!"),HttpStatus.OK);
        User user=new User(signUpForm.getName(),signUpForm.getUsername(),signUpForm.getPassword(),signUpForm.getEmail(),passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> stringSet=signUpForm.getRoles();
        Set<Role> roles=new HashSet<>();

        stringSet.forEach(role -> {
            switch (role){
                case "admin":
                    Role adminRole=roleService.findByName(RoleName.ADMIN).orElseThrow(
                            ()->new RuntimeException("Role Not Found!")
                    );
                    roles.add(adminRole);
                    break;
                case "user":
                    Role userRole=roleService.findByName(RoleName.USER).orElseThrow(
                            ()->new RuntimeException("Role Not Found!")
                    );
                    roles.add(userRole);
                    break;
                default:
                    Role pmRole=roleService.findByName(RoleName.PM).orElseThrow(
                            ()->new RuntimeException("Role Not Found!")
                    );
                    roles.add(pmRole);
            }
        });
        user.setRoles(roles);
        //userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("Create Success!"),HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody SignInForm signInForm){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(),signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtProvider.createJwt(authentication);
        UserPrincipal userPrincipal= (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(new ResponseJwt(token,userPrincipal.getName(),userPrincipal.getAuthorities()));
    }
}

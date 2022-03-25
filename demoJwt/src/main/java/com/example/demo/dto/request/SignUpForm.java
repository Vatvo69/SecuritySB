package com.example.demo.dto.request;

import com.example.demo.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
    private String name;
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
}

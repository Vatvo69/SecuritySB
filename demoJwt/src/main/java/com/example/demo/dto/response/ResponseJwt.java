package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseJwt {
    private int id;
    private String token;
    private String name;
    private Collection<?extends GrantedAuthority> roles;

    public ResponseJwt(String token, String name, Collection<? extends GrantedAuthority> authorities) {
        this.token=token;
        this.name=name;
        this.roles=authorities;
    }
}

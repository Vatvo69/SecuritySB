package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user",uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    @Size(min = 3,max = 50)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Size(min = 3,max = 50)
    @Column(name = "username")
    private String username;

    @JsonIgnore
    @NotBlank
    @Size(min = 3,max = 100)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Email
    @NaturalId
    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles=new HashSet<>();


    public User(String name, String username, String password, String email, String encode) {
        this.name=name;
        this.username=username;
        this.password=password;
        this.email=email;
        this.password=encode;
    }
}

package com.example.demo.service.impl;

import com.example.demo.model.Role;
import com.example.demo.model.RoleName;
import com.example.demo.repository.IRoleRepository;
import com.example.demo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {
    @Autowired
    IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByUsername(RoleName name) {
        return roleRepository.findByUsername(name);
    }
}

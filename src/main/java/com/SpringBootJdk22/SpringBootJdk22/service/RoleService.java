package com.SpringBootJdk22.SpringBootJdk22.service;

import com.SpringBootJdk22.SpringBootJdk22.model.Role;
import com.SpringBootJdk22.SpringBootJdk22.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Tìm tất cả các vai trò
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    // Tìm vai trò theo tên
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}

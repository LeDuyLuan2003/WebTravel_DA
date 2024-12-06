package com.SpringBootJdk22.SpringBootJdk22;

import com.SpringBootJdk22.SpringBootJdk22.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootJdk22Application implements CommandLineRunner {
	@Autowired
	private RoleService roleService;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootJdk22Application.class, args);
	}

	@Override
	public void run(String... args) {
		// Danh sách các role mặc định
		String[] defaultRoles = {"ADMIN", "EMPLOYEE", "USER"};

		for (String role : defaultRoles) {
			roleService.addRoleIfNotExists(role);
		}
	}
}

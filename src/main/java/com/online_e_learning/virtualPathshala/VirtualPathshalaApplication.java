package com.online_e_learning.virtualPathshala;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VirtualPathshalaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualPathshalaApplication.class, args);
	}

	// ✅ Create default admin user when app starts
	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String adminEmail = "admin@virtualpathshala.com";

			// ✅ If exists, skip creation
			if (userRepository.existsByEmail(adminEmail)) {
				System.out.println("✅ Admin already exists!");
				return;
			}

			// ✅ Create a new admin with BCrypt password
			User admin = new User();
			admin.setName("Super Admin");
			admin.setEmail(adminEmail);
			admin.setMobile("+91 7644014111");
			admin.setRole(Role.ADMIN);
			admin.setStatus(Status.ACTIVE);

			// ✅ Use BCryptPasswordEncoder
			admin.setPasswordHash(passwordEncoder.encode("teamRaushan$04"));

			User savedAdmin = userRepository.save(admin);

			System.out.println("✅ Default Admin Created ✅");
			System.out.println("📌 Email: " + savedAdmin.getEmail());
			System.out.println("🔑 Password: teamRaushan$04");
			System.out.println("🎯 Role: " + savedAdmin.getRole());
			System.out.println("🔐 Password Encoded with: BCrypt");
			System.out.println("📱 Mobile: " + savedAdmin.getMobile());
			System.out.println("📊 Status: " + savedAdmin.getStatus());
			System.out.println("🆔 User ID: " + savedAdmin.getId());
		};
	}
}
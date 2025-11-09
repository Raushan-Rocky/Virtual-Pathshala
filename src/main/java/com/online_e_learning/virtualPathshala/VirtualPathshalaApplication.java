package com.online_e_learning.virtualPathshala;

import com.online_e_learning.virtualPathshala.enums.Role;
import com.online_e_learning.virtualPathshala.enums.Status;
import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class VirtualPathshalaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualPathshalaApplication.class, args);
	}

	// ✅ Create default admin user when app starts
	@Bean
	CommandLineRunner initAdmin(UserRepository userRepository) {
		return args -> {
			String adminEmail = "admin@virtualpathshala.com";

			// ✅ If exists, skip creation
			if (userRepository.existsByEmail(adminEmail)) {
				System.out.println("✅ Admin already exists!");
				return;
			}

			// ✅ Create a new admin
			User admin = new User();
			admin.setName("Super Admin");
			admin.setEmail(adminEmail);
			admin.setMobile("+91 7644014111");
			admin.setRole(Role.ADMIN);
			admin.setStatus(Status.ACTIVE);
			admin.setPasswordHash(hashPassword("teamRaushan$04")); // ✅ Default password

			userRepository.save(admin);
			System.out.println("✅ Default Admin Created ✅");
			System.out.println("📌 Email: admin@virtualpathshala.com");
			System.out.println("🔑 Password: teamRaushan$04");
		};
	}

	// ✅ Hash Password (same used in AuthService)
	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error hashing password", e);
		}
	}
}

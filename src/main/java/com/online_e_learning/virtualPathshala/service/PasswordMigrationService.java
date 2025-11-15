package com.online_e_learning.virtualPathshala.service;

import com.online_e_learning.virtualPathshala.model.User;
import com.online_e_learning.virtualPathshala.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class PasswordMigrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Check if password needs migration from SHA-256 to BCrypt
     */
    public boolean needsMigration(String storedHash) {
        return storedHash != null && storedHash.length() == 64 && isSHA256Hash(storedHash);
    }

    /**
     * Migrate password during login if it's SHA-256
     */
    public boolean migratePasswordDuringLogin(String rawPassword, String storedHash, User user) {
        try {
            if (needsMigration(storedHash) && verifySHA256(rawPassword, storedHash)) {
                String newPasswordHash = passwordEncoder.encode(rawPassword);
                user.setPasswordHash(newPasswordHash);
                userRepository.save(user);
                System.out.println("✅ Password migrated to BCrypt for user: " + user.getEmail());
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("❌ Password migration failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if string is a valid SHA-256 hash
     */
    private boolean isSHA256Hash(String hash) {
        return hash.matches("^[a-f0-9]{64}$");
    }

    /**
     * Verify SHA-256 password
     */
    private boolean verifySHA256(String rawPassword, String storedHash) {
        try {
            String computedHash = hashWithSHA256(rawPassword);
            return computedHash.equals(storedHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Hash with SHA-256 (for migration purposes)
     */
    private String hashWithSHA256(String password) {
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

    /**
     * Migrate all SHA-256 passwords to BCrypt
     */
    public void migrateAllPasswords() {
        List<User> users = userRepository.findAll();
        int migratedCount = 0;

        for (User user : users) {
            if (needsMigration(user.getPasswordHash())) {
                // For known admin password
                if (user.getEmail().equals("admin@virtualpathshala.com")) {
                    String newPasswordHash = passwordEncoder.encode("teamRaushan$04");
                    user.setPasswordHash(newPasswordHash);
                    userRepository.save(user);
                    migratedCount++;
                    System.out.println("✅ Migrated admin password to BCrypt");
                }
            }
        }

        System.out.println("✅ Password migration completed: " + migratedCount + " users migrated");
    }
}
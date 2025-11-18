package com.online_e_learning.virtualPathshala.config;

import com.online_e_learning.virtualPathshala.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthEntryPoint authEntryPoint;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(new JwtAccessDeniedHandler())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/video/**").permitAll()
                        // ✅ PUBLIC ENDPOINTS - Anyone can access (NO AUTH REQUIRED)
                        .requestMatchers(
                                "/", "/homepage", "/login", "/signup", "/forgotpass",
                                "/studentlogin",
                                "/api/auth/**",       // All auth endpoints
                                "/api/signup",        // Signup endpoint
                                "/api/login",         // Login endpoint
                                "/api/public/**",
                                "/css/**", "/js/**", "/image/**", "/webjars/**",
                                "/uploads/**", "/favicon.ico", "/error"
                        ).permitAll()

                        // ✅ STUDENT ENDPOINTS - Add this section
                        .requestMatchers("/student").hasRole("STUDENT")
                        .requestMatchers("/student/**").hasRole("STUDENT")

                        // ✅ USER ENDPOINTS - Role-based access
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/teachers").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/users/students").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/users/email/{email}").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/users/role/{role}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/teacher/{id}/profile").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/users/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/change-password").authenticated()

                        // ✅ ADMIN USER MANAGEMENT ENDPOINTS - CRITICAL SECURITY
                        .requestMatchers(HttpMethod.GET, "/api/admin/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/admin/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/admin/users/role").hasRole("ADMIN")

                        // ✅ ADMIN DASHBOARD ENDPOINTS
                        .requestMatchers(HttpMethod.GET, "/api/admin/dashboard/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/dashboard").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/dashboard/stats").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/courses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/admin/enrollments").hasRole("ADMIN")

                        // ✅ ADMIN PAGES
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")

                        // ✅ STUDENT ENDPOINTS
                        .requestMatchers("/student/**", "/api/student/**").hasRole("STUDENT")

                        // ✅ TEACHER ENDPOINTS
                        .requestMatchers("/teacher/**", "/api/teacher/**").hasRole("TEACHER")

                        // ✅ COURSE ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/courses/create").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/courses/update/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/delete/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAnyRole("TEACHER", "ADMIN", "STUDENT")

                        // ✅ ASSIGNMENT ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/assignments/create").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/assignments/update/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/assignments/delete/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/assignments/**").hasAnyRole("TEACHER", "STUDENT")

                        // ✅ ENROLLMENT ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/enrollments/create").hasAnyRole("STUDENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/enrollments/**").hasAnyRole("STUDENT", "ADMIN")

                        // ✅ LESSON ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/lessons/create").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/lessons/update/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/lessons/delete/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/lessons/**").hasAnyRole("TEACHER", "STUDENT")

                        // ✅ RESOURCE ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/resources/create").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/resources/**").hasAnyRole("TEACHER", "STUDENT")

                        // ✅ SUBMISSION ENDPOINTS
                        .requestMatchers(HttpMethod.POST, "/api/submissions/create").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/submissions/grade/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/submissions/**").hasAnyRole("STUDENT", "TEACHER")

                        // ✅ GRADES ENDPOINTS
                        .requestMatchers("/api/grades/**").hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                        // ✅ DASHBOARD ENDPOINTS - Add this in SecurityConfig
                        .requestMatchers("/api/dashboard/admin").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/teacher/**").hasRole("TEACHER")
                        .requestMatchers("/api/dashboard/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/dashboard/**").authenticated()

                        // ✅ All other requests require authentication
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setJwtTokenProvider(tokenProvider);
        filter.setUserDetailsService(customUserDetailsService);
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8040",
                "http://localhost:3000",
                "http://localhost:8000",
                "http://127.0.0.1:8040",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8000"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
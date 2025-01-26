package com.storelink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.storelink.services.MyUserDetailsService;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private final MyUserDetailsService userDetailsService;
	 private final JwtFilter jwtFilter;

	 public SecurityConfig(MyUserDetailsService userDetailsService, JwtFilter jwtFilter) {
		 this.userDetailsService = userDetailsService;
	     this.jwtFilter = jwtFilter;
	 }
	 
	 @Order(1)
	 @Bean
	 public SecurityFilterChain cmsSecurityFilter(HttpSecurity http) throws Exception {
	     http
	         // Apply this filter chain only to /cms/** paths
	         .securityMatcher("/cms/**")
	         
	         // CSRF configuration (ensure this is intentional)
	         .csrf(csrf -> csrf.disable())
	         
	         // Authorization rules
	         .authorizeHttpRequests(auth -> auth
	             .requestMatchers("/cms/login").permitAll() // Allow login endpoint
	             .anyRequest().hasRole("ADMIN") // Restrict other requests to ROLE_ADMIN
	         )
	         
	         // Form login configuration
	         .formLogin(form -> form
	             .loginPage("/cms/login") // Custom login page
	             .loginProcessingUrl("/cms/login") // Form action URL for processing login
	             .defaultSuccessUrl("/cms/dashboard", true) // Redirect to dashboard on successful login
	             .failureUrl("/cms/login?error=true")
	         )
	         
	         // Logout configuration
	         .logout(logout -> logout
	             .logoutUrl("/cms/logout") // Logout URL
	             .invalidateHttpSession(true) // Invalidate session on logout
	             .clearAuthentication(true) // Clear authentication information
	             .logoutSuccessUrl("/cms/login?logout=true") // Redirect to login on successful logout
	         )
	         
	         // Session management configuration
	         
	         .sessionManagement(session -> {
	             session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
	             session.maximumSessions(1) // Allow only one session per user
//	             		.maxSessionsPreventsLogin(true) //prevent a second login
	                    .expiredUrl("/cms/login?expired=true");// Redirect to login page on session expiry
//	             session.invalidSessionUrl("/cms/login?invalid=true");// Redirect to login page on invalid session
	             session.sessionFixation().migrateSession();// Prevent session fixation attacks// Create session only when needed
	         });

	     return http.build();
	 }




	 @Order(2)
	 @Bean
	 public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
	     http
	         .securityMatcher("/api/**") // Apply this filter chain only to /api/** paths
	         .csrf(csrf -> csrf.disable())
	         .authorizeHttpRequests(auth -> auth
	             .requestMatchers("/api/auth/**").permitAll()
	             .anyRequest().authenticated())
	         .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	     return http.build();
	 }
	
	@Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }	

}

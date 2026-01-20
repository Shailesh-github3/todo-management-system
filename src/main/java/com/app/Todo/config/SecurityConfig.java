package com.app.Todo.config;

import com.app.Todo.models.User;
import com.app.Todo.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepo userRepo;

    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. UserDetailsService: Tells Spring how to find users in the DB
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> userRes = Optional.ofNullable(userRepo.findByUsername(username));
                if (userRes.isEmpty()) {
                    throw new UsernameNotFoundException("User not found");
                }
                return userRes.get(); // Return the User entity which implements UserDetails
            }
        };
    }

    // 3. Security Rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 1. PUBLIC URLs (Login, Register, Static Files)
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()

                        // 2. PRIVATE URLs (Tasks, Profile)
                        .requestMatchers("/tasks/**", "/profile/**").authenticated()

                        // 3. ALL OTHERS (Catch-All)
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/tasks", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
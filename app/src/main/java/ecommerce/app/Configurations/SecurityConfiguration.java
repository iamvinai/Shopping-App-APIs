package ecommerce.app.Configurations;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ecommerce.app.Data.RoleRepository;
import ecommerce.app.Data.UserRepository;
import ecommerce.app.model.AppRoles;
import ecommerce.app.model.Roles;
import ecommerce.app.model.Users;
import ecommerce.app.security.AuthEntryPointJwt;
import ecommerce.app.security.AuthTokenFilter;
import ecommerce.app.security.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private AuthTokenFilter authTokenFilter;
    

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(myUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
                return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf->csrf.disable())
                    .exceptionHandling(exception-> exception.authenticationEntryPoint(unauthorizedHandler))
                    .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth->
                                            auth.requestMatchers("/api/auth/**").permitAll()
                                            // .requestMatchers("/api/admin/**").permitAll()
                                            // .requestMatchers("/api/public/**").permitAll()
                                            .requestMatchers("/swagger-ui/**").permitAll()
                                            .requestMatchers("/api/test/**").permitAll()
                                            .requestMatchers("/images/**").permitAll()
                                            .requestMatchers("/v3/appi-docs/**").permitAll()
                                            .requestMatchers("/h2-console/**").permitAll()
                                            .anyRequest().authenticated());
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.headers(headers->headers.frameOptions(frame->frame.sameOrigin()));
        httpSecurity.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    } 
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Roles userRole = roleRepository.findByRoleName(AppRoles.ROLE_USER)
                    .orElseGet(() -> {
                        Roles newUserRole = new Roles(AppRoles.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Roles sellerRole = roleRepository.findByRoleName(AppRoles.ROLE_SELLER)
                    .orElseGet(() -> {
                        Roles newSellerRole = new Roles(AppRoles.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Roles adminRole = roleRepository.findByRoleName(AppRoles.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Roles newAdminRole = new Roles(AppRoles.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Roles> userRoles = Set.of(userRole);
            Set<Roles> sellerRoles = Set.of(sellerRole);
            Set<Roles> adminRoles = Set.of(userRole, sellerRole, adminRole);

            // Create users if not already present
            if (!userRepository.existsByUsername("user1")) {
                Users user1 = new Users("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("seller1")) {
                Users seller1 = new Users("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUsername("admin")) {
                Users admin = new Users("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }


}

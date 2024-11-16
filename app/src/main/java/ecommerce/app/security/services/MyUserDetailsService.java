package ecommerce.app.security.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ecommerce.app.Data.UserRepository;
import ecommerce.app.model.Users;
import jakarta.transaction.Transactional;

@Service
public class MyUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
        .orElseThrow(()-> new UsernameNotFoundException("User not found with username "+username));
        return MyUserDetails.build(user);
    }

}

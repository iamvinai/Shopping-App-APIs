package ecommerce.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ecommerce.app.configuration.AppContants;
import ecommerce.app.data.UserRepository;
import ecommerce.app.err.ResourceNotFoundException;
import ecommerce.app.model.Users;

@Component
public class AuthUtils {

    @Autowired
    private UserRepository userRepository;
    public String getLoggedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(()-> new ResourceNotFoundException(AppContants.USER_TABLE,"username",authentication.getName()));
        return user.getEmail();
    }

    public Users getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("getLoggedInUser: "+authentication.getName());
        Users user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(()-> new ResourceNotFoundException(AppContants.USER_TABLE,"username",authentication.getName()));
        return user;
    }

}

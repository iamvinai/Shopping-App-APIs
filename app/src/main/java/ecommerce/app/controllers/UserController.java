package ecommerce.app.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.app.model.Users;
import ecommerce.app.services.UserServiceImpl;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @GetMapping("/users")
    public List<Users> getUsers(){
        return userService.getUsers();
    }

    @PutMapping("/users")
    public void saveUser(@RequestBody Users user){
        userService.createUser(user);
    }
}

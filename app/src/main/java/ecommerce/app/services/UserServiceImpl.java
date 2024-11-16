package ecommerce.app.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.app.data.RolesRepository;
import ecommerce.app.data.UserRepository;
import ecommerce.app.model.AppRoles;
import ecommerce.app.model.Roles;
import ecommerce.app.model.Users;
import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;
    @Override
    @Transactional
    public List<Users> getUsers() {
        List<Users> users = userRepository.findAll();
          for (Users user : users) {
        System.out.println(user.getRoles().size()); 
    }// Forces loading the roles collection
        return users;
    
    }

    @Override
    public Users getUser(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }



    @Override
    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public Users createUser(Users user) {
        Roles roleAdmin = new Roles();
        roleAdmin.setRoleName(AppRoles.ROLE_ADMIN);

        Roles roleUser = new Roles();
        roleUser.setRoleName(AppRoles.ROLE_USER);
            // Save roles before associating them with the user
        rolesRepository.save(roleAdmin);
        rolesRepository.save(roleUser);


        // Create a user
        Users user1 = new Users();
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        // Set roles for the user

        Set<Roles> roles = new HashSet<>();
        roles.add(roleAdmin);
        roles.add(roleUser);
        user1.setRoles(roles);
        // roleAdmin.getUsers().add(user1);
        // roleUser.getUsers().add(user1);
        userRepository.save(user1);


        return user1;
    }

    @Override
    public Users updateUser(Users user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

}

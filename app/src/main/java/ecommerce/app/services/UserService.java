package ecommerce.app.services;

import java.util.List;

import ecommerce.app.model.Users;

public interface UserService {

    List<Users> getUsers();
    Users getUser(Long id);
    Users createUser(Users user);
    Users updateUser(Users user);
    void deleteUser(Long id);
}

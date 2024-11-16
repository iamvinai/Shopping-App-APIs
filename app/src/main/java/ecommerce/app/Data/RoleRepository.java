package ecommerce.app.Data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.app.model.AppRoles;
import ecommerce.app.model.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRoleName(AppRoles roleUser);

}

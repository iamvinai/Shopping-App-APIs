package ecommerce.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce.app.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {

}

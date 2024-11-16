package ecommerce.app.Data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce.app.model.Addresses;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, Long> {

}

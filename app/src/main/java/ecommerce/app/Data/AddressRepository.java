package ecommerce.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ecommerce.app.model.Addresses;
import jakarta.transaction.Transactional;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Addresses a WHERE a.id = ?1")
    public void deleteById(@SuppressWarnings("null") Long id);
}

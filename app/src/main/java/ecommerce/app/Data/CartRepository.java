package ecommerce.app.data;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ecommerce.app.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("Select c from Cart c WHERE c.user.email =?1")
    Optional<Cart> findCartByEmail(String email);

    @Query("Select c from Cart c WHERE c.user.id =?1")
    Optional<Cart> findCartByUserId(Long userId);

    @Query("Select c from Cart c WHERE c.user.email =?1 and c.id =?2")
    Optional<Cart> findCartByEmailAndCartId(String email, Long cartId);

    @Query("Select c from Cart c JOIN Fetch c.cart_items ci JOIN Fetch ci.product p WHERE p.id =?1")
    Optional<List<Cart>> findCartByProductId(Long productId);
}

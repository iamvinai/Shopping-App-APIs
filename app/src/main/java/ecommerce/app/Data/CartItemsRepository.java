package ecommerce.app.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ecommerce.app.model.CartItems;
import jakarta.transaction.Transactional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    
    @Query("SELECT cItem FROM CartItems cItem WHERE cItem.cart.id=?1 AND cItem.product.id=?2")
    CartItems findByCartIdAndProductId(Long id1, Long id2);

    @Query("SELECT cItem FROM CartItems cItem WHERE cItem.cart.id=?1 AND cItem.product.id=?2")
    CartItems findCartItemByProductIdAndCartId(Long cartId, Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItems cItem WHERE cItem.id=?1")
    void deleteById(@SuppressWarnings("null") Long cartItemId);
}

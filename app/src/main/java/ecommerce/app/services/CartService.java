package ecommerce.app.services;

import java.util.List;

import ecommerce.app.res.CartDTO;

public interface CartService {

     CartDTO addProductToCart(Long productId,Integer quantity);
     List<CartDTO> getAllCarts();
     CartDTO getUserCart(String email,Long cartId);
     CartDTO updateProductQuantityInCart(Long productId, Integer quantity);
     Boolean deleteProductFromCart(Long productId,Long cartId);
}

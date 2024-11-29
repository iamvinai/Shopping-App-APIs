package ecommerce.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.app.configuration.AppContants;
import ecommerce.app.data.CartRepository;
import ecommerce.app.err.ResourceNotFoundException;
import ecommerce.app.model.Cart;
import ecommerce.app.res.CartDTO;
import ecommerce.app.services.CartService;
import ecommerce.app.utils.AuthUtils;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtils authUtil;
    @PostMapping("/cart/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, 
            @PathVariable Integer quantity) {
                CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOs = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOs,HttpStatus.OK);
    }
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUseCart() {

        String useremail = authUtil.getLoggedUserEmail();
        Cart userCart = cartRepository.findCartByEmail(useremail).
        orElseThrow(()-> new ResourceNotFoundException(AppContants.CART_TABLE,"email",useremail));
        CartDTO cartDTO = cartService.getUserCart(useremail,userCart.getId());
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
    }
     @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation) {

        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/cart/products/{productId}/delete")
    public ResponseEntity<String> deleteCartProduct(@PathVariable Long productId) {
        String useremail = authUtil.getLoggedUserEmail();
        Cart userCart = cartRepository.findCartByEmail(useremail).
        orElseThrow(()-> new ResourceNotFoundException(AppContants.CART_TABLE,"email",useremail));
        Boolean isDeleted = cartService.deleteProductFromCart(productId,userCart.getId());
        if(!isDeleted){
            throw new ResourceNotFoundException(AppContants.PRODUCT_TABLE,"productId",productId);
        }   
        return new ResponseEntity<String>("Deleted successfully!", HttpStatus.OK);
    }
}

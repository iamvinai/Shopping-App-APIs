package ecommerce.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.app.configuration.AppContants;
import ecommerce.app.data.CartItemsRepository;
import ecommerce.app.data.CartRepository;
import ecommerce.app.data.ProductRepository;
import ecommerce.app.err.CustomException;
import ecommerce.app.err.ResourceExistsException;
import ecommerce.app.err.ResourceNotFoundException;
import ecommerce.app.model.Cart;
import ecommerce.app.model.CartItems;
import ecommerce.app.model.Product;
import ecommerce.app.res.CartDTO;
import ecommerce.app.res.ProductDTO;
import ecommerce.app.utils.AuthUtils;
import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository  cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtils authUtils;
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
        Cart cart = fetchCart();
        //Retrieve Product details
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException(AppContants.PRODUCT_TABLE,"id",productId));
        CartItems cartItem = cartItemsRepository.findByCartIdAndProductId(cart.getId(),product.getId());
        if (cartItem != null) {
            throw new ResourceExistsException(AppContants.CART_ITEM_TABLE,product.getName());
        }
        if(product.getQuantity() == 0) {
            throw new CustomException(AppContants.PRODUCT_TABLE, "Out of stock");
        }
        if(product.getQuantity() < quantity) {
            throw new CustomException(AppContants.PRODUCT_TABLE, "Only "+product.getQuantity()+" units available");
        }
        cartItem = new CartItems();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(product.getDiscount());
        cartItem.setFinalPrice(product.getPrice());
        cartItemsRepository.save(cartItem);
        if(cart.getCart_items() == null) {
            List<CartItems> cartItems = new ArrayList<>();
            cartItems.add(cartItem);
            cart.setCart_items(cartItems);
        }
        else{
            cart.getCart_items().add(cartItem);
        }
        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProduct().getPrice()*quantity);
        cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);

        List<CartItems> cartItems = cart.getCart_items();
        if(cartItems == null) {
            return cartDTO;
        }
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item->{
            ProductDTO productDTO = modelMapper.map(item.getProduct(),ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;

    }
        
    private Cart fetchCart() {
        Optional<Cart> userCart = cartRepository.findCartByEmail(authUtils.getLoggedUserEmail());
        if (userCart.isPresent()) {
            return userCart.get();
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtils.getLoggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.size() == 0) {
            throw new ResourceNotFoundException(AppContants.CART_TABLE,"email",authUtils.getLoggedUserEmail());
        }
        List<CartDTO> cartDTOs = new ArrayList<>();
        cartDTOs = carts.stream().map(cart->{
            CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
            List<ProductDTO> productDTOs = cart.getCart_items().stream().map(
                item->{
                    ProductDTO productDTO = modelMapper.map(item.getProduct(),ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                }
            ).collect(Collectors.toList());
            cartDTO.setProducts(productDTOs);
            return cartDTO;
        }).collect(Collectors.toList());
        return cartDTOs;
    }

    @Override
    public CartDTO getUserCart(String email,Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(email,cartId)
        .orElseThrow(()-> new ResourceNotFoundException(AppContants.CART_TABLE,"cartId",cartId));
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
         Stream<ProductDTO> productDTOStream = cart.getCart_items().stream().map(item->{
            ProductDTO productDTO = modelMapper.map(item.getProduct(),ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }
    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        System.out.println(quantity);
        String emailId = authUtils.getLoggedUserEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId)
        .orElseThrow(()-> new ResourceNotFoundException(AppContants.CART_TABLE,"email",emailId));
        Long cartId  = userCart.getId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItems cartItem = cartItemsRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new CustomException(AppContants.PRODUCT_TABLE, product.getName() + " not available in the cart!!!");
        }
        if(quantity >0 && cartItem.getQuantity()+quantity < product.getQuantity()) {
            throw new CustomException(AppContants.PRODUCT_TABLE, "Only "+product.getQuantity()+" units available");
        }
        
        if(cartItem.getQuantity() + quantity > product.getQuantity()) {
            throw new CustomException(AppContants.PRODUCT_TABLE, ": Only "+product.getQuantity()+" units of " +product.getName()+" available");
        }
        cartItem.setFinalPrice(product.getFinalPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProduct().getPrice() * quantity));
        cartRepository.save(cart);
        CartItems updatedItem = cartItemsRepository.save(cartItem);
        if(updatedItem.getQuantity() <= 0){
            cart.getCart_items().remove(updatedItem);
            cartItemsRepository.deleteById(updatedItem.getId());
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItems> cartItems = cart.getCart_items();
        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });
        cartDTO.setProducts(productStream.toList());
        return cartDTO;
    }
    @Override
    public Boolean deleteProductFromCart(Long productId,Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItems cartItem = cartItemsRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new CustomException(AppContants.PRODUCT_TABLE, "Product " + product.getName() + " not available in the cart!!!");
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProduct().getPrice() * cartItem.getQuantity()));
        cartItemsRepository.deleteById(cartItem.getId());
        cartRepository.save(cart);
        
        return true;
    }


}

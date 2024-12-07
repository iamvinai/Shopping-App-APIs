package ecommerce.app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecommerce.app.configuration.AppContants;
import ecommerce.app.data.AddressRepository;
import ecommerce.app.data.CartRepository;
import ecommerce.app.data.OrderItemRepository;
import ecommerce.app.data.OrderRepository;
import ecommerce.app.data.PaymentRepository;
import ecommerce.app.data.ProductRepository;
import ecommerce.app.err.CustomException;
import ecommerce.app.err.ResourceNotFoundException;
import ecommerce.app.model.Addresses;
import ecommerce.app.model.Cart;
import ecommerce.app.model.CartItems;
import ecommerce.app.model.Order;
import ecommerce.app.model.OrderItem;
import ecommerce.app.model.Payment;
import ecommerce.app.model.Product;
import ecommerce.app.res.AddressDTO;
import ecommerce.app.res.OrderDTO;
import ecommerce.app.res.OrderItemDTO;
import ecommerce.app.res.OrderRequestDTO;
import ecommerce.app.utils.AuthUtils;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressesRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderDTO placeOrder(String paymentMethod, OrderRequestDTO orderRequestDTO) {
        String emailId = authUtils.getLoggedUserEmail();
        Long addressId = orderRequestDTO.getAddressId();
        //System.out.println(emailId);
        Cart cart = cartRepository.findCartByEmail(emailId).
                orElseThrow(()-> new ResourceNotFoundException(AppContants.CART_TABLE,"email",emailId));
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        Addresses address = addressesRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setOrderTotal(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setShippingAddress(address);

        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setPgId(orderRequestDTO.getPgId());
        payment.setPgStatus(orderRequestDTO.getPgStatus());
        payment.setPgResponse(orderRequestDTO.getPgResponseMessage());
        payment = paymentRepository.save(payment);
        order.setPayment(payment);

        List<CartItems> cartItems = cart.getCart_items();
        if (cartItems.isEmpty()) {
            throw new CustomException(AppContants.CART_TABLE," is empty");
        }
        Order savedOrder = orderRepository.save(order);
        List<OrderItem> orderItems = new ArrayList<>();
        Double totalPrice = 0.0;
        Iterator<CartItems> iterator = cartItems.iterator();
        while(iterator.hasNext()) {
            CartItems cartItem = iterator.next();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setPriceAfterDiscount(calculateFinalPrice(cartItem.getProduct().getPrice(), cartItem.getDiscount())*cartItem.getQuantity());
            totalPrice += orderItem.getPriceAfterDiscount();
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);
        CopyOnWriteArrayList<CartItems> cartItemsList = new CopyOnWriteArrayList<>(cart.getCart_items());
        for(CartItems item : cartItemsList) {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
            //System.out.println("Cart Id: "+cart.getId());
            cartService.deleteProductFromCart(item.getProduct().getId(),cart.getId());
        }

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        orderDTO.setOrderItems(orderItemDTOs);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        orderDTO.setShippingAddress(modelMapper.map(address, AddressDTO.class));
        orderDTO.setOrderTotal(totalPrice);
        return orderDTO;
       }
    private Double calculateFinalPrice(Double currentPrice, Double discount) {
        Double finalPrice = currentPrice - ((currentPrice*discount*0.01));
        return finalPrice;
    }

}

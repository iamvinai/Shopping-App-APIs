package ecommerce.app.services;

import ecommerce.app.res.OrderDTO;
import ecommerce.app.res.OrderRequestDTO;

public interface OrderService {

    OrderDTO placeOrder(String paymentMethod, OrderRequestDTO orderRequestDTO);

}

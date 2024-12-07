package ecommerce.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.app.res.OrderDTO;
import ecommerce.app.res.OrderRequestDTO;
import ecommerce.app.services.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

   
    

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payment/{paymentMethod}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable("paymentMethod") String paymentMethod,@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO order = orderService.placeOrder(paymentMethod,orderRequestDTO);
        return ResponseEntity.ok(order);
    }


}

package ecommerce.app.res;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

   
    private Long id;
    private String email;
    private LocalDate orderDate;
    private String orderStatus;
    private double orderTotal;
    private PaymentDTO payment;
    private List<OrderItemDTO> orderItems;
    private AddressDTO shippingAddress;
}

package ecommerce.app.res;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    private ProductDTO product;
    @JsonIgnore
    private OrderDTO order;
    private Integer quantity;
    private Double priceAfterDiscount;
    private Double discount;
}

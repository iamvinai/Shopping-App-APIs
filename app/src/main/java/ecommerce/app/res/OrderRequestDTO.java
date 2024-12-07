package ecommerce.app.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private Long addressId;
    private String paymentMethod;
    private String pgName;
    private String pgId;
    private String pgStatus;
    private String pgResponseMessage;
}

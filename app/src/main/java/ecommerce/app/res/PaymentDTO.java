package ecommerce.app.res;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    @Size(min = 5, message = "Payment Method name must be at least 5 characters long")
    private String paymentMethod;
    private String paymentID;
    private String paymentStatus;
    private String paymentResponse;
}

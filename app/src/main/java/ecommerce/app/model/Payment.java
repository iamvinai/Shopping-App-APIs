package ecommerce.app.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_method")
    @Size(min = 5, message = "Payment Method name must be at least 5 characters long")
    private String paymentMethod;

   
    @Column(name = "payment_id")
    private String pgId;

    @Column(name = "payment_status")
    private String pgStatus;

    @Column(name = "payment_response")
    private String pgResponse;
}

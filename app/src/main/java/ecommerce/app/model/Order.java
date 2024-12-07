package ecommerce.app.model;

import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @Column(name = "email")
    @Email
    @NotBlank
    @Size(min = 5, max = 50)
    private String email;
    @Column(name = "order_date")
    private LocalDate orderDate;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "order_total")
    private double orderTotal;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
    private List<OrderItem> orderItems;
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Addresses shippingAddress;
}

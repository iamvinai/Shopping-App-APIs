package ecommerce.app.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.app.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}

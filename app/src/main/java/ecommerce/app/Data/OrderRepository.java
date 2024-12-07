package ecommerce.app.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.app.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

package ecommerce.app.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.app.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}

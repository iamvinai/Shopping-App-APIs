package ecommerce.app.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ecommerce.app.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    public Page<Product> findByCategoryIdOrderByPriceAsc(Long categoryId, Pageable pageable);
    public List<Product> findByNameLikeIgnoreCase(String keyword);
}
package ecommerce.app.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.app.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

}

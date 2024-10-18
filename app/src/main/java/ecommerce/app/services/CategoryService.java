package ecommerce.app.services;

import ecommerce.app.dto.CategoryDTO;
import ecommerce.app.dto.CategoryResponse;

public interface CategoryService {
    CategoryResponse getCategories(Integer pageNumber,Integer pageSize, String sortBy,String sortOrder);
    void createCategories(CategoryDTO categoryDTO);
    String deleteCategories(Long id);
    String updateCategories(Long id,CategoryDTO categoryDTO);
}

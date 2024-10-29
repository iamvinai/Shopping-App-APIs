package ecommerce.app.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ecommerce.app.Repositories.CategoryRepository;
import ecommerce.app.exceptions.NoDataPresentException;
import ecommerce.app.exceptions.ResourceExistsException;
import ecommerce.app.exceptions.ResourceNotFoundException;
import ecommerce.app.model.Category;
import ecommerce.app.payload.CategoryDTO;
import ecommerce.app.payload.CategoryResponse;
@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }
/**
 * Retrieves a paginated, sorted list of categories.
 *
 * @param pageNumber the page number to retrieve (zero-based)
 * @param pageSize the number of categories per page
 * @param sortBy the field by which to sort the categories
 * @param sortOrder the order of sorting: "asc" for ascending, "desc" for descending
 * @return a CategoryResponse containing the list of categories and pagination details
 * @throws NoDataPresentException if no categories are available
 */
    @Override
    public CategoryResponse getCategories(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by("asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        //sort = sort.reverse();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoriesPage.getContent();
        if(categories.isEmpty()){
            throw new NoDataPresentException("Category");
        }
        List<CategoryDTO> categoryDTOs = categories.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse  categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOs);
        categoryResponse.setPageNumber(categoriesPage.getNumber());
        categoryResponse.setPageSize(categoriesPage.getSize());
        categoryResponse.setTotalElements(categoriesPage.getTotalElements());
        categoryResponse.setTotalPages(categoriesPage.getTotalPages());
        categoryResponse.setLastPage(categoriesPage.isLast());
        return categoryResponse;
    }

    @Override
    public void createCategories(CategoryDTO categoryDTO) {
        Optional<Category> category2 = Optional.ofNullable(categoryRepository.findByName(categoryDTO.getName()));
        if(category2.isPresent()){
            throw new ResourceExistsException("Category",categoryDTO.getName());
        }
        Category category = modelMapper.map(categoryDTO,Category.class);
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategories(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category savedCategory = optionalCategory
                                .orElseThrow(() ->new ResourceNotFoundException("Category","id",id));
        categoryRepository.delete(savedCategory);
       return "Category deleted successfully";
    }

    @Override
    public String updateCategories(Long id,CategoryDTO categoryDTOUpdate) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Category savedCategory = optionalCategory
                                .orElseThrow(() ->new ResourceNotFoundException("Category","id",id));
        savedCategory.setName(categoryDTOUpdate.getName());
        categoryRepository.save(savedCategory);
        return "Updated successfully";
    }
    

}

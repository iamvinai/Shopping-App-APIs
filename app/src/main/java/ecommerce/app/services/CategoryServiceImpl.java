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
import ecommerce.app.exceptions.CategoryExistsException;
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
    @Override
    public CategoryResponse getCategories(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by("asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        //sort = sort.reverse();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Category> categoriesPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoriesPage.getContent();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No category found");
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
            throw new CategoryExistsException(categoryDTO.getName(),"name");
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

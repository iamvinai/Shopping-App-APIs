package ecommerce.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.app.configuration.AppContants;
import ecommerce.app.res.CategoryDTO;
import ecommerce.app.res.CategoryResponse;
import ecommerce.app.services.CategoryServiceImpl;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/api")
public class CategoryController {

    private CategoryServiceImpl categoryService;
    public CategoryController(CategoryServiceImpl categoryService){
        this.categoryService = categoryService;
    }
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(@RequestParam(required = false,name = "pageNumber",defaultValue = AppContants.PAGE_NUMBER) Integer pageNumber,
                                                        @RequestParam(required = false,name = "pageSize",defaultValue = AppContants.PAGE_SIZE) Integer pageSize,
                                                        @RequestParam(required = false,name = "sortBy",defaultValue = AppContants.SORT_BY) String sortBy,
                                                        @RequestParam(required = false,name = "sortOrder",defaultValue = AppContants.SORT_ORDER) String sortOrder){
        return new ResponseEntity<>(categoryService.getCategories(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/admin/categories")
    public ResponseEntity insertCategories(@Valid @RequestBody CategoryDTO categoryDTO){ 
        categoryService.createCategories(categoryDTO);
        return new ResponseEntity<>("Category created successfully!",HttpStatus.OK);
    }
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable Long id){
        String status = categoryService.deleteCategories(id);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }
    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<String> upadteCategories(@PathVariable Long id,@Valid @RequestBody CategoryDTO categoryDTO){
        String status = categoryService.updateCategories(id,categoryDTO);
        return new ResponseEntity<String>(status,HttpStatus.OK);
    }

}

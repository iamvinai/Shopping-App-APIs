package ecommerce.app.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import ecommerce.app.config.AppContants;
import ecommerce.app.payload.ProductDTO;
import ecommerce.app.payload.ProductResponse;
import ecommerce.app.services.ProductService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/category/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                @PathVariable Long categoryId) {
        ProductDTO productDTOSaved = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<ProductDTO>(productDTOSaved, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(value = "pageNumber", required = false, defaultValue = AppContants.PAGE_NUMBER) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", required = false, defaultValue = AppContants.PAGE_SIZE) Integer pageSize,
                                                         @RequestParam(value = "sortBy", required = false, defaultValue = AppContants.SORT_BY) String sortBy,
                                                         @RequestParam(value = "sortOrder", required = false, defaultValue = AppContants.SORT_ORDER) String sortOrder) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize,  sortBy,  sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategoryId(@PathVariable Long categoryId,
    @RequestParam(value = "pageNumber", required = false, defaultValue = AppContants.PAGE_NUMBER) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", required = false, defaultValue = AppContants.PAGE_SIZE) Integer pageSize,
                                                         @RequestParam(value = "sortBy", required = false, defaultValue = AppContants.SORT_BY) String sortBy,
                                                         @RequestParam(value = "sortOrder", required = false, defaultValue = AppContants.SORT_ORDER) String sortOrder) {
        ProductResponse productResponse = productService.findByCategoryId(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword) {
        ProductResponse productResponse = productService.findByKeyword(keyword);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO,
                                                @PathVariable Long productId) {
        ProductDTO productDTOUpdated = productService.updateProduct(productDTO, productId);
        return new ResponseEntity<ProductDTO>(productDTOUpdated, HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/product/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        ProductDTO productDTO = productService.deleteProduct(id);
        return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                @RequestParam("image") MultipartFile image) throws IllegalStateException, IOException {
        ProductDTO productDTOUpdated = productService.updateProductImage(productId, image);
        return new ResponseEntity<ProductDTO>(productDTOUpdated, HttpStatus.CREATED);
    }
}

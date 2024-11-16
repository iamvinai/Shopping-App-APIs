package ecommerce.app.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import ecommerce.app.res.ProductDTO;
import ecommerce.app.res.ProductResponse;
public interface ProductService {

    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId);
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder);
    public ProductResponse findByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    public ProductResponse findByKeyword(String keyword);
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId);
    public ProductDTO deleteProduct(Long productId);
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IllegalStateException, IOException;
}

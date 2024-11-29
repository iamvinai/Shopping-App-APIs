package ecommerce.app.services;


import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ecommerce.app.configuration.AppContants;
import ecommerce.app.data.CartRepository;
import ecommerce.app.data.CategoryRepository;
import ecommerce.app.data.ProductRepository;
import ecommerce.app.err.NoDataPresentException;
import ecommerce.app.err.ResourceExistsException;
import ecommerce.app.err.ResourceNotFoundException;
import ecommerce.app.model.Category;
import ecommerce.app.model.Product;
import ecommerce.app.model.Cart;
import ecommerce.app.model.CartItems;
import ecommerce.app.res.ProductDTO;
import ecommerce.app.res.ProductResponse;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;
    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(AppContants.CATEGORY_TABLE,"id",categoryId));
        Product product = modelMapper.map(productDTO,Product.class);
        List<Product> productsListed = category.getProducts();
        productsListed.forEach(productListed -> {
            if(productListed.getName().equals(product.getName())){
                throw new ResourceExistsException(AppContants.PRODUCT_TABLE,product.getName());
            }
        });
        product.setCategory(category);
        double currentPrice = productDTO.getPrice();
        double discount = productDTO.getDiscount();
        double finalPrice = calculateFinalPrice(currentPrice,discount);
        product.setFinalPrice(finalPrice);
        product.setImage(AppContants.DEFAULT_IMAGE);
        category.getProducts().add(product);
        categoryRepository.save(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);

    }
    public double calculateFinalPrice(double currentPrice, double discount) {
        double finalPrice = currentPrice - ((currentPrice*discount*0.01));
        return finalPrice;
    }
    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(AppContants.ASC_ORDER.equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        //sort = sort.reverse();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> productsPage = productRepository.findAll(pageable);
        List<Product> products = productsPage.getContent();
        if(products.isEmpty()) {
            throw new NoDataPresentException(AppContants.PRODUCT_TABLE);
        }
        List<ProductDTO> productDTOs = products.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOs);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }
    @Override
    public ProductResponse findByCategoryId(Long categoryId,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort pageSort = Sort.by(AppContants.ASC_ORDER.equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize,pageSort);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(AppContants.CATEGORY_TABLE,"id",categoryId));
        Page<Product> productsPage = productRepository.findByCategoryIdOrderByPriceAsc(category.getId(),pageable);
        if(productsPage.isEmpty()) {
            throw new NoDataPresentException(AppContants.PRODUCT_TABLE);
        }
        List<Product> products = productsPage.getContent();
        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOs);
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setLastPage(productsPage.isLast());
        return productResponse;
    }
    @Override
    public ProductResponse findByKeyword(String keyword) {
        List<Product> products = productRepository.findByNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOs);
        return productResponse;
    }
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                                .orElseThrow(()-> new ResourceNotFoundException(AppContants.PRODUCT_TABLE,"id",productId));
        Double oldPrice = existingProduct.getPrice();
        existingProduct.setName(null!=productDTO.getName()?productDTO.getName():existingProduct.getName());
        existingProduct.setPrice(-1.0<productDTO.getPrice()?productDTO.getPrice():existingProduct.getPrice());
        existingProduct.setDiscount(-1.0<productDTO.getDiscount() && 100>=productDTO.getDiscount()?productDTO.getDiscount():existingProduct.getDiscount());
        if(productDTO.getDiscount()<=100 || productDTO.getDiscount()>=0) {
            double currentPrice = productDTO.getPrice();
            double discount = productDTO.getDiscount();
            double finalPrice = calculateFinalPrice(currentPrice,discount);
            existingProduct.setFinalPrice(finalPrice);
        }
        existingProduct.setQuantity(0!=productDTO.getQuantity()?productDTO.getQuantity():existingProduct.getQuantity());     
        Product savedProduct =  productRepository.save(existingProduct);
        List<Cart> carts = cartRepository.findCartByProductId(savedProduct.getId())
        .orElseThrow(()-> new ResourceNotFoundException(AppContants.CART_TABLE,"productId",savedProduct.getId()));
        carts.forEach(cart -> {
            double totalAmount = 0.0;
            int countOfItems=0;
            for(CartItems cartItem : cart.getCart_items()) {
                    totalAmount += (cartItem.getProduct().getPrice() * cartItem.getQuantity());
                    countOfItems+=existingProduct.getId().equals(cartItem.getProduct().getId())?cartItem.getQuantity():1;
            }
            cart.setTotalPrice(cart.getTotalPrice() - oldPrice*countOfItems + totalAmount);
            cartRepository.save(cart);
        });
        ProductDTO productDTOSaved = modelMapper.map(savedProduct,ProductDTO.class);
        return productDTOSaved;
    }
    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                    .orElseThrow(()-> new ResourceNotFoundException(AppContants.PRODUCT_TABLE,"id",productId));
        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);
    }
    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IllegalStateException, IOException {
        Product productFromDB = productRepository.findById(productId)
                    .orElseThrow(()-> new ResourceNotFoundException(AppContants.PRODUCT_TABLE,"id",productId));
        
        String fileName = fileService.uploadImageToServer(path,image);
        productFromDB.setImage(fileName);
        Product savedProduct = productRepository.save(productFromDB);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

}

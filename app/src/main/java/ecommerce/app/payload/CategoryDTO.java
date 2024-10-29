package ecommerce.app.payload;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    @NotBlank
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
    private List<ProductDTO> products;
}

package ecommerce.app.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    Long id;
    @NotBlank
    @Size(min = 5, message = "Name must be at least 5 characters long")
    String name;
}

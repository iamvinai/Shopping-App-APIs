package ecommerce.app.res;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {


    private Long id;
    @Size(min = 5, message = "Street Name must be at least 5 characters long")
    private String street;
    @Size(min = 5, message = "City Name must be at least 5 characters long")
    private String city;
    private String state;
    @Size(min = 3, message = "Country Name must be at least 3 characters long")
    private String country;
    @Size(min = 5, max = 5, message = "Zip Code must be only 5 characters long")
    private String zipCode;
}

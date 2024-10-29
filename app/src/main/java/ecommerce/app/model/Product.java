package ecommerce.app.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;
    private String description;
    private double price;
    private double discount;
    private double finalPrice;
    private String image;
    private String quantity;
    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE,})
    @JoinColumn(name = "category_id")
    private Category category;

}

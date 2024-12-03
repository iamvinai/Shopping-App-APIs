package ecommerce.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Addresses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;
    @Column(name = "street_name")
    @Size(min = 5, message = "Street Name must be at least 5 characters long")
    private String street;
    @Column(name = "city_name")
    @Size(min = 5, message = "City Name must be at least 5 characters long")
    private String city;
    @Column(name = "state_name")
    private String state;
    @Column(name = "country_name")
    @Size(min = 3, message = "Country Name must be at least 3 characters long")
    private String country;
    @Size(min = 5, max = 5, message = "Zip Code must be only 5 characters long")
    @Column(name = "zip_code")
    private String zipCode;
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users users;
    

}

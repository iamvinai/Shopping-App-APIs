package ecommerce.app.model;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    @Size(max = 20, message = "Name must be at most 20 characters long")
    private String username;
    @Column(name = "email")
    @Email
    @NotBlank
    @Size(min = 5, max = 50)
    private String email;
    @Column(name = "password",unique = true)
    @Size(min = 5, max = 120)
    @NotBlank
    private String password;
    public Users(String username2, String email2, String encode) {
           this.username = username2;
           this.email = email2;
           this.password = encode;     
        }
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
        @JsonProperty
    private Set<Roles> roles;
    @OneToMany(mappedBy = "users",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Addresses> addresses;
    @ToString.Exclude
    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},
    orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<Product> products;
    @ToString.Exclude
    @OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST,CascadeType.MERGE},orphanRemoval = true,fetch = FetchType.EAGER)
    private Cart cart;

}

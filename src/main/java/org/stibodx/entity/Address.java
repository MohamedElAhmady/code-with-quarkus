package org.stibodx.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Address extends PanacheEntityBase {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @NotBlank(message = "Street is required")
    @Column(nullable = false)
    private String street;
    
    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;
    
    @Column(name = "state_province")
    private String state;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @NotBlank(message = "Country is required")
    @Column(nullable = false)
    private String country;

    @Column(name = "is_primary")
    private boolean isPrimary = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public Address(String street, String city, String state, String postalCode,
                   String country, boolean isPrimary) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isPrimary = isPrimary;
    }
}

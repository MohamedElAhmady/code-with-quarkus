package org.stibodx.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "Address",
    description = "Address information for a user"
)
public class AddressDTO {
    
    @Schema(
        description = "Unique identifier for the address",
        example = "456e7890-e89b-12d3-a456-426614174001",
        readOnly = true
    )
    private UUID id;
    
    @NotBlank(message = "Street is required")
    @Schema(
        description = "Street address including house number and street name",
        example = "123 Main Street",
        required = true,
        maxLength = 255
    )
    private String street;
    
    @NotBlank(message = "City is required")
    @Schema(
        description = "City name",
        example = "San Francisco",
        required = true,
        maxLength = 100
    )
    private String city;
    
    @Schema(
        description = "State or province",
        example = "CA",
        maxLength = 50
    )
    private String state;
    
    @Schema(
        description = "Postal or ZIP code",
        example = "94105",
        maxLength = 20
    )
    private String postalCode;
    
    @NotBlank(message = "Country is required")
    @Schema(
        description = "Country name",
        example = "USA",
        required = true,
        maxLength = 100
    )
    private String country;
}

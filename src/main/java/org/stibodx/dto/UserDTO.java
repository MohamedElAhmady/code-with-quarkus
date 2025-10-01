package org.stibodx.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonbPropertyOrder({"id", "firstName", "lastName", "dateOfBirth", "email", "address", "job", "createdAt", "updatedAt"})
@JsonPropertyOrder({"id", "firstName", "lastName", "dateOfBirth", "email", "address", "job", "createdAt", "updatedAt"})
@Schema(
    name = "User",
    description = "User data transfer object containing user information and optional address"
)
public class UserDTO {
    
    @Schema(
        description = "Unique identifier for the user",
        example = "123e4567-e89b-12d3-a456-426614174000",
        readOnly = true
    )
    private UUID id;
    
    @NotBlank(message = "First name is required")
    @Schema(
        description = "User's first name",
        example = "John",
        required = true,
        minLength = 1,
        maxLength = 50
    )
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Schema(
        description = "User's last name",
        example = "Doe",
        required = true,
        minLength = 1,
        maxLength = 50
    )
    private String lastName;
    
    @Past(message = "Date of birth must be in the past")
    @Schema(
        description = "User's date of birth",
        example = "1990-01-15",
        format = "date"
    )
    private LocalDate dateOfBirth;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Schema(
        description = "User's email address",
        example = "john.doe@example.com",
        required = true,
        format = "email"
    )
    private String email;

    @Schema(
        description = "User's job title or profession",
        example = "Software Developer",
        maxLength = 100
    )
    private String job;
    
    @Schema(
        description = "Timestamp when the user was created",
        example = "2024-01-15T10:30:00Z",
        readOnly = true
    )
    private LocalDateTime createdAt;
    
    @Schema(
        description = "Timestamp when the user was last updated",
        example = "2024-01-15T10:30:00Z",
        readOnly = true
    )
    private LocalDateTime updatedAt;

    @Valid
    @Schema(
        description = "User's address information (optional)",
        nullable = true
    )
    private AddressDTO address;
}
package org.stibodx.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.stibodx.dto.UserDTO;
import org.stibodx.dto.PagedResult;
import org.stibodx.service.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "users", description = "User management operations")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided information. Email must be unique across all users. Address is optional and can be provided during user creation."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "User created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = UserDTO.class),
                examples = {
                    @ExampleObject(
                        name = "user_with_address",
                        summary = "User created with address",
                        value = """
                        {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "firstName": "Alice",
                          "lastName": "Johnson",
                          "dateOfBirth": "1985-06-20",
                          "email": "alice.johnson@example.com",
                          "job": "Product Manager",
                          "createdAt": "2024-01-15T10:30:00Z",
                          "updatedAt": "2024-01-15T10:30:00Z",
                          "address": {
                            "id": "456e7890-e89b-12d3-a456-426614174001",
                            "street": "123 Tech Street",
                            "city": "San Francisco",
                            "state": "CA",
                            "postalCode": "94105",
                            "country": "USA"
                          }
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = {
                    @ExampleObject(
                        name = "validation_error",
                        summary = "Validation error",
                        value = """
                        {
                          "error": "Error creating user: First name is required"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "invalid_email",
                        summary = "Invalid email format",
                        value = """
                        {
                          "error": "Error creating user: Email should be valid"
                        }
                        """
                    )
                }
            )
        ),
        @APIResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = @ExampleObject(
                    name = "duplicate_email",
                    summary = "Duplicate email error",
                    value = """
                    {
                      "error": "Error creating user: User with email alice.johnson@example.com already exists"
                    }
                    """
                )
            )
        )
    })
    public Response createUser(
        @Valid 
        @Schema(
            description = "User data for creation",
            example = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "dateOfBirth": "1990-01-15",
              "email": "john.doe@example.com",
              "job": "Software Developer",
              "address": {
                "street": "123 Main Street",
                "city": "San Francisco",
                "state": "CA",
                "postalCode": "94105",
                "country": "USA"
              }
            }
            """
        )
        UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return Response.status(Response.Status.CREATED)
                .entity(createdUser)
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a specific user by their unique identifier"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = UserDTO.class),
                examples = @ExampleObject(
                    name = "user_found",
                    summary = "User found",
                    value = """
                    {
                      "id": "123e4567-e89b-12d3-a456-426614174000",
                      "firstName": "John",
                      "lastName": "Doe",
                      "dateOfBirth": "1990-01-15",
                      "email": "john.doe@example.com",
                      "job": "Software Developer",
                      "createdAt": "2024-01-15T10:30:00Z",
                      "updatedAt": "2024-01-15T10:30:00Z",
                      "address": {
                        "id": "456e7890-e89b-12d3-a456-426614174001",
                        "street": "123 Main Street",
                        "city": "San Francisco",
                        "state": "CA",
                        "postalCode": "94105",
                        "country": "USA"
                      }
                    }
                    """
                )
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid UUID format",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = @ExampleObject(
                    name = "invalid_uuid",
                    summary = "Invalid UUID format",
                    value = """
                    {
                      "error": "Invalid UUID format"
                    }
                    """
                )
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = @ExampleObject(
                    name = "user_not_found",
                    summary = "User not found",
                    value = """
                    {
                      "error": "User not found with id: 123e4567-e89b-12d3-a456-426614174000"
                    }
                    """
                )
            )
        )
    })
    public Response getUserById(
        @Parameter(
            description = "The unique identifier of the user",
            required = true,
            example = "123e4567-e89b-12d3-a456-426614174000",
            schema = @Schema(type = SchemaType.STRING, format = "uuid")
        )
        @PathParam("id") UUID id) {
        Optional<UserDTO> user = userService.findById(id);
        if (user.isPresent()) {
            return Response.ok(user.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found with id: " + id)
                    .build();
        }
    }

    @GET
    @Operation(
        summary = "Get all users with pagination",
        description = "Retrieves a paginated list of all users in the system. Use page and size parameters to control pagination."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Paginated list of users retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = PagedResult.class),
                examples = @ExampleObject(
                    name = "paginated_users",
                    summary = "Paginated users response",
                    value = """
                    {
                      "data": [
                        {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "firstName": "John",
                          "lastName": "Doe",
                          "dateOfBirth": "1990-01-15",
                          "email": "john.doe@example.com",
                          "job": "Software Developer",
                          "createdAt": "2024-01-15T10:30:00Z",
                          "updatedAt": "2024-01-15T10:30:00Z",
                          "address": {
                            "id": "456e7890-e89b-12d3-a456-426614174001",
                            "street": "123 Main Street",
                            "city": "San Francisco",
                            "state": "CA",
                            "postalCode": "94105",
                            "country": "USA"
                          }
                        }
                      ],
                      "pagination": {
                        "page": 0,
                        "size": 10,
                        "totalElements": 25,
                        "totalPages": 3,
                        "hasNext": true,
                        "hasPrevious": false
                      }
                    }
                    """
                )
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid pagination parameters",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = @ExampleObject(
                    name = "invalid_pagination",
                    summary = "Invalid pagination parameters",
                    value = """
                    {
                      "error": "Page number cannot be negative"
                    }
                    """
                )
            )
        )
    })
    public Response getAllUsers(
        @Parameter(
            description = "Page number (0-based)",
            example = "0",
            schema = @Schema(type = SchemaType.INTEGER, minimum = "0", defaultValue = "0")
        )
        @QueryParam("page") @DefaultValue("0") int page,
        
        @Parameter(
            description = "Number of items per page (1-100)",
            example = "10",
            schema = @Schema(type = SchemaType.INTEGER, minimum = "1", maximum = "100", defaultValue = "10")
        )
        @QueryParam("size") @DefaultValue("10") int size) {
        
        PagedResult<UserDTO> pagedUsers = userService.findAllPaginated(page, size);
        return Response.ok(pagedUsers).build();
    }

    @GET
    @Path("/by-email/{email}")
    @Operation(
        summary = "Get user by email",
        description = "Retrieves a specific user by their email address. This is useful for user-friendly lookups and integrations with external systems."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = UserDTO.class),
                examples = @ExampleObject(
                    name = "user_found_by_email",
                    summary = "User found by email",
                    value = """
                    {
                      "id": "123e4567-e89b-12d3-a456-426614174000",
                      "firstName": "John",
                      "lastName": "Doe",
                      "dateOfBirth": "1990-01-15",
                      "email": "john.doe@example.com",
                      "job": "Software Developer",
                      "createdAt": "2024-01-15T10:30:00Z",
                      "updatedAt": "2024-01-15T10:30:00Z",
                      "address": {
                        "id": "456e7890-e89b-12d3-a456-426614174001",
                        "street": "123 Main Street",
                        "city": "San Francisco",
                        "state": "CA",
                        "postalCode": "94105",
                        "country": "USA"
                      }
                    }
                    """
                )
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = @ExampleObject(
                    name = "user_not_found_by_email",
                    summary = "User not found by email",
                    value = """
                    {
                      "error": "User not found with email: john.doe@example.com"
                    }
                    """
                )
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Invalid email format",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                examples = @ExampleObject(
                    name = "invalid_email",
                    summary = "Invalid email format",
                    value = """
                    {
                      "error": "Invalid email format"
                    }
                    """
                )
            )
        )
    })
    public Response getUserByEmail(
        @Parameter(
            description = "The email address of the user",
            required = true,
            example = "john.doe@example.com",
            schema = @Schema(type = SchemaType.STRING, format = "email")
        )
        @PathParam("email") String email) {
        UserDTO user = userService.findByEmail(email.trim().toLowerCase());
        return Response.ok(user).build();
    }
}
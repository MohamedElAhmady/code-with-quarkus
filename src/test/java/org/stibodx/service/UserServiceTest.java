package org.stibodx.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.stibodx.dto.UserDTO;
import org.stibodx.dto.PagedResult;
import org.stibodx.exception.UserNotFoundException;
import org.stibodx.exception.InvalidEmailException;
import org.stibodx.exception.UserAlreadyExistsException;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@QuarkusTest
@TestTransaction
class UserServiceTest {

    @Inject
    UserService userService;

    private UserDTO testUserDTO;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setFirstName("Test");
        testUserDTO.setLastName("User");
        testUserDTO.setEmail("new.test.user@example.com");
        testUserDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testUserDTO.setJob("Test Engineer");
        
        testUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    }

    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully with valid data")
        void shouldCreateUserSuccessfully() {
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setFirstName("New");
            newUserDTO.setLastName("User");
            newUserDTO.setEmail("new.user@example.com");
            newUserDTO.setDateOfBirth(LocalDate.of(1995, 5, 15));
            newUserDTO.setJob("Software Developer");

            UserDTO createdUser = userService.createUser(newUserDTO);

            assertNotNull(createdUser);
            assertNotNull(createdUser.getId());
            assertEquals("New", createdUser.getFirstName());
            assertEquals("User", createdUser.getLastName());
            assertEquals("new.user@example.com", createdUser.getEmail());
            assertEquals(LocalDate.of(1995, 5, 15), createdUser.getDateOfBirth());
            assertEquals("Software Developer", createdUser.getJob());
        }

        @Test
        @DisplayName("Should throw exception when user with email already exists")
        void shouldThrowExceptionWhenUserAlreadyExists() {
            UserDTO uniqueUserDTO = new UserDTO();
            uniqueUserDTO.setFirstName("Duplicate");
            uniqueUserDTO.setLastName("Test");
            uniqueUserDTO.setEmail("duplicate.test@example.com");
            uniqueUserDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
            uniqueUserDTO.setJob("Test Engineer");
            
            userService.createUser(uniqueUserDTO);

            UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(uniqueUserDTO)
            );

            assertEquals("User with email duplicate.test@example.com already exists", 
                exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Find User Tests")
    class FindUserTests {

        @Test
        @DisplayName("Should find user by ID when user exists")
        void shouldFindUserById() {
            Optional<UserDTO> foundUser = userService.findById(testUserId);

            assertTrue(foundUser.isPresent());
            assertEquals(testUserId, foundUser.get().getId());
            assertEquals("Test", foundUser.get().getFirstName());
            assertEquals("User", foundUser.get().getLastName());
        }

        @Test
        @DisplayName("Should return empty when user ID does not exist")
        void shouldReturnEmptyWhenUserNotFound() {
            UUID nonExistentId = UUID.randomUUID();

            Optional<UserDTO> foundUser = userService.findById(nonExistentId);

            assertFalse(foundUser.isPresent());
        }

        @Test
        @DisplayName("Should find all users")
        void shouldFindAllUsers() {
            List<UserDTO> users = userService.findAll();

            assertNotNull(users);
            assertFalse(users.isEmpty());
            assertTrue(users.size() >= 3); // At least the test data users
        }

        @Test
        @DisplayName("Should find user by email when user exists")
        void shouldFindUserByEmail() {
            String email = "test.user@example.com";

            UserDTO foundUser = userService.findByEmail(email);

            assertNotNull(foundUser);
            assertEquals(email, foundUser.getEmail());
            assertEquals("Test", foundUser.getFirstName());
        }

        @Test
        @DisplayName("Should throw exception when user not found by email")
        void shouldThrowExceptionWhenUserNotFoundByEmail() {
            String nonExistentEmail = "nonexistent@example.com";

            UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findByEmail(nonExistentEmail)
            );
            
            assertTrue(exception.getMessage().contains(nonExistentEmail));
        }

        @Test
        @DisplayName("Should throw exception for null email")
        void shouldThrowExceptionForNullEmail() {
            InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userService.findByEmail(null)
            );
            
            assertTrue(exception.getMessage().contains("null or empty"));
        }

        @Test
        @DisplayName("Should throw exception for empty email")
        void shouldThrowExceptionForEmptyEmail() {
            InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userService.findByEmail("")
            );
            
            assertTrue(exception.getMessage().contains("null or empty"));
        }

        @Test
        @DisplayName("Should throw exception for invalid email format")
        void shouldThrowExceptionForInvalidEmailFormat() {
            String invalidEmail = "invalid-email";

            InvalidEmailException exception = assertThrows(
                InvalidEmailException.class,
                () -> userService.findByEmail(invalidEmail)
            );
            
            assertTrue(exception.getMessage().contains("Invalid email format"));
        }
    }

    @Nested
    @DisplayName("Pagination Tests")
    class PaginationTests {

        @Test
        @DisplayName("Should return paginated results with valid parameters")
        void shouldReturnPaginatedResults() {
            UserDTO createdUser = userService.createUser(testUserDTO);
            
            PagedResult<UserDTO> result = userService.findAllPaginated(0, 2);
            
            assertNotNull(result);
            assertNotNull(result.getUsers());
            assertEquals(0, result.getPagination().getPage());
            assertEquals(2, result.getPagination().getSize());
            assertTrue(result.getPagination().getTotalElements() >= 3);
            assertTrue(result.getPagination().getTotalPages() >= 2);
            assertFalse(result.getPagination().isHasPrevious());
            assertTrue(result.getPagination().isHasNext());
        }

        @Test
        @DisplayName("Should return second page correctly")
        void shouldReturnSecondPage() {
            PagedResult<UserDTO> result = userService.findAllPaginated(1, 2);

            assertNotNull(result);
            assertEquals(1, result.getPagination().getPage());
            assertEquals(2, result.getPagination().getSize());
            assertTrue(result.getPagination().isHasPrevious());
        }

        @Test
        @DisplayName("Should throw exception for negative page number")
        void shouldThrowExceptionForNegativePage() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findAllPaginated(-1, 10)
            );
            
            assertTrue(exception.getMessage().contains("Page number cannot be negative"));
        }

        @Test
        @DisplayName("Should throw exception for zero page size")
        void shouldThrowExceptionForZeroPageSize() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findAllPaginated(0, 0)
            );
            
            assertTrue(exception.getMessage().contains("Page size must be positive"));
        }

        @Test
        @DisplayName("Should throw exception for negative page size")
        void shouldThrowExceptionForNegativePageSize() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findAllPaginated(0, -5)
            );
            
            assertTrue(exception.getMessage().contains("Page size must be positive"));
        }

        @Test
        @DisplayName("Should throw exception for page size exceeding limit")
        void shouldThrowExceptionForPageSizeExceedingLimit() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findAllPaginated(0, 101)
            );
            
            assertTrue(exception.getMessage().contains("Page size cannot exceed 100"));
        }

        @Test
        @DisplayName("Should handle empty results for high page number")
        void shouldHandleEmptyResultsForHighPageNumber() {
            PagedResult<UserDTO> result = userService.findAllPaginated(100, 10);

            assertNotNull(result);
            assertTrue(result.getUsers().isEmpty());
            assertEquals(100, result.getPagination().getPage());
            assertEquals(10, result.getPagination().getSize());
            assertTrue(result.getPagination().isHasPrevious());
            assertFalse(result.getPagination().isHasNext());
        }
    }
}
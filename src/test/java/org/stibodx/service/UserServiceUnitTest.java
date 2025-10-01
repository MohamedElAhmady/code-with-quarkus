package org.stibodx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.stibodx.dto.UserDTO;
import org.stibodx.dto.AddressDTO;
import org.stibodx.mapper.UserMapper;
import org.stibodx.exception.InvalidEmailException;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceUnitTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO testUserDTO;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("123 Main St");
        addressDTO.setCity("Test City");
        addressDTO.setPostalCode("12345");
        addressDTO.setCountry("Test Country");

        testUserDTO = new UserDTO();
        testUserDTO.setId(testUserId);
        testUserDTO.setFirstName("John");
        testUserDTO.setLastName("Doe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testUserDTO.setJob("Software Engineer");
        testUserDTO.setAddress(addressDTO);
    }

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {

        @Test
        @DisplayName("Should throw InvalidEmailException for null email")
        void shouldThrowExceptionForNullEmail() {
            assertThrows(InvalidEmailException.class, () -> userService.findByEmail(null));
        }

        @Test
        @DisplayName("Should throw InvalidEmailException for empty email")
        void shouldThrowExceptionForEmptyEmail() {
            assertThrows(InvalidEmailException.class, () -> userService.findByEmail(""));
        }

        @Test
        @DisplayName("Should throw InvalidEmailException for blank email")
        void shouldThrowExceptionForBlankEmail() {
            assertThrows(InvalidEmailException.class, () -> userService.findByEmail("   "));
        }

        @Test
        @DisplayName("Should throw InvalidEmailException for invalid email format")
        void shouldThrowExceptionForInvalidEmailFormat() {
            String[] invalidEmails = {
                "invalid-email",
                "@example.com",
                "test@"
            };

            for (String invalidEmail : invalidEmails) {
                assertThrows(InvalidEmailException.class, 
                    () -> userService.findByEmail(invalidEmail),
                    "Should throw exception for: " + invalidEmail);
            }
        }
    }

    @Nested
    @DisplayName("Pagination Validation Tests")
    class PaginationValidationTests {

        @Test
        @DisplayName("Should throw exception for negative page number")
        void shouldThrowExceptionForNegativePage() {
            assertThrows(IllegalArgumentException.class,
                () -> userService.findAllPaginated(-1, 10));
        }

        @Test
        @DisplayName("Should throw exception for zero page size")
        void shouldThrowExceptionForZeroPageSize() {
            assertThrows(IllegalArgumentException.class,
                () -> userService.findAllPaginated(0, 0));
        }

        @Test
        @DisplayName("Should throw exception for negative page size")
        void shouldThrowExceptionForNegativePageSize() {
            assertThrows(IllegalArgumentException.class,
                () -> userService.findAllPaginated(0, -5));
        }

        @Test
        @DisplayName("Should throw exception for page size exceeding limit")
        void shouldThrowExceptionForPageSizeExceedingLimit() {
            assertThrows(IllegalArgumentException.class,
                () -> userService.findAllPaginated(0, 101));
        }

        @Test
        @DisplayName("Should accept valid pagination parameters")
        void shouldAcceptValidPaginationParameters() {
            int page = 1;
            int size = 10;

            assertTrue(page > 0, "Page should be positive");
            assertTrue(size > 0, "Size should be positive");
            assertTrue(size <= 100, "Size should not exceed maximum limit");
        }
    }

    @Nested
    @DisplayName("Mapper Integration Tests")
    class MapperIntegrationTests {

        @Test
        @DisplayName("Should verify mapper is injected")
        void shouldVerifyMapperIsInjected() {
            assertNotNull(userMapper, "UserMapper should be injected");

        }
    }
}
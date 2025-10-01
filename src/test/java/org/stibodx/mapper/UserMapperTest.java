package org.stibodx.mapper;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.stibodx.dto.UserDTO;
import org.stibodx.dto.AddressDTO;
import org.stibodx.entity.User;
import org.stibodx.entity.Address;

import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@QuarkusTest
class UserMapperTest {

    @Inject
    UserMapper userMapper;

    private UserDTO testUserDTO;
    private User testUser;
    private AddressDTO testAddressDTO;
    private Address testAddress;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setId(UUID.randomUUID());
        testUserDTO.setFirstName("John");
        testUserDTO.setLastName("Doe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setDateOfBirth(LocalDate.of(1990, 5, 15));
        testUserDTO.setJob("Software Engineer");

        testAddressDTO = new AddressDTO();
        testAddressDTO.setId(UUID.randomUUID());
        testAddressDTO.setStreet("123 Main St");
        testAddressDTO.setCity("Test City");
        testAddressDTO.setPostalCode("12345");
        testAddressDTO.setCountry("Test Country");

        testUserDTO.setAddress(testAddressDTO);

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setFirstName("Jane");
        testUser.setLastName("Smith");
        testUser.setEmail("jane.smith@example.com");
        testUser.setDateOfBirth(LocalDate.of(1985, 10, 20));
        testUser.setJob("Product Manager");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        testAddress = new Address();
        testAddress.setId(UUID.randomUUID());
        testAddress.setStreet("456 Oak Ave");
        testAddress.setCity("Sample City");
        testAddress.setPostalCode("67890");
        testAddress.setCountry("Sample Country");
        testAddress.setUser(testUser);

        testUser.setAddress(testAddress);
    }

    @Nested
    @DisplayName("DTO to Entity Mapping Tests")
    class DTOToEntityTests {

        @Test
        @DisplayName("Should map UserDTO to User entity correctly")
        void shouldMapUserDTOToEntity() {
            User mappedUser = userMapper.toEntity(testUserDTO);

            assertNotNull(mappedUser);
            assertEquals(testUserDTO.getId(), mappedUser.getId());
            assertEquals(testUserDTO.getFirstName(), mappedUser.getFirstName());
            assertEquals(testUserDTO.getLastName(), mappedUser.getLastName());
            assertEquals(testUserDTO.getEmail(), mappedUser.getEmail());
            assertEquals(testUserDTO.getDateOfBirth(), mappedUser.getDateOfBirth());
            assertEquals(testUserDTO.getJob(), mappedUser.getJob());
        }

        @Test
        @DisplayName("Should map UserDTO with Address to User entity correctly")
        void shouldMapUserDTOWithAddressToEntity() {
            User mappedUser = userMapper.toEntity(testUserDTO);

            assertNotNull(mappedUser);
            assertNotNull(mappedUser.getAddress());
            
            Address mappedAddress = mappedUser.getAddress();
            assertEquals(testAddressDTO.getId(), mappedAddress.getId());
            assertEquals(testAddressDTO.getStreet(), mappedAddress.getStreet());
            assertEquals(testAddressDTO.getCity(), mappedAddress.getCity());
            assertEquals(testAddressDTO.getPostalCode(), mappedAddress.getPostalCode());
            assertEquals(testAddressDTO.getCountry(), mappedAddress.getCountry());
            
            assertEquals(mappedUser, mappedAddress.getUser());
        }

        @Test
        @DisplayName("Should handle null UserDTO")
        void shouldHandleNullUserDTO() {
            User mappedUser = userMapper.toEntity(null);

            assertNull(mappedUser);
        }

        @Test
        @DisplayName("Should handle UserDTO without address")
        void shouldHandleUserDTOWithoutAddress() {
            testUserDTO.setAddress(null);

            User mappedUser = userMapper.toEntity(testUserDTO);

            assertNotNull(mappedUser);
            assertNull(mappedUser.getAddress());
            assertEquals(testUserDTO.getFirstName(), mappedUser.getFirstName());
            assertEquals(testUserDTO.getLastName(), mappedUser.getLastName());
        }

        @Test
        @DisplayName("Should handle UserDTO with partial data")
        void shouldHandleUserDTOWithPartialData() {
            UserDTO partialUserDTO = new UserDTO();
            partialUserDTO.setFirstName("Partial");
            partialUserDTO.setEmail("partial@example.com");

            User mappedUser = userMapper.toEntity(partialUserDTO);

            assertNotNull(mappedUser);
            assertEquals("Partial", mappedUser.getFirstName());
            assertEquals("partial@example.com", mappedUser.getEmail());
            assertNull(mappedUser.getLastName());
            assertNull(mappedUser.getDateOfBirth());
            assertNull(mappedUser.getJob());
        }
    }

    @Nested
    @DisplayName("Entity to DTO Mapping Tests")
    class EntityToDTOTests {

        @Test
        @DisplayName("Should map User entity to UserDTO correctly")
        void shouldMapUserEntityToDTO() {
            UserDTO mappedDTO = userMapper.toDTO(testUser);

            assertNotNull(mappedDTO);
            assertEquals(testUser.getId(), mappedDTO.getId());
            assertEquals(testUser.getFirstName(), mappedDTO.getFirstName());
            assertEquals(testUser.getLastName(), mappedDTO.getLastName());
            assertEquals(testUser.getEmail(), mappedDTO.getEmail());
            assertEquals(testUser.getDateOfBirth(), mappedDTO.getDateOfBirth());
            assertEquals(testUser.getJob(), mappedDTO.getJob());
        }

        @Test
        @DisplayName("Should map User entity with Address to UserDTO correctly")
        void shouldMapUserEntityWithAddressToDTO() {
            UserDTO mappedDTO = userMapper.toDTO(testUser);

            assertNotNull(mappedDTO);
            assertNotNull(mappedDTO.getAddress());
            
            AddressDTO mappedAddressDTO = mappedDTO.getAddress();
            assertEquals(testAddress.getId(), mappedAddressDTO.getId());
            assertEquals(testAddress.getStreet(), mappedAddressDTO.getStreet());
            assertEquals(testAddress.getCity(), mappedAddressDTO.getCity());
            assertEquals(testAddress.getPostalCode(), mappedAddressDTO.getPostalCode());
            assertEquals(testAddress.getCountry(), mappedAddressDTO.getCountry());
        }

        @Test
        @DisplayName("Should handle null User entity")
        void shouldHandleNullUserEntity() {
            UserDTO mappedDTO = userMapper.toDTO(null);

            assertNull(mappedDTO);
        }

        @Test
        @DisplayName("Should handle User entity without address")
        void shouldHandleUserEntityWithoutAddress() {
            testUser.setAddress(null);

            UserDTO mappedDTO = userMapper.toDTO(testUser);

            assertNotNull(mappedDTO);
            assertNull(mappedDTO.getAddress());
            assertEquals(testUser.getFirstName(), mappedDTO.getFirstName());
            assertEquals(testUser.getLastName(), mappedDTO.getLastName());
        }

        @Test
        @DisplayName("Should handle User entity with partial data")
        void shouldHandleUserEntityWithPartialData() {
            User partialUser = new User();
            partialUser.setId(UUID.randomUUID());
            partialUser.setFirstName("Partial");
            partialUser.setEmail("partial@example.com");

            UserDTO mappedDTO = userMapper.toDTO(partialUser);

            assertNotNull(mappedDTO);
            assertEquals(partialUser.getId(), mappedDTO.getId());
            assertEquals("Partial", mappedDTO.getFirstName());
            assertEquals("partial@example.com", mappedDTO.getEmail());
            assertNull(mappedDTO.getLastName());
            assertNull(mappedDTO.getDateOfBirth());
            assertNull(mappedDTO.getJob());
        }
    }

    @Nested
    @DisplayName("Bidirectional Mapping Tests")
    class BidirectionalMappingTests {

        @Test
        @DisplayName("Should maintain data integrity in round-trip mapping")
        void shouldMaintainDataIntegrityInRoundTripMapping() {
            User mappedEntity = userMapper.toEntity(testUserDTO);
            UserDTO roundTripDTO = userMapper.toDTO(mappedEntity);

            assertNotNull(roundTripDTO);
            assertEquals(testUserDTO.getId(), roundTripDTO.getId());
            assertEquals(testUserDTO.getFirstName(), roundTripDTO.getFirstName());
            assertEquals(testUserDTO.getLastName(), roundTripDTO.getLastName());
            assertEquals(testUserDTO.getEmail(), roundTripDTO.getEmail());
            assertEquals(testUserDTO.getDateOfBirth(), roundTripDTO.getDateOfBirth());
            assertEquals(testUserDTO.getJob(), roundTripDTO.getJob());

            if (testUserDTO.getAddress() != null) {
                assertNotNull(roundTripDTO.getAddress());
                assertEquals(testUserDTO.getAddress().getStreet(), roundTripDTO.getAddress().getStreet());
                assertEquals(testUserDTO.getAddress().getCity(), roundTripDTO.getAddress().getCity());
            }
        }

        @Test
        @DisplayName("Should maintain data integrity in reverse round-trip mapping")
        void shouldMaintainDataIntegrityInReverseRoundTripMapping() {
            UserDTO mappedDTO = userMapper.toDTO(testUser);
            User roundTripEntity = userMapper.toEntity(mappedDTO);

            assertNotNull(roundTripEntity);
            assertEquals(testUser.getId(), roundTripEntity.getId());
            assertEquals(testUser.getFirstName(), roundTripEntity.getFirstName());
            assertEquals(testUser.getLastName(), roundTripEntity.getLastName());
            assertEquals(testUser.getEmail(), roundTripEntity.getEmail());
            assertEquals(testUser.getDateOfBirth(), roundTripEntity.getDateOfBirth());
            assertEquals(testUser.getJob(), roundTripEntity.getJob());

            if (testUser.getAddress() != null) {
                assertNotNull(roundTripEntity.getAddress());
                assertEquals(testUser.getAddress().getStreet(), roundTripEntity.getAddress().getStreet());
                assertEquals(testUser.getAddress().getCity(), roundTripEntity.getAddress().getCity());
                assertEquals(roundTripEntity, roundTripEntity.getAddress().getUser());
            }
        }
    }
}
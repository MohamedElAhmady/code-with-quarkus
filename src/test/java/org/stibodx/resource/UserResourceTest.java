package org.stibodx.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@TestTransaction
class UserResourceTest {

    @Nested
    @DisplayName("Create User Endpoint Tests")
    class CreateUserTests {

        @Test
        @DisplayName("Should create user successfully with valid data")
        void shouldCreateUserSuccessfully() {
            String userJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "dateOfBirth": "1990-01-15",
                    "email": "john.doe.test@example.com",
                    "job": "Software Developer",
                    "address": {
                        "street": "123 Main Street",
                        "city": "San Francisco",
                        "state": "CA",
                        "postalCode": "94105",
                        "country": "USA"
                    }
                }
                """;

            given()
                .contentType(ContentType.JSON)
                .body(userJson)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("firstName", equalTo("John"))
                .body("lastName", equalTo("Doe"))
                .body("email", equalTo("john.doe.test@example.com"))
                .body("job", equalTo("Software Developer"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .body("address.street", equalTo("123 Main Street"))
                .body("address.city", equalTo("San Francisco"));
        }

        @Test
        @DisplayName("Should return 400 for invalid user data")
        void shouldReturn400ForInvalidUserData() {
            String invalidUserJson = """
                {
                    "firstName": "",
                    "lastName": "Doe",
                    "dateOfBirth": "1990-01-15",
                    "email": "invalid-email",
                    "job": "Software Developer"
                }
                """;

            given()
                .contentType(ContentType.JSON)
                .body(invalidUserJson)
            .when()
                .post("/users")
            .then()
                .statusCode(400);
        }

        @Test
        @DisplayName("Should return 409 when user already exists")
        void shouldReturn409WhenUserAlreadyExists() {
            // First, create a user
            String userJson = """
                {
                    "firstName": "Jane",
                    "lastName": "Smith",
                    "dateOfBirth": "1985-05-20",
                    "email": "jane.smith.duplicate@example.com",
                    "job": "Product Manager"
                }
                """;

            given()
                .contentType(ContentType.JSON)
                .body(userJson)
            .when()
                .post("/users")
            .then()
                .statusCode(201);

            // Try to create the same user again
            given()
                .contentType(ContentType.JSON)
                .body(userJson)
            .when()
                .post("/users")
            .then()
                .statusCode(409);
        }

        @Test
        @DisplayName("Should return 500 for malformed JSON")
        void shouldReturn500ForMalformedJSON() {
            given()
                .contentType(ContentType.JSON)
                .body("{invalid json}")
            .when()
                .post("/users")
            .then()
                .statusCode(500);
        }
    }

    @Nested
    @DisplayName("Get User Endpoint Tests")
    class GetUserTests {

        @Test
        @DisplayName("Should get user by ID successfully")
        void shouldGetUserByIdSuccessfully() {
            // First create a user to retrieve
            String userJson = """
                {
                    "firstName": "Alice",
                    "lastName": "Johnson",
                    "dateOfBirth": "1992-03-10",
                    "email": "alice.johnson.get@example.com",
                    "job": "Designer"
                }
                """;

            String userId = given()
                .contentType(ContentType.JSON)
                .body(userJson)
            .when()
                .post("/users")
            .then()
                .statusCode(201)
                .extract()
                .path("id");

            // Now retrieve the user
            given()
            .when()
                .get("/users/{id}", userId)
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(userId))
                .body("firstName", equalTo("Alice"))
                .body("lastName", equalTo("Johnson"))
                .body("email", equalTo("alice.johnson.get@example.com"));
        }

        @Test
        @DisplayName("Should return 404 for non-existent user ID")
        void shouldReturn404ForNonExistentUserId() {
            String nonExistentId = UUID.randomUUID().toString();
            
            given()
            .when()
                .get("/users/{id}", nonExistentId)
            .then()
                .statusCode(404);
        }

        @Test
        @DisplayName("Should return 500 for invalid UUID format")
        void shouldReturn500ForInvalidUUIDFormat() {
            given()
            .when()
                .get("/users/{id}", "invalid-uuid")
            .then()
                .statusCode(500);
        }

        @Test
        @DisplayName("Should get user by email successfully")
        void shouldGetUserByEmailSuccessfully() {
            // First create a user to retrieve
            String userJson = """
                {
                    "firstName": "Bob",
                    "lastName": "Wilson",
                    "dateOfBirth": "1988-07-25",
                    "email": "bob.wilson.email@example.com",
                    "job": "Engineer"
                }
                """;

            given()
                .contentType(ContentType.JSON)
                .body(userJson)
            .when()
                .post("/users")
            .then()
                .statusCode(201);

            // Now retrieve the user by email
            given()
            .when()
                .get("/users/by-email/{email}", "bob.wilson.email@example.com")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("firstName", equalTo("Bob"))
                .body("lastName", equalTo("Wilson"))
                .body("email", equalTo("bob.wilson.email@example.com"));
        }

        @Test
        @DisplayName("Should return 404 for non-existent email")
        void shouldReturn404ForNonExistentEmail() {
            given()
            .when()
                .get("/users/by-email/{email}", "nonexistent@example.com")
            .then()
                .statusCode(404);
        }
    }

    @Nested
    @DisplayName("Get All Users Endpoint Tests")
    class GetAllUsersTests {

        @Test
        @DisplayName("Should get all users successfully")
        void shouldGetAllUsersSuccessfully() {
            given()
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", notNullValue())
                .body("size()", greaterThanOrEqualTo(0));
        }

        @Test
        @DisplayName("Should get users with pagination parameters")
        void shouldGetUsersWithPaginationParameters() {
            given()
                .queryParam("page", 0)
                .queryParam("size", 5)
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("users", notNullValue())
                .body("users.size()", lessThanOrEqualTo(5))
                .body("pagination.page", equalTo(0))
                .body("pagination.size", equalTo(5));
        }

        @Test
        @DisplayName("Should handle high page number gracefully")
        void shouldHandleHighPageNumberGracefully() {
            given()
                .queryParam("page", 100)
                .queryParam("size", 10)
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("users", hasSize(0))
                .body("pagination.page", equalTo(100))
                .body("pagination.size", equalTo(10))
                .body("pagination.hasNext", equalTo(false));
        }
    }

    @Nested
    @DisplayName("Content Type and Headers Tests")
    class ContentTypeAndHeadersTests {

        @Test
        @DisplayName("Should accept and return JSON content type")
        void shouldAcceptAndReturnJSONContentType() {
            given()
                .accept(ContentType.JSON)
            .when()
                .get("/users")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
        }

        @Test
        @DisplayName("Should handle missing Accept header gracefully")
        void shouldHandleMissingAcceptHeaderGracefully() {
            given()
            .when()
                .get("/users")
            .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("OpenAPI Documentation Tests")
    class OpenAPIDocumentationTests {

        @Test
        @DisplayName("Should provide OpenAPI specification")
        void shouldProvideOpenAPISpecification() {
            given()
            .when()
                .get("/q/openapi")
            .then()
                .statusCode(200);
        }
    }

    @Nested
    @DisplayName("Health Check Tests")
    class HealthCheckTests {

        @Test
        @DisplayName("Should provide health check endpoint")
        void shouldProvideHealthCheckEndpoint() {
            given()
            .when()
                .get("/q/health")
            .then()
                .statusCode(200);
        }

        @Test
        @DisplayName("Should provide readiness check")
        void shouldProvideReadinessCheck() {
            given()
            .when()
                .get("/q/health/ready")
            .then()
                .statusCode(200);
        }

        @Test
        @DisplayName("Should provide liveness check")
        void shouldProvideLivenessCheck() {
            given()
            .when()
                .get("/q/health/live")
            .then()
                .statusCode(200);
        }
    }
}
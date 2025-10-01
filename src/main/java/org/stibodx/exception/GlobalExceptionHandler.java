package org.stibodx.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Global exception handlers for the application.
 * Each exception type has its own dedicated mapper for better separation of concerns.
 */
public class GlobalExceptionHandler {

    /**
     * Utility method to create consistent error responses.
     */
    public static Map<String, String> createErrorResponse(String error, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return errorResponse;
    }

    @Provider
    public static class UserNotFoundExceptionMapper implements ExceptionMapper<UserNotFoundException> {
        @Override
        public Response toResponse(UserNotFoundException exception) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse("User not found", exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class InvalidEmailExceptionMapper implements ExceptionMapper<InvalidEmailException> {
        @Override
        public Response toResponse(InvalidEmailException exception) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse("Invalid email", exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class UserAlreadyExistsExceptionMapper implements ExceptionMapper<UserAlreadyExistsException> {
        @Override
        public Response toResponse(UserAlreadyExistsException exception) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(createErrorResponse("User already exists", exception.getMessage()))
                    .build();
        }
    }

    @Provider
    public static class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException exception) {
            Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
            Map<String, String> errors = new HashMap<>();
            
            for (ConstraintViolation<?> violation : violations) {
                String fieldName = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errors.put(fieldName, message);
            }
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Validation failed");
            errorResponse.put("violations", errors);
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        }
    }

    @Provider
    public static class GenericExceptionMapper implements ExceptionMapper<Exception> {
        @Override
        public Response toResponse(Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createErrorResponse("Internal server error", "An unexpected error occurred"))
                    .build();
        }
    }
}
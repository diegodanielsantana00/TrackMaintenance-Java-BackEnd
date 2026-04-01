package com.danieldiego.trackMaintenance.adapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard API response wrapper")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(

        @Schema(description = "HTTP status code")
        int status,

        @Schema(description = "Response message")
        String message,

        @Schema(description = "Response payload")
        T data,

        @Schema(description = "Timestamp of the response")
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status, String message, T details) {
        return new ApiResponse<>(status, message, details, LocalDateTime.now());
    }
}

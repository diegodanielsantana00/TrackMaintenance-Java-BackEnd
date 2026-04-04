package com.danieldiego.trackMaintenance.adapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Paginated API response wrapper")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagedApiResponse<T>(

        @Schema(description = "HTTP status code")
        int status,

        @Schema(description = "Response message")
        String message,

        @Schema(description = "Response payload list")
        List<T> data,

        @Schema(description = "Current page number (0-based)")
        int page,

        @Schema(description = "Page size")
        int size,

        @Schema(description = "Total number of elements")
        long totalElements,

        @Schema(description = "Total number of pages")
        int totalPages,

        @Schema(description = "Timestamp of the response")
        LocalDateTime timestamp
) {
    public static <T> PagedApiResponse<T> of(
            int status, String message, org.springframework.data.domain.Page<T> pageData) {
        return new PagedApiResponse<>(
                status,
                message,
                pageData.getContent(),
                pageData.getNumber(),
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                LocalDateTime.now()
        );
    }
}

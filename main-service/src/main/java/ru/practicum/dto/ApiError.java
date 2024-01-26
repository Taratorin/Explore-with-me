package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
	private final HttpStatus status;
	private final String reason;
	private final String message;
	private final List<String> errors;
	private final LocalDateTime timestamp = LocalDateTime.now();
}
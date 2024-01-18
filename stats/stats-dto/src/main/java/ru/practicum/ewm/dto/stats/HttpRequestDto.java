package ru.practicum.ewm.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpRequestDto {
    private HttpServletRequest request;
    private String app;
    private LocalDateTime timestamp;
}

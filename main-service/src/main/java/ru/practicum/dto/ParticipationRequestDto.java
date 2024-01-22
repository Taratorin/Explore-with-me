package ru.practicum.dto;

import lombok.Data;

@Data
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long eventId;
    private Long requesterId;
    private String status;
}

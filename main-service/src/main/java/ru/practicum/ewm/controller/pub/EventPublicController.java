package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.service.EventService;

@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
}

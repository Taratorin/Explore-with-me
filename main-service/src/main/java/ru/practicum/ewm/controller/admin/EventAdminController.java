package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.NewEventDto;

import javax.validation.Valid;


@RequiredArgsConstructor
public class EventAdminController {
//    private final EventService eventService;

    @GetMapping()
    public void endpoint(@RequestParam(defaultValue = "10") int id, @Valid NewEventDto eventDto) {

    }
}

package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT, REJECT_EVENT
    }
}

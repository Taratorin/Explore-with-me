package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW;

        @JsonCreator
        public static StateAction from(String stateAction) {
            for (StateAction state : values()) {
                if (state.name().equalsIgnoreCase(stateAction)) {
                    return state;
                }
            }
            return null;
        }
    }
}

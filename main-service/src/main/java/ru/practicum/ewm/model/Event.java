package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;
    private String annotation;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private State state;
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private Integer confirmedRequests;
    private Integer views;
}

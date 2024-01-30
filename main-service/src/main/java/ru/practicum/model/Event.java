package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;
    private String annotation;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    private float lat;
    private float lon;
    private Boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Transient
    private Integer confirmedRequests;
    @Transient
    private long views;

    public String getUri() {
        return "/events/" + id;
    }
}

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
    private float locationLat;
    private float locationLon;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private State state;
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
}

package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "compilations", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @CollectionTable(name = "events_compilations", joinColumns = @JoinColumn(name = "compilation"))
    @Column(name = "event_id")
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}

package ru.practicum.model;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@DynamicUpdate
@Table(name = "comments", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class EventComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    private String title;
    private String text;
    private LocalDateTime ts;
    @Transient
    private Long likes;
    @Transient
    private Long dislikes;
    @Transient
    private Long rating;
    @Column(name = "is_edited")
    private Boolean isEdited;
    @Column(name = "ts_edition")
    private LocalDateTime tsEdition;
    @ElementCollection
    @CollectionTable(name = "complaints_comments", joinColumns = @JoinColumn(name = "comment_id"))
    @Column(name = "complaint_id")
    private List<Complaint> complaint;
}

package ru.practicum.model;


import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

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
    @Formula(value = "(SELECT COUNT(l.user_id) FROM likes as l WHERE l.comment_id = id AND l.like_dislike = 1)")
    private Long likes;
    @Formula(value = "(SELECT COUNT(l.user_id) FROM likes as l WHERE l.comment_id = id AND l.like_dislike = -1)")
    private Long dislikes;
    @Formula(value = "(SELECT SUM(l.like_dislike) FROM likes as l WHERE l.comment_id = id)")
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

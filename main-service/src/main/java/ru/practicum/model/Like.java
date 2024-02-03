package ru.practicum.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "likes", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private EventComment comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "like_dislike")
    private int like;
}
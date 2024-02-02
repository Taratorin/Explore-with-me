package ru.practicum.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "complaints", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private EventComment comment;
    @ManyToOne
    @JoinColumn(name = "complainer_id")
    private User complainer;
    private String text;
    @Enumerated(EnumType.STRING)
    private StateComplaint state;
}

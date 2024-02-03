package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.CommentLikes;
import ru.practicum.model.EventComment;
import ru.practicum.model.Like;
import ru.practicum.model.User;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByCommentAndUser(EventComment comment, User user);

    void deleteAllByCommentId(long commentId);

    void deleteAllByComment(EventComment comment);

    @Query(value = "select new ru.practicum.model.CommentLikes(l.comment.id,\n" +
            "sum(case when l.like = 1 then l.like else 0 end),\n" +
            "sum(case when l.like = -1 then l.like else 0 end),\n" +
            "sum(l.like))\n" +
            "from Like as l \n" +
            "where l.comment in (?1)\n" +
            "group by l.comment.id")
    List<CommentLikes> getCommentLikesByComments(List<EventComment> comments);

    @Query(value = "select new ru.practicum.model.CommentLikes(l.comment.id,\n" +
            "sum(case when l.like = 1 then l.like else 0 end),\n" +
            "sum(case when l.like = -1 then l.like else 0 end),\n" +
            "sum(l.like))\n" +
            "from Like as l \n" +
            "where l.comment = ?1\n" +
            "group by l.comment.id")
    CommentLikes getCommentLikesByComment(EventComment comment);

    @Query(value = "select new ru.practicum.model.CommentLikes(l.comment.id,\n" +
            "sum(case when l.like = 1 then l.like else 0 end),\n" +
            "sum(case when l.like = -1 then l.like else 0 end),\n" +
            "sum(l.like))\n" +
            "from Like as l \n" +
            "where l.comment.id in (?1)\n" +
            "group by l.comment.id")
    List<CommentLikes> getCommentLikesByCommentIds(List<Long> commentIds);

    @Query(value = "select new ru.practicum.model.CommentLikes(l.comment.id,\n" +
            "sum(case when l.like = 1 then l.like else 0 end),\n" +
            "sum(case when l.like = -1 then l.like else 0 end),\n" +
            "sum(l.like))\n" +
            "from Like as l \n" +
            "where l.comment.id = ?1\n" +
            "group by l.comment.id")
    CommentLikes getCommentLikesByCommentIds(Long commentId);
}
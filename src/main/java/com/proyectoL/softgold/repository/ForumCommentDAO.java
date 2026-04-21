package com.proyectoL.softgold.repository;

import com.proyectoL.softgold.model.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForumCommentDAO extends JpaRepository<ForumComment, Long> {
    List<ForumComment> findByPost_IdOrderByFechaCreacionAsc(Long postId);
}

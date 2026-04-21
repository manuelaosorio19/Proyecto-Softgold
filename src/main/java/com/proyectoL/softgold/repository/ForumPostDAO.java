package com.proyectoL.softgold.repository;

import com.proyectoL.softgold.model.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ForumPostDAO extends JpaRepository<ForumPost, Long> {
    List<ForumPost> findByActivoTrueOrderByFechaCreacionDesc();
    List<ForumPost> findByCategoriaAndActivoTrue(String categoria);
    List<ForumPost> findByTituloContainingIgnoreCaseAndActivoTrue(String titulo);
}

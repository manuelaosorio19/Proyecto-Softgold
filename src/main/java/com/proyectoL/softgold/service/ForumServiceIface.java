package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.ForumComment;
import com.proyectoL.softgold.model.ForumPost;
import java.util.List;
import java.util.Optional;

public interface ForumServiceIface {
    List<ForumPost> listarPostsActivos();
    Optional<ForumPost> buscarPostPorId(Long id);
    ForumPost guardarPost(ForumPost post);
    void eliminarPost(Long id);
    ForumComment guardarComentario(ForumComment comment);
    List<ForumPost> buscarPorTitulo(String titulo);
    List<ForumPost> filtrarPorCategoria(String categoria);
}

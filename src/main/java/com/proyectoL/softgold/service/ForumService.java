package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.ForumComment;
import com.proyectoL.softgold.model.ForumPost;
import com.proyectoL.softgold.repository.ForumCommentDAO;
import com.proyectoL.softgold.repository.ForumPostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumService implements ForumServiceIface {

    @Autowired
    private ForumPostDAO postDAO;

    @Autowired
    private ForumCommentDAO commentDAO;

    @Override
    public List<ForumPost> listarPostsActivos() {
        return postDAO.findByActivoTrueOrderByFechaCreacionDesc();
    }

    @Override
    public Optional<ForumPost> buscarPostPorId(Long id) {
        return postDAO.findById(id);
    }

    @Override
    public ForumPost guardarPost(ForumPost post) {
        return postDAO.save(post);
    }

    @Override
    public void eliminarPost(Long id) {
        postDAO.findById(id).ifPresent(p -> {
            p.setActivo(false);
            postDAO.save(p);
        });
    }

    @Override
    public ForumComment guardarComentario(ForumComment comment) {
        return commentDAO.save(comment);
    }

    @Override
    public List<ForumPost> buscarPorTitulo(String titulo) {
        return postDAO.findByTituloContainingIgnoreCaseAndActivoTrue(titulo);
    }

    @Override
    public List<ForumPost> filtrarPorCategoria(String categoria) {
        return postDAO.findByCategoriaAndActivoTrue(categoria);
    }
}

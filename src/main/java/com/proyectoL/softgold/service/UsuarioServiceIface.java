package com.proyectoL.softgold.service;

import java.util.List;
import java.util.Optional;

import com.proyectoL.softgold.model.Usuario;

public interface UsuarioServiceIface {
    public List<Usuario> obtenerTodos();

    public void guardarUsuario(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorId(Long id);

    public void eliminar(Long id);

    boolean incrementarIntentosFallidos(String email);

    void actualizarPassword(String email, String newEncodedPassword);
}

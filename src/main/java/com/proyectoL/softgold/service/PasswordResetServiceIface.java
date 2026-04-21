package com.proyectoL.softgold.service;

import java.util.Optional;

import com.proyectoL.softgold.model.PasswordResetToken;
import com.proyectoL.softgold.model.Usuario;

public interface PasswordResetServiceIface {

    PasswordResetToken createPasswordResetToken(Usuario usuario); // Nuevo método

    PasswordResetToken findByToken(String token); // Nuevo método

    void delete(PasswordResetToken token); // Nuevo método

    void createPasswordResetTokenForUser(String email, String token);

    boolean validatePasswordResetToken(String token);

    void changePassword(String token, String newPassword);

    Optional<Usuario> findUserByEmail(String email);

    void deleteTokensByUsuarioId(Long usuarioId);

}

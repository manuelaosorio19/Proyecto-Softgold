package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.PasswordResetToken;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.PasswordResetTokenDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

//Maneja la logica para recuperacion de contraseñas
@Service
public class PasswordResetService implements PasswordResetServiceIface {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordResetTokenDAO passwordResetTokenDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public PasswordResetService(UsuarioDAO usuarioDAO, PasswordResetTokenDAO passwordResetTokenDAO,
            PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordResetTokenDAO = passwordResetTokenDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Usuario> findUserByEmail(String email) {
        return usuarioDAO.findByEmail(email); // Buscar el usuario por su correo electrónico
    }

    // Método para crear un token de restablecimiento de contraseña
    @Override
    public PasswordResetToken createPasswordResetToken(Usuario usuario) {
        String token = generateRandomToken();
        PasswordResetToken passwordResetToken = new PasswordResetToken(null, token, usuario,
                LocalDateTime.now().plusHours(24));
        return passwordResetTokenDAO.save(passwordResetToken);
    }

    // Método para buscar un token por su valor
    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenDAO.findByToken(token).orElse(null);
    }

    // Método para eliminar un token
    @Override
    public void delete(PasswordResetToken token) {
        passwordResetTokenDAO.delete(token);
    }

    @Override
    public void createPasswordResetTokenForUser(String email, String token) {
        Usuario usuario = usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        passwordResetTokenDAO.findByUsuario(usuario)
                .ifPresent(passwordResetTokenDAO::delete);
        PasswordResetToken myToken = new PasswordResetToken(null, token, usuario, LocalDateTime.now().plusHours(24));
        passwordResetTokenDAO.save(myToken);

        String resetLink = "http://localhost:9090/cambiarPassword?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);

        System.out.println("Enlace de recuperación enviado a " + email + ": " + resetLink);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenDAO.findByToken(token);

        if (tokenOpt.isPresent()) {
            PasswordResetToken resetToken = tokenOpt.get();
            LocalDateTime ahora = LocalDateTime.now();
            return resetToken.getExpiracion().isAfter(ahora);
        }
        return false;
    }

    @Override
    public void changePassword(String token, String newPassword) {
        Optional<PasswordResetToken> optionalToken = passwordResetTokenDAO.findByToken(token);
        if (!optionalToken.isPresent())
            return;

        PasswordResetToken passToken = optionalToken.get();
        Usuario usuario = passToken.getUsuario();
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioDAO.save(usuario);

        passwordResetTokenDAO.delete(passToken);
    }

    private String generateRandomToken() {
        return java.util.UUID.randomUUID().toString(); // el UUID es un identificador único universal
    }

    @Transactional
    @Override
    public void deleteTokensByUsuarioId(Long usuarioId) {
        passwordResetTokenDAO.deleteByUsuarioId(usuarioId);
    }
}

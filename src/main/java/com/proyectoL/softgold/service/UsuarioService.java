package com.proyectoL.softgold.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;

//Maneja la logica relacionada a los usuarios
@Service
public class UsuarioService implements UsuarioServiceIface {

    @Autowired
    private UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RolDAO rolDAO;

    @Override
    @Transactional
    public void guardarUsuario(Usuario usuario) {
        // Asignar manualmente el rol predeterminado
        Rol rol = rolDAO.findByNombre("USUARIO");

        if (rol == null || rol.getNombre() == null) {
            throw new RuntimeException("Rol no encontrado: USUARIO");
        }

        usuario.setRoles(List.of(rol));

        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlano())); // usar passwordPlano

        // Guardar el usuario
        usuarioDAO.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioDAO.findById(id);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioDAO.deleteById(id);
    }

    @Transactional
    public void login(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Credenciales inválidas.");
        }

        Usuario usuario = usuarioOpt.get();

        // NO bloquear ni incrementar intentos si es ADMINISTRADOR
        if ("ADMINISTRADOR".equalsIgnoreCase(usuario.getTipoUsuario())) {
            usuario.setIntentosFallidos(0);
            usuario.setBloqueado(false);
            usuario.setTiempoBloqueo(null);
            usuarioDAO.save(usuario);
            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                throw new RuntimeException("Credenciales inválidas.");
            }
            return;
        }

        if (usuario.isBloqueado()) {
            if (usuario.getTiempoBloqueo() != null
                    && usuario.getTiempoBloqueo().plusMinutes(15).isBefore(LocalDateTime.now())) {
                usuario.setBloqueado(false);
                usuario.setIntentosFallidos(0);
                usuario.setTiempoBloqueo(null);
                usuarioDAO.save(usuario);
            } else {
                enviarCorreoCuentaBloqueada(usuario);
                throw new RuntimeException("Cuenta bloqueada. Intenta más tarde.");
            }
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            if (usuario.getIntentosFallidos() == 3) {
                usuario.setBloqueado(true);
                usuario.setTiempoBloqueo(LocalDateTime.now());
                enviarCorreoCuentaBloqueada(usuario); // Enviar notificación de bloqueo
            }
            usuarioDAO.save(usuario);
            throw new RuntimeException("Credenciales inválidas.");
        }

        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        usuario.setTiempoBloqueo(null);
        usuarioDAO.save(usuario);
    }

    private void enviarCorreoCuentaBloqueada(Usuario usuario) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(usuario.getEmail());
        message.setSubject("Tu cuenta ha sido bloqueada");
        message.setText("Tu cuenta ha sido bloqueada debido a múltiples intentos fallidos de inicio de sesión. "
                + "La cuenta se desbloqueará en 15 minutos o puedes intentar más tarde.");
        mailSender.send(message);
    }

    public Usuario validarCredenciales(String email, String password) {
        Optional<Usuario> optUsuario = usuarioDAO.findByEmail(email);

        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean incrementarIntentosFallidos(String email) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // NO bloquear ni incrementar intentos si es ADMINISTRADOR
            if ("ADMINISTRADOR".equalsIgnoreCase(usuario.getTipoUsuario())) {
                usuario.setIntentosFallidos(0);
                usuario.setBloqueado(false);
                usuario.setTiempoBloqueo(null);
                usuarioDAO.save(usuario);
                return false;
            }

            // Si está bloqueado
            if (usuario.isBloqueado()) {
                // y ya pasaron los 15 minutos
                if (usuario.getTiempoBloqueo() != null &&
                        usuario.getTiempoBloqueo().plusMinutes(15).isBefore(LocalDateTime.now())) {

                    // desbloquear automáticamente
                    usuario.setBloqueado(false);
                    usuario.setIntentosFallidos(0);
                    usuario.setTiempoBloqueo(null);
                    usuarioDAO.save(usuario);

                    System.out.println("Cuenta desbloqueada automáticamente para: " + email);

                    return false; // Ya está desbloqueado, puede seguir intentando
                }

                System.out.println("Cuenta aún bloqueada para: " + email);
                // Si aún no han pasado los 15 minutos, sigue bloqueado
                return true;
            }

            // No estaba bloqueado: aumentar intento
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
            System.out.println("Intento fallido #" + usuario.getIntentosFallidos() + " para: " + email);

            // Si llegó a 3 intentos = bloquear
            if (usuario.getIntentosFallidos() >= 3) {
                usuario.setBloqueado(true);
                usuario.setTiempoBloqueo(LocalDateTime.now());
                enviarCorreoCuentaBloqueada(usuario);
                System.out.println("Cuenta bloqueada para: " + email);

            }

            usuarioDAO.save(usuario);
            return usuario.isBloqueado();
        } else {
            throw new RuntimeException("Usuario no encontrado con el email: " + email);
        }
    }

    @Override
    @Transactional
    public void actualizarPassword(String email, String newEncodedPassword) {
        usuarioDAO.findByEmail(email).ifPresent(usuario -> {
            usuario.setPassword(newEncodedPassword);
            usuarioDAO.save(usuario);
        });
    }

    public boolean isAccountNonLocked(Usuario usuario) {
        if (usuario.isBloqueado()) {
            if (usuario.getTiempoBloqueo() != null &&
                    usuario.getTiempoBloqueo().plusMinutes(15).isBefore(LocalDateTime.now())) {

                usuario.setBloqueado(false);
                usuario.setIntentosFallidos(0);
                usuario.setTiempoBloqueo(null);
                usuarioDAO.save(usuario);

                System.out.println("Cuenta desbloqueada automáticamente para: " + usuario.getEmail());
                return true;
            }
            return false;
        }
        return true;
    }

}
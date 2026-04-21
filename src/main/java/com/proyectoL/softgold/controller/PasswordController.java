package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.PasswordResetToken;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.service.PasswordResetServiceIface;
import com.proyectoL.softgold.service.UsuarioServiceIface;
import com.proyectoL.softgold.service.EmailServiceIface;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

//Controlador encargado de manejar la recuperacion de contraseñas desde una interfaz web
@Controller
public class PasswordController {

    private final UsuarioServiceIface usuarioService;
    private final PasswordResetServiceIface passwordResetService;
    private final EmailServiceIface emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Constructor con inyección de dependencias
    public PasswordController(UsuarioServiceIface usuarioService,
            PasswordResetServiceIface passwordResetService,
            EmailServiceIface emailService) {
        this.usuarioService = usuarioService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
    }

    // Mostrar formulario para solicitar recuperación de contraseña (correo)
    @GetMapping("/recuperar-contrasena")
    public String showRecoverPasswordForm(Model model) {
        model.addAttribute("titulo", "Recuperar Contraseña");
        return "vistas/recuperarPassword"; // Vista para solicitar correo
    }

    // Procesar la solicitud de recuperación de contraseña
    @PostMapping("/enviar-token")
    public String sendPasswordResetToken(@RequestParam("email") String email, RedirectAttributes flash) {
        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

        if (usuario == null) {
            flash.addFlashAttribute("error", "No se encontró un usuario con ese correo");
            return "redirect:/recuperar-contrasena";
        }

        try {
            PasswordResetToken token = passwordResetService.createPasswordResetToken(usuario);
            String resetLink = "http://localhost:9090/cambiar-contrasena/" + token.getToken();
            emailService.sendPasswordResetEmail(usuario.getEmail(), resetLink);
            flash.addFlashAttribute("exito", "Te hemos enviado un enlace para cambiar la contraseña");
            return "redirect:/login";
        } catch (Exception e) {
            e.printStackTrace(); // <-- Agrega esto
            flash.addFlashAttribute("error", "Ocurrió un error al procesar la solicitud.");
            return "redirect:/recuperar-contrasena";
        }
    }

    // Mostrar formulario para cambiar la contraseña
    @GetMapping("/cambiar-contrasena/{token}")
    public String showChangePasswordForm(@PathVariable("token") String token, Model model, RedirectAttributes flash) {
        PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if (passwordResetToken == null) {
            flash.addFlashAttribute("error", "El token de recuperación es inválido o ha expirado");
            return "redirect:/login";
        }

        model.addAttribute("titulo", "Cambiar Contraseña");
        model.addAttribute("token", token);
        return "vistas/cambiarPassword"; // Vista para cambiar la contraseña
    }

    // Procesar el cambio de contraseña (recuperación por token)
    @PostMapping("/cambiar-contrasena")
    public String processChangePassword(@RequestParam("password") String password,
            @RequestParam("token") String token, RedirectAttributes flash) {
        PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if (passwordResetToken == null) {
            flash.addFlashAttribute("error", "El token de recuperación es inválido o ha expirado");
            return "redirect:/login";
        }

        Usuario usuario = passwordResetToken.getUsuario();
        usuarioService.actualizarPassword(usuario.getEmail(), passwordEncoder.encode(password));
        passwordResetService.delete(passwordResetToken);

        flash.addFlashAttribute("exito", "Tu contraseña ha sido cambiada exitosamente");
        return "redirect:/login";
    }

    // Mostrar formulario de cambio de contraseña (usuario autenticado)
    @GetMapping("/cuenta/cambiarPassword")
    public String showCuentaCambiarPassword(Model model) {
        model.addAttribute("titulo", "Cambiar Contraseña");
        return "vistas/cuentaCambiarPassword";
    }

    // Procesar cambio de contraseña (usuario autenticado)
    @PostMapping("/cuenta/cambiarPassword")
    public String processCuentaCambiarPassword(
            @RequestParam("passwordActual") String passwordActual,
            @RequestParam("passwordNueva") String passwordNueva,
            Authentication auth,
            RedirectAttributes flash) {

        String email = auth.getName();
        Usuario usuario = usuarioService.buscarPorEmail(email).orElse(null);

        if (usuario == null) {
            flash.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/cuenta/cambiarPassword";
        }

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            flash.addFlashAttribute("error", "La contraseña actual es incorrecta");
            return "redirect:/cuenta/cambiarPassword";
        }

        if (passwordNueva.length() < 8) {
            flash.addFlashAttribute("error", "La nueva contraseña debe tener al menos 8 caracteres");
            return "redirect:/cuenta/cambiarPassword";
        }

        usuarioService.actualizarPassword(email, passwordEncoder.encode(passwordNueva));
        flash.addFlashAttribute("exito", "Contraseña actualizada correctamente");
        return "redirect:/cuenta/cambiarPassword";
    }

    // Mostrar perfil del usuario autenticado
    @GetMapping("/cuenta/perfil")
    public String showPerfil(Authentication auth, Model model) {
        model.addAttribute("titulo", "Mi Perfil");
        String email = auth.getName();
        usuarioService.buscarPorEmail(email).ifPresent(u -> model.addAttribute("usuario", u));
        return "vistas/cuentaPerfil";
    }
}

package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Rol;
import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.MinaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios/mineros")
public class MineroController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private RolDAO rolDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinaDAO minaDAO;

    @Autowired
    private com.proyectoL.softgold.service.PasswordResetService passwordResetService;

    // Listar mineros
    @GetMapping("")
    public String listarMineros(Model model) {
        List<Usuario> mineros = usuarioDAO.findByTipoUsuario("MINERO");
        model.addAttribute("listaUsuarios", mineros);
        model.addAttribute("tipoUsuario", "MINERO");
        model.addAttribute("activePage", "mineros");

        return "vistas/listarMineros";
    }

    // Mostrar formulario para crear un minero
    @GetMapping("/crear")
    public String mostrarFormularioCrearMinero(Model model) {
        Usuario usuario = new Usuario();
        usuario.setTipoUsuario("MINERO");
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Crear Minero");
        model.addAttribute("minas", minaDAO.findAll());
        model.addAttribute("activePage", "mineros");

        return "vistas/crearMinero";
    }

    // Procesar la creación de un minero
    @PostMapping("/crear")
    public String crearMinero(
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Crear Minero");
            return "vistas/crearMinero";
        }

        Rol rolMinero = rolDAO.findByNombre("MINERO");
        if (rolMinero == null) {
            redirectAttrs.addFlashAttribute("error", "El rol 'MINERO' no existe en la base de datos.");
            return "redirect:/admin/usuarios/mineros";
        }

        if (usuarioDAO.existsByCedula(usuario.getCedula())) {
            model.addAttribute("errorCedula", "Ya existe un usuario con la cédula ingresada.");
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Crear Minero");
            return "vistas/crearMinero";
        }

        if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            model.addAttribute("errorEmail", "Ya existe un usuario con el correo ingresado.");
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Crear Minero");
            return "vistas/crearMinero";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPasswordPlano()));
        usuario.setRoles(Collections.singleton(rolMinero));
        usuario.setBloqueado(false);
        usuario.setIntentosFallidos(0);

        usuarioDAO.save(usuario);

        redirectAttrs.addFlashAttribute("mensaje", "Minero creado exitosamente.");
        return "redirect:/admin/usuarios/mineros";
    }

    // Mostrar formulario para editar un minero
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMinero(@PathVariable Long id, Model model, RedirectAttributes redirectAttrs) {
        Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getTipoUsuario().equals("MINERO")) {
            redirectAttrs.addFlashAttribute("error", "Minero no encontrado.");
            return "redirect:/admin/usuarios/mineros";
        }
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("titulo", "Editar Minero");
        model.addAttribute("minas", minaDAO.findAll()); // SIEMPRE enviar minas
        model.addAttribute("activePage", "mineros");
        return "vistas/editarMinero";
    }

    // Procesar la edición de un minero
    @PostMapping("/editar/{id}")
    public String procesarEditarMinero(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result,
            RedirectAttributes redirectAttrs,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("minas", minaDAO.findAll());
            model.addAttribute("titulo", "Editar Minero");
            model.addAttribute("activePage", "mineros");
            return "vistas/editarMinero";
        }

        Optional<Usuario> usuarioOpt = usuarioDAO.findById(id);
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getTipoUsuario().equals("MINERO")) {
            redirectAttrs.addFlashAttribute("error", "Minero no encontrado.");
            return "redirect:/admin/usuarios/mineros";
        }

        Usuario mineroExistente = usuarioOpt.get();
        mineroExistente.setNombre1(usuario.getNombre1());
        mineroExistente.setNombre2(usuario.getNombre2());
        mineroExistente.setApellido1(usuario.getApellido1());
        mineroExistente.setApellido2(usuario.getApellido2());
        mineroExistente.setEmail(usuario.getEmail());
        mineroExistente.setTelefono(usuario.getTelefono());
        mineroExistente.setMina(usuario.getMina());
        if (usuario.getPasswordPlano() != null && !usuario.getPasswordPlano().isEmpty()) {
            mineroExistente.setPassword(passwordEncoder.encode(usuario.getPasswordPlano()));
        }

        usuarioDAO.save(mineroExistente);
        redirectAttrs.addFlashAttribute("exito", "Minero actualizado correctamente.");
        return "redirect:/admin/usuarios/mineros";
    }

    // Eliminar un minero
    @GetMapping("/eliminar/{id}")
    public String eliminarMinero(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        passwordResetService.deleteTokensByUsuarioId(id);
        usuarioDAO.deleteById(id);
        redirectAttrs.addFlashAttribute("exito", "Minero eliminado correctamente.");
        return "redirect:/admin/usuarios/mineros";
    }

    // Buscar mineros por cédula
    @GetMapping("/buscar")
    public String buscarMinerosPorCedula(@RequestParam("cedula") String cedula, Model model) {
        List<Usuario> mineros;
        if (cedula == null || cedula.trim().isEmpty()) {
            mineros = usuarioDAO.findByTipoUsuario("MINERO");
        } else {
            Usuario minero = usuarioDAO.findByCedulaAndTipoUsuario(cedula, "MINERO");
            mineros = minero != null ? List.of(minero) : List.of();
        }
        model.addAttribute("listaUsuarios", mineros);
        model.addAttribute("tipoUsuario", "MINERO");
        model.addAttribute("activePage", "mineros");

        return "vistas/listarMineros";
    }

}

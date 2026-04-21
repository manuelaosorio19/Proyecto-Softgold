package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.ForumComment;
import com.proyectoL.softgold.model.ForumPost;
import com.proyectoL.softgold.service.ForumServiceIface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/foro")
public class ForumController {

    @Autowired
    private ForumServiceIface forumService;

    @GetMapping({"", "/"})
    public String listar(@RequestParam(required = false) String categoria,
                         @RequestParam(required = false) String buscar,
                         Model model) {
        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("posts", forumService.buscarPorTitulo(buscar));
            model.addAttribute("buscar", buscar);
        } else if (categoria != null && !categoria.isBlank()) {
            model.addAttribute("posts", forumService.filtrarPorCategoria(categoria));
            model.addAttribute("categoriaActiva", categoria);
        } else {
            model.addAttribute("posts", forumService.listarPostsActivos());
        }
        model.addAttribute("categorias", new String[]{"GENERAL", "SEGURIDAD", "TECNICO", "ANUNCIOS", "COMUNIDAD"});
        return "vistas/foro/listarPosts";
    }

    @GetMapping("/crear")
    public String mostrarFormCrear(Model model) {
        model.addAttribute("post", new ForumPost());
        model.addAttribute("categorias", new String[]{"GENERAL", "SEGURIDAD", "TECNICO", "ANUNCIOS", "COMUNIDAD"});
        return "vistas/foro/crearPost";
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("post") ForumPost post,
                        BindingResult result, Model model,
                        Authentication auth, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", new String[]{"GENERAL", "SEGURIDAD", "TECNICO", "ANUNCIOS", "COMUNIDAD"});
            return "vistas/foro/crearPost";
        }
        if (auth != null && auth.isAuthenticated()) {
            post.setAutorNombre(auth.getName());
        }
        forumService.guardarPost(post);
        ra.addFlashAttribute("mensajeExito", "Post publicado exitosamente.");
        return "redirect:/foro";
    }

    @GetMapping("/{id}")
    public String verPost(@PathVariable Long id, Model model) {
        return forumService.buscarPostPorId(id).map(post -> {
            model.addAttribute("post", post);
            model.addAttribute("nuevoComentario", new ForumComment());
            return "vistas/foro/verPost";
        }).orElse("redirect:/foro");
    }

    @PostMapping("/{id}/comentar")
    public String comentar(@PathVariable Long id,
                           @Valid @ModelAttribute("nuevoComentario") ForumComment comentario,
                           BindingResult result, Authentication auth, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorComentario", "El comentario no puede estar vacío.");
            return "redirect:/foro/" + id;
        }
        forumService.buscarPostPorId(id).ifPresent(post -> {
            comentario.setPost(post);
            if (auth != null && auth.isAuthenticated()) {
                comentario.setAutorNombre(auth.getName());
            }
            forumService.guardarComentario(comentario);
        });
        ra.addFlashAttribute("mensajeExito", "Comentario agregado.");
        return "redirect:/foro/" + id;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        forumService.eliminarPost(id);
        ra.addFlashAttribute("mensajeExito", "Post eliminado.");
        return "redirect:/foro";
    }
}

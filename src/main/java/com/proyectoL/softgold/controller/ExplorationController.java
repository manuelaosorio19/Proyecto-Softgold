package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.ZonaExploracion;
import com.proyectoL.softgold.repository.MinaDAO;
import com.proyectoL.softgold.service.ExplorationServiceIface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/exploracion")
public class ExplorationController {

    @Autowired
    private ExplorationServiceIface explorationService;

    @Autowired
    private MinaDAO minaDAO;

    @GetMapping({"", "/"})
    public String listar(Model model) {
        model.addAttribute("zonas", explorationService.listarTodas());
        return "vistas/exploracion/listarZonas";
    }

    @GetMapping("/mapa")
    public String mapaExploracion(Model model) {
        model.addAttribute("zonas", explorationService.listarTodas());
        return "vistas/exploracion/mapaExploracion";
    }

    @GetMapping("/crear")
    public String mostrarFormCrear(Model model) {
        model.addAttribute("zona", new ZonaExploracion());
        model.addAttribute("minas", minaDAO.findAll());
        return "vistas/exploracion/crearZona";
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("zona") ZonaExploracion zona,
                        BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("minas", minaDAO.findAll());
            return "vistas/exploracion/crearZona";
        }
        explorationService.guardar(zona);
        ra.addFlashAttribute("mensajeExito", "Zona de exploración creada exitosamente.");
        return "redirect:/admin/exploracion";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormEditar(@PathVariable Long id, Model model) {
        return explorationService.buscarPorId(id).map(zona -> {
            model.addAttribute("zona", zona);
            model.addAttribute("minas", minaDAO.findAll());
            return "vistas/exploracion/editarZona";
        }).orElse("redirect:/admin/exploracion");
    }

    @PostMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @Valid @ModelAttribute("zona") ZonaExploracion zona,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("minas", minaDAO.findAll());
            return "vistas/exploracion/editarZona";
        }
        zona.setId(id);
        explorationService.guardar(zona);
        ra.addFlashAttribute("mensajeExito", "Zona actualizada exitosamente.");
        return "redirect:/admin/exploracion";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        explorationService.eliminar(id);
        ra.addFlashAttribute("mensajeExito", "Zona eliminada.");
        return "redirect:/admin/exploracion";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String estado,
                         @RequestParam(required = false) String tipo,
                         Model model) {
        if (nombre != null && !nombre.isBlank()) {
            model.addAttribute("zonas", explorationService.buscarPorNombre(nombre));
        } else if (estado != null && !estado.isBlank()) {
            model.addAttribute("zonas", explorationService.filtrarPorEstado(estado));
        } else if (tipo != null && !tipo.isBlank()) {
            model.addAttribute("zonas", explorationService.filtrarPorTipo(tipo));
        } else {
            model.addAttribute("zonas", explorationService.listarTodas());
        }
        return "vistas/exploracion/listarZonas";
    }
}

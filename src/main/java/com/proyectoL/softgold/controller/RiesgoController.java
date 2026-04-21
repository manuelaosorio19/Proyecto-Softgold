package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.Riesgo;
import com.proyectoL.softgold.repository.RiesgoDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/riesgos")
public class RiesgoController {

    @Autowired
    private RiesgoDAO riesgoDAO;

    @GetMapping("")
    public String listarRiesgos(Model model) {
        model.addAttribute("riesgos", riesgoDAO.findAll());
        return "vistas/listarRiesgos";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearRiesgo(Model model) {
        model.addAttribute("riesgo", new Riesgo());
        return "vistas/crearRiesgo";
    }

    @PostMapping("/crear")
    public String crearRiesgo(@Valid @ModelAttribute("riesgo") Riesgo riesgo, BindingResult result) {
        if (result.hasErrors()) {
            return "vistas/crearRiesgo";
        }
        riesgoDAO.save(riesgo);
        return "redirect:/admin/riesgos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarRiesgo(@PathVariable Long id, Model model) {
        Riesgo riesgo = riesgoDAO.findById(id).orElse(null);
        if (riesgo == null) {
            return "redirect:/admin/riesgos";
        }
        model.addAttribute("riesgo", riesgo);
        return "vistas/editarRiesgo";
    }

    @PostMapping("/editar/{id}")
    public String editarRiesgo(@PathVariable Long id, @Valid @ModelAttribute("riesgo") Riesgo riesgo,
            BindingResult result) {
        if (result.hasErrors()) {
            return "vistas/editarRiesgo";
        }
        Riesgo riesgoOriginal = riesgoDAO.findById(id).orElse(null);
        if (riesgoOriginal == null) {
            return "redirect:/admin/riesgos";
        }
        riesgoOriginal.setDescripcion(riesgo.getDescripcion());
        riesgoDAO.save(riesgoOriginal);
        return "redirect:/admin/riesgos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRiesgo(@PathVariable Long id) {
        riesgoDAO.deleteById(id);
        return "redirect:/admin/riesgos";
    }
}
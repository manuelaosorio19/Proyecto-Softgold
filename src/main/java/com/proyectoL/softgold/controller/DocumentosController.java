package com.proyectoL.softgold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentosController {

    @GetMapping("/admin/seguridad")
    public String guiaSeguridad(Model model) {
        model.addAttribute("titulo", "Guía de Seguridad");
        return "vistas/documentos/guiaSeguridad";
    }

    @GetMapping("/admin/cambios")
    public String controlCambios(Model model) {
        model.addAttribute("titulo", "Control de Cambios");
        return "vistas/documentos/controlCambios";
    }

    @GetMapping("/admin/mejora")
    public String mejoraContinua(Model model) {
        model.addAttribute("titulo", "Mejora Continua");
        return "vistas/documentos/mejoraContinua";
    }

    @GetMapping("/soporte/satisfaccion")
    public String satisfaccion(Model model) {
        model.addAttribute("titulo", "Formulario de Satisfacción");
        return "vistas/documentos/satisfaccion";
    }
}

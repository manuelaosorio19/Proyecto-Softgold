package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.model.SupportTicket;
import com.proyectoL.softgold.service.SupportServiceIface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/soporte")
public class SupportController {

    @Autowired
    private SupportServiceIface supportService;

    // Mostrar formulario de reporte
    @GetMapping("/reporte")
    public String mostrarFormulario(SupportTicket ticket, Model model) {
        model.addAttribute("titulo", "Reporte de problema");
        return "vistas/reporteSoporte";
    }

    // Procesar envío de reporte
    @PostMapping("/reporte")
    public String enviarReporte(@Valid SupportTicket ticket, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            return "vistas/reporteSoporte";
        }
        supportService.crearTicket(ticket);
        flash.addFlashAttribute("exito", "Tu reporte se ha enviado. Gracias.");
        return "redirect:/";
    }

    // Listado para admin
    @GetMapping("/tickets")
    public String listarTickets(Model model) {
        model.addAttribute("tickets", supportService.listarTickets());
        return "vistas/listarSoporte";
    }

    // Cerrar ticket (admin)
    @PostMapping("/tickets/{id}/cerrar")
    public String cerrarTicket(@PathVariable Long id, RedirectAttributes flash) {
        supportService.cerrarTicket(id);
        flash.addFlashAttribute("exito", "Ticket cerrado.");
        return "redirect:/soporte/tickets";
    }
}
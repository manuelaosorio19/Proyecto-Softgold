package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/filtros")
public class FiltroController {

    @Autowired private MinaDAO minaDAO;
    @Autowired private MapaDAO mapaDAO;
    @Autowired private RiesgoDAO riesgoDAO;
    @Autowired private UsuarioDAO usuarioDAO;
    @Autowired private ZonaExploracionDAO zonaDAO;

    @GetMapping({"", "/"})
    public String busquedaAvanzada(
            @RequestParam(required = false) String entidad,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String tipoUsuario,
            @RequestParam(required = false) String estadoZona,
            Model model) {

        model.addAttribute("entidad", entidad);
        model.addAttribute("q", q);

        if ("minas".equals(entidad)) {
            if (departamento != null && !departamento.isBlank()) {
                model.addAttribute("resultados", minaDAO.findByDepartamentoContainingIgnoreCase(departamento));
            } else if (q != null && !q.isBlank()) {
                com.proyectoL.softgold.model.Mina m = minaDAO.findByNombre(q);
                model.addAttribute("resultados", m != null ? java.util.List.of(m) : java.util.List.of());
            } else {
                model.addAttribute("resultados", minaDAO.findAll());
            }
        } else if ("mapas".equals(entidad)) {
            if (q != null && !q.isBlank()) {
                model.addAttribute("resultados", mapaDAO.findByDescripcionContainingIgnoreCase(q));
            } else {
                model.addAttribute("resultados", mapaDAO.findAll());
            }
        } else if ("usuarios".equals(entidad)) {
            if (tipoUsuario != null && !tipoUsuario.isBlank()) {
                model.addAttribute("resultados", usuarioDAO.findByTipoUsuario(tipoUsuario));
            } else {
                model.addAttribute("resultados", usuarioDAO.findAll());
            }
        } else if ("riesgos".equals(entidad)) {
            model.addAttribute("resultados", riesgoDAO.findAll());
        } else if ("zonas".equals(entidad)) {
            if (estadoZona != null && !estadoZona.isBlank()) {
                model.addAttribute("resultados", zonaDAO.findByEstado(estadoZona));
            } else {
                model.addAttribute("resultados", zonaDAO.findAll());
            }
        }

        return "vistas/filtros/buscar";
    }
}

package com.proyectoL.softgold.controller;

import com.proyectoL.softgold.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/informes")
public class InformeController {

    @Autowired private MinaDAO minaDAO;
    @Autowired private MapaDAO mapaDAO;
    @Autowired private RiesgoDAO riesgoDAO;
    @Autowired private UsuarioDAO usuarioDAO;
    @Autowired private ZonaExploracionDAO zonaDAO;
    @Autowired private ForumPostDAO forumDAO;

    @GetMapping({"", "/"})
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("totalMinas", minaDAO.count());
        model.addAttribute("totalMapas", mapaDAO.count());
        model.addAttribute("totalRiesgos", riesgoDAO.count());
        model.addAttribute("totalZonas", zonaDAO.count());
        model.addAttribute("totalPosts", forumDAO.count());

        long totalMineros = usuarioDAO.findAll().stream()
            .filter(u -> "MINERO".equals(u.getTipoUsuario())).count();
        long totalEmpleados = usuarioDAO.findAll().stream()
            .filter(u -> "EMPLEADO".equals(u.getTipoUsuario())).count();

        model.addAttribute("totalMineros", totalMineros);
        model.addAttribute("totalEmpleados", totalEmpleados);

        Map<String, Long> zonasPorEstado = new LinkedHashMap<>();
        zonasPorEstado.put("EN_EXPLORACION", zonaDAO.findByEstado("EN_EXPLORACION").stream().count());
        zonasPorEstado.put("PENDIENTE", zonaDAO.findByEstado("PENDIENTE").stream().count());
        zonasPorEstado.put("CERRADA", zonaDAO.findByEstado("CERRADA").stream().count());
        model.addAttribute("zonasPorEstado", zonasPorEstado);

        return "vistas/informes/dashboard";
    }

    @GetMapping("/minas")
    public String reporteMinas(Model model) {
        model.addAttribute("minas", minaDAO.findAll());
        model.addAttribute("totalMinas", minaDAO.count());
        return "vistas/informes/reporteMinas";
    }

    @GetMapping("/ambiental")
    public String impactoAmbiental(Model model) {
        model.addAttribute("minas", minaDAO.findAll());
        model.addAttribute("riesgos", riesgoDAO.findAll());
        model.addAttribute("totalRiesgos", riesgoDAO.count());
        return "vistas/informes/impactoAmbiental";
    }

    @GetMapping("/inventario")
    public String inventario(Model model) {
        model.addAttribute("minas", minaDAO.findAll());
        model.addAttribute("mapas", mapaDAO.findAll());
        model.addAttribute("zonas", zonaDAO.findAll());
        model.addAttribute("totalMinas", minaDAO.count());
        model.addAttribute("totalMapas", mapaDAO.count());
        model.addAttribute("totalZonas", zonaDAO.count());
        return "vistas/informes/inventario";
    }
}

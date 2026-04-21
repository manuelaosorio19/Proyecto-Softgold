package com.proyectoL.softgold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyectoL.softgold.model.Mina;
import com.proyectoL.softgold.repository.MinaDAO;
import com.proyectoL.softgold.repository.RiesgoDAO;
import com.proyectoL.softgold.repository.MapaDAO;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/minas")
public class MinaController {
    @Autowired
    private MinaDAO minaDAO;

    @Autowired
    private MapaDAO mapaDAO;

    @Autowired
    private RiesgoDAO riesgoDAO;

    @GetMapping("")
    public String listarMinas(Model model) {
        model.addAttribute("minas", minaDAO.findAll());
        return "vistas/listarMinas";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrearMina(Model model) {
        model.addAttribute("mina", new Mina());
        model.addAttribute("mapas", mapaDAO.findAll());
        model.addAttribute("riesgos", riesgoDAO.findAll());

        return "vistas/crearMina";
    }

    @PostMapping("/crear")
    public String crearMina(@Valid @ModelAttribute("mina") Mina mina, BindingResult result,
            RedirectAttributes redirectAttrs, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaDAO.findAll());
            model.addAttribute("riesgos", riesgoDAO.findAll());

            return "vistas/crearMina";
        }

        minaDAO.save(mina);
        redirectAttrs.addFlashAttribute("mensajeExito", "Mina creado exitosamente.");

        return "redirect:/admin/minas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarMina(@PathVariable Long id, Model model) {
        Mina mina = minaDAO.findById(id).orElse(null);
        if (mina == null) {
            return "redirect:/admin/minas";
        }
        model.addAttribute("mina", mina);
        model.addAttribute("mapas", mapaDAO.findAll());
        model.addAttribute("riesgos", riesgoDAO.findAll());

        return "vistas/editarMina";
    }

    @PostMapping("/editar/{id}")
    public String editarMina(@PathVariable Long id, @Valid @ModelAttribute("mina") Mina mina,
            RedirectAttributes redirectAttrs, BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mapas", mapaDAO.findAll());
            model.addAttribute("riesgos", riesgoDAO.findAll());

            return "vistas/editarMina";
        }
        Mina minaOriginal = minaDAO.findById(id).orElse(null);
        if (minaOriginal == null) {
            return "redirect:/admin/minas";
        }
        minaOriginal.setNombre(mina.getNombre());
        minaOriginal.setDepartamento(mina.getDepartamento());
        minaOriginal.setMapas(mina.getMapas());
        minaOriginal.setRiesgos(mina.getRiesgos());

        minaDAO.save(minaOriginal);
        redirectAttrs.addFlashAttribute("mensajeExito", "Mina actualizada exitosamente.");

        return "redirect:/admin/minas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMina(@PathVariable Long id) {
        Mina mina = minaDAO.findById(id).orElse(null);
        if (mina != null) {
            if (mina.getUsuarios() != null) {
                mina.getUsuarios().forEach(usuario -> usuario.setMina(null));
            }
            minaDAO.save(mina);
            minaDAO.deleteById(id);
        }
        return "redirect:/admin/minas";
    }

    @GetMapping("/buscar")
    public String buscarMinasPorDepartamento(@RequestParam("departamento") String departamento, Model model) {
        List<Mina> minas;
        if (departamento == null || departamento.trim().isEmpty()) {
            minas = minaDAO.findAll();
        } else {
            minas = minaDAO.findByDepartamentoContainingIgnoreCase(departamento);
        }
        model.addAttribute("minas", minas);
        return "vistas/listarMinas";
    }
}
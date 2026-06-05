package com.proyectoL.softgold.casos;

import com.proyectoL.softgold.controller.RiesgoController;
import com.proyectoL.softgold.model.Riesgo;
import com.proyectoL.softgold.repository.RiesgoDAO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CP-08 — Módulo de Gestión de Riesgos
 */
@WebMvcTest(RiesgoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Gestión de Riesgos — CP-08")
class CP08RiesgoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RiesgoDAO riesgoDAO;

    @Test
    @DisplayName("CP-08: Crear riesgo con descripción válida guarda y redirige")
    void cp08_crearRiesgoValido() throws Exception {
        mockMvc.perform(post("/admin/riesgos/crear")
                        .param("descripcion", "Derrumbe de talud en zona norte"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/riesgos"));

        verify(riesgoDAO, times(1)).save(any(Riesgo.class));
    }
}

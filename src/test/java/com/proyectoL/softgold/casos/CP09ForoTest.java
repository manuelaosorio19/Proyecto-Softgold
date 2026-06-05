package com.proyectoL.softgold.casos;

import com.proyectoL.softgold.controller.ForumController;
import com.proyectoL.softgold.service.ForumServiceIface;

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
 * CP-09 — Módulo de Foro
 */
@WebMvcTest(ForumController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Foro Comunitario — CP-09")
class CP09ForoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumServiceIface forumService;

    @Test
    @DisplayName("CP-09: Crear post en foro con datos válidos publica y redirige")
    void cp09_crearPostForoValido() throws Exception {
        mockMvc.perform(post("/foro/crear")
                        .param("titulo", "Consulta sobre seguridad en minas subterráneas")
                        .param("contenido", "Solicito información sobre los protocolos de ventilación en galerías de más de 200 metros.")
                        .param("categoria", "SEGURIDAD")
                        .param("autorNombre", "Carlos Pérez"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/foro"));

        verify(forumService, times(1)).guardarPost(any());
    }
}

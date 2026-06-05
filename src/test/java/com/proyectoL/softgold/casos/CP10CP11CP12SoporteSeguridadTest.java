package com.proyectoL.softgold.casos;

import com.proyectoL.softgold.controller.SupportController;
import com.proyectoL.softgold.model.SupportTicket;
import com.proyectoL.softgold.security.CustomLoginPassword;
import com.proyectoL.softgold.security.CustomUserDetailsService;
import com.proyectoL.softgold.security.SecurityConfig;
import com.proyectoL.softgold.service.SupportServiceIface;
import com.proyectoL.softgold.service.UsuarioServiceIface;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CP-10, CP-11, CP-12 — Soporte y Seguridad.
 * Se importa SecurityConfig para que las reglas de acceso reales sean aplicadas.
 */
@WebMvcTest(SupportController.class)
@Import(SecurityConfig.class)
@DisplayName("Soporte y Seguridad — CP-10, CP-11, CP-12")
class CP10CP11CP12SoporteSeguridadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SupportServiceIface supportService;
    @MockBean private UsuarioServiceIface usuarioService;
    @MockBean private CustomUserDetailsService userDetailsService;
    @MockBean private CustomLoginPassword customLoginPassword;

    @Test
    @DisplayName("CP-10: Enviar ticket de soporte (ruta pública) crea ticket y redirige")
    void cp10_enviarTicketSoporteValido() throws Exception {
        mockMvc.perform(post("/soporte/reporte")
                        .param("nombre", "Juan Pérez")
                        .param("email", "juan@test.com")
                        .param("asunto", "Error en sistema de mapas")
                        .param("descripcion", "El mapa no carga en el módulo de exploración."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(supportService, times(1)).crearTicket(any(SupportTicket.class));
    }

    @Test
    @DisplayName("CP-11: Acceso a /soporte/tickets sin autenticación redirige al login")
    void cp11_accesoTicketsSinAuthRedirige() throws Exception {
        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("CP-12: Administrador puede listar tickets de soporte")
    void cp12_adminListaTicketsSoporte() throws Exception {
        when(supportService.listarTickets()).thenReturn(List.of());

        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/listarSoporte"));
    }
}

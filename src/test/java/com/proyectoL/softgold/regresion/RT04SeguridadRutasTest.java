package com.proyectoL.softgold.regresion;

import com.proyectoL.softgold.controller.SupportController;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * RT-04 — Regresión Seguridad: Verificar que las reglas de acceso por rol
 * siguen aplicándose tras cambios en la aplicación.
 * Importa SecurityConfig para usar las reglas reales de acceso.
 */
@WebMvcTest(SupportController.class)
@Import(SecurityConfig.class)
@DisplayName("RT-04: Regresión — Seguridad y control de acceso por roles")
class RT04SeguridadRutasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SupportServiceIface supportService;
    @MockBean private UsuarioServiceIface usuarioService;
    @MockBean private CustomUserDetailsService userDetailsService;
    @MockBean private CustomLoginPassword customLoginPassword;

    @Test
    @DisplayName("RT-04a: /soporte/tickets sin autenticación redirige a /login")
    void rt04a_ticketsSinAuthRedirige() throws Exception {
        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "MINERO")
    @DisplayName("RT-04b: /soporte/tickets con rol MINERO devuelve 403 Forbidden")
    void rt04b_ticketsConRolMineroEsForbidden() throws Exception {
        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLEADO")
    @DisplayName("RT-04c: /soporte/tickets con rol EMPLEADO devuelve 403 Forbidden")
    void rt04c_ticketsConRolEmpleadoEsForbidden() throws Exception {
        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("RT-04d: /soporte/tickets con rol ADMINISTRADOR devuelve 200 OK")
    void rt04d_ticketsConRolAdminEs200() throws Exception {
        when(supportService.listarTickets()).thenReturn(List.of());

        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/listarSoporte"));
    }

    @Test
    @WithMockUser(roles = "USUARIO")
    @DisplayName("RT-04e: /soporte/tickets con rol USUARIO devuelve 403 Forbidden")
    void rt04e_ticketsConRolUsuarioEsForbidden() throws Exception {
        mockMvc.perform(get("/soporte/tickets"))
                .andExpect(status().isForbidden());
    }
}

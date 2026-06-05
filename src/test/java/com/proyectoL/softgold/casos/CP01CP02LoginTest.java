package com.proyectoL.softgold.casos;

import com.proyectoL.softgold.controller.LoginController;
import com.proyectoL.softgold.repository.MinaDAO;
import com.proyectoL.softgold.repository.RolDAO;
import com.proyectoL.softgold.repository.UsuarioDAO;
import com.proyectoL.softgold.service.PasswordResetService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * CP-01 / CP-02 — Módulo de Autenticación
 */
@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Autenticación — CP-01 y CP-02")
class CP01CP02LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UsuarioDAO usuarioDAO;
    @MockBean private RolDAO rolDAO;
    @MockBean private MinaDAO minaDAO;
    @MockBean private PasswordResetService passwordResetService;
    @MockBean private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("CP-01: Página de login accesible sin autenticación")
    void cp01_loginPaginaAccesible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/login"));
    }

    @Test
    @DisplayName("CP-02: Login con error muestra mensaje de credenciales incorrectas")
    void cp02_loginConErrorMuestraMensaje() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("vistas/login"));
    }
}

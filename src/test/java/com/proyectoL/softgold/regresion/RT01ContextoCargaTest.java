package com.proyectoL.softgold.regresion;

import com.proyectoL.softgold.controller.MinaController;
import com.proyectoL.softgold.controller.RiesgoController;
import com.proyectoL.softgold.controller.ForumController;
import com.proyectoL.softgold.controller.SupportController;
import com.proyectoL.softgold.controller.LoginController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RT-01 — Regresión: El contexto de Spring carga correctamente y todos los
 * controladores principales están disponibles en el contexto.
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DisplayName("RT-01: Contexto de la aplicación carga correctamente")
class RT01ContextoCargaTest {

    @Autowired(required = false)
    private MinaController minaController;

    @Autowired(required = false)
    private RiesgoController riesgoController;

    @Autowired(required = false)
    private ForumController forumController;

    @Autowired(required = false)
    private SupportController supportController;

    @Autowired(required = false)
    private LoginController loginController;

    @Test
    @DisplayName("RT-01a: MinaController se instancia correctamente en el contexto")
    void rt01a_minaControllerCargado() {
        assertThat(minaController).isNotNull();
    }

    @Test
    @DisplayName("RT-01b: RiesgoController se instancia correctamente en el contexto")
    void rt01b_riesgoControllerCargado() {
        assertThat(riesgoController).isNotNull();
    }

    @Test
    @DisplayName("RT-01c: ForumController se instancia correctamente en el contexto")
    void rt01c_forumControllerCargado() {
        assertThat(forumController).isNotNull();
    }

    @Test
    @DisplayName("RT-01d: SupportController se instancia correctamente en el contexto")
    void rt01d_supportControllerCargado() {
        assertThat(supportController).isNotNull();
    }

    @Test
    @DisplayName("RT-01e: LoginController se instancia correctamente en el contexto")
    void rt01e_loginControllerCargado() {
        assertThat(loginController).isNotNull();
    }
}

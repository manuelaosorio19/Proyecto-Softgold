package com.proyectoL.softgold.regresion;

import com.proyectoL.softgold.controller.ForumController;
import com.proyectoL.softgold.model.ForumPost;
import com.proyectoL.softgold.service.ForumServiceIface;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * RT-05 — Regresión Foro: Verificar que las operaciones del foro (listar, ver,
 * crear, comentar) siguen funcionando correctamente sin efectos secundarios.
 */
@WebMvcTest(ForumController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RT-05: Regresión — Módulo Foro Comunitario")
class RT05ForoRegresionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumServiceIface forumService;

    @Test
    @DisplayName("RT-05a: Listar posts del foro retorna vista y modelo correctos")
    void rt05a_listarPostsRetornaVistaCorrecta() throws Exception {
        ForumPost post = new ForumPost();
        post.setId(1L);
        post.setTitulo("Post de prueba");
        post.setContenido("Contenido de prueba");
        post.setCategoria("GENERAL");
        post.setActivo(true);
        post.setFechaCreacion(LocalDateTime.now());

        when(forumService.listarPostsActivos()).thenReturn(List.of(post));

        mockMvc.perform(get("/foro"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/foro/listarPosts"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    @DisplayName("RT-05b: Crear post con datos válidos guarda y redirige a listado")
    void rt05b_crearPostValidoGuardaYRedirige() throws Exception {
        mockMvc.perform(post("/foro/crear")
                        .param("titulo", "Post de Regresión")
                        .param("contenido", "Verificando que el módulo de foro sigue operativo.")
                        .param("categoria", "GENERAL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/foro"));

        verify(forumService, times(1)).guardarPost(any(ForumPost.class));
    }

    @Test
    @DisplayName("RT-05c: Crear post con título vacío regresa al formulario")
    void rt05c_crearPostTituloVacioRegresaFormulario() throws Exception {
        mockMvc.perform(post("/foro/crear")
                        .param("titulo", "")
                        .param("contenido", "Contenido válido.")
                        .param("categoria", "GENERAL"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/foro/crearPost"));

        verify(forumService, never()).guardarPost(any());
    }

    @Test
    @DisplayName("RT-05d: Ver post existente retorna detalles del post")
    void rt05d_verPostExistenteRetornaDetalles() throws Exception {
        ForumPost post = new ForumPost();
        post.setId(1L);
        post.setTitulo("Detalle Post");
        post.setContenido("Contenido detallado.");
        post.setCategoria("TECNICO");
        post.setActivo(true);
        post.setFechaCreacion(LocalDateTime.now());

        when(forumService.buscarPostPorId(1L)).thenReturn(Optional.of(post));

        mockMvc.perform(get("/foro/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/foro/verPost"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    @DisplayName("RT-05e: Filtrar por categoría retorna posts de esa categoría")
    void rt05e_filtrarPorCategoriaRetornaPostsFiltrados() throws Exception {
        ForumPost post = new ForumPost();
        post.setTitulo("Post Seguridad");
        post.setCategoria("SEGURIDAD");
        post.setActivo(true);

        when(forumService.filtrarPorCategoria("SEGURIDAD")).thenReturn(Arrays.asList(post));

        mockMvc.perform(get("/foro").param("categoria", "SEGURIDAD"))
                .andExpect(status().isOk())
                .andExpect(view().name("vistas/foro/listarPosts"))
                .andExpect(model().attribute("categoriaActiva", "SEGURIDAD"));
    }
}

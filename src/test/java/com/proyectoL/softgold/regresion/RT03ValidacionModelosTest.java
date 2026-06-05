package com.proyectoL.softgold.regresion;

import com.proyectoL.softgold.model.Mina;
import com.proyectoL.softgold.model.Riesgo;
import com.proyectoL.softgold.model.SupportTicket;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RT-03 — Regresión Validación de Modelos: Verificar que las anotaciones de
 * validación de las entidades siguen funcionando correctamente.
 */
@DisplayName("RT-03: Regresión — Validaciones de modelos de dominio")
class RT03ValidacionModelosTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("RT-03a: Mina con nombre vacío produce violación de validación")
    void rt03a_minaConNombreVacioFallaValidacion() {
        Mina mina = new Mina();
        mina.setNombre("");
        mina.setDepartamento("Antioquia");

        Set<ConstraintViolation<Mina>> violaciones = validator.validate(mina);

        assertThat(violaciones).isNotEmpty();
        assertThat(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nombre")))
                .isTrue();
    }

    @Test
    @DisplayName("RT-03b: Mina con datos válidos no produce violaciones")
    void rt03b_minaConDatosValidosNoFalla() {
        Mina mina = new Mina();
        mina.setNombre("Mina El Dorado");
        mina.setDepartamento("Antioquia");

        Set<ConstraintViolation<Mina>> violaciones = validator.validate(mina);

        assertThat(violaciones).isEmpty();
    }

    @Test
    @DisplayName("RT-03c: Riesgo con descripción vacía produce violación de validación")
    void rt03c_riesgoConDescripcionVaciaFallaValidacion() {
        Riesgo riesgo = new Riesgo();
        riesgo.setDescripcion("");

        Set<ConstraintViolation<Riesgo>> violaciones = validator.validate(riesgo);

        assertThat(violaciones).isNotEmpty();
        assertThat(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("descripcion")))
                .isTrue();
    }

    @Test
    @DisplayName("RT-03d: Riesgo con descripción válida no produce violaciones")
    void rt03d_riesgoConDescripcionValidaNoFalla() {
        Riesgo riesgo = new Riesgo();
        riesgo.setDescripcion("Explosión por gas metano en galería principal");

        Set<ConstraintViolation<Riesgo>> violaciones = validator.validate(riesgo);

        assertThat(violaciones).isEmpty();
    }

    @Test
    @DisplayName("RT-03e: SupportTicket con email inválido produce violación")
    void rt03e_supportTicketEmailInvalidoFalla() {
        SupportTicket ticket = new SupportTicket();
        ticket.setNombre("Juan Pérez");
        ticket.setEmail("no-es-un-email");
        ticket.setAsunto("Error");
        ticket.setDescripcion("Descripción del problema");

        Set<ConstraintViolation<SupportTicket>> violaciones = validator.validate(ticket);

        assertThat(violaciones).isNotEmpty();
        assertThat(violaciones.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")))
                .isTrue();
    }

    @Test
    @DisplayName("RT-03f: SupportTicket con todos los datos válidos no produce violaciones")
    void rt03f_supportTicketValidoNoFalla() {
        SupportTicket ticket = new SupportTicket();
        ticket.setNombre("María López");
        ticket.setEmail("maria@softgold.com");
        ticket.setAsunto("Consulta técnica");
        ticket.setDescripcion("El sistema presenta lentitud al cargar mapas grandes.");

        Set<ConstraintViolation<SupportTicket>> violaciones = validator.validate(ticket);

        assertThat(violaciones).isEmpty();
    }
}

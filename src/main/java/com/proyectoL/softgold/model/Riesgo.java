package com.proyectoL.softgold.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "riesgo")
public class Riesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_riesgo")
    private Long codRiesgo;

    @Column(nullable = false)
    @NotBlank(message = "La descripción del riesgo es obligatoria")
    private String descripcion;

    @ManyToMany(mappedBy = "riesgos")
    private List<Mina> minas;

    public Long getCodRiesgo() {
        return codRiesgo;
    }

    public void setCodRiesgo(Long codRiesgo) {
        this.codRiesgo = codRiesgo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Mina> getMinas() {
        return minas;
    }

    public void setMinas(List<Mina> minas) {
        this.minas = minas;
    }
}

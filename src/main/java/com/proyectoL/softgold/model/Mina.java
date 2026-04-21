package com.proyectoL.softgold.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "mina")
public class Mina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_mina")
    private Long codMina;

    @Column(nullable = false)
    @NotBlank(message = "El nombre de la mina es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "La ubicación de la mina es obligatoria")
    private String departamento;

    // Relación 1:N con Usuario
    @OneToMany(mappedBy = "mina")
    private List<Usuario> usuarios;

    // Relación N:M con Mapa
    @ManyToMany
    @JoinTable(name = "mina_mapa", joinColumns = @JoinColumn(name = "Codigo_Mina"), inverseJoinColumns = @JoinColumn(name = "Codigo_Mapa"))
    private List<Mapa> mapas;

    // Relación N:M con Riesgo
    @ManyToMany
    @JoinTable(name = "mina_riesgo", joinColumns = @JoinColumn(name = "Codigo_Mina"), inverseJoinColumns = @JoinColumn(name = "cod_riesgo"))
    private List<Riesgo> riesgos;

    public Long getCodMina() {
        return codMina;
    }

    public void setCodMina(Long codMina) {
        this.codMina = codMina;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Mapa> getMapas() {
        return mapas;
    }

    public void setMapas(List<Mapa> mapas) {
        this.mapas = mapas;
    }

    public List<Riesgo> getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(List<Riesgo> riesgos) {
        this.riesgos = riesgos;
    }

    public Mina() {
    }

    public Mina(Long codMina, String nombre, String departamento, List<Usuario> usuarios,
            List<Mapa> mapas) {
        this.codMina = codMina;
        this.nombre = nombre;
        this.departamento = departamento;
        this.usuarios = usuarios;
        this.mapas = mapas;
    }

    @Override
    public String toString() {
        return "Mina [codMina=" + codMina + ", nombre=" + nombre + ", departamento=" + departamento + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Mina mina = (Mina) o;
        return codMina != null && codMina.equals(mina.getCodMina());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(codMina);
    }

}

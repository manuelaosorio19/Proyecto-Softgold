package com.proyectoL.softgold.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cedula", nullable = false, unique = true)
    @NotBlank(message = "La cédula es obligatoria")
    private String cedula;

    @Column(name = "tipo_documento", nullable = false)
    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    @Column(name = "nombre_1", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre1;

    @Column(name = "nombre_2", nullable = false)
    private String nombre2;

    @Column(name = "apellido_1", nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido1;

    @Column(name = "apellido_2", nullable = false)
    private String apellido2;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Transient
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "La contraseña debe tener al menos 8 caracteres, una letra mayúscula y un número")
    private String passwordPlano;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) // relacion de muchos a muchos
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"))
    private Collection<Rol> roles;

    @Column(name = "intentos_fallidos", nullable = false)
    private int intentosFallidos;

    @Column(name = "bloqueado", nullable = false)
    private boolean bloqueado;

    @Column(name = "tiempo_bloqueo", nullable = true)
    private LocalDateTime tiempoBloqueo;

    @Column(name = "tipo_usuario", nullable = false)
    @NotBlank(message = "El tipo de usuario es obligatorio")
    private String tipoUsuario;

    @Column(name = "telefono")
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "cod_mina")
    private Mina mina;

    @Column(name = "tipo_empleado")
    private String tipoEmpleado;

    @Column(name = "area")
    private String area;

    public Usuario() {
        this.intentosFallidos = 0;
        this.bloqueado = false;
        this.tiempoBloqueo = null;
    }

    public Usuario(Long id, String tipoDocumento, @NotBlank String nombre1, String nombre2,
            @NotBlank String apellido1,
            String apellido2, @Email @NotBlank String email, String password, Collection<Rol> roles) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.intentosFallidos = 0;
        this.bloqueado = false;
        this.tiempoBloqueo = null;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Rol> roles) {
        this.roles = roles;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public LocalDateTime getTiempoBloqueo() {
        return tiempoBloqueo;
    }

    public void setTiempoBloqueo(LocalDateTime tiempoBloqueo) {
        this.tiempoBloqueo = tiempoBloqueo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Mina getMina() {
        return mina;
    }

    public void setMina(Mina mina) {
        this.mina = mina;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !bloqueado;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPasswordPlano() {
        return passwordPlano;
    }

    public void setPasswordPlano(String passwordPlano) {
        this.passwordPlano = passwordPlano;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre1 + " " + apellido1 + ", email=" + email + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}

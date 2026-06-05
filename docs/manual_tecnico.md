# MANUAL TÉCNICO DEL SISTEMA SOFTGOLD

---

**Versión:** 1.0.0  
**Fecha:** Mayo 2026  
**Estado:** Oficial  
**Clasificación:** Uso interno – Equipo de desarrollo  
**Elaborado por:** Equipo SoftGold  
**Proyecto:** Ingeniería de Software II – Universidad Autónoma de Manizales  

---

## CONTROL DE CAMBIOS

| Versión | Fecha | Descripción | Autor |
|---------|-------|-------------|-------|
| 1.0.0 | Mayo 2026 | Versión inicial del manual técnico | Equipo SoftGold |

---

## TABLA DE CONTENIDOS

1. Descripción General del Sistema  
2. Objetivo del Software  
3. Arquitectura del Sistema  
4. Tecnologías Utilizadas  
5. Estructura del Proyecto  
6. Modelos de Datos  
7. Configuración del Entorno de Desarrollo  
8. Instalación del Proyecto  
9. Ejecución Local del Sistema  
10. Rutas y Controladores  
11. Seguridad Implementada  
12. Servicio de Correo Electrónico  
13. Manejo de Excepciones y Logs  
14. Validaciones  
15. Geolocalización y Mapas  
16. Procedimiento de Despliegue  
17. Buenas Prácticas Implementadas  
18. Estrategia de Mantenimiento  
19. Recomendaciones Futuras  
20. Glosario Técnico  

---

## 1. DESCRIPCIÓN GENERAL DEL SISTEMA

SoftGold es un sistema de información web empresarial diseñado para la gestión integral de operaciones mineras. La plataforma centraliza la administración de minas, riesgos geológicos, zonas de exploración, personal operativo y comunicación interna a través de un entorno web seguro y multirol.

El sistema implementa una arquitectura MVC (Modelo-Vista-Controlador) de tres capas sobre el stack Spring Boot, con renderizado del lado del servidor mediante Thymeleaf, persistencia en MySQL y seguridad gestionada por Spring Security.

### 1.1 Contexto del Negocio

La industria minera requiere sistemas robustos para la gestión de activos, zonas geográficas y personal especializado. SoftGold responde a esta necesidad proporcionando:

- Un panel centralizado para administradores con acceso a todos los módulos operativos.
- Interfaces diferenciadas para mineros y empleados según su rol dentro de la organización.
- Módulos de geolocalización integrados para el trazado de zonas de exploración y mapas mineros.
- Un sistema de foro interno para comunicación entre los miembros del equipo.
- Gestión documental integrada con plantillas estandarizadas.
- Sistema de soporte técnico mediante tickets.

### 1.2 Alcance del Sistema

El sistema abarca los siguientes procesos:

| Proceso | Descripción | Módulo |
|---------|-------------|--------|
| Gestión de Minas | CRUD completo de unidades mineras | MinaController |
| Gestión de Mapas | Registro y visualización de mapas geoespaciales | MapaController |
| Gestión de Riesgos | Catálogo y asociación de riesgos por mina | RiesgoController |
| Exploración | Registro de zonas de exploración con coordenadas GPS | ExplorationController |
| Foro | Comunicación interna con posts y comentarios | ForumController |
| Informes | Dashboard ejecutivo y reportes operativos | InformeController |
| Soporte | Sistema de tickets de soporte técnico | SupportController |
| Filtros | Búsqueda avanzada multi-criterio | FiltroController |
| Autenticación | Login, registro y recuperación de contraseña | SecurityConfig |
| Gestión de Usuarios | Administración de usuarios por rol | UsuarioController |

---

## 2. OBJETIVO DEL SOFTWARE

### 2.1 Objetivo General

Proporcionar una plataforma web integral que permita a las empresas del sector minero gestionar eficientemente sus operaciones, recursos humanos, infraestructura de minas y comunicación interna, garantizando el control de acceso por roles y la integridad de los datos.

### 2.2 Objetivos Específicos

1. Centralizar el registro y seguimiento de minas, mapas y zonas de exploración en una única plataforma.
2. Implementar un sistema de control de acceso basado en roles (RBAC) para garantizar la seguridad de la información.
3. Facilitar la comunicación interna del equipo minero a través de un foro con categorías.
4. Proveer reportes e informes gerenciales para la toma de decisiones.
5. Gestionar solicitudes de soporte técnico mediante un sistema de tickets.
6. Integrar visualización geoespacial mediante Leaflet.js y OpenStreetMap para el trazado de zonas.

---

## 3. ARQUITECTURA DEL SISTEMA

### 3.1 Diagrama de Arquitectura General

```
┌─────────────────────────────────────────────────────────┐
│                    CLIENTE (Browser)                     │
│         Bootstrap + Thymeleaf + Leaflet.js              │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP (Puerto 9090)
                         ▼
┌─────────────────────────────────────────────────────────┐
│                SERVIDOR DE APLICACIÓN                    │
│              Spring Boot 3.4.4 / Java 17                │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────┐  │
│  │  Controller  │  │   Service    │  │  Repository   │  │
│  │   Layer      │→ │    Layer     │→ │    Layer      │  │
│  │ (MVC/HTTP)  │  │ (Business)   │  │ (JPA/DAO)     │  │
│  └─────────────┘  └──────────────┘  └───────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │         Spring Security (Autenticación/RBAC)       │  │
│  └───────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────┐  │
│  │         Spring Boot Mail (SMTP Gmail)              │  │
│  └───────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────┘
                         │ JDBC / JPA
                         ▼
┌─────────────────────────────────────────────────────────┐
│              BASE DE DATOS                               │
│           MySQL 8.x – Schema: softgold                  │
│              Puerto: 3306                                │
└─────────────────────────────────────────────────────────┘
                         │ SMTP TLS
                         ▼
┌─────────────────────────────────────────────────────────┐
│           SERVICIO EXTERNO                               │
│         Gmail SMTP (smtp.gmail.com:587)                  │
└─────────────────────────────────────────────────────────┘
```

### 3.2 Patrón de Diseño MVC

SoftGold implementa el patrón **Model-View-Controller (MVC)** con las siguientes responsabilidades:

| Capa | Tecnología | Responsabilidad |
|------|-----------|-----------------|
| **Model** | JPA Entities + Spring Data | Representación de datos y acceso a BD |
| **View** | Thymeleaf + HTML/CSS/JS | Renderizado de plantillas del lado del servidor |
| **Controller** | Spring MVC @Controller | Manejo de solicitudes HTTP y enrutamiento |
| **Security** | Spring Security | Autenticación, autorización y filtros |
| **Service** | Spring @Service | Lógica de negocio desacoplada |
| **Repository** | Spring Data JPA | Abstracción de acceso a datos |

### 3.3 Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────┐
│                      SOFTGOLD APPLICATION                        │
│                                                                   │
│  ┌───────────────┐    ┌───────────────┐    ┌─────────────────┐  │
│  │ SecurityConfig│    │ DataInitializer│    │GlobalController │  │
│  │               │    │               │    │    Advice       │  │
│  └───────┬───────┘    └───────────────┘    └─────────────────┘  │
│          │                                                        │
│  ┌───────▼──────────────────────────────────────────────────┐   │
│  │                    CONTROLLERS                             │   │
│  │  MinaCtrl  MapaCtrl  RiesgoCtrl  ExplorationCtrl          │   │
│  │  ForumCtrl  InformeCtrl  SupportCtrl  FiltroCtrl          │   │
│  │  AdminCtrl  UsuarioCtrl  LoginCtrl  PasswordCtrl          │   │
│  └───────────────────────┬───────────────────────────────────┘  │
│                           │                                       │
│  ┌────────────────────────▼──────────────────────────────────┐  │
│  │                      SERVICES                              │  │
│  │  MinaService  MapaService  ExplorationService              │  │
│  │  ForumService  SupportService  EmailService                │  │
│  │  PasswordResetService  UsuarioService                      │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │                                       │
│  ┌────────────────────────▼──────────────────────────────────┐  │
│  │                   REPOSITORIES (DAO)                       │  │
│  │  MinaDAO  MapaDAO  RiesgoDAO  ZonaExploracionDAO           │  │
│  │  ForumPostDAO  ForumCommentDAO  SupportTicketDAO           │  │
│  │  UsuarioDAO  RolDAO  PasswordResetTokenDAO                 │  │
│  └────────────────────────┬───────────────────────────────────┘  │
│                           │ Spring Data JPA / Hibernate           │
│  ┌────────────────────────▼───────────────────────────────────┐  │
│  │                    MySQL Database                           │  │
│  └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4. TECNOLOGÍAS UTILIZADAS

### 4.1 Backend

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Java | 17 LTS | Lenguaje de programación principal |
| Spring Boot | 3.4.4 | Framework principal del backend |
| Spring MVC | 6.x | Framework MVC y manejo de rutas |
| Spring Security | 6.x | Autenticación y autorización |
| Spring Data JPA | 3.x | Persistencia y ORM |
| Hibernate | 6.x | Implementación JPA / ORM |
| Spring Boot Mail | 3.x | Envío de correos electrónicos |
| Bean Validation | 3.x | Validación de entidades |
| BCrypt | - | Cifrado de contraseñas |

### 4.2 Frontend

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Thymeleaf | 3.x | Motor de plantillas del lado del servidor |
| Bootstrap | 5.x | Framework CSS / sistema de grilla |
| Bootstrap Icons | 1.x | Iconografía vectorial |
| Leaflet.js | 1.9.4 | Mapas interactivos geoespaciales |
| OpenStreetMap | - | Proveedor de tiles de mapas (sin API key) |
| Montserrat | - | Tipografía principal (Google Fonts) |
| JavaScript ES6+ | - | Lógica del lado del cliente |

### 4.3 Base de Datos

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| MySQL | 8.x | Motor de base de datos relacional |
| MySQL Connector/J | 8.x | Driver JDBC para Java |

### 4.4 Herramientas de Construcción y DevOps

| Herramienta | Versión | Propósito |
|------------|---------|-----------|
| Apache Maven | 3.x | Gestión de dependencias y construcción |
| Spring Boot DevTools | 3.x | Recarga automática en desarrollo |
| Maven Wrapper (mvnw) | - | Ejecución portable de Maven |

### 4.5 Servicios Externos

| Servicio | Propósito |
|---------|-----------|
| Gmail SMTP (smtp.gmail.com:587) | Envío de correos para recuperación de contraseña |
| OpenStreetMap Tile Server | Tiles de mapas para Leaflet.js |

---

## 5. ESTRUCTURA DEL PROYECTO

```
softgold_final/
├── src/
│   ├── main/
│   │   ├── java/com/proyectoL/softgold/
│   │   │   ├── SoftgoldApplication.java          ← Clase principal (main)
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java           ← Carga inicial de datos (roles, admin)
│   │   │   │   └── PasswordEncoderConfig.java     ← Bean BCryptPasswordEncoder
│   │   │   ├── controller/
│   │   │   │   ├── AdminController.java           ← Panel de administración
│   │   │   │   ├── MinaController.java            ← CRUD de minas
│   │   │   │   ├── MapaController.java            ← CRUD de mapas
│   │   │   │   ├── RiesgoController.java          ← CRUD de riesgos
│   │   │   │   ├── ExplorationController.java     ← CRUD de zonas de exploración
│   │   │   │   ├── ForumController.java           ← Foro: posts y comentarios
│   │   │   │   ├── InformeController.java         ← Dashboard e informes
│   │   │   │   ├── SupportController.java         ← Sistema de tickets
│   │   │   │   ├── FiltroController.java          ← Búsqueda avanzada
│   │   │   │   ├── UsuarioController.java         ← Gestión de usuarios
│   │   │   │   ├── EmpleadoController.java        ← Gestión de empleados
│   │   │   │   ├── MineroController.java          ← Gestión de mineros
│   │   │   │   ├── LoginController.java           ← Control de inicio de sesión
│   │   │   │   ├── PasswordController.java        ← Cambio de contraseña (Mi Cuenta)
│   │   │   │   ├── PasswordResetController.java   ← Recuperación por email
│   │   │   │   ├── DocumentosController.java      ← Documentos del sistema
│   │   │   │   ├── GlobalControllerAdvice.java    ← Atributos globales de modelo
│   │   │   │   ├── CustomSuccessHandler.java      ← Redirección post-login por rol
│   │   │   │   ├── RedireccionController.java     ← Redirección genérica por rol
│   │   │   │   ├── InicioController.java          ← Dashboard ADMINISTRADOR
│   │   │   │   ├── MineroInicioController.java    ← Dashboard MINERO
│   │   │   │   ├── EmpleadosInicioController.java ← Dashboard EMPLEADO
│   │   │   │   ├── ValidacionController.java      ← Validaciones adicionales
│   │   │   │   └── VistaController.java           ← Vistas generales
│   │   │   ├── model/
│   │   │   │   ├── Usuario.java                   ← Entidad usuario (implements UserDetails)
│   │   │   │   ├── Rol.java                       ← Entidad rol
│   │   │   │   ├── Mina.java                      ← Entidad mina
│   │   │   │   ├── Mapa.java                      ← Entidad mapa geoespacial
│   │   │   │   ├── Riesgo.java                    ← Entidad riesgo geológico
│   │   │   │   ├── ZonaExploracion.java           ← Entidad zona de exploración
│   │   │   │   ├── ForumPost.java                 ← Entidad post del foro
│   │   │   │   ├── ForumComment.java              ← Entidad comentario del foro
│   │   │   │   ├── SupportTicket.java             ← Entidad ticket de soporte
│   │   │   │   └── PasswordResetToken.java        ← Token para recuperación de contraseña
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioDAO.java                ← Repositorio de usuarios
│   │   │   │   ├── RolDAO.java                    ← Repositorio de roles
│   │   │   │   ├── MinaDAO.java                   ← Repositorio de minas
│   │   │   │   ├── MapaDAO.java                   ← Repositorio de mapas
│   │   │   │   ├── RiesgoDAO.java                 ← Repositorio de riesgos
│   │   │   │   ├── ZonaExploracionDAO.java        ← Repositorio de zonas
│   │   │   │   ├── ForumPostDAO.java              ← Repositorio de posts
│   │   │   │   ├── ForumCommentDAO.java           ← Repositorio de comentarios
│   │   │   │   ├── SupportTicketDAO.java          ← Repositorio de tickets
│   │   │   │   └── PasswordResetTokenDAO.java     ← Repositorio de tokens de reset
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java            ← Configuración principal de seguridad
│   │   │   │   ├── CustomUserDetailsService.java  ← Carga de usuario por email
│   │   │   │   └── CustomLoginPassword.java       ← Manejador de fallos de login
│   │   │   └── service/
│   │   │       ├── MinaService.java / MinaServiceIface.java
│   │   │       ├── MapaService.java / MapaServiceIface.java
│   │   │       ├── ExplorationService.java / ExplorationServiceIface.java
│   │   │       ├── ForumService.java / ForumServiceIface.java
│   │   │       ├── SupportService.java / SupportServiceIface.java
│   │   │       ├── EmailService.java / EmailServiceIface.java
│   │   │       ├── PasswordResetService.java / PasswordResetServiceIface.java
│   │   │       └── UsuarioService.java / UsuarioServiceIface.java
│   │   └── resources/
│   │       ├── application.properties             ← Configuración de la aplicación
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── styles.css                 ← Hoja de estilos principal
│   │       │   ├── js/
│   │       │   │   ├── atajos.js                  ← Atajos de teclado
│   │       │   │   ├── inactividad.js             ← Cierre por inactividad
│   │       │   │   ├── tema.js                    ← Toggle modo oscuro/claro
│   │       │   │   ├── busqueda.js                ← Búsqueda dinámica
│   │       │   │   ├── cambiarPassword.js         ← Validación de contraseña
│   │       │   │   ├── crearAdmin.js              ← Lógica creación de admin
│   │       │   │   ├── eliminar.js                ← Confirmación de eliminación
│   │       │   │   ├── login.js                   ← Lógica de login
│   │       │   │   └── registro.js                ← Lógica de registro
│   │       │   ├── reporte_incidente.html         ← Documento estático
│   │       │   ├── formulario_satisfaccion.html   ← Documento estático
│   │       │   ├── service_level.html             ← Documento estático
│   │       │   ├── control_cambios.html           ← Documento estático
│   │       │   └── mejor_continua.html            ← Documento estático
│   │       └── templates/
│   │           ├── plantillas/
│   │           │   └── principal.html             ← Plantilla base (fragmentos)
│   │           └── vistas/
│   │               ├── login.html
│   │               ├── registro.html
│   │               ├── inicio.html
│   │               ├── inicioMinero.html
│   │               ├── inicioEmpleado.html
│   │               ├── admin.html
│   │               ├── perfil.html / perfilAdmin.html
│   │               ├── listarMinas.html / crearMina.html / editarMina.html
│   │               ├── listarMapas.html / crearMapa.html / editarMapa.html
│   │               ├── listarRiesgos.html / crearRiesgo.html / editarRiesgo.html
│   │               ├── listarMineros.html / crearMinero.html / editarMinero.html
│   │               ├── listarEmpleados.html / crearEmpleado.html / editarEmpleado.html
│   │               ├── listarSoporte.html / reporteSoporte.html
│   │               ├── recuperarPassword.html / restablecerPassword.html
│   │               ├── cuentaBloqueada.html
│   │               ├── cambiarPassword.html / cuentaCambiarPassword.html
│   │               ├── cuentaPerfil.html
│   │               ├── exploracion/
│   │               │   ├── listarZonas.html
│   │               │   ├── crearZona.html
│   │               │   ├── editarZona.html
│   │               │   └── mapaExploracion.html
│   │               ├── foro/
│   │               │   ├── listarPosts.html
│   │               │   ├── crearPost.html
│   │               │   └── verPost.html
│   │               ├── informes/
│   │               │   ├── dashboard.html
│   │               │   ├── reporteMinas.html
│   │               │   ├── impactoAmbiental.html
│   │               │   └── inventario.html
│   │               ├── filtros/
│   │               │   └── buscar.html
│   │               └── documentos/
│   │                   ├── guiaSeguridad.html
│   │                   ├── controlCambios.html
│   │                   ├── mejoraContinua.html
│   │                   └── satisfaccion.html
├── pom.xml                                        ← Descriptor de proyecto Maven
├── mvnw / mvnw.cmd                                ← Maven Wrapper
└── .gitignore
```

---

## 6. MODELOS DE DATOS

### 6.1 Diagrama Entidad-Relación (Conceptual)

```
┌──────────────┐     N:M     ┌─────────────┐
│   USUARIO    │─────────────│     ROL     │
│──────────────│             │─────────────│
│ id (PK)      │             │ id (PK)     │
│ cedula       │             │ nombre      │
│ tipoDocumento│             └─────────────┘
│ nombre1      │
│ nombre2      │     N:1     ┌─────────────┐
│ apellido1    │─────────────│    MINA     │
│ apellido2    │             │─────────────│
│ email        │             │ cod_mina(PK)│
│ password     │             │ nombre      │
│ tipoUsuario  │             │ departamento│
│ telefono     │             └──────┬──────┘
│ tipoEmpleado │                    │ N:M
│ area         │             ┌──────▼──────┐
│ bloqueado    │             │    MAPA     │
│ intentosFall │             │─────────────│
│ tiempoBloqueo│             │ codigo(PK)  │
└──────────────┘             │ titulo      │
                             │ descripcion │
                             │ latitud     │
                             │ longitud    │
                             │ coordenadas │
                             └─────────────┘
                                    │ N:M
                             ┌──────▼──────┐
                             │   RIESGO    │
                             │─────────────│
                             │ cod_riesgo  │
                             │ descripcion │
                             └─────────────┘

┌──────────────────────┐
│    ZONA_EXPLORACION  │
│──────────────────────│
│ id (PK)              │
│ nombre               │
│ descripcion          │
│ latitud              │
│ longitud             │
│ tipo                 │
│ estado               │
│ fechaRegistro        │
│ mina_id (FK)         │
└──────────────────────┘

┌──────────────┐     1:N     ┌──────────────────┐
│  FORUM_POST  │─────────────│  FORUM_COMMENT   │
│──────────────│             │──────────────────│
│ id (PK)      │             │ id (PK)          │
│ titulo       │             │ contenido        │
│ contenido    │             │ autorNombre      │
│ categoria    │             │ fechaCreacion    │
│ autorNombre  │             │ post_id (FK)     │
│ fechaCreacion│             └──────────────────┘
│ activo       │
└──────────────┘

┌──────────────────────────┐
│     SUPPORT_TICKET       │
│──────────────────────────│
│ id (PK)                  │
│ nombre                   │
│ email                    │
│ asunto                   │
│ descripcion              │
│ creado                   │
│ estado (OPEN/CLOSED)     │
└──────────────────────────┘

┌──────────────────────────┐
│   PASSWORD_RESET_TOKEN   │
│──────────────────────────│
│ id (PK)                  │
│ token                    │
│ usuario_id (FK)          │
│ fechaExpiracion          │
└──────────────────────────┘
```

### 6.2 Descripción de Entidades

#### Usuario
Entidad central del sistema. Implementa `UserDetails` de Spring Security para integración directa con el sistema de autenticación.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| id | Long | PK, AUTO | Identificador interno |
| cedula | String | UNIQUE, NOT NULL | Documento de identidad |
| tipoDocumento | String | NOT NULL | CC, CE, TI, NIT |
| nombre1 | String | NOT NULL | Primer nombre |
| nombre2 | String | - | Segundo nombre |
| apellido1 | String | NOT NULL | Primer apellido |
| apellido2 | String | - | Segundo apellido |
| email | String | UNIQUE, NOT NULL | Correo (username en Spring Security) |
| password | String | NOT NULL | Hash BCrypt |
| tipoUsuario | String | NOT NULL | Categoría del usuario |
| telefono | String | - | Número de contacto |
| tipoEmpleado | String | - | Solo para empleados |
| area | String | - | Área de trabajo |
| intentosFallidos | int | DEFAULT 0 | Contador para bloqueo |
| bloqueado | boolean | DEFAULT false | Estado de bloqueo |
| tiempoBloqueo | LocalDateTime | NULL | Timestamp de bloqueo |
| roles | Collection<Rol> | N:M | Roles del usuario |
| mina | Mina | FK | Mina asignada |

#### Mina
Representa una unidad minera en operación.

| Campo | Tipo | Restricción | Descripción |
|-------|------|------------|-------------|
| cod_mina | Long | PK, AUTO | Código interno |
| nombre | String | NOT NULL | Nombre de la mina |
| departamento | String | NOT NULL | Departamento geográfico |
| mapas | Set<Mapa> | N:M | Mapas asociados |
| riesgos | Set<Riesgo> | N:M | Riesgos identificados |
| usuarios | List<Usuario> | 1:N (inverso) | Personal asignado |

#### Rol

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Long | PK |
| nombre | String | Nombre con prefijo ROLE_ |

**Roles disponibles:**
- `ROLE_ADMINISTRADOR` — Acceso total al sistema
- `ROLE_MINERO` — Acceso a módulos operativos del minero
- `ROLE_EMPLEADO` — Acceso a módulos del empleado
- `ROLE_USUARIO` — Acceso básico de consulta

---

## 7. CONFIGURACIÓN DEL ENTORNO DE DESARROLLO

### 7.1 Requisitos Previos

| Herramienta | Versión Mínima | Descarga |
|------------|---------------|---------|
| JDK (Java) | 17 LTS | adoptium.net |
| Maven | 3.8+ | maven.apache.org |
| MySQL | 8.0+ | mysql.com |
| Git | 2.x | git-scm.com |
| IDE recomendado | IntelliJ IDEA o VS Code | - |

### 7.2 Variables de Entorno (application.properties)

El archivo de configuración se encuentra en:  
`src/main/resources/application.properties`

```properties
# Nombre de la aplicación
spring.application.name=softgold

# Puerto del servidor
server.port=9090

# Configuración de conexión MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/softgold?serverTimezone=America/Bogota&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# Configuración JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true

# Logging SQL
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Configuración SMTP Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<correo-remitente>@gmail.com
spring.mail.password=<contraseña-de-aplicacion>
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# Timeout de sesión
server.servlet.session.timeout=15m
```

> **ADVERTENCIA:** En entornos de producción, las credenciales de correo y base de datos deben gestionarse mediante variables de entorno del sistema operativo o un gestor de secretos, nunca en texto plano dentro del repositorio.

### 7.3 Descripción de Propiedades Clave

| Propiedad | Valor por defecto | Descripción |
|-----------|-----------------|-------------|
| `server.port` | 9090 | Puerto HTTP de la aplicación |
| `spring.jpa.hibernate.ddl-auto` | update | Actualiza el esquema sin destruir datos |
| `server.servlet.session.timeout` | 15m | Expiración de sesión por inactividad |
| `createDatabaseIfNotExist=true` | - | Crea la BD automáticamente si no existe |

---

## 8. INSTALACIÓN DEL PROYECTO

### 8.1 Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd softgold_final
```

### 8.2 Configurar la Base de Datos

**Paso 1:** Verificar que MySQL esté corriendo:
```bash
# Windows (PowerShell)
Get-Service -Name "MySQL*"

# Linux
sudo systemctl status mysql
```

**Paso 2:** Crear el esquema (opcional — se crea automáticamente):
```sql
CREATE DATABASE IF NOT EXISTS softgold 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;
```

**Paso 3:** Verificar el usuario de MySQL y sus permisos:
```sql
-- En producción, crear usuario dedicado:
CREATE USER 'softgold_user'@'localhost' IDENTIFIED BY 'contraseña_segura';
GRANT ALL PRIVILEGES ON softgold.* TO 'softgold_user'@'localhost';
FLUSH PRIVILEGES;
```

### 8.3 Configurar el Archivo de Propiedades

Editar `src/main/resources/application.properties`:
- Actualizar `spring.datasource.username` y `spring.datasource.password` según el entorno.
- Actualizar credenciales de Gmail para el servicio de correo.

### 8.4 Instalar Dependencias y Compilar

```bash
# Con Maven Wrapper (recomendado)
./mvnw clean install -DskipTests

# Con Maven instalado globalmente
mvn clean install -DskipTests
```

### 8.5 Verificar la Compilación

```bash
./mvnw package -DskipTests
```

El artefacto generado será: `target/softgold-0.0.1-SNAPSHOT.jar`

---

## 9. EJECUCIÓN LOCAL DEL SISTEMA

### 9.1 Desde el IDE

En IntelliJ IDEA o VS Code con extensión Spring Boot:
1. Abrir el proyecto como proyecto Maven.
2. Localizar la clase `SoftgoldApplication.java`.
3. Ejecutar con el botón "Run" o `Shift+F10`.

### 9.2 Desde la Línea de Comandos

```bash
# Ejecutar con Maven Wrapper
./mvnw spring-boot:run

# Ejecutar el JAR generado
java -jar target/softgold-0.0.1-SNAPSHOT.jar

# Ejecutar con perfil de producción
java -jar target/softgold-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 9.3 Verificar el Inicio

Acceder en el navegador a: `http://localhost:9090`

La aplicación redirigirá automáticamente a `/login`.

### 9.4 Credenciales por Defecto

El sistema crea automáticamente un usuario administrador al iniciar por primera vez (via `DataInitializer.java`):

| Campo | Valor |
|-------|-------|
| Email | admin@softgold.com |
| Contraseña | admin123 |
| Rol | ADMINISTRADOR |

> **IMPORTANTE:** Cambiar estas credenciales inmediatamente en cualquier entorno que no sea desarrollo local.

---

## 10. RUTAS Y CONTROLADORES

### 10.1 Rutas Públicas (sin autenticación)

| Ruta | Método | Descripción |
|------|--------|-------------|
| `/login` | GET, POST | Formulario de inicio de sesión |
| `/registro` | GET, POST | Registro de nuevos usuarios |
| `/recuperar` | GET | Formulario de recuperación de contraseña |
| `/recuperar-password` | POST | Envío de email de recuperación |
| `/cambiarPassword/{token}` | GET, POST | Restablecimiento de contraseña |
| `/cuenta-bloqueada` | GET | Página de cuenta bloqueada |
| `/foro` | GET | Listado público del foro |
| `/foro/{id}` | GET | Vista de un post del foro |
| `/foro/crear` | GET, POST | Creación de post (público) |
| `/foro/{id}/comentar` | POST | Agregar comentario (público) |
| `/soporte/reporte` | GET, POST | Formulario de reporte público |
| `/css/**` | GET | Recursos CSS |
| `/js/**` | GET | Recursos JavaScript |
| `/images/**` | GET | Recursos de imágenes |

### 10.2 Rutas del Administrador (`/admin/**`)

| Ruta | Método | Descripción |
|------|--------|-------------|
| `/admin` | GET | Panel de administración |
| `/admin/minas` | GET | Listar todas las minas |
| `/admin/minas/crear` | GET, POST | Crear una mina |
| `/admin/minas/editar/{id}` | GET, POST | Editar una mina |
| `/admin/minas/eliminar/{id}` | GET | Eliminar una mina |
| `/admin/minas/buscar` | GET | Buscar minas por departamento |
| `/admin/mapas` | GET | Listar mapas |
| `/admin/mapas/crear` | GET, POST | Crear mapa |
| `/admin/mapas/editar/{id}` | GET, POST | Editar mapa |
| `/admin/mapas/eliminar/{id}` | GET | Eliminar mapa |
| `/admin/riesgos` | GET | Listar riesgos |
| `/admin/riesgos/crear` | GET, POST | Crear riesgo |
| `/admin/riesgos/editar/{id}` | GET, POST | Editar riesgo |
| `/admin/riesgos/eliminar/{id}` | GET | Eliminar riesgo |
| `/admin/exploracion` | GET | Listar zonas de exploración |
| `/admin/exploracion/crear` | GET, POST | Crear zona de exploración |
| `/admin/exploracion/editar/{id}` | GET, POST | Editar zona |
| `/admin/exploracion/eliminar/{id}` | GET | Eliminar zona |
| `/admin/exploracion/mapa` | GET | Mapa general de exploración |
| `/admin/filtros/buscar` | GET | Búsqueda avanzada |
| `/admin/informes/dashboard` | GET | Dashboard gerencial |
| `/admin/informes/reporte-minas` | GET | Reporte de minas |
| `/admin/informes/impacto-ambiental` | GET | Informe ambiental |
| `/admin/informes/inventario` | GET | Inventario de recursos |
| `/admin/usuarios` | GET | Listar todos los usuarios |
| `/admin/usuarios/crear` | GET, POST | Crear usuario |
| `/admin/usuarios/editar/{id}` | GET, POST | Editar usuario |
| `/admin/usuarios/eliminar/{id}` | GET | Eliminar usuario |
| `/admin/mineros` | GET | Listar mineros |
| `/admin/mineros/crear` | GET, POST | Crear minero |
| `/admin/empleados` | GET | Listar empleados |
| `/admin/empleados/crear` | GET, POST | Crear empleado |

### 10.3 Rutas del Sistema de Soporte

| Ruta | Método | Rol Requerido | Descripción |
|------|--------|--------------|-------------|
| `/soporte/reporte` | GET, POST | Público | Crear ticket público |
| `/soporte/tickets` | GET | ADMINISTRADOR | Listar tickets |
| `/soporte/tickets/{id}/cerrar` | POST | ADMINISTRADOR | Cerrar un ticket |

### 10.4 Rutas del Foro

| Ruta | Método | Rol Requerido | Descripción |
|------|--------|--------------|-------------|
| `/foro` | GET | Público | Listar posts |
| `/foro/crear` | GET, POST | Público | Crear post |
| `/foro/{id}` | GET | Público | Ver post y comentarios |
| `/foro/{id}/comentar` | POST | Público | Añadir comentario |
| `/foro/eliminar/{id}` | GET | ADMINISTRADOR | Eliminar post |

### 10.5 Convenciones de Controladores

Todos los controladores siguen el patrón estándar Spring MVC:

```java
@Controller
@RequestMapping("/admin/minas")
public class MinaController {

    @Autowired
    private MinaDAO minaDAO;

    @GetMapping("")
    public String listar(Model model) {
        model.addAttribute("minas", minaDAO.findAll());
        return "vistas/listarMinas";  // Retorna nombre de template Thymeleaf
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute Mina mina, 
                        BindingResult result,
                        RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) return "vistas/crearMina";
        minaDAO.save(mina);
        redirectAttrs.addFlashAttribute("mensajeExito", "Mina creada exitosamente.");
        return "redirect:/admin/minas";
    }
}
```

---

## 11. SEGURIDAD IMPLEMENTADA

### 11.1 Diagrama de Flujo de Autenticación

```
Usuario ingresa email/contraseña
            │
            ▼
    Spring Security intercepta POST /login
            │
            ▼
    CustomUserDetailsService.loadUserByUsername(email)
            │
            ├─── Usuario no encontrado → AuthenticationException
            │
            ▼
    BCryptPasswordEncoder.matches(plano, hash)
            │
            ├─── No coincide → CustomLoginPassword.onAuthenticationFailure()
            │         └── incrementa intentosFallidos
            │         └── si >= 3 → marca bloqueado=true
            │         └── redirige /login?error
            │
            ├─── Usuario bloqueado → redirige /cuenta-bloqueada
            │
            ▼
    Autenticación exitosa
            │
            ▼
    CustomSuccessHandler.onAuthenticationSuccess()
            │
            ▼
    Redirección por rol:
    ├── ADMINISTRADOR → /inicio
    ├── MINERO → /minero/inicio
    ├── EMPLEADO → /empleado/inicio
    └── USUARIO → /usuario/inicio
```

### 11.2 Configuración de Spring Security

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        // Rutas públicas
        .requestMatchers("/login", "/registro", "/recuperar/**", ...).permitAll()
        // Rutas por rol
        .requestMatchers("/foro/eliminar/**").hasRole("ADMINISTRADOR")
        .requestMatchers("/soporte/tickets/**").hasRole("ADMINISTRADOR")
        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
        .requestMatchers("/minero/**").hasRole("MINERO")
        .requestMatchers("/empleado/**").hasRole("EMPLEADO")
        .requestMatchers("/usuario/**").hasRole("USUARIO")
        // Todo lo demás requiere autenticación
        .anyRequest().authenticated()
    )
    .formLogin(form -> form
        .loginPage("/login")
        .usernameParameter("email")
        .defaultSuccessUrl("/redirectByRole", true)
        .failureHandler(customLoginPassword)
    )
    .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
    )
    .csrf(csrf -> csrf.disable());
    return http.build();
}
```

### 11.3 Cifrado de Contraseñas

SoftGold utiliza **BCrypt** con el factor de trabajo por defecto (10 rondas):

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

El hash generado tiene la forma: `$2a$10$<salt><hash>` (60 caracteres).

### 11.4 Bloqueo de Cuenta

El sistema implementa protección contra ataques de fuerza bruta:

| Intentos Fallidos | Acción |
|------------------|--------|
| 1-2 | Mensaje de error, contador incrementado |
| 3+ | Cuenta bloqueada, redirección a `/cuenta-bloqueada` |

El desbloqueo de cuentas debe hacerlo manualmente el administrador desde el panel de gestión de usuarios.

### 11.5 Recuperación de Contraseña

Flujo de recuperación implementado:

1. Usuario ingresa su email en `/recuperar`.
2. Sistema genera un token UUID y lo guarda en `PASSWORD_RESET_TOKEN`.
3. Se envía un email con el enlace `http://host/cambiarPassword/{token}`.
4. El token tiene una expiración configurada.
5. El usuario accede al enlace, ingresa la nueva contraseña y el sistema la actualiza con BCrypt.

### 11.6 Control de Roles y Autoridades

Los roles se almacenan con el prefijo `ROLE_` en la base de datos. Spring Security los verifica mediante:

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
        .collect(Collectors.toList());
}
```

La tabla de mapeo de roles es:

| Rol en BD | Autoridad Spring Security | Acceso |
|-----------|--------------------------|--------|
| ROLE_ADMINISTRADOR | hasRole("ADMINISTRADOR") | /admin/**, /soporte/tickets/**, /foro/eliminar/** |
| ROLE_MINERO | hasRole("MINERO") | /minero/** |
| ROLE_EMPLEADO | hasRole("EMPLEADO") | /empleado/** |
| ROLE_USUARIO | hasRole("USUARIO") | /usuario/** |

### 11.7 Sesión y Timeout

La sesión HTTP expira automáticamente después de **15 minutos de inactividad**:
```properties
server.servlet.session.timeout=15m
```

Adicionalmente, `inactividad.js` implementa un temporizador del lado del cliente que redirige al login después de inactividad.

---

## 12. SERVICIO DE CORREO ELECTRÓNICO

### 12.1 Configuración SMTP

SoftGold utiliza Gmail SMTP con TLS para el envío de correos. Se requiere una **contraseña de aplicación** de Google (no la contraseña de la cuenta).

### 12.2 Uso del EmailService

```java
@Service
public class EmailService implements EmailServiceIface {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void enviarEmailRecuperacion(String destinatario, String enlace) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de contraseña - SoftGold");
        mensaje.setText("Haga clic en el siguiente enlace: " + enlace);
        mailSender.send(mensaje);
    }
}
```

### 12.3 Obtener Contraseña de Aplicación Google

1. Acceder a `myaccount.google.com`.
2. Ir a **Seguridad > Verificación en dos pasos > Contraseñas de aplicaciones**.
3. Crear una contraseña para "Correo" y "Windows/Mac".
4. Usar esa contraseña (formato: `xxxx xxxx xxxx xxxx`) en `spring.mail.password`.

---

## 13. MANEJO DE EXCEPCIONES Y LOGS

### 13.1 GlobalControllerAdvice

La clase `GlobalControllerAdvice.java` provee:
- Atributos de modelo disponibles en todos los templates (usuario autenticado, roles, etc.)
- Manejo global de excepciones con `@ExceptionHandler`

### 13.2 Logging de SQL

El sistema registra todas las consultas SQL en modo debug:
```properties
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### 13.3 Logs de Spring Boot

Spring Boot genera logs automáticamente en consola. En producción se recomienda configurar Logback para persistir en archivos:

```properties
# Agregar en application.properties para producción
logging.file.name=/var/log/softgold/softgold.log
logging.level.root=INFO
logging.level.com.proyectoL.softgold=DEBUG
```

---

## 14. VALIDACIONES

### 14.1 Validación de Entidades (Bean Validation)

SoftGold utiliza Jakarta Bean Validation con anotaciones estándar:

| Anotación | Campo | Regla |
|-----------|-------|-------|
| `@NotBlank` | cedula, nombre1, apellido1, email | Campo obligatorio |
| `@Email` | email | Formato de email válido |
| `@Pattern` | passwordPlano | `^(?=.*[A-Z])(?=.*\d).{8,20}$` |

**Regla de contraseña:** Mínimo 8 caracteres, máximo 20, al menos una mayúscula y un número.

### 14.2 Validación en Controladores

```java
@PostMapping("/crear")
public String crear(@Valid @ModelAttribute("mina") Mina mina, 
                    BindingResult result, 
                    Model model) {
    if (result.hasErrors()) {
        // Recargar datos necesarios para el formulario
        model.addAttribute("mapas", mapaDAO.findAll());
        return "vistas/crearMina";  // Muestra errores en el template
    }
    minaDAO.save(mina);
    return "redirect:/admin/minas";
}
```

### 14.3 Validación del Lado del Cliente

Los scripts JavaScript complementan la validación del servidor:
- `registro.js`: Valida formato de contraseña y coincidencia antes de enviar.
- `cambiarPassword.js`: Valida nueva contraseña al cambiarla.
- `busqueda.js`: Gestiona búsqueda dinámica en tiempo real.

---

## 15. GEOLOCALIZACIÓN Y MAPAS

### 15.1 Integración de Leaflet.js

El sistema integra **Leaflet.js v1.9.4** con tiles de **OpenStreetMap** (sin necesidad de API key).

**Inclusión en el template base (`principal.html`):**
```html
<!-- CSS Leaflet (en fragmento head) -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>

<!-- JS Leaflet (en fragmento footer) -->
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
```

### 15.2 Inicialización Estándar del Mapa

```javascript
const map = L.map('mapa-id').setView([latitud, longitud], zoom);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

// Añadir marcador
L.marker([latitud, longitud])
  .addTo(map)
  .bindPopup('<b>Nombre de la zona</b><br>Descripción');
```

### 15.3 Clases CSS para Contenedores de Mapa

| Clase CSS | Altura | Uso |
|-----------|--------|-----|
| `.sg-map-container` | 350px | Mapas de detalle |
| `.sg-map-full` | 580px | Mapa general de exploración |

---

## 16. PROCEDIMIENTO DE DESPLIEGUE

### 16.1 Construcción del Artefacto

```bash
# Limpiar y empaquetar
./mvnw clean package -DskipTests

# El JAR se genera en:
# target/softgold-0.0.1-SNAPSHOT.jar
```

### 16.2 Despliegue en Servidor Linux

```bash
# Copiar el JAR al servidor
scp target/softgold-0.0.1-SNAPSHOT.jar usuario@servidor:/opt/softgold/

# Crear archivo de configuración externo
nano /opt/softgold/application.properties

# Ejecutar como servicio
java -jar /opt/softgold/softgold-0.0.1-SNAPSHOT.jar \
  --spring.config.location=/opt/softgold/application.properties
```

### 16.3 Configuración como Servicio systemd (Linux)

```ini
# /etc/systemd/system/softgold.service
[Unit]
Description=SoftGold Sistema de Gestión Minera
After=network.target mysql.service

[Service]
Type=simple
User=softgold
WorkingDirectory=/opt/softgold
ExecStart=/usr/bin/java -jar /opt/softgold/softgold-0.0.1-SNAPSHOT.jar
EnvironmentFile=/opt/softgold/softgold.env
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
systemctl daemon-reload
systemctl enable softgold
systemctl start softgold
```

---

## 17. BUENAS PRÁCTICAS IMPLEMENTADAS

| Práctica | Implementación |
|----------|---------------|
| Separation of Concerns | Capas Controller / Service / Repository claramente separadas |
| DRY (Don't Repeat Yourself) | Plantilla base `principal.html` con fragmentos Thymeleaf |
| Interfaz + Implementación | Todos los services tienen su interfaz (`*Iface`) |
| Validación en múltiples capas | Bean Validation + JavaScript del lado del cliente |
| Cifrado de contraseñas | BCrypt (nunca texto plano en BD) |
| Control de acceso por roles | RBAC con Spring Security |
| Manejo de estados HTTP | Redirección Post/Redirect/Get para prevenir reenvíos |
| Flash attributes | Mensajes de éxito/error sin repetir solicitudes |
| Portabilidad | Maven Wrapper permite ejecución sin instalar Maven |

---

## 18. ESTRATEGIA DE MANTENIMIENTO

### 18.1 Actualizaciones de Dependencias

- Revisar trimestralmente nuevas versiones de Spring Boot en `start.spring.io`.
- Ejecutar `./mvnw versions:display-dependency-updates` para identificar dependencias desactualizadas.
- Actualizar Leaflet.js cuando haya versiones con correcciones de seguridad.

### 18.2 Respaldo de la Base de Datos

```bash
# Backup diario automatizado
mysqldump -u root -p softgold > backup_$(date +%Y%m%d).sql

# Restauración
mysql -u root -p softgold < backup_20260101.sql
```

### 18.3 Monitoreo

- Verificar el estado del servicio: `systemctl status softgold`
- Revisar logs: `tail -f /var/log/softgold/softgold.log`
- Monitorear espacio en disco y memoria: `df -h && free -m`

---

## 19. RECOMENDACIONES FUTURAS

| Prioridad | Mejora | Justificación |
|-----------|--------|---------------|
| Alta | Migrar credenciales a variables de entorno | Seguridad en producción |
| Alta | Habilitar CSRF protection | Protección contra ataques CSRF |
| Alta | Implementar HTTPS/SSL | Cifrado de datos en tránsito |
| Media | Agregar paginación en tablas | Rendimiento con grandes volúmenes |
| Media | Implementar refresh tokens JWT | Sesiones más robustas |
| Media | Agregar pruebas unitarias e integración | Calidad y mantenibilidad |
| Baja | Integrar Swagger/OpenAPI | Documentación de API automática |
| Baja | Implementar caché (Redis) | Rendimiento en consultas frecuentes |
| Baja | Agregar soporte multiidioma (i18n) | Internacionalización |
| Baja | Exportación de reportes a PDF/Excel | Funcionalidad empresarial |

---

## 20. GLOSARIO TÉCNICO

| Término | Definición |
|---------|-----------|
| **BCrypt** | Algoritmo de hash adaptativo para contraseñas, resistente a ataques de fuerza bruta |
| **DAO** | Data Access Object — interfaz de acceso a la base de datos |
| **DDL** | Data Definition Language — instrucciones SQL para definir estructuras |
| **DevTools** | Módulo de Spring Boot que activa recarga automática en desarrollo |
| **JDBC** | Java Database Connectivity — API de Java para conectarse a bases de datos |
| **JPA** | Jakarta Persistence API — especificación ORM estándar de Java |
| **JWT** | JSON Web Token — estándar de tokens de autenticación |
| **Leaflet.js** | Biblioteca JavaScript de código abierto para mapas interactivos |
| **MVC** | Model-View-Controller — patrón de diseño de software |
| **ORM** | Object-Relational Mapping — mapeo entre objetos Java y tablas BD |
| **RBAC** | Role-Based Access Control — control de acceso basado en roles |
| **SMTP** | Simple Mail Transfer Protocol — protocolo de envío de correos |
| **Spring Security** | Framework de seguridad para aplicaciones Spring |
| **Thymeleaf** | Motor de plantillas HTML del lado del servidor para Java |
| **TLS** | Transport Layer Security — protocolo de cifrado en red |
| **UUID** | Universally Unique Identifier — identificador único de 128 bits |
| **UserDetails** | Interfaz de Spring Security que representa al usuario autenticado |

---

*Fin del Manual Técnico del Sistema SoftGold — Versión 1.0.0*

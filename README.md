# SoftGold - Sistema de Gestión Minera

Aplicación web desarrollada con **Spring Boot** para la gestión integral de operaciones mineras. Permite administrar minas, mapas, zonas de exploración, personal (mineros y empleados), riesgos, foro interno y soporte técnico, con control de acceso basado en roles.

---

## Creadores

| Nombre | Rol |
|--------|-----|
| **Manuela Osorio** | Desarrolladora |
| **Luisa Arcila** | Desarrolladora |
| **Alexis Arcila** | Desarrollador |

Proyecto desarrollado para la asignatura **Ingeniería de Software II** — Universidad Autónoma de Manizales.

---

## Arquitectura

El proyecto sigue el patrón **MVC (Model-View-Controller)** sobre Spring Boot 3.4.4 con las siguientes capas:

```
softgold/
├── config/          # Inicialización de datos y configuración de beans
├── controller/      # Controladores MVC (HTTP handlers por módulo)
├── model/           # Entidades JPA (Usuario, Mina, Mapa, Riesgo, etc.)
├── repository/      # DAOs con Spring Data JPA
├── service/         # Lógica de negocio (interfaces + implementaciones)
├── security/        # Spring Security: roles, filtros, login personalizado
└── SoftgoldApplication.java
```

### Capas principales

| Capa | Tecnología | Descripción |
|------|-----------|-------------|
| Presentación | Thymeleaf + HTML/CSS/JS | Vistas por rol (admin, empleado, minero) |
| Controladores | Spring MVC | Enrutamiento y validación de formularios |
| Servicios | Spring Service | Lógica de negocio desacoplada |
| Persistencia | Spring Data JPA + Hibernate | Acceso a base de datos vía repositorios |
| Base de datos | MySQL 8 | Esquema generado automáticamente por Hibernate |
| Seguridad | Spring Security | Autenticación por formulario, roles y bloqueo de cuenta |
| Correo | Spring Mail (Gmail SMTP) | Notificaciones y recuperación de contraseña |

### Módulos funcionales

- **Autenticación**: registro, login, bloqueo tras 3 intentos fallidos, recuperación y cambio de contraseña por correo.
- **Gestión de usuarios**: CRUD de mineros y empleados por parte del administrador.
- **Minas y Mapas**: creación, edición y listado de minas con sus mapas asociados.
- **Zonas de Exploración**: registro y visualización cartográfica de zonas exploradas.
- **Riesgos**: registro y seguimiento de riesgos operativos.
- **Foro interno**: publicación de posts y comentarios entre usuarios.
- **Soporte**: sistema de tickets con reportes para el administrador.
- **Informes**: dashboard con reportes de minas, impacto ambiental e inventario.
- **Documentos**: guías de seguridad, control de cambios, mejora continua y satisfacción.

### Roles y permisos

| Rol | Acceso |
|-----|--------|
| `ADMINISTRADOR` | CRUD completo, gestión de tickets de soporte, eliminación en foro |
| `EMPLEADO` | Vistas de empleado, módulos habilitados por el admin |
| `MINERO` | Vistas de minero, módulos habilitados por el admin |
| Público | Login, registro, recuperación de contraseña, foro (lectura/escritura) |

---

## Requisitos previos

- Java 17 o superior
- Maven 3.8+
- MySQL 8.0+
- Cuenta Gmail con **App Password** habilitada (para el envío de correos)

---

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/manuelaosorio19/Proyecto-Softgold.git
cd Proyecto-Softgold
```

### 2. Crear la base de datos

```sql
CREATE DATABASE softgold CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> Hibernate crea las tablas automáticamente al iniciar la aplicación (`ddl-auto=update`).

### 3. Configurar `application.properties`

Editar el archivo `src/main/resources/application.properties` con tus credenciales locales:

```properties
# Conexión MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/softgold?serverTimezone=America/Bogota&createDatabaseIfNotExist=true
spring.datasource.username=TU_USUARIO_MYSQL
spring.datasource.password=TU_PASSWORD_MYSQL

# Correo (Gmail con App Password)
spring.mail.username=TU_CORREO@gmail.com
spring.mail.password=TU_APP_PASSWORD
```

> Para generar un App Password de Gmail: cuenta de Google > Seguridad > Verificación en dos pasos > Contrasenas de aplicaciones.

### 4. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación quedará disponible en: [http://localhost:9090](http://localhost:9090)

### 5. Usuario administrador por defecto

Al iniciar por primera vez, `DataInitializer` crea automáticamente un usuario administrador. Revisar `src/main/java/com/proyectoL/softgold/config/DataInitializer.java` para ver las credenciales iniciales.

---

## Tecnologías utilizadas

| Tecnología | Versión |
|-----------|---------|
| Java | 17 |
| Spring Boot | 3.4.4 |
| Spring Security | 6.x |
| Spring Data JPA / Hibernate | 6.x |
| Thymeleaf | 3.x |
| MySQL | 8.x |
| Maven | 3.8+ |
| Bootstrap (via templates) | — |

---

## Estructura de directorios relevante

```
src/
├── main/
│   ├── java/com/proyectoL/softgold/
│   │   ├── config/          # DataInitializer, PasswordEncoderConfig
│   │   ├── controller/      # AdminController, MinaController, ForumController, etc.
│   │   ├── model/           # Usuario, Mina, Mapa, Riesgo, ZonaExploracion, etc.
│   │   ├── repository/      # DAOs (UsuarioDAO, MinaDAO, MapaDAO, etc.)
│   │   ├── security/        # SecurityConfig, CustomUserDetailsService
│   │   └── service/         # Servicios e interfaces por módulo
│   └── resources/
│       ├── application.properties
│       ├── static/          # CSS, JS
│       └── templates/       # Vistas Thymeleaf organizadas por módulo
└── test/                    # Tests unitarios e integración
```

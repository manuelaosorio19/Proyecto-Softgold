# MANUAL DE USUARIO DEL SISTEMA SOFTGOLD

---

**Versión:** 1.0.0  
**Fecha:** Mayo 2026  
**Estado:** Oficial  
**Dirigido a:** Usuarios finales del sistema  
**Clasificación:** Distribución general  

---

## TABLA DE CONTENIDOS

1. Introducción al Sistema  
2. Objetivo de la Plataforma  
3. Requisitos Mínimos  
4. Acceso al Sistema  
5. Recuperación de Contraseña  
6. Navegación General  
7. Panel Principal (Dashboard)  
8. Módulo de Minas  
9. Módulo de Mapas  
10. Módulo de Riesgos  
11. Módulo de Zonas de Exploración  
12. Módulo de Foro  
13. Módulo de Informes y Reportes  
14. Sistema de Soporte  
15. Búsqueda y Filtros Avanzados  
16. Gestión de Usuarios  
17. Documentos del Sistema  
18. Mi Cuenta (Perfil)  
19. Atajos de Teclado  
20. Modo Oscuro / Claro  
21. Mensajes de Error Frecuentes  
22. Preguntas Frecuentes  
23. Cierre de Sesión  

---

## 1. INTRODUCCIÓN AL SISTEMA

**SoftGold** es una plataforma web empresarial diseñada para la gestión integral de operaciones mineras. El sistema permite a los equipos de trabajo administrar minas, zonas de exploración, riesgos geológicos, personal, documentos y comunicación interna desde un único entorno centralizado y seguro.

### 1.1 ¿Quién puede usar SoftGold?

El sistema está diseñado para cuatro tipos de usuarios:

| Tipo de Usuario | Descripción | Nivel de Acceso |
|----------------|-------------|-----------------|
| **Administrador** | Personal directivo y de sistemas | Acceso total al sistema |
| **Minero** | Trabajadores operativos de campo | Módulos operativos asignados |
| **Empleado** | Personal administrativo interno | Módulos administrativos |
| **Usuario** | Consultores o acceso básico | Visualización y consultas |

---

## 2. OBJETIVO DE LA PLATAFORMA

SoftGold busca:

- Centralizar la información de todas las unidades mineras de la organización.
- Proveer visibilidad geoespacial de zonas de exploración mediante mapas interactivos.
- Facilitar la identificación y seguimiento de riesgos geológicos.
- Habilitar la comunicación interna a través del foro.
- Generar reportes e informes ejecutivos para la toma de decisiones.
- Gestionar solicitudes de soporte técnico de manera ordenada.

---

## 3. REQUISITOS MÍNIMOS

Para utilizar SoftGold de manera óptima, se requiere:

### 3.1 Hardware Recomendado (Usuario Final)

| Componente | Requisito Mínimo |
|-----------|-----------------|
| Procesador | Intel Core i3 o equivalente |
| Memoria RAM | 4 GB |
| Pantalla | Resolución 1280 x 720 o superior |
| Conexión a Internet | 5 Mbps o superior |

### 3.2 Software Recomendado

| Componente | Opción |
|-----------|--------|
| Navegador (recomendado) | Google Chrome 100+ |
| Navegador (alternativo) | Microsoft Edge 100+, Firefox 95+ |
| Sistema operativo | Windows 10/11, macOS 11+, Ubuntu 20+ |

> **NOTA:** SoftGold es una aplicación web y **no requiere instalación** en el equipo del usuario. Solo necesita un navegador web moderno y conexión a la red donde esté alojado el servidor.

---

## 4. ACCESO AL SISTEMA

### 4.1 Inicio de Sesión

1. Abrir el navegador web.
2. Ingresar la dirección URL del sistema proporcionada por el administrador.  
   *Ejemplo: `http://servidor.empresa.com:9090`*
3. El sistema mostrará la pantalla de inicio de sesión.

---
**[CAPTURA DE PANTALLA: Pantalla de inicio de sesión de SoftGold]**

---

4. Ingresar los datos de acceso:
   - **Correo electrónico:** Su correo institucional registrado.
   - **Contraseña:** Su contraseña de acceso.
5. Hacer clic en el botón **"Ingresar"**.

### 4.2 Resultado del Inicio de Sesión

- Si las credenciales son correctas, el sistema redirigirá automáticamente al panel correspondiente a su rol.
- Si las credenciales son incorrectas, aparecerá un mensaje de error. Tiene **3 intentos** antes de que la cuenta sea bloqueada.

### 4.3 Cuenta Bloqueada

Si su cuenta es bloqueada por múltiples intentos fallidos, el sistema mostrará la página de **"Cuenta Bloqueada"**. En este caso, debe contactar al administrador del sistema para que restablezca su acceso.

> **IMPORTANTE:** Nunca comparta su contraseña con otras personas. Cada usuario debe tener credenciales individuales.

---

## 5. RECUPERACIÓN DE CONTRASEÑA

Si olvidó su contraseña, siga estos pasos:

1. En la pantalla de inicio de sesión, hacer clic en **"¿Olvidaste tu contraseña?"**.
2. El sistema mostrará un formulario donde debe ingresar su **correo electrónico** registrado.
3. Hacer clic en **"Enviar enlace de recuperación"**.

---
**[CAPTURA DE PANTALLA: Formulario de recuperación de contraseña]**

---

4. Revisar su bandeja de entrada. Recibirá un correo de SoftGold con un enlace de recuperación.
5. Hacer clic en el enlace del correo.
6. Ingresar su nueva contraseña respetando los siguientes requisitos:
   - Mínimo **8 caracteres**
   - Máximo **20 caracteres**
   - Al menos **una letra mayúscula**
   - Al menos **un número**
7. Confirmar la nueva contraseña y hacer clic en **"Cambiar contraseña"**.

> **NOTA:** El enlace de recuperación tiene un tiempo de expiración. Si el enlace expiró, repita el proceso para recibir un nuevo enlace.

---

## 6. NAVEGACIÓN GENERAL

### 6.1 Estructura de la Interfaz (Usuario Autenticado)

La interfaz de SoftGold para usuarios autenticados se compone de tres zonas:

```
┌────────────────────────────────────────────────────────────────┐
│  TOPBAR (barra superior fija)                                   │
│  Logo | Título del módulo actual | Usuario actual | Cerrar sesión│
├──────────────┬─────────────────────────────────────────────────┤
│              │                                                   │
│   SIDEBAR    │           CONTENIDO PRINCIPAL                    │
│  (Menú       │                                                   │
│  lateral     │   Aquí se muestra el módulo activo               │
│  izquierdo)  │                                                   │
│              │                                                   │
│  - Inicio    │                                                   │
│  - Minas     │                                                   │
│  - Mapas     │                                                   │
│  - Riesgos   │                                                   │
│  - Explorac. │                                                   │
│  - Foro      │                                                   │
│  - Informes  │                                                   │
│  - Soporte   │                                                   │
│  - Mi Cuenta │                                                   │
│              │                                                   │
├──────────────┴─────────────────────────────────────────────────┤
│  PIE DE PÁGINA (footer)                                         │
└────────────────────────────────────────────────────────────────┘
```

### 6.2 Menú Lateral (Sidebar)

El menú lateral izquierdo contiene los accesos directos a todos los módulos del sistema. Los módulos visibles dependen del rol del usuario.

### 6.3 Barra Superior (Topbar)

La barra superior muestra:
- El nombre/logo de SoftGold.
- El nombre del módulo activo.
- El nombre del usuario autenticado.
- El botón de cerrar sesión.
- El botón de alternar modo oscuro/claro.

### 6.4 Navegación en Dispositivos Móviles

En pantallas pequeñas o dispositivos móviles, el menú lateral se oculta automáticamente. Para mostrarlo, hacer clic en el ícono de menú (☰) en la barra superior.

---

## 7. PANEL PRINCIPAL (DASHBOARD)

Al iniciar sesión, el sistema muestra el **panel principal** correspondiente a su rol. Este panel presenta un resumen ejecutivo del estado actual del sistema.

---
**[CAPTURA DE PANTALLA: Panel principal del Administrador]**

---

### 7.1 Panel del Administrador

El panel del administrador muestra:
- Estadísticas generales (número de minas, usuarios, tickets abiertos).
- Accesos rápidos a los módulos más utilizados.
- Resumen de actividad reciente.

### 7.2 Panel del Minero

Muestra información relevante para el trabajador de campo:
- Mina asignada.
- Zonas de exploración activas.
- Últimas publicaciones del foro.

### 7.3 Panel del Empleado

Muestra información administrativa:
- Usuarios del área.
- Tickets de soporte pendientes.
- Accesos a documentos del sistema.

---

## 8. MÓDULO DE MINAS

Este módulo permite gestionar el catálogo de minas de la organización.

**Acceso:** Menú lateral → **Minas**  
**Ruta:** `/admin/minas`  
**Rol requerido:** Administrador

---
**[CAPTURA DE PANTALLA: Listado de minas]**

---

### 8.1 Listar Minas

Al acceder al módulo, se muestra una tabla con todas las minas registradas:

| Columna | Descripción |
|---------|-------------|
| Código | Identificador único de la mina |
| Nombre | Nombre de la unidad minera |
| Departamento | Ubicación geográfica departamental |
| Mapas asociados | Número de mapas vinculados |
| Riesgos | Riesgos identificados |
| Acciones | Botones de editar y eliminar |

### 8.2 Buscar Minas

En la parte superior de la tabla hay un campo de búsqueda para filtrar minas por **departamento**:

1. Ingresar el nombre del departamento.
2. El sistema filtra automáticamente la lista.

### 8.3 Crear una Nueva Mina

1. Hacer clic en el botón **"+ Nueva Mina"**.
2. Completar el formulario:

---
**[CAPTURA DE PANTALLA: Formulario de creación de mina]**

---

| Campo | Descripción | Obligatorio |
|-------|-------------|-------------|
| Nombre | Nombre de la mina | Sí |
| Departamento | Departamento donde se ubica | Sí |
| Mapas asociados | Seleccionar mapas del catálogo | No |
| Riesgos asociados | Seleccionar riesgos identificados | No |

3. Hacer clic en **"Guardar"**.
4. El sistema muestra el mensaje: *"Mina creada exitosamente."*

### 8.4 Editar una Mina

1. En la lista de minas, hacer clic en el ícono de edición (✏️) de la mina deseada.
2. Modificar los campos necesarios.
3. Hacer clic en **"Actualizar"**.

### 8.5 Eliminar una Mina

1. Hacer clic en el ícono de eliminación (🗑️) de la mina.
2. Confirmar la acción en el diálogo de confirmación.

> **ADVERTENCIA:** Eliminar una mina también desasociará a todos los usuarios y zonas vinculadas a ella. Esta acción no se puede deshacer.

---

## 9. MÓDULO DE MAPAS

Permite registrar y gestionar los mapas geoespaciales asociados a las minas.

**Acceso:** Menú lateral → **Mapas**  
**Ruta:** `/admin/mapas`  
**Rol requerido:** Administrador

---
**[CAPTURA DE PANTALLA: Listado de mapas con vista previa]**

---

### 9.1 Información de un Mapa

| Campo | Descripción |
|-------|-------------|
| Título | Nombre descriptivo del mapa |
| Descripción | Detalles del área cubierta |
| Latitud | Coordenada de latitud del punto central |
| Longitud | Coordenada de longitud del punto central |
| Coordenadas | Descripción textual de coordenadas |

### 9.2 Ver Mapa en Leaflet

Algunos mapas incluyen visualización interactiva mediante **Leaflet.js + OpenStreetMap**. Al hacer clic en el botón **"Ver en mapa"**, se abre la vista geoespacial donde puede:
- Hacer zoom con la rueda del ratón o con los botones +/-.
- Arrastrar el mapa para desplazarse.
- Hacer clic en marcadores para ver información.

---
**[CAPTURA DE PANTALLA: Vista del mapa interactivo con Leaflet.js]**

---

---

## 10. MÓDULO DE RIESGOS

Permite catalogar los riesgos geológicos identificados y asociarlos a las minas correspondientes.

**Acceso:** Menú lateral → **Riesgos**  
**Ruta:** `/admin/riesgos`  
**Rol requerido:** Administrador

### 10.1 Tipos de Riesgos Comunes

Los riesgos que pueden registrarse incluyen (ejemplos):
- Derrumbes o deslizamientos.
- Inundaciones subterráneas.
- Acumulación de gases tóxicos.
- Riesgo sísmico.
- Falla estructural de galerías.

### 10.2 Crear un Riesgo

1. Hacer clic en **"+ Nuevo Riesgo"**.
2. Ingresar la descripción del riesgo.
3. Hacer clic en **"Guardar"**.

### 10.3 Asociar Riesgos a Minas

Los riesgos se asocian a las minas desde el **Módulo de Minas** → Editar mina → Seleccionar riesgos.

---

## 11. MÓDULO DE ZONAS DE EXPLORACIÓN

Este módulo permite registrar, visualizar y gestionar las zonas geográficas de exploración minera con coordenadas GPS precisas.

**Acceso:** Menú lateral → **Exploración**  
**Ruta:** `/admin/exploracion`  
**Rol requerido:** Administrador

---
**[CAPTURA DE PANTALLA: Listado de zonas de exploración]**

---

### 11.1 Información de una Zona de Exploración

| Campo | Descripción |
|-------|-------------|
| Nombre | Identificación de la zona |
| Descripción | Detalles del área |
| Latitud | Coordenada GPS latitud |
| Longitud | Coordenada GPS longitud |
| Tipo | Categoría de exploración |
| Estado | Activo / Inactivo / En evaluación |
| Mina asociada | Mina a la que pertenece la zona |
| Fecha de registro | Fecha de creación del registro |

### 11.2 Crear una Zona de Exploración

1. Hacer clic en **"+ Nueva Zona"**.
2. Completar los datos del formulario, incluyendo las coordenadas GPS.
3. Seleccionar la mina a la que pertenece.
4. Hacer clic en **"Guardar"**.

### 11.3 Mapa General de Exploración

Para ver todas las zonas de exploración en el mapa interactivo:

1. En el módulo de Exploración, hacer clic en **"Ver Mapa General"**.
2. El sistema mostrará un mapa con marcadores para cada zona registrada.
3. Hacer clic en un marcador para ver la información detallada de esa zona.

---
**[CAPTURA DE PANTALLA: Mapa general de exploración con múltiples marcadores]**

---

---

## 12. MÓDULO DE FORO

El foro es el espacio de comunicación interna del sistema. Permite publicar temas, hacer consultas y responder a publicaciones de los compañeros.

**Acceso:** Menú lateral → **Foro** o desde el menú público  
**Ruta:** `/foro`  
**Rol requerido:** Acceso público (lectura y escritura)

---
**[CAPTURA DE PANTALLA: Listado del foro con categorías]**

---

### 12.1 Listar Publicaciones

La pantalla principal del foro muestra todas las publicaciones activas con:
- Título del post.
- Categoría.
- Autor.
- Fecha de publicación.
- Número de comentarios.

### 12.2 Ver una Publicación

1. Hacer clic en el título de la publicación.
2. Se mostrará el contenido completo y los comentarios.

### 12.3 Crear una Publicación

1. Hacer clic en **"+ Nueva Publicación"**.
2. Completar los campos:

| Campo | Descripción |
|-------|-------------|
| Título | Tema de la publicación |
| Contenido | Detalle del mensaje |
| Categoría | Clasificación del tema |
| Nombre del autor | Su nombre |

3. Hacer clic en **"Publicar"**.

### 12.4 Comentar en una Publicación

1. Abrir la publicación.
2. En la sección de comentarios, escribir su respuesta.
3. Ingresar su nombre como autor.
4. Hacer clic en **"Comentar"**.

### 12.5 Eliminar una Publicación

Solo los **Administradores** pueden eliminar publicaciones. Para hacerlo:
1. Abrir la publicación.
2. Hacer clic en el botón **"Eliminar"** (visible solo para administradores).

---

## 13. MÓDULO DE INFORMES Y REPORTES

Proporciona herramientas de visualización y análisis para la toma de decisiones gerenciales.

**Acceso:** Menú lateral → **Informes**  
**Ruta:** `/admin/informes`  
**Rol requerido:** Administrador

### 13.1 Dashboard Gerencial

El dashboard presenta un resumen visual de los indicadores clave:

---
**[CAPTURA DE PANTALLA: Dashboard gerencial con estadísticas]**

---

Los indicadores incluyen:
- Total de minas activas.
- Total de zonas de exploración por estado.
- Distribución de personal por rol.
- Riesgos más frecuentes.

### 13.2 Reporte de Minas

Acceso: **Informes → Reporte de Minas**

Muestra un reporte detallado con:
- Lista de todas las minas.
- Departamento de ubicación.
- Mapas y riesgos asociados.
- Personal asignado por mina.

### 13.3 Impacto Ambiental

Acceso: **Informes → Impacto Ambiental**

Presenta información sobre:
- Zonas de exploración por tipo.
- Riesgos ambientales identificados.
- Estado de las zonas activas.

### 13.4 Inventario

Acceso: **Informes → Inventario**

Listado consolidado de recursos registrados en el sistema.

---

## 14. SISTEMA DE SOPORTE

El sistema de soporte permite a los usuarios reportar problemas técnicos o solicitar asistencia.

### 14.1 Crear un Ticket de Soporte (Acceso Público)

Cualquier persona puede crear un ticket sin necesidad de iniciar sesión:

**Acceso:** `/soporte/reporte`

1. Completar el formulario:

| Campo | Descripción |
|-------|-------------|
| Nombre completo | Nombre de quien reporta |
| Correo electrónico | Email para respuesta |
| Asunto | Título del problema |
| Descripción | Detalle del inconveniente |

2. Hacer clic en **"Enviar Reporte"**.

---
**[CAPTURA DE PANTALLA: Formulario de reporte de soporte]**

---

### 14.2 Gestión de Tickets (Solo Administradores)

**Acceso:** Menú lateral → **Soporte → Tickets**

El administrador puede:
- Ver todos los tickets abiertos y cerrados.
- Ver el detalle de cada ticket.
- Cerrar un ticket cuando el problema sea resuelto.

| Estado | Descripción |
|--------|-------------|
| **OPEN** | Ticket abierto, pendiente de atención |
| **CLOSED** | Ticket resuelto y cerrado |

---
**[CAPTURA DE PANTALLA: Lista de tickets de soporte]**

---

---

## 15. BÚSQUEDA Y FILTROS AVANZADOS

El módulo de filtros permite realizar búsquedas cruzadas en el sistema.

**Acceso:** Menú lateral → **Filtros**  
**Ruta:** `/admin/filtros/buscar`  
**Rol requerido:** Administrador

### 15.1 Criterios de Búsqueda

Puede buscar utilizando los siguientes criterios:

| Criterio | Ejemplo |
|---------|---------|
| Nombre de mina | "La Colosa" |
| Departamento | "Tolima" |
| Estado de zona | Activo / Inactivo |
| Tipo de zona de exploración | Primaria / Secundaria |

### 15.2 Realizar una Búsqueda

1. Ingresar los criterios de búsqueda en los campos disponibles.
2. Hacer clic en **"Buscar"** o presionar `Enter`.
3. Los resultados se mostrarán en la tabla inferior.

---
**[CAPTURA DE PANTALLA: Módulo de filtros con resultados]**

---

---

## 16. GESTIÓN DE USUARIOS

Permite al administrador crear, editar y gestionar los usuarios del sistema.

**Acceso:** Menú lateral → **Usuarios**  
**Rol requerido:** Administrador

### 16.1 Tipos de Gestión de Usuarios

| Sección | Descripción |
|---------|-------------|
| Lista general de usuarios | Todos los usuarios del sistema |
| Gestión de Mineros | CRUD específico para usuarios con rol MINERO |
| Gestión de Empleados | CRUD específico para usuarios con rol EMPLEADO |

### 16.2 Crear un Usuario

1. Seleccionar el tipo de usuario a crear (Minero o Empleado).
2. Hacer clic en **"+ Nuevo Usuario"**.
3. Completar el formulario:

| Campo | Obligatorio | Descripción |
|-------|-------------|-------------|
| Tipo de documento | Sí | CC, CE, TI, NIT |
| Número de documento | Sí | Número único |
| Primer nombre | Sí | - |
| Segundo nombre | No | - |
| Primer apellido | Sí | - |
| Segundo apellido | No | - |
| Correo electrónico | Sí | Email único de acceso |
| Contraseña | Sí | Min. 8 caracteres, 1 mayúscula, 1 número |
| Teléfono | No | - |
| Mina asignada | No | Para mineros |
| Tipo de empleado | No | Para empleados |
| Área | No | Para empleados |

4. Hacer clic en **"Guardar"**.

---
**[CAPTURA DE PANTALLA: Formulario de creación de usuario]**

---

### 16.3 Editar un Usuario

1. En la lista de usuarios, hacer clic en el ícono de edición.
2. Modificar los campos necesarios.
3. Hacer clic en **"Actualizar"**.

### 16.4 Eliminar un Usuario

1. Hacer clic en el ícono de eliminación del usuario.
2. Confirmar la acción.

> **NOTA:** Eliminar un usuario es irreversible. Asegúrese de que el usuario ya no requiere acceso al sistema antes de proceder.

---

## 17. DOCUMENTOS DEL SISTEMA

SoftGold incluye documentos estandarizados para la gestión operativa:

**Acceso:** Menú lateral → **Documentos**

| Documento | Descripción |
|-----------|-------------|
| **Guía de Seguridad** | Protocolo de seguridad para el trabajo en minas |
| **Reporte de Incidente** | Formulario para reportar incidentes operativos |
| **Formulario de Satisfacción** | Encuesta de satisfacción del servicio |
| **Control de Cambios** | Registro de cambios en operaciones |
| **Mejora Continua** | Formato para propuestas de mejora |
| **Niveles de Servicio (SLA)** | Acuerdos de nivel de servicio |

Cada documento puede ser visualizado directamente en el navegador y está disponible para impresión o descarga.

---

## 18. MI CUENTA (PERFIL)

Cada usuario puede gestionar su información personal desde el menú **"Mi Cuenta"**.

**Acceso:** Menú lateral → **Mi Cuenta**

### 18.1 Ver Perfil

---
**[CAPTURA DE PANTALLA: Vista del perfil de usuario]**

---

En esta sección puede visualizar:
- Nombre completo.
- Correo electrónico.
- Tipo de documento y número.
- Teléfono.
- Mina asignada (si aplica).
- Rol en el sistema.

### 18.2 Cambiar Contraseña

1. Dentro de **Mi Cuenta**, hacer clic en **"Cambiar Contraseña"**.
2. Completar el formulario:

| Campo | Descripción |
|-------|-------------|
| Contraseña actual | Su contraseña vigente |
| Nueva contraseña | Mínimo 8 caracteres, 1 mayúscula, 1 número |
| Confirmar nueva contraseña | Repetir la nueva contraseña |

3. Hacer clic en **"Actualizar Contraseña"**.

---
**[CAPTURA DE PANTALLA: Formulario de cambio de contraseña]**

---

---

## 19. ATAJOS DE TECLADO

SoftGold incluye atajos de teclado para mejorar la productividad:

| Atajo | Acción |
|-------|--------|
| `Ctrl + B` | Ir al campo de búsqueda |
| `Ctrl + /` | Abrir el panel de atajos de teclado |

Para ver el listado completo de atajos, presionar `Ctrl + /` en cualquier pantalla.

---
**[CAPTURA DE PANTALLA: Modal de atajos de teclado]**

---

---

## 20. MODO OSCURO / CLARO

SoftGold soporta dos temas visuales:

- **Modo claro:** Fondo blanco, texto oscuro. Ideal para ambientes bien iluminados.
- **Modo oscuro:** Fondo oscuro, texto claro. Reduce la fatiga visual en ambientes de poca luz.

### Cambiar el Tema

1. Localizar el ícono de sol/luna en la barra superior o en el sidebar.
2. Hacer clic para alternar entre los modos.

La preferencia se guarda automáticamente en su navegador y se mantiene en futuras sesiones.

---

## 21. MENSAJES DE ERROR FRECUENTES

| Mensaje | Causa | Solución |
|---------|-------|---------|
| *"Correo o contraseña incorrectos"* | Credenciales inválidas | Verificar email y contraseña |
| *"Su cuenta ha sido bloqueada"* | 3 o más intentos fallidos | Contactar al administrador |
| *"El email ya está registrado"* | Email duplicado en registro | Usar otro correo o iniciar sesión |
| *"La cédula ya existe"* | Documento duplicado | Verificar si el usuario ya existe |
| *"La contraseña debe tener al menos 8 caracteres, una mayúscula y un número"* | Formato inválido | Crear contraseña más fuerte |
| *"El campo es obligatorio"* | Campo vacío en formulario | Completar todos los campos requeridos |
| *"Sesión expirada"* | Inactividad de 15 minutos | Iniciar sesión nuevamente |
| *"No tiene permisos para acceder a esta página"* | Rol insuficiente | Contactar al administrador |
| *"Error al enviar el correo"* | Problema con servidor SMTP | Contactar soporte técnico |
| *"Recurso no encontrado"* | URL inválida o recurso eliminado | Verificar la URL o volver al inicio |

---

## 22. PREGUNTAS FRECUENTES

**¿Puedo usar SoftGold desde mi celular?**  
Sí. La interfaz es responsive y se adapta a pantallas móviles. En pantallas pequeñas, el menú lateral se oculta y se accede mediante el botón de menú (☰).

**¿Qué hago si olvidé mi contraseña?**  
Use la opción "¿Olvidaste tu contraseña?" en la pantalla de inicio de sesión. Recibirá un correo con un enlace para restablecerla.

**¿Puedo registrarme yo mismo en el sistema?**  
Depende de la configuración del administrador. Si el registro público está habilitado, puede acceder a `/registro`. De lo contrario, el administrador debe crear su cuenta.

**¿Mis datos están seguros?**  
Sí. Las contraseñas se almacenan cifradas (nunca en texto plano). El acceso al sistema requiere autenticación y el sistema cierra la sesión automáticamente después de 15 minutos de inactividad.

**¿Puedo exportar los reportes a PDF o Excel?**  
Actualmente el sistema no tiene exportación nativa. Sin embargo, puede usar la función de impresión del navegador (Ctrl+P) para guardar cualquier vista como PDF.

**¿Cómo puedo ver en qué mina estoy registrado?**  
Acceder a **Mi Cuenta** desde el menú lateral. La mina asignada se muestra en la información del perfil.

**¿Qué diferencia hay entre los tipos de usuario?**  
- **Administrador:** Acceso total al sistema, gestión de todos los módulos.
- **Minero:** Acceso a módulos operativos y zonas de exploración.
- **Empleado:** Acceso a módulos administrativos del área.
- **Usuario:** Acceso de consulta y visualización.

**¿Cómo reporto un problema técnico?**  
Use el formulario de soporte disponible en `/soporte/reporte` o en el menú lateral bajo "Soporte".

**¿El sistema funciona sin internet?**  
No. SoftGold requiere conexión a la red donde está alojado el servidor. Los mapas de Leaflet.js también requieren acceso a internet para cargar los tiles de OpenStreetMap.

**¿Puedo tener más de un rol en el sistema?**  
Técnicamente el sistema soporta múltiples roles por usuario. Sin embargo, la asignación de roles la realiza el administrador.

---

## 23. CIERRE DE SESIÓN

Para cerrar su sesión de manera segura:

**Opción 1:** Hacer clic en el botón **"Cerrar Sesión"** en la barra superior o en la parte inferior del menú lateral.

**Opción 2:** El sistema cerrará la sesión automáticamente después de **15 minutos de inactividad**.

> **RECOMENDACIÓN:** Siempre cierre su sesión manualmente cuando termine de usar el sistema, especialmente en equipos compartidos. Nunca deje el sistema abierto sin supervisión.

Después de cerrar sesión, el sistema lo redirigirá a la página de inicio de sesión.

---

## RECOMENDACIONES GENERALES DE USO

1. **Use contraseñas fuertes:** Al menos 8 caracteres con mayúsculas y números.
2. **No comparta sus credenciales:** Cada usuario debe tener su propio acceso.
3. **Cierre sesión al terminar:** Especialmente en equipos compartidos.
4. **Reporte problemas inmediatamente:** Use el sistema de soporte para notificar errores.
5. **Mantenga actualizado su perfil:** Revise que su información de contacto esté vigente.
6. **Use Chrome o Edge:** Para la mejor experiencia visual y de rendimiento.
7. **Active el modo oscuro:** Si trabaja en ambientes con poca iluminación.

---

*Fin del Manual de Usuario del Sistema SoftGold — Versión 1.0.0*

# MANUAL DE IMPLEMENTACIÓN Y DESPLIEGUE DEL SISTEMA SOFTGOLD

---

**Versión:** 1.0.0  
**Fecha:** Mayo 2026  
**Estado:** Oficial  
**Dirigido a:** Equipo DevOps / Administradores de Sistemas  
**Clasificación:** Confidencial – Uso técnico interno  

---

## TABLA DE CONTENIDOS

1. Introducción  
2. Requisitos del Servidor  
3. Preparación del Entorno  
4. Instalación de Java y Maven  
5. Instalación y Configuración de MySQL  
6. Instalación del Backend (SoftGold)  
7. Configuración de Variables de Entorno  
8. Configuración de la Base de Datos  
9. Configuración del Servidor Web (Nginx)  
10. Configuración SSL/TLS  
11. Configuración Docker  
12. Configuración CI/CD  
13. Proceso de Despliegue  
14. Configuración de Backups  
15. Monitoreo del Sistema  
16. Validación Post-Despliegue  
17. Checklist de Implementación  
18. Procedimiento de Actualización  
19. Procedimiento de Rollback  
20. Problemas Comunes y Soluciones  

---

## 1. INTRODUCCIÓN

Este documento describe los procedimientos técnicos necesarios para instalar, configurar y desplegar el sistema SoftGold en entornos de desarrollo, pruebas (QA) y producción.

SoftGold es una aplicación Java/Spring Boot que se ejecuta como un proceso autónomo en el puerto 9090, con dependencia de una base de datos MySQL 8.x. El sistema no requiere un servidor de aplicaciones externo (Tomcat se incluye embebido en Spring Boot).

### 1.1 Ambientes de Despliegue

| Ambiente | Propósito | URL típica |
|----------|-----------|-----------|
| **Desarrollo (DEV)** | Pruebas locales del desarrollador | http://localhost:9090 |
| **Pruebas (QA)** | Validación y control de calidad | http://qa.softgold.empresa.com |
| **Producción (PROD)** | Sistema en uso real | https://softgold.empresa.com |

### 1.2 Estrategia de Configuración

Cada ambiente mantiene su propio archivo de propiedades, que sobreescribe los valores del `application.properties` base mediante perfiles de Spring:

```
application.properties          ← Configuración base común
application-dev.properties      ← Sobrescrituras para desarrollo
application-qa.properties       ← Sobrescrituras para QA
application-prod.properties     ← Sobrescrituras para producción
```

---

## 2. REQUISITOS DEL SERVIDOR

### 2.1 Requisitos de Hardware (Producción)

| Componente | Mínimo | Recomendado |
|-----------|--------|-------------|
| CPU | 2 núcleos / 2.0 GHz | 4 núcleos / 3.0 GHz |
| Memoria RAM | 4 GB | 8 GB |
| Almacenamiento | 40 GB SSD | 100 GB SSD |
| Ancho de banda | 10 Mbps | 100 Mbps |

### 2.2 Requisitos de Software del Servidor

| Software | Versión | Propósito |
|---------|---------|-----------|
| Sistema operativo | Ubuntu 22.04 LTS / Debian 12 / CentOS 8 | SO base |
| Java (JDK) | 17 LTS (OpenJDK o Eclipse Temurin) | Ejecución de la aplicación |
| MySQL | 8.0+ | Base de datos |
| Nginx | 1.24+ | Proxy inverso / SSL termination |
| Maven | 3.8+ | Build del proyecto (si se compila en servidor) |
| Docker | 24+ (opcional) | Contenedores |
| Docker Compose | 2.x (opcional) | Orquestación de contenedores |

### 2.3 Puertos Requeridos

| Puerto | Protocolo | Servicio |
|--------|-----------|---------|
| 80 | TCP | HTTP (Nginx → redirige a HTTPS) |
| 443 | TCP | HTTPS (Nginx SSL) |
| 9090 | TCP | SoftGold (interno, no exponer al exterior) |
| 3306 | TCP | MySQL (solo acceso local) |

> **SEGURIDAD:** El puerto 9090 de SoftGold y el 3306 de MySQL NO deben estar expuestos directamente a internet. Solo Nginx (puertos 80/443) debe estar visible externamente.

---

## 3. PREPARACIÓN DEL ENTORNO

### 3.1 Actualizar el Sistema Operativo

```bash
# Ubuntu/Debian
sudo apt update && sudo apt upgrade -y

# CentOS/RHEL
sudo dnf update -y
```

### 3.2 Crear Usuario de Servicio

```bash
# Crear usuario dedicado para SoftGold (sin shell de login)
sudo useradd -r -s /bin/false -d /opt/softgold softgold

# Crear directorio de instalación
sudo mkdir -p /opt/softgold
sudo chown softgold:softgold /opt/softgold

# Crear directorio de logs
sudo mkdir -p /var/log/softgold
sudo chown softgold:softgold /var/log/softgold
```

### 3.3 Configurar Firewall

```bash
# Ubuntu con UFW
sudo ufw allow 22/tcp    # SSH
sudo ufw allow 80/tcp    # HTTP
sudo ufw allow 443/tcp   # HTTPS
sudo ufw enable

# Verificar reglas
sudo ufw status
```

---

## 4. INSTALACIÓN DE JAVA Y MAVEN

### 4.1 Instalar OpenJDK 17

```bash
# Ubuntu/Debian
sudo apt install -y openjdk-17-jdk

# CentOS/RHEL
sudo dnf install -y java-17-openjdk-devel

# Verificar instalación
java -version
# Salida esperada: openjdk version "17.x.x" ...

javac -version
# Salida esperada: javac 17.x.x
```

### 4.2 Configurar JAVA_HOME

```bash
# Agregar a /etc/profile.d/java.sh
sudo bash -c 'cat > /etc/profile.d/java.sh << EOF
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
export PATH=$PATH:$JAVA_HOME/bin
EOF'

source /etc/profile.d/java.sh
echo $JAVA_HOME
```

### 4.3 Instalar Maven (para compilación en servidor)

```bash
# Ubuntu/Debian
sudo apt install -y maven

# Verificar
mvn -version
# Apache Maven 3.x.x
```

> **ALTERNATIVA:** Si prefiere compilar en un servidor de CI/CD y solo desplegar el JAR, Maven no necesita instalarse en el servidor de producción.

---

## 5. INSTALACIÓN Y CONFIGURACIÓN DE MySQL

### 5.1 Instalar MySQL 8

```bash
# Ubuntu/Debian
sudo apt install -y mysql-server

# CentOS/RHEL
sudo dnf install -y mysql-server

# Iniciar y habilitar el servicio
sudo systemctl start mysql
sudo systemctl enable mysql

# Verificar estado
sudo systemctl status mysql
```

### 5.2 Asegurar la Instalación de MySQL

```bash
sudo mysql_secure_installation
```

Responda las preguntas:
- `Validate password plugin:` YES
- Nivel de contraseña: 2 (STRONG)
- `Remove anonymous users:` YES
- `Disallow root login remotely:` YES
- `Remove test database:` YES
- `Reload privilege tables:` YES

### 5.3 Crear Base de Datos y Usuario para SoftGold

```sql
-- Acceder a MySQL como root
sudo mysql -u root -p

-- Crear la base de datos
CREATE DATABASE softgold
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Crear usuario dedicado para la aplicación
CREATE USER 'softgold_app'@'localhost' IDENTIFIED BY 'ContraseñaSegura2026!';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON softgold.* TO 'softgold_app'@'localhost';
FLUSH PRIVILEGES;

-- Verificar
SHOW GRANTS FOR 'softgold_app'@'localhost';

EXIT;
```

### 5.4 Verificar Configuración de MySQL

```bash
# Probar conexión con el nuevo usuario
mysql -u softgold_app -p softgold
# Ingresar la contraseña cuando se solicite
# Salida esperada: Welcome to the MySQL monitor...
```

---

## 6. INSTALACIÓN DEL BACKEND (SOFTGOLD)

### 6.1 Compilar el Proyecto (en servidor de CI o local)

```bash
# Clonar el repositorio
git clone <url-repositorio> softgold_final
cd softgold_final

# Compilar y generar el JAR
./mvnw clean package -DskipTests

# El artefacto se genera en:
# target/softgold-0.0.1-SNAPSHOT.jar
```

### 6.2 Transferir el JAR al Servidor

```bash
# Desde el equipo de desarrollo (o CI/CD)
scp target/softgold-0.0.1-SNAPSHOT.jar \
    usuario@servidor:/opt/softgold/softgold.jar
```

### 6.3 Verificar el JAR

```bash
# En el servidor
ls -lh /opt/softgold/softgold.jar
# Debe mostrar el archivo con su tamaño

# Verificar que es un JAR ejecutable válido
java -jar /opt/softgold/softgold.jar --version 2>&1 | head -5
```

---

## 7. CONFIGURACIÓN DE VARIABLES DE ENTORNO

### 7.1 Archivo de Variables de Entorno

Crear el archivo `/opt/softgold/softgold.env` con las configuraciones específicas del ambiente:

```bash
sudo nano /opt/softgold/softgold.env
```

Contenido del archivo:

```bash
# Puerto del servidor
SERVER_PORT=9090

# Base de datos
DB_URL=jdbc:mysql://localhost:3306/softgold?serverTimezone=America/Bogota&createDatabaseIfNotExist=true
DB_USERNAME=softgold_app
DB_PASSWORD=ContraseñaSegura2026!

# Correo SMTP
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=correo@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx

# Perfil de Spring
SPRING_PROFILES_ACTIVE=prod

# Logging
LOG_LEVEL=INFO
LOG_FILE=/var/log/softgold/softgold.log
```

```bash
# Asegurar permisos restrictivos (solo root y el servicio pueden leerlo)
sudo chmod 600 /opt/softgold/softgold.env
sudo chown root:softgold /opt/softgold/softgold.env
```

### 7.2 Archivo application-prod.properties

Crear el archivo de configuración de producción en `/opt/softgold/config/application-prod.properties`:

```properties
# Puerto
server.port=${SERVER_PORT:9090}

# Base de datos
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA (producción: no auto-modificar esquema)
spring.jpa.hibernate.ddl-auto=validate

# Mail
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Logging (producción)
logging.level.root=WARN
logging.level.com.proyectoL.softgold=INFO
logging.file.name=${LOG_FILE:/var/log/softgold/softgold.log}
logging.file.max-size=50MB
logging.file.max-history=30

# Sesión
server.servlet.session.timeout=15m
```

> **IMPORTANTE:** En producción, cambiar `spring.jpa.hibernate.ddl-auto` de `update` a `validate`. Esto previene modificaciones accidentales al esquema de la base de datos.

---

## 8. CONFIGURACIÓN DE LA BASE DE DATOS

### 8.1 Migraciones y Esquema Inicial

Hibernate con `ddl-auto=update` crea automáticamente las tablas en el primer arranque. Para inicializar el sistema:

**Primera vez (DEV/QA):** Ejecutar la aplicación con `ddl-auto=update` para crear el esquema.

**Producción:** El esquema debe crearse manualmente o mediante un script SQL antes del primer arranque.

### 8.2 Script SQL de Creación del Esquema

```sql
-- Esquema de SoftGold (referencia)
-- Ejecutar en MySQL como softgold_app

USE softgold;

CREATE TABLE IF NOT EXISTS roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS minas (
  cod_mina BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  departamento VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS mapas (
  codigo_mapa BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255),
  descripcion TEXT,
  latitud DOUBLE,
  longitud DOUBLE,
  coordenadas TEXT
);

CREATE TABLE IF NOT EXISTS riesgos (
  cod_riesgo BIGINT AUTO_INCREMENT PRIMARY KEY,
  descripcion TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  cedula VARCHAR(50) NOT NULL UNIQUE,
  tipo_documento VARCHAR(10) NOT NULL,
  nombre_1 VARCHAR(100) NOT NULL,
  nombre_2 VARCHAR(100),
  apellido_1 VARCHAR(100) NOT NULL,
  apellido_2 VARCHAR(100),
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  tipo_usuario VARCHAR(50) NOT NULL,
  telefono VARCHAR(20),
  tipo_empleado VARCHAR(100),
  area VARCHAR(100),
  intentos_fallidos INT NOT NULL DEFAULT 0,
  bloqueado BOOLEAN NOT NULL DEFAULT FALSE,
  tiempo_bloqueo DATETIME,
  cod_mina BIGINT,
  FOREIGN KEY (cod_mina) REFERENCES minas(cod_mina) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS usuario_roles (
  usuario_id BIGINT NOT NULL,
  rol_id BIGINT NOT NULL,
  PRIMARY KEY (usuario_id, rol_id),
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
  FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS mina_mapas (
  mina_cod_mina BIGINT NOT NULL,
  mapas_codigo_mapa BIGINT NOT NULL,
  PRIMARY KEY (mina_cod_mina, mapas_codigo_mapa),
  FOREIGN KEY (mina_cod_mina) REFERENCES minas(cod_mina),
  FOREIGN KEY (mapas_codigo_mapa) REFERENCES mapas(codigo_mapa)
);

CREATE TABLE IF NOT EXISTS mina_riesgos (
  mina_cod_mina BIGINT NOT NULL,
  riesgos_cod_riesgo BIGINT NOT NULL,
  PRIMARY KEY (mina_cod_mina, riesgos_cod_riesgo),
  FOREIGN KEY (mina_cod_mina) REFERENCES minas(cod_mina),
  FOREIGN KEY (riesgos_cod_riesgo) REFERENCES riesgos(cod_riesgo)
);

CREATE TABLE IF NOT EXISTS zona_exploracion (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  descripcion TEXT,
  latitud DOUBLE,
  longitud DOUBLE,
  tipo VARCHAR(100),
  estado VARCHAR(50),
  fecha_registro DATETIME,
  mina_id BIGINT,
  FOREIGN KEY (mina_id) REFERENCES minas(cod_mina) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS forum_posts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(500) NOT NULL,
  contenido TEXT NOT NULL,
  categoria VARCHAR(100),
  autor_nombre VARCHAR(255),
  fecha_creacion DATETIME,
  activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS forum_comments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  contenido TEXT NOT NULL,
  autor_nombre VARCHAR(255),
  fecha_creacion DATETIME,
  post_id BIGINT,
  FOREIGN KEY (post_id) REFERENCES forum_posts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS support_tickets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  asunto VARCHAR(500) NOT NULL,
  descripcion TEXT NOT NULL,
  creado DATETIME NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(255) NOT NULL UNIQUE,
  usuario_id BIGINT NOT NULL,
  fecha_expiracion DATETIME NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Insertar roles iniciales
INSERT IGNORE INTO roles (nombre) VALUES
  ('ROLE_ADMINISTRADOR'),
  ('ROLE_MINERO'),
  ('ROLE_EMPLEADO'),
  ('ROLE_USUARIO');
```

### 8.3 Datos Iniciales del Administrador

El `DataInitializer.java` crea automáticamente el administrador al arrancar. Si necesita hacerlo manualmente:

```sql
-- Contraseña: admin123 (hash BCrypt)
-- IMPORTANTE: Cambiar en producción
INSERT INTO usuarios (cedula, tipo_documento, nombre_1, apellido_1, email, password, tipo_usuario, intentos_fallidos, bloqueado)
VALUES ('0000000000', 'CC', 'Admin', 'SoftGold', 'admin@softgold.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'ADMINISTRADOR', 0, false);

-- Asignar rol administrador (ajustar IDs según corresponda)
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id FROM usuarios u, roles r
WHERE u.email = 'admin@softgold.com' AND r.nombre = 'ROLE_ADMINISTRADOR';
```

---

## 9. CONFIGURACIÓN DEL SERVIDOR WEB (NGINX)

Nginx actúa como proxy inverso, redirigiendo el tráfico externo al puerto 9090 de SoftGold.

### 9.1 Instalar Nginx

```bash
# Ubuntu/Debian
sudo apt install -y nginx

# Iniciar y habilitar
sudo systemctl start nginx
sudo systemctl enable nginx
```

### 9.2 Configuración de Nginx (HTTP)

```bash
sudo nano /etc/nginx/sites-available/softgold
```

```nginx
server {
    listen 80;
    server_name softgold.empresa.com;

    # Redirigir HTTP a HTTPS (activar después de configurar SSL)
    # return 301 https://$host$request_uri;

    # Configuración temporal sin SSL
    location / {
        proxy_pass http://localhost:9090;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_connect_timeout 60s;
        proxy_read_timeout 60s;
        proxy_send_timeout 60s;
        client_max_body_size 10M;
    }

    # Logs
    access_log /var/log/nginx/softgold_access.log;
    error_log /var/log/nginx/softgold_error.log;
}
```

```bash
# Activar el sitio
sudo ln -s /etc/nginx/sites-available/softgold /etc/nginx/sites-enabled/

# Verificar configuración
sudo nginx -t

# Recargar Nginx
sudo systemctl reload nginx
```

---

## 10. CONFIGURACIÓN SSL/TLS

### 10.1 Instalar Certbot (Let's Encrypt — Gratuito)

```bash
sudo apt install -y certbot python3-certbot-nginx

# Obtener certificado SSL
sudo certbot --nginx -d softgold.empresa.com

# Verificar renovación automática
sudo certbot renew --dry-run
```

### 10.2 Configuración Nginx con SSL

Después de que Certbot configure SSL automáticamente, verificar que el archivo `/etc/nginx/sites-available/softgold` contenga:

```nginx
server {
    listen 443 ssl;
    server_name softgold.empresa.com;

    ssl_certificate /etc/letsencrypt/live/softgold.empresa.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/softgold.empresa.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4:!DHE;
    ssl_prefer_server_ciphers on;

    location / {
        proxy_pass http://localhost:9090;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto https;
        proxy_connect_timeout 60s;
        proxy_read_timeout 60s;
        client_max_body_size 10M;
    }

    access_log /var/log/nginx/softgold_access.log;
    error_log /var/log/nginx/softgold_error.log;
}

# Redirección HTTP → HTTPS
server {
    listen 80;
    server_name softgold.empresa.com;
    return 301 https://$host$request_uri;
}
```

---

## 11. CONFIGURACIÓN DOCKER

Si prefiere desplegar SoftGold usando contenedores Docker:

### 11.1 Dockerfile

```dockerfile
# Dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/softgold-0.0.1-SNAPSHOT.jar softgold.jar

EXPOSE 9090

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "softgold.jar"]
```

### 11.2 Docker Compose

```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: softgold-mysql
    restart: unless-stopped
    environment:
      MYSQL_DATABASE: softgold
      MYSQL_USER: softgold_app
      MYSQL_PASSWORD: ${DB_PASSWORD}
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    networks:
      - softgold-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 10s
      retries: 5

  softgold:
    build: .
    container_name: softgold-app
    restart: unless-stopped
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/softgold?serverTimezone=America/Bogota
      SPRING_DATASOURCE_USERNAME: softgold_app
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      SPRING_MAIL_USERNAME: ${MAIL_USERNAME}
      SPRING_MAIL_PASSWORD: ${MAIL_PASSWORD}
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "9090:9090"
    networks:
      - softgold-network
    volumes:
      - softgold_logs:/var/log/softgold

networks:
  softgold-network:
    driver: bridge

volumes:
  mysql_data:
  softgold_logs:
```

### 11.3 Archivo .env para Docker Compose

```bash
# .env (no commitear a git)
DB_PASSWORD=ContraseñaSegura2026!
MYSQL_ROOT_PASSWORD=RootPasswordSeguro2026!
MAIL_USERNAME=correo@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx
```

### 11.4 Comandos Docker

```bash
# Construir y levantar
docker compose up -d --build

# Ver logs
docker compose logs -f softgold

# Detener
docker compose down

# Detener y eliminar volúmenes (cuidado en producción)
docker compose down -v
```

---

## 12. CONFIGURACIÓN CI/CD

### 12.1 Pipeline GitHub Actions (Ejemplo)

```yaml
# .github/workflows/deploy.yml
name: Build and Deploy SoftGold

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Cache Maven packages
        uses: actions/cache@v3
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
      
      - name: Build with Maven
        run: ./mvnw clean package -DskipTests
      
      - name: Upload JAR
        uses: actions/upload-artifact@v4
        with:
          name: softgold-jar
          path: target/softgold-0.0.1-SNAPSHOT.jar

  deploy:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - name: Download JAR
        uses: actions/download-artifact@v4
        with:
          name: softgold-jar
      
      - name: Deploy to server
        uses: appleboy/scp-action@master
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          source: "softgold-0.0.1-SNAPSHOT.jar"
          target: "/opt/softgold/"
      
      - name: Restart service
        uses: appleboy/ssh-action@master
        with:
          host: ${{ secrets.SERVER_HOST }}
          username: ${{ secrets.SERVER_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          script: |
            cp /opt/softgold/softgold-0.0.1-SNAPSHOT.jar /opt/softgold/softgold.jar
            sudo systemctl restart softgold
            sleep 15
            systemctl is-active softgold
```

---

## 13. PROCESO DE DESPLIEGUE

### 13.1 Configurar SoftGold como Servicio systemd

```bash
sudo nano /etc/systemd/system/softgold.service
```

```ini
[Unit]
Description=SoftGold - Sistema de Gestión Minera
Documentation=https://softgold.empresa.com/docs
After=network.target mysql.service
Wants=mysql.service

[Service]
Type=simple
User=softgold
Group=softgold
WorkingDirectory=/opt/softgold

# Cargar variables de entorno
EnvironmentFile=/opt/softgold/softgold.env

# Comando de arranque
ExecStart=/usr/bin/java \
  -Xms256m \
  -Xmx512m \
  -jar /opt/softgold/softgold.jar \
  --spring.config.additional-location=/opt/softgold/config/

# Reinicio automático ante fallos
Restart=on-failure
RestartSec=15
StartLimitIntervalSec=60
StartLimitBurst=3

# Estándar de salida
StandardOutput=append:/var/log/softgold/softgold.log
StandardError=append:/var/log/softgold/softgold.log

# Seguridad del proceso
NoNewPrivileges=yes
PrivateTmp=yes

[Install]
WantedBy=multi-user.target
```

```bash
# Activar e iniciar el servicio
sudo systemctl daemon-reload
sudo systemctl enable softgold
sudo systemctl start softgold

# Verificar estado
sudo systemctl status softgold
```

### 13.2 Verificar el Arranque

```bash
# Ver los últimos logs de arranque
sudo journalctl -u softgold -n 50 --no-pager

# El arranque exitoso mostrará algo similar a:
# Tomcat started on port 9090 (http) with context path ''
# Started SoftgoldApplication in X.XXX seconds
```

### 13.3 Probar la Aplicación

```bash
# Prueba de conectividad local
curl -I http://localhost:9090/login

# Respuesta esperada:
# HTTP/1.1 200
```

---

## 14. CONFIGURACIÓN DE BACKUPS

### 14.1 Script de Backup de Base de Datos

```bash
sudo nano /opt/softgold/scripts/backup_db.sh
```

```bash
#!/bin/bash
# backup_db.sh — Backup diario de la base de datos SoftGold

BACKUP_DIR="/var/backups/softgold"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/softgold_$DATE.sql.gz"
RETENTION_DAYS=30

# Cargar variables
source /opt/softgold/softgold.env

# Crear directorio si no existe
mkdir -p "$BACKUP_DIR"

# Realizar backup con compresión
mysqldump \
  -u "$DB_USERNAME" \
  -p"$DB_PASSWORD" \
  --single-transaction \
  --routines \
  --triggers \
  softgold | gzip > "$BACKUP_FILE"

# Verificar éxito
if [ $? -eq 0 ]; then
    echo "$(date): Backup exitoso: $BACKUP_FILE" >> /var/log/softgold/backup.log
else
    echo "$(date): ERROR en backup" >> /var/log/softgold/backup.log
fi

# Eliminar backups más antiguos que RETENTION_DAYS días
find "$BACKUP_DIR" -name "softgold_*.sql.gz" -mtime +"$RETENTION_DAYS" -delete
```

```bash
# Dar permisos de ejecución
chmod +x /opt/softgold/scripts/backup_db.sh

# Programar con cron (backup diario a las 2:00 AM)
sudo crontab -e -u softgold
```

Agregar la línea:
```
0 2 * * * /opt/softgold/scripts/backup_db.sh
```

### 14.2 Script de Backup del JAR

```bash
#!/bin/bash
# backup_jar.sh — Backup del JAR antes de actualizar

cp /opt/softgold/softgold.jar \
   /opt/softgold/backups/softgold_$(date +%Y%m%d_%H%M%S).jar
```

### 14.3 Estrategia de Retención

| Tipo de Backup | Frecuencia | Retención |
|---------------|-----------|----------|
| Base de datos | Diario | 30 días |
| Base de datos (semanal) | Semanal | 3 meses |
| JAR de la aplicación | Por despliegue | Últimas 5 versiones |
| Configuración | Por cambio | Indefinida (en Git) |

---

## 15. MONITOREO DEL SISTEMA

### 15.1 Estado del Servicio

```bash
# Estado general
sudo systemctl status softgold

# Logs en tiempo real
sudo journalctl -u softgold -f

# Últimas 100 líneas del log
tail -100 /var/log/softgold/softgold.log
```

### 15.2 Monitoreo de Recursos

```bash
# Uso de CPU y memoria por el proceso
ps aux | grep softgold

# Memoria del sistema
free -h

# Espacio en disco
df -h

# Conexiones activas al puerto 9090
ss -tlnp | grep 9090
```

### 15.3 Monitoreo de la Base de Datos

```sql
-- Conexiones activas
SHOW PROCESSLIST;

-- Tamaño de la base de datos
SELECT 
  table_schema AS 'Base de Datos',
  ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Tamaño (MB)'
FROM information_schema.tables
WHERE table_schema = 'softgold'
GROUP BY table_schema;
```

### 15.4 Script de Monitoreo de Salud

```bash
#!/bin/bash
# health_check.sh

URL="http://localhost:9090/login"
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$URL")

if [ "$HTTP_STATUS" != "200" ]; then
    echo "$(date): ALERTA - SoftGold no responde (HTTP $HTTP_STATUS)" \
    >> /var/log/softgold/health.log
    # Opcional: reiniciar el servicio
    sudo systemctl restart softgold
fi
```

```
# Cron: verificar cada 5 minutos
*/5 * * * * /opt/softgold/scripts/health_check.sh
```

---

## 16. VALIDACIÓN POST-DESPLIEGUE

Después de cada despliegue, ejecutar las siguientes verificaciones:

### 16.1 Verificaciones Técnicas

```bash
# 1. Servicio activo
systemctl is-active softgold
# Esperado: active

# 2. Puerto escuchando
ss -tlnp | grep 9090
# Esperado: LISTEN ... :9090

# 3. Respuesta HTTP
curl -s -o /dev/null -w "%{http_code}" http://localhost:9090/login
# Esperado: 200

# 4. Sin errores críticos en logs
grep -c "ERROR" /var/log/softgold/softgold.log
# Idealmente: 0

# 5. Conexión a base de datos (debe aparecer en logs de arranque)
grep "HikariPool" /var/log/softgold/softgold.log | tail -5
```

### 16.2 Verificaciones Funcionales

| Verificación | Pasos | Resultado Esperado |
|-------------|-------|-------------------|
| Login | Acceder a `/login` e ingresar con admin | Redirige al dashboard |
| Listado de minas | Acceder a `/admin/minas` | Muestra tabla de minas |
| Creación de entidad | Crear una mina de prueba | Mensaje de éxito |
| Foro | Acceder a `/foro` | Carga lista de posts |
| Mapas | Acceder a un mapa Leaflet | Mapa interactivo visible |
| Correo | Usar recuperación de contraseña | Email recibido |
| Logout | Cerrar sesión | Redirige a login |

---

## 17. CHECKLIST DE IMPLEMENTACIÓN

### 17.1 Checklist Pre-Despliegue

- [ ] Servidor con OS actualizado
- [ ] Java 17 instalado y verificado
- [ ] MySQL 8 instalado y configurado
- [ ] Usuario `softgold_app` creado en MySQL con permisos adecuados
- [ ] Base de datos `softgold` creada
- [ ] Firewall configurado (puertos 80, 443, 22)
- [ ] Nginx instalado y configurado
- [ ] Certificado SSL obtenido (producción)
- [ ] Directorio `/opt/softgold` creado con permisos correctos
- [ ] Usuario de sistema `softgold` creado
- [ ] Archivo `softgold.env` creado con permisos 600
- [ ] Archivo `application-prod.properties` configurado
- [ ] Scripts de backup creados y cron configurado
- [ ] JAR compilado y transferido al servidor
- [ ] Servicio systemd creado y habilitado

### 17.2 Checklist Post-Despliegue

- [ ] Servicio arranca sin errores
- [ ] Se puede acceder a la URL del sistema
- [ ] Login con credenciales de administrador funciona
- [ ] Todos los módulos cargan correctamente
- [ ] Mapas Leaflet.js renderizan correctamente
- [ ] Formularios guardan datos en la base de datos
- [ ] Servicio de correo funciona (test de recuperación de contraseña)
- [ ] Timeout de sesión funciona a los 15 minutos
- [ ] Logs se están generando correctamente
- [ ] Backup cron está configurado
- [ ] Contraseña del administrador por defecto fue cambiada
- [ ] Credenciales de development removidas de production

---

## 18. PROCEDIMIENTO DE ACTUALIZACIÓN

### 18.1 Pasos para Actualizar SoftGold

```bash
# 1. Hacer backup previo
/opt/softgold/scripts/backup_db.sh
cp /opt/softgold/softgold.jar /opt/softgold/backups/softgold_prev.jar

# 2. Detener el servicio
sudo systemctl stop softgold

# 3. Reemplazar el JAR
cp /ruta/al/nuevo/softgold-nueva-version.jar /opt/softgold/softgold.jar

# 4. Iniciar el servicio
sudo systemctl start softgold

# 5. Verificar arranque (esperar ~30 segundos)
sleep 30
sudo systemctl status softgold

# 6. Validar funcionamiento
curl -s -o /dev/null -w "%{http_code}" http://localhost:9090/login
```

### 18.2 Tiempo de Indisponibilidad Estimado

| Fase | Tiempo |
|------|--------|
| Detención del servicio | < 5 segundos |
| Reemplazo del JAR | < 10 segundos |
| Arranque de la aplicación | 15-30 segundos |
| **Total de downtime** | **~1 minuto** |

---

## 19. PROCEDIMIENTO DE ROLLBACK

Si después de una actualización se detectan problemas críticos:

```bash
# 1. Detener la versión problemática
sudo systemctl stop softgold

# 2. Restaurar el JAR anterior
cp /opt/softgold/backups/softgold_prev.jar /opt/softgold/softgold.jar

# 3. Si la base de datos fue modificada, restaurar backup
sudo systemctl stop mysql
# (Restaurar backup de BD si aplica)
mysql -u softgold_app -p softgold < /var/backups/softgold/softgold_YYYYMMDD.sql
sudo systemctl start mysql

# 4. Iniciar la versión anterior
sudo systemctl start softgold

# 5. Verificar
sudo systemctl status softgold
curl -s -o /dev/null -w "%{http_code}" http://localhost:9090/login
```

---

## 20. PROBLEMAS COMUNES Y SOLUCIONES

### 20.1 La aplicación no inicia

**Síntoma:** `systemctl status softgold` muestra "failed" o el puerto 9090 no responde.

**Causas y soluciones:**

| Causa | Diagnóstico | Solución |
|-------|-------------|---------|
| Java no encontrado | `journalctl -u softgold -n 20` | Verificar `java -version` y `which java` |
| Puerto 9090 en uso | `ss -tlnp \| grep 9090` | Matar el proceso que usa el puerto |
| MySQL no disponible | Revisar logs de arranque | `systemctl start mysql` |
| Error en configuración | Ver logs con `journalctl -u softgold` | Verificar `application-prod.properties` |
| Sin permisos en directorio | Ver error en logs | `chown -R softgold:softgold /opt/softgold` |

### 20.2 Error de conexión a la base de datos

**Síntoma:** En logs aparece `Communications link failure` o `Access denied for user`.

**Solución:**
```bash
# Verificar que MySQL está corriendo
sudo systemctl status mysql

# Verificar credenciales
mysql -u softgold_app -p softgold

# Verificar URL de conexión en la configuración
grep "datasource.url" /opt/softgold/config/application-prod.properties
```

### 20.3 Los mapas Leaflet.js no cargan

**Síntoma:** El contenedor del mapa aparece en gris o vacío.

**Causas:**
- El servidor no tiene acceso a internet (para los tiles de OpenStreetMap).
- El elemento HTML del mapa no tiene altura definida.

**Solución:**
- Verificar acceso a internet: `curl -I https://tile.openstreetmap.org`
- Si no hay internet, considerar un servidor de tiles local (TileServer GL).

### 20.4 El correo de recuperación no llega

**Síntoma:** El proceso de recuperación de contraseña no envía el email.

**Diagnóstico:**
```bash
grep "EmailService\|JavaMailSender\|mail" /var/log/softgold/softgold.log | tail -20
```

**Soluciones:**
- Verificar que `spring.mail.username` y `spring.mail.password` sean correctos.
- Confirmar que la contraseña de aplicación de Gmail está vigente.
- Verificar que el servicio SMTP (puerto 587) no esté bloqueado por el firewall del servidor.

### 20.5 Sesión expira muy rápido

**Síntoma:** Los usuarios son redirigidos al login frecuentemente.

**Solución:**
```properties
# En application-prod.properties, aumentar el timeout
server.servlet.session.timeout=30m
```

### 20.6 Rendimiento lento con muchos usuarios

**Síntoma:** La aplicación responde lentamente bajo carga.

**Soluciones:**
- Aumentar la memoria JVM: `-Xms512m -Xmx1024m`
- Optimizar el pool de conexiones MySQL:
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```
- Agregar índices a las tablas más consultadas.
- Implementar caché de segundo nivel con EhCache.

### 20.7 Nginx devuelve 502 Bad Gateway

**Síntoma:** El navegador muestra "502 Bad Gateway" al acceder al sistema.

**Causa:** SoftGold no está corriendo o no responde en el puerto 9090.

**Solución:**
```bash
# Verificar que SoftGold esté activo
sudo systemctl status softgold

# Si está caído, revisar logs y reiniciar
sudo journalctl -u softgold -n 50
sudo systemctl restart softgold
```

---

*Fin del Manual de Implementación y Despliegue del Sistema SoftGold — Versión 1.0.0*

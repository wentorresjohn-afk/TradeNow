# TradeNow

## Integrantes

* Dylan Joel Molina López
* Gabriel Jesús Mora Hernández
* John Wen Torres
* María Jesús Salas Jiménez
* Sebastián Vázquez Navarro

---

## Descripción del proyecto

TradeNow es una plataforma digital de trueques que responde a la necesidad de personas que poseen bienes, habilidades o servicios sin aprovechar pero enfrentan necesidades que no pueden cubrir debido a limitaciones económicas. Los mecanismos actuales como grupos de WhatsApp o redes sociales no ofrecen trazabilidad, seguridad ni respaldo formal entre las partes, lo que genera desconfianza y hace que muchos intercambios potenciales nunca se concreten. El sistema resuelve esta problemática centralizando las ofertas, facilitando el encuentro entre contrapartes compatibles y dejando registro formal de cada acuerdo, sin necesidad de dinero como intermediario.

---

## Roles

### Usuario General

Actor principal de la plataforma. Puede publicar bienes o servicios para intercambio, explorar ofertas, proponer y negociar trueques, confirmar intercambios realizados y calificar a otros usuarios.

### Administrador

Responsable de la gestión y mantenimiento de la plataforma. Administra usuarios, categorías, contenido del sistema y genera reportes para garantizar el correcto funcionamiento del entorno.

### Moderador de Contenido

Encargado de supervisar las publicaciones y reportes de la comunidad. Puede aprobar, rechazar o eliminar contenido que incumpla las normas y gestionar comportamientos inapropiados reportados por los usuarios.

---

## Tecnologías utilizadas

### Sistema

* Lenguaje principal: Java 21
* Framework: Spring Boot
* Persistencia: Spring Data JPA
* Base de datos: MySQL
* Manejo de dependencias: Apache Maven 3.9.14

### Herramientas de desarrollo

* Postman
* GitHub
* Jira
* IntelliJ IDEA

---

## Estructura del proyecto

```text
src/main/java/com.tm3200.TradeNow

├── Controller
│   ├── PostsController
│   ├── ProposalController
│   ├── RatingController
│   ├── ReportController
│   ├── TradeController
│   └── UserController
│
├── Model
│   ├── DTO
│   ├── Enum
│   ├── PostsEntitys
│   │   ├── Category
│   │   └── Zone
│   │
│   ├── Posts
│   ├── Proposal
│   ├── Rating
│   ├── Trade
│   ├── User
│   └── UserReputation
│
├── Repository
│   ├── PostsJpaRepository
│   ├── ProposalJpaRepository
│   ├── RatingJpaRepository
│   ├── TradeJpaRepository
│   ├── UserJpaRepository
│   └── UserReputationJpaRepository
│
├── Service
│   ├── PostsService
│   ├── ProposalService
│   ├── RatingService
│   ├── ReportsService
│   ├── TradeService
│   └── UserService
│
└── TradeNowApplication
```

El proyecto sigue una arquitectura por capas basada en Spring Boot, donde los controladores reciben las solicitudes HTTP, los servicios contienen la lógica de negocio, los repositorios gestionan el acceso a la base de datos mediante JPA y las entidades representan el modelo de datos persistente.

---

## Requisitos previos

Para ejecutar TradeNow se requiere:

* Java 21
* Apache Maven 3.9.14
* MySQL
* IntelliJ IDEA
* Postman
* GitHub

---

## Arquitectura por capas

TradeNow utiliza una arquitectura por capas basada en Spring Boot para separar responsabilidades y facilitar el mantenimiento del sistema.

### Controller

La capa Controller recibe las solicitudes HTTP enviadas por los clientes (Postman o Frontend) y delega el procesamiento a los servicios correspondientes.

Controladores implementados:

* PostsController
* ProposalController
* RatingController
* ReportController
* TradeController
* UserController

### Service

La capa Service contiene la lógica de negocio del sistema. Se encarga de procesar la información, aplicar reglas de negocio y coordinar las operaciones entre controladores y repositorios.

Servicios implementados:

* PostsService
* ProposalService
* RatingService
* ReportsService
* TradeService
* UserService

### Repository

La capa Repository gestiona el acceso a la base de datos mediante Spring Data JPA y permite realizar operaciones CRUD sobre las entidades del sistema.

Repositorios implementados:

* PostsJpaRepository
* ProposalJpaRepository
* RatingJpaRepository
* TradeJpaRepository
* UserJpaRepository
* UserReputationJpaRepository

### Model

La capa Model contiene las entidades persistentes, DTOs y enumeraciones utilizadas por el sistema.

Entidades principales:

* User
* Posts
* Proposal
* Trade
* Rating
* UserReputation
* Category
* Zone

### Base de datos

La persistencia de datos se realiza mediante MySQL utilizando JPA/Hibernate para el mapeo objeto-relacional.


---

## Base de datos

TradeNow utiliza MySQL como sistema gestor de base de datos. La persistencia se realiza mediante Spring Data JPA e Hibernate, permitiendo el mapeo entre las entidades del sistema y las tablas de la base de datos.

---

## Relaciones entre entidades

* User → Posts (un usuario puede tener múltiples publicaciones).
* Posts → Category (una publicación pertenece a una categoría).
* Posts → Zone (una publicación pertenece a una zona geográfica).
* Category → Category (una categoría puede tener una categoría padre).
* Proposal → User (una propuesta es enviada por un usuario).
* Proposal → Posts (publicación objetivo).
* Proposal → Posts (publicación ofrecida).
* Proposal → Proposal (una propuesta puede generar una contraoferta).
* Trade → User (user1).
* Trade → User (user2).
* Rating → Trade (una calificación pertenece a un trueque).
* Rating → User (usuario que califica).
* Rating → User (usuario calificado).
* UserReputation → User (una reputación pertenece a un usuario).

---

## Instalación

1. Clonar el repositorio desde GitHub.
2. Abrir el proyecto en IntelliJ IDEA.
3. Crear una base de datos MySQL para el sistema.
4. Configurar las credenciales de acceso en el archivo `application.properties`.
5. Ejecutar:

```bash
mvn clean install
```

---

## Pasos de ejecución del programa

1. Iniciar el servicio de MySQL.
2. Verificar que la base de datos configurada en `application.properties` exista.
3. Abrir el proyecto en IntelliJ IDEA.
4. Ejecutar la clase principal `TradeNowApplication`.
5. Esperar a que Spring Boot finalice el proceso de inicio.
6. Verificar en la consola que la aplicación se encuentra ejecutándose correctamente.
7. Utilizar Postman para realizar pruebas sobre los endpoints disponibles.

---

## Endpoints de la API

### Usuarios y perfiles

* POST `/api/aut/registro`
* POST `/api/aut/login`
* GET `/api/usuarios/{id}`
* PUT `/api/usuarios/{id}`
* GET `/api/usuarios/{id}/historial`
* PUT `/api/usuarios/{id}/estado`

### Publicaciones

* GET `/api/publicaciones/all`
* GET `/api/publicaciones/{id}`
* POST `/api/publicaciones/new`
* PUT `/api/publicaciones/edit/{id}`
* DELETE `/api/publicaciones/delete/{id}`
* GET `/api/publicaciones/filter`

### Propuestas de intercambio

* POST `/api/propuestas`
* GET `/api/propuestas/recibidas`
* GET `/api/propuestas/enviadas`
* PUT `/api/propuestas/{id}/responder`

### Trueques activos

* GET `/api/trueques`
* GET `/api/trueques/{id}`
* PUT `/api/trueques/{id}/confirmar`

### Reputación y calificaciones

* POST `/api/calificaciones`
* GET `/api/usuarios/{id}/reputacion`

### Reportes

* GET `/api/reportes/categorias`
* GET `/api/reportes/usuarios-activos`
* GET `/api/reportes/por-zona`
* GET `/api/reportes/valor-total`

---

## Evidencias del estado actual del proyecto

### Evidencia 1 - Estructura del proyecto

Captura de la estructura del proyecto
<img width="246" height="143" alt="image" src="https://github.com/user-attachments/assets/d56178c0-7856-48eb-a0d9-9979a103f41e" />


### Evidencia 2 - Base de datos

No disponible en esta etapa del proyecto. La configuración y validación de la base de datos MySQL se encuentra en desarrollo.

### Evidencia 3 - Ejecución de la aplicación

Captura de la consola mostrando el inicio correcto de Spring Boot.
<img width="1858" height="952" alt="image" src="https://github.com/user-attachments/assets/64e7885b-f90b-4fe6-98f7-ad9108b0aa7d" />

### Evidencia 4 - Prueba de endpoints

Agregar captura de una prueba exitosa realizada mediante Postman.

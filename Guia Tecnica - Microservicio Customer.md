# Guía Técnica - Microservicio

## Microservicio: customer

**Descripción:** Gestiona los datos del perfil del cliente y operaciones relacionadas, incluyendo creación, actualización, activación e inactivación de clientes. La autenticación se realiza mediante JWT.

## Pila Tecnológica

-   Java (21), Spring Boot
-   Spring Security (configurado para validación de JWT)

## Base de Datos

-   H2

## Justificación Técnica

Se elige H2 dado que el alcance de este microservicio esta limitado unicamente a una prueba tecnica, por lo que la facilidad de uso que proporciona H2 es mas que idonea para validar los conocimientos tecnicos del candidato al realizar un microservicio.

## Arquitectura Interna

-   Arquitectura Hexagonal

## Metodología de Desarrollo: Desarrollo Guiado por Pruebas (TDD)

El desarrollo de este microservicio debe adherirse a la metodología de Desarrollo Guiado por Pruebas (TDD). Se requiere que los desarrolladores:

1.  **Escriban Primero Pruebas que Fallen:** Antes de escribir cualquier código de implementación para una nueva funcionalidad o comportamiento de un endpoint, escriban pruebas automatizadas que definan la funcionalidad deseada e inicialmente fallen.
2.  **Escriban el Mínimo Código para Pasar:** Implementen solo el código necesario para que las pruebas que fallan pasen.
3.  **Refactoricen:** Mejoren la estructura y el diseño del código asegurándose de que todas las pruebas continúen pasando.

Los casos de prueba descritos para cada endpoint en esta guía sirven como la base inicial para los ciclos de TDD. Sin embargo, se espera que el desarrollador amplíen estos ejemplos, creando un conjunto completo de pruebas unitarias, de integración y potencialmente de contrato para cubrir diversos escenarios, casos límite y condiciones de fallo, asegurando una alta calidad, mantenibilidad y robustez del código.

## Dominio

-   Entidades como `Customer` (que contiene campos como customerId, name, birthDate, gender (enlazado a Catalog.Gender), numCta, status (enlazado a Catalog.Status), country (enlazado a Catalog.Country) y isActive).

-   **Enumeraciones y Mapeo a Catálogos:** Se deben definir enums en el código de la aplicación para representar los valores de los catálogos. Esto proporciona seguridad de tipos y mejora la legibilidad. Cada enum se mapeará a su respectiva tabla de catálogo en la base de datos.

    -   **Gender:**
        -   **Enum:** `Gender { MALE, FEMALE }`
        -   **Mapeo a DB:** `Catalog.Gender` (columnas: `id`, `name`)

    -   **Status:**
        -   **Enum:** `Status { ACTIVE, INACTIVE }`
        -   **Mapeo a DB:** `Catalog.Status` (columnas: `id`, `name`)

    -   **Country:**
        -   **Enum:** `Country { COLOMBIA, CHILE, ARGENTINA, BRASIL, ECUADOR, MEXICO, PANAMA }`
        -   **Mapeo a DB:** `Catalog.Country` (columnas: `id`, `name`)

## Adaptadores

-   **Adaptadores de Entrada (Driving Adapters):**
    -   Controladores REST (`CustomerController`). Los filtros de Spring Security manejan la validación del JWT antes de que las solicitudes lleguen al controlador.
-   **Adaptadores de Salida (Driven Adapters):**
    -   Repositorio JPA/JDBC (`CustomerRepository`) para interactuar con la base de datos.
    
## Otras Consideraciones Tecnicas
    
    -   Todo log debe seguir una politica de secure logging basada en PCI DSS.

## Endpoints de la API REST (`/api/v1/customers`)

---

### 1.1 Endpoint: Crear un Nuevo Cliente

-   **URL:** `/api/v1/customers`
-   **Método:** `POST`
-   **Descripción:** Crea un nuevo cliente en el sistema. Valida los datos de entrada y aplica reglas de negocio específicas por país (por ejemplo, para Chile) antes de almacenar la información en la base de datos. El estado inicial del cliente siempre será dado por el request, no se tendra `ACTIVE` por defecto.
-   **Autorización:** Requiere un JWT válido con los permisos o roles adecuados. Spring Security validará el token y asegurará que contenga la autoridad necesaria (por ejemplo, `SCOPE_customers:write` o el rol `ROLE_WRITER`).

**Posibles Respuestas HTTP:**

-   **201 Created:** Cliente creado con éxito. Devuelve el DTO del cliente recién creado, incluyendo el ID generado.
-   **400 Bad Request:** Los datos proporcionados en el cuerpo de la solicitud son inválidos. Esto puede deberse a campos faltantes, formatos incorrectos (fecha, `numCTA`), valores no permitidos en enumeraciones (`gender`, `country`), o violación de las reglas de validación (por ejemplo, `birthDate` fuera del rango permitido, `numCTA` de Chile sin el prefijo '003').
-   **401 Unauthorized:** Falta el token JWT, está mal formado o ha expirado.
-   **403 Forbidden:** El token JWT es válido pero no tiene los roles o autoridades requeridos (ej. `SCOPE_customers:write`).
-   **500 Internal Server Error:** Error inesperado en el servidor, como un problema de conexión con la base de datos o un fallo en la lógica interna.

**Mensajes de Error Útiles:**

-   **400:** `"Error de validación: El campo 'name' no puede estar vacío."`, `"La fecha de nacimiento 'birthDate' debe ser posterior a 1990-01-01."`, `"El valor 'OTRO' no es válido para el campo 'gender'. Valores permitidos: [MALE, FEMALE]."`, `"Para el país CHILE, el 'numCTA' debe comenzar con '003'."`
-   **401:** (Generado por Spring Security) `"Acceso no autorizado."`
-   **403:** (Generado por Spring Security) `"Acceso denegado. Permisos insuficientes."`
-   **500:** `"Ocurrió un error interno al crear el cliente. ID de Error: [UUID]"`

**Recomendaciones Técnicas:**

-   **Framework y Persistencia:** Utilizar Spring Boot con Spring Data JPA. Definir una entidad JPA (`@Entity`) `Customer` y un `CustomerRepository` que extienda de `JpaRepository` para interactuar con la base de datos.
-   **DTOs (Data Transfer Objects):** Usar DTOs para el request (`CreateCustomerRequestDTO`) y la respuesta (`CustomerResponseDTO`) para desacoplar la API de las entidades de base de datos. Documentar estos DTOs en Swagger.
-   **Validación:** Aplicar validaciones estándar de Jakarta Bean Validation en el `CreateCustomerRequestDTO` (ej. `@NotBlank`, `@Past`, `@Pattern`, `@Size`).
-   **Patrón Strategy:** Implementar un patrón Strategy para la validación de `numCTA` dependiente del país.
    -   Usar dentro del servicio (en la capa de aplicacion) una interfaz `CountryValidationStrategy` con un método `validate(Customer customer)`.
    -   Crear implementaciones como `ChileanValidationStrategy` (que verifica el prefijo '003') y `DefaultValidationStrategy` (que no realiza validaciones adicionales).
    -   Usar un factory o la inyección de dependencias de Spring para seleccionar la estrategia correcta en el servicio basándose en el campo `country` del Customer.
-   **Mapeo de Catálogos:** En la capa de servicio, mapear los enums (`gender`, `country`, `status`) a las entidades de catálogo correspondientes de la base de datos antes de persistir la entidad `Customer`.

**Pruebas:**

#### Caso Exitoso (Cliente de Colombia con status `ACTIVE`):

-   **Entrada:**
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_escritura>`
    -   Cuerpo:
        ```json
        {
          "name": "Juan Pérez",
          "birthDate": "1995-08-20",
          "gender": "MALE",
          "numCTA": "123456789012",
          "country": "COLOMBIA",
          "status": "ACTIVE"
        }
        ```
-   **Salida Esperada (Estado 201):**
    ```json
    {
      "customerId": "d4a7f8b1-9c3e-4a5d-b1e2-f3c4d5a6b7c8",
      "name": "Juan Pérez",
      "birthDate": "1995-08-20",
      "gender": "MALE",
      "numCTA": "123456789012",
      "status": "ACTIVE",
      "country": "COLOMBIA"
    }
    ```

#### Caso Exitoso (Cliente de Chile con status `INACTIVE`):

-   **Entrada:**
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_escritura>`
    -   Cuerpo:
        ```json
        {
          "name": "Ana Silva",
          "birthDate": "2001-02-10",
          "gender": "FEMALE",
          "numCTA": "003987654321",
          "country": "CHILE",
          "status": "INACTIVE"
        }
        ```
-   ***Salida Esperada (Estado 201):**
    ```json
    {
      "customerId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
      "name": "Ana Silva",
      "birthDate": "2001-02-10",
      "gender": "FEMALE",
      "numCTA": "003987654321",
      "status": "INACTIVE",
      "country": "CHILE"
    }
    ```

#### Caso de Fallo (Status Inválido):

-   **Entrada:**
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_escritura>`
    -   Cuerpo:
        ```json
        {
          "name": "Usuario de Prueba",
          "birthDate": "1999-01-01",
          "gender": "MALE",
          "numCTA": "12345",
          "country": "COLOMBIA",
          "status": "PENDING"
        }
        ```
-   **Salida Esperada (Estado 400):**
    ```json
    {
      "timestamp": "...",
      "status": 400,
      "error": "Bad Request",
      "message": "Error de validación: El valor 'PENDING' no es válido para el campo 'status'. Valores permitidos: [ACTIVE, INACTIVE]."
    }
    ```


#### Caso de Fallo (Datos Inválidos - `birthDate` fuera de rango):

-   **Entrada:**
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_escritura>`
    -   Cuerpo:
        ```json
        {
          "name": "Pedro",
          "birthDate": "1989-12-31",
          "gender": "MALE",
          "numCTA": "987654321",
          "country": "COLOMBIA",
          "status": "ACTIVE"
        }
        ```
-   **Salida Esperada (Estado 400):**
    ```json
    {
      "timestamp": "...",
      "status": 400,
      "error": "Bad Request",
      "message": "Error de validación: La fecha de nacimiento 'birthDate' debe ser igual o posterior a 1990-01-01."
    }
    ```

#### Caso de Fallo (Validación de Estrategia - `numCTA` de Chile inválido):

-   **Entrada:**
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_escritura>`
    -   Cuerpo:
        ```json
        {
          "name": "Carlos Rojas",
          "birthDate": "1995-11-23",
          "gender": "MALE",
          "numCTA": "111987654321",
          "country": "CHILE",
          "status": "ACTIVE"
        }
        ```
-   **Salida Esperada (Estado 400):**
    ```json
    {
      "timestamp": "...",
      "status": 400,
      "error": "Bad Request",
      "message": "Error de validación: Para el país CHILE, el 'numCTA' debe comenzar con '003'."
    }
    ```

---

### 1.2 Endpoint: Actualizar Cliente

-   **URL:** `/api/v1/customers/{customerId}`
-   **Método:** `PATCH`
-   **Descripción:** Actualiza los datos de un cliente existente (`name`, `birthDate`, `gender`, `numCTA`). Una precondición clave es que el cliente debe tener un estado `ACTIVE` para poder ser actualizado.
-   **Autorización:** Requiere un JWT válido con permisos de actualización (ej. `SCOPE_customers:update`).

**Posibles Respuestas HTTP:**

-   **200 OK:** Cliente actualizado con éxito. Devuelve el DTO del cliente con los datos actualizados.
-   **400 Bad Request:** Los datos en el cuerpo de la solicitud son inválidos (mismas validaciones que en la creación).
-   **401 Unauthorized:** Token JWT inválido.
-   **403 Forbidden:** Token JWT válido pero sin las autoridades requeridas.
-   **404 Not Found:** No se encontró ningún cliente con el `customerId` proporcionado.
-   **409 Conflict:** La operación no puede ser completada porque el cliente no está en estado `ACTIVE`.
-   **500 Internal Server Error:** Error interno del servidor.

**Mensajes de Error Útiles:**

-   **400:** `"Error de validación: El formato de 'birthDate' es incorrecto."`
-   **404:** `"No se encontró el cliente con ID: [customerId]"`
-   **409:** `"La operación falló: El cliente con ID [customerId] no está activo y no puede ser modificado."`
-   **500:** `"Ocurrió un error interno al actualizar el cliente. ID de Error: [UUID]"`

**Recomendaciones Técnicas:**

-   **DTOs:** Utilizar un `UpdateCustomerRequestDTO` que contenga solo los campos actualizables. La respuesta será un `CustomerResponseDTO`.
-   **Lógica de Servicio:**
    1.  Recuperar la entidad `Customer` usando el `customerId` a través del repositorio de Spring Data. Si no existe, lanzar una excepción que resulte en un 404.
    2.  Verificar si `customer.getStatus().getName().equals("ACTIVE")`. Si no es así, lanzar una excepción de conflicto de negocio (409).
    3.  Aplicar las actualizaciones del DTO a la entidad.
    4.  Persistir los cambios dentro de una transacción (`@Transactional` en el método del servicio).
    5.  Registrar un Log de la actualizacion.

**Pruebas:**

#### Caso Exitoso:

-   **Entrada:**
    -   URL: `/api/v1/customers/d4a7f8b1-9c3e-4a5d-b1e2-f3c4d5a6b7c8`
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_update>`
    -   Cuerpo:
        ```json
        {
          "name": "Juan Pérez Actualizado",
          "numCTA": "987654321098"
        }
        ```
-   **Salida Esperada (Estado 200):** DTO del cliente con los campos actualizados.

#### Caso de Fallo (Cliente Inactivo):

-   **Entrada:**
    -   URL: `/api/v1/customers/{id_cliente_inactivo}`
    -   Cuerpo: `{ "name": "Juan Pérez Intento de actualización" }`
-   **Salida Esperada (Estado 409):**
    ```json
    {
      "timestamp": "...",
      "status": 409,
      "error": "Conflict",
      "message": "La operación falló: El cliente con ID {id_cliente_inactivo} no está activo y no puede ser modificado."
    }
    ```

#### Caso de Fallo (Cliente No Encontrado):

-   **Entrada:**
    -   URL: `/api/v1/customers/id-inexistente`
    -   Cuerpo: `{ "name": "Test" }`
-   **Salida Esperada (Estado 404):** Mensaje indicando que el cliente no fue encontrado.

---

### 1.3 Endpoint: Desactivar Cliente

-   **URL:** `/api/v1/customers/{customerId}/deactivate`
-   **Método:** `PATCH`
-   **Descripción:** Cambia el estado de un cliente a `INACTIVE` y registra la fecha de desactivación (`inactivateDate`). Solo se puede desactivar un cliente que se encuentre actualmente `ACTIVE`.
-   **Autorización:** Requiere JWT con permisos específicos (ej. `SCOPE_customers:deactivate`).

**Posibles Respuestas HTTP:**

-   **200 OK:** Cliente desactivado con éxito. Devuelve el DTO del cliente actualizado.
-   **401 Unauthorized:** Token JWT inválido.
-   **403 Forbidden:** Permisos insuficientes.
-   **404 Not Found:** El cliente con el `customerId` no existe.
-   **409 Conflict:** El cliente ya se encuentra en estado `INACTIVE`.
-   **500 Internal Server Error:** Error interno.

**Mensajes de Error Útiles:**

-   **404:** `"No se encontró el cliente con ID: [customerId]"`
-   **409:** `"El cliente con ID [customerId] ya se encuentra inactivo."`

**Recomendaciones Técnicas:**

-   **Lógica de Servicio:**
    1.  Buscar al cliente por su ID.
    2.  Verificar que su estado actual sea `ACTIVE`. Si no lo es, devolver un 409.
    3.  Cambiar el estado a `INACTIVE`.
    4.  Guarda inactivateDate, activateDate.
    5.  Guardar los cambios en la base de datos.

**Pruebas:**

#### Caso Exitoso:

-   **Entrada:**
    -   URL: `/api/v1/customers/{id_cliente_activo}/deactivate`
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_deactivate>`
-   **Salida Esperada (Estado 200):** DTO del cliente con `status: "INACTIVE"` y la fecha de inactivación actualizada.

#### Caso de Fallo (Cliente ya Inactivo):

-   **Entrada:** URL: `/api/v1/customers/{id_cliente_ya_inactivo}/deactivate`
-   **Salida Esperada (Estado 409):** Mensaje indicando que el cliente ya está inactivo.

#### Caso de Fallo (Cliente No Encontrado):

-   **Entrada:**
    -   URL: `/api/v1/customers/id-inexistente/deactivate`
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_deactivate>`
-   **Salida Esperada (Estado 404):**
    ```json
    {
      "timestamp": "...",
      "status": 404,
      "error": "Not Found",
      "message": "No se encontró el cliente con ID: id-inexistente"
    }
    ```

---

### 1.4 Endpoint: Activar Cliente

-   **URL:** `/api/v1/customers/{customerId}/activate`
-   **Método:** `PATCH`
-   **Descripción:** Cambia el estado de un cliente a `ACTIVE` y registra la fecha de activación (`activateDate`). Solo se puede activar un cliente que se encuentre actualmente `INACTIVE`.
-   **Autorización:** Requiere JWT con permisos específicos (ej. `SCOPE_customers:activate`).

**Posibles Respuestas HTTP:**

-   **200 OK:** Cliente activado con éxito. Devuelve el DTO del cliente actualizado.
-   **401 Unauthorized:** Token JWT inválido.
-   **403 Forbidden:** Permisos insuficientes.
-   **404 Not Found:** El cliente con el `customerId` no existe.
-   **409 Conflict:** El cliente ya se encuentra en estado `ACTIVE`.
-   **500 Internal Server Error:** Error interno.

**Mensajes de Error Útiles:**

-   **404:** `"No se encontró el cliente con ID: [customerId]"`
-   **409:** `"El cliente con ID [customerId] ya se encuentra activo."`

**Recomendaciones Técnicas:**

-   **Lógica de Servicio:**
    1.  Buscar al cliente por su ID.
    2.  Verificar que su estado actual sea `INACTIVE`. Si no lo es, devolver un 409.
    3.  Cambiar el estado a `ACTIVE`.
    4.  Guarda inactivateDate, activateDate.
    5.  Guardar los cambios en la base de datos.

**Pruebas:**

#### Caso Exitoso:

-   **Entrada:**
    -   URL: `/api/v1/customers/{id_cliente_inactivo}/activate`
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_activate>`
-   **Salida Esperada (Estado 200):** DTO del cliente con `status: "ACTIVE"` y la fecha de activación actualizada.

#### Caso de Fallo (Cliente ya Activo):

-   **Entrada:** URL: `/api/v1/customers/{id_cliente_ya_activo}/activate`
-   **Salida Esperada (Estado 409):** Mensaje indicando que el cliente ya está activo.

#### Caso de Fallo (Cliente No Encontrado):

-   **Entrada:**
    -   URL: `/api/v1/customers/id-inexistente/activate`
    -   Cabecera: `Authorization: Bearer <jwt_valido_con_permiso_activate>`
-   **Salida Esperada (Estado 404):**
    ```json
    {
      "timestamp": "...",
      "status": 404,
      "error": "Not Found",
      "message": "No se encontró el cliente con ID: id-inexistente"
    }
    ```
    

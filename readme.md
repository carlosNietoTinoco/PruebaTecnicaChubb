# ChubbTest Application

Esta aplicación corresponde a la prueba técnica para Chubb. En esta prueba se crea el microservicio **customer**, este microservicio se encuentra detalladamente documentado a nivel técnico en el archivo **Guía Técnica - Microservicio Customer.md**, que se encuentra en la raíz de este proyecto.

## Desplegar el proyecto para probarlo

### Consideraciones para las pruebas

Los 4 endpoints de este microservicio están protegidos por autenticación con JWT. Por lo cual, debe utilizar alguno de estos tokens, dependiendo de las restricciones de cada endpoint:

**Token 1**: Este token posee los siguientes scopes: SCOPE_customers:write, SCOPE_customers:read, SCOPE_customers:update, SCOPE_customers:deactivate y SCOPE_customers:activate

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyLXRlc3Qtd3JpdGVyIiwic2NvcGUiOiJjdXN0b21lcnM6d3JpdGUgY3VzdG9tZXJzOnJlYWQgY3VzdG9tZXJzOnVwZGF0ZSBjdXN0b21lcnM6ZGVhY3RpdmF0ZSBjdXN0b21lcnM6YWN0aXZhdGUiLCJpYXQiOjE2NzI1MzEyMDAsImV4cCI6MjMwMzIyMjQwMH0.Wab16rWdBkU_lh9ui9IomWlosxfSR9i-BuYDpIVyCOs

**Token 2**: Este token posee los siguientes scopes: SCOPE_customers:write y SCOPE_customers:read

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyLXRlc3Qtd3JpdGVyIiwic2NvcGUiOiJjdXN0b21lcnM6d3JpdGUgY3VzdG9tZXJzOnJlYWQiLCJpYXQiOjE2NzI1MzEyMDAsImV4cCI6MjMwMzIyMjQwMH0.bFDXKVRw6NYR8xzkL89aWVn46RcULPg9_SLxWFP1HXo

Si utiliza la colección de Postman, estos tokens ya están configurados. Se recomienda ejecutar primero las 3 requests de creación de customer, dado que la colección de Postman capturará automáticamente los IDs creados en estas peticiones para ser usados en las siguientes peticiones (update, deactivate y activate).

### Despliegue

Para probar este proyecto se recomiendan 2 formas:

### Despliegue con Docker (Recomendada)

Para llevar a cabo este despliegue son necesarios estos prerrequisitos:

**1-** No tener ningún proceso ejecutándose en el puerto **56790**.

**2-** Tener instalado **Docker** con una versión igual o superior a 20.10.

Teniendo cumplidos estos 2 requisitos, debe clonar este proyecto en su máquina local, luego dirigirse a la raíz del mismo **/PruebaTecnicaChubb**, abrir esta ruta en una terminal y ejecutar el comando **docker compose up --build -d**. Esto levantará el microservicio, dejándolo disponible para su consumo en la URL **http://localhost:56790/**. Puede probarlo directamente o puede usar la colección de Postman **CustomerService.postman_collection.json** que se encuentra en la raíz del proyecto.

### Despliegue Manual

Para llevar a cabo este despliegue son necesarios estos prerrequisitos:

**1-** No tener ningún proceso ejecutándose en el puerto **8080**.

**2-** Tener instalado **Java 17** y **Maven**. 

Teniendo cumplidos estos 2 requisitos, debe clonar este proyecto en su máquina local, luego dirigirse a la raíz del mismo **/PruebaTecnicaChubb** y proceder a compilar y ejecutar el proyecto, dejándolo disponible para su consumo en la URL **http://localhost:8080/**. Puede probarlo directamente o puede usar la colección de Postman **CustomerService.postman_collection.json** que se encuentra en la raíz del proyecto, pero en este caso deberá modificar el puerto en cada request antes de enviarla.


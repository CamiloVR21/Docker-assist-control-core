# BBO / MS-THRESHOLD

Microservicio de control de Asistencia

## Features

- Control de Asistencia

## Tech

Tecnologias del proyecto

- [JDK 21](https://adoptium.net/temurin/releases/) - Java
- [JRE 17](https://www.graalvm.org/downloads/) - GraalVM
- [Quarkus 3.13](https://quarkus.io/) - Quarkus
- [myBatis 3](https://github.com/quarkiverse/quarkus-mybatis) - MyBatis
- [POI 5](https://quarkus.io/extensions/io.quarkiverse.poi/quarkus-poi) - POI
  
## Installation

Para lanzar el microservicio es necesario configurar la siguientes variables de entorno

#### Variables de entorno

| Nombre variable | Valor por defecto | 
|-----------------|-------------------|
| PORT            | 8080              |
| DB_HOST         | localhost         |
| DB_PORT         | 5432              |
| DB_NAME         | assist-control    |
| DB_SCHEMA       | public            |
| DB_USER         | postgres          |
| DB_PASS         |                   |
| quarkus.profile | local             |



```sh
mvn quarkus:dev
```

## Autorización

Para la autorización se utiliza [jcasbin](https://casbin.org/) y los archivos de configuración estan contenidos en la carpeta resources/perm

#### Archivos de autorización

| Ruta                      | Objetivo                             | 
|---------------------------|--------------------------------------|
| resources/perm/model.conf | -                                    |
| resources/perm/policy.cvs | Archivo de políticas de autorización |

> Note: _La autorización esta implementada manualmente (no con casbin)_

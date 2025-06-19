This template is used to start a new microservice project using Spring-boot.
This project has been generated with springboot-microservice-archetype-archetype,
see [archetype version file](archetype.version)

* [Architecture](#architecture)
* [Build](#build)
* [Development](#development)
* [Testing](#Tests)

# Architecture
## Data model
![data model](/doc/ethernet-model.png)

# Build
Refer to the [Develop with the archetype](https://millwall.cofe.btireland.ie/talos/talos-wiki/-/wikis/training/tutorials/developing-with-archetype)
to understand how to build this project

TODO for new project: if build differs from the standard archetype, give the specific command to build here

# Development
Refer to the [Develop with the archetype](https://millwall.cofe.btireland.ie/talos/talos-wiki/-/wikis/training/tutorials/developing-with-archetype)
to understand how to develop with it

## Starting the application locally

You will want to modify application.yaml to set the database name, host/port, username and password to match those of your local machine. It's recommended to run MariaDB 10.3 at the time of writing.

To run the application locally, make sure you set SPRING_PROFILES_ACTIVE to local. For example : `SPRING_PROFILES_ACTIVE=local mvn spring-boot:run`.

The JWK URI for Spring Security is set to use the dev instance of the single sign on server. If you don't want to use this, you can either set it to another URI, or you can disable security by activating the nosecurity profile : `SPRING_PROFILES_ACTIVE=local,nosecurity mvn spring-boot:run`.

# Tests
Refer to the [Develop with the archetype](https://millwall.cofe.btireland.ie/talos/talos-wiki/-/wikis/training/tutorials/developing-with-archetype)
to understand how to run tests on this project

TODO for new project: if running tests differs from the standard archetype, give the specific command to run here


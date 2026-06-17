# Spring PetClinic Sample Application

[![Build Status (Maven)](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/maven-build.yml)
[![Build Status (Gradle)](https://github.com/spring-projects/spring-petclinic/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/spring-projects/spring-petclinic/actions/workflows/gradle-build.yml)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/spring-projects/spring-petclinic) [![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://github.com/codespaces/new?hide_repo_select=true&ref=main&repo=7517918)

## Table of Contents

- [Understanding the Application](#understanding-the-spring-petclinic-application)
- [Running Locally](#run-petclinic-locally)
- [Building and Running a Container](#building-and-running-a-container)
- [Database Configuration](#database-configuration)
- [Working with Petclinic in Your IDE](#working-with-petclinic-in-your-ide)
- [Compiling the CSS](#compiling-the-css)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Test Applications](#test-applications)
- [Looking for Something in Particular?](#looking-for-something-in-particular)
- [Interesting Branches and Forks](#interesting-spring-petclinic-branches-and-forks)
- [Contributing](#contributing)
- [License](#license)

## Understanding the Spring Petclinic Application

See the presentation here:
[Spring Petclinic Sample Application (legacy slides)](https://speakerdeck.com/michaelisvy/spring-petclinic-sample-application?slide=20)

> **Note:** These slides refer to a legacy, pre–Spring Boot version of Petclinic and may not reflect the current Spring Boot–based implementation.
> For up-to-date information, please refer to this repository and its documentation.

## Run Petclinic Locally

Spring Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/) or [Gradle](https://spring.io/guides/gs/gradle/).

### Prerequisites

- Java 17 or newer (full JDK, not a JRE)
- [Git command line tool](https://help.github.com/articles/set-up-git)

### Clone and Run

```bash
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
```

**Using Maven:**

```bash
./mvnw spring-boot:run
```

**Using Gradle:**

```bash
./gradlew bootRun
```

You can then access the Petclinic at <http://localhost:8080/>.

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

## Building and Running a Container

There is no `Dockerfile` in this project. You can build a container image (if you have a Docker daemon) using the Spring Boot build plugin:

```bash
./mvnw spring-boot:build-image
```

Once the image is built, you can run it:

```bash
docker run -p 8080:8080 docker.io/library/spring-petclinic:latest
```

To verify the image was created:

```bash
docker images | grep petclinic
```

## Database Configuration

In its default configuration, Petclinic uses an in-memory database (H2) which gets populated at startup with data. The H2 console is exposed at `http://localhost:8080/h2-console`, and it is possible to inspect the content of the database using the `jdbc:h2:mem:<uuid>` URL. The UUID is printed at startup to the console.

### Using MySQL or PostgreSQL

A persistent database configuration is available for MySQL and PostgreSQL. When switching database types, the app needs to run with a different Spring profile:

- **MySQL:** `spring.profiles.active=mysql`
- **PostgreSQL:** `spring.profiles.active=postgres`

See the [Spring Boot documentation](https://docs.spring.io/spring-boot/how-to/properties-and-configuration.html#howto.properties-and-configuration.set-active-spring-profiles) for more detail on how to set the active profile.

### Starting a Database with Docker

**MySQL:**

```bash
docker run -e MYSQL_USER=petclinic -e MYSQL_PASSWORD=petclinic -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:9.6
```

**PostgreSQL:**

```bash
docker run -e POSTGRES_USER=petclinic -e POSTGRES_PASSWORD=petclinic -e POSTGRES_DB=petclinic -p 5432:5432 postgres:18.3
```

### Starting a Database with Docker Compose

Instead of vanilla `docker` you can also use the provided `docker-compose.yml` file to start the database containers. Each one has a service named after the Spring profile:

```bash
docker compose up mysql
```

or

```bash
docker compose up postgres
```

Further documentation is provided for [MySQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/mysql/petclinic_db_setup_mysql.txt)
and [PostgreSQL](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources/db/postgres/petclinic_db_setup_postgres.txt).

## Working with Petclinic in Your IDE

### Eclipse or STS

1. Open the project via `File -> Import -> Maven -> Existing Maven project`, then select the root directory of the cloned repo.
2. Build on the command line with `./mvnw generate-resources` or use the Eclipse launcher (right-click on project and `Run As -> Maven install`) to generate the CSS.
3. Run the application's main method by right-clicking on it and choosing `Run As -> Java Application`.

### IntelliJ IDEA

1. In the main menu, choose `File -> Open` and select the Petclinic [pom.xml](pom.xml). Click on the `Open` button.
2. CSS files are generated from the Maven build. You can build them on the command line with `./mvnw generate-resources` or right-click on the `spring-petclinic` project then `Maven -> Generates sources and Update Folders`.
3. A run configuration named `PetClinicApplication` should have been created for you if you're using a recent Ultimate version. Otherwise, run the application by right-clicking on the `PetClinicApplication` main class and choosing `Run 'PetClinicApplication'`.

### VS Code

1. Open the project folder in VS Code.
2. Install the recommended Java and Spring Boot extensions if prompted.
3. Use the built-in terminal to run `./mvnw generate-resources` to generate CSS files.
4. Run the application using the Spring Boot Dashboard or the `Run` button on the main class.

### Verify

Visit [http://localhost:8080](http://localhost:8080) in your browser.

## Compiling the CSS

There is a `petclinic.css` in `src/main/resources/static/resources/css`. It was generated from the `petclinic.scss` source, combined with the [Bootstrap](https://getbootstrap.com/) library. If you make changes to the `scss`, or upgrade Bootstrap, you will need to re-compile the CSS resources using the Maven profile "css":

```bash
./mvnw package -P css
```

> **Note:** There is no build profile for Gradle to compile the CSS.

## Kubernetes Deployment

Kubernetes manifests are provided in the `k8s/` directory for deploying Petclinic to a Kubernetes cluster. The configuration includes a `Service` and `Deployment` resource:

```bash
kubectl apply -f k8s/petclinic.yml
```

A separate database configuration is also available:

```bash
kubectl apply -f k8s/db.yml
```

## Test Applications

At development time we recommend you use the test applications set up as `main()` methods in `PetClinicIntegrationTests` (using the default H2 database and also adding Spring Boot Devtools), `MySqlTestApplication` and `PostgresIntegrationTests`. These are set up so that you can run the apps in your IDE to get fast feedback and also run the same classes as integration tests against the respective database. The MySQL integration tests use Testcontainers to start the database in a Docker container, and the Postgres tests use Docker Compose to do the same thing.

## Looking for Something in Particular?

| Spring Boot Configuration | Class or Java Property Files |
|---------------------------|------------------------------|
| The Main Class | [PetClinicApplication](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/PetClinicApplication.java) |
| Properties Files | [application.properties](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/resources) |
| Caching | [CacheConfiguration](https://github.com/spring-projects/spring-petclinic/blob/main/src/main/java/org/springframework/samples/petclinic/system/CacheConfiguration.java) |

## Interesting Spring Petclinic Branches and Forks

The Spring Petclinic "main" branch in the [spring-projects](https://github.com/spring-projects/spring-petclinic) GitHub org is the "canonical" implementation based on Spring Boot and Thymeleaf. There are [quite a few forks](https://spring-petclinic.github.io/docs/forks.html) in the GitHub org [spring-petclinic](https://github.com/spring-petclinic). If you are interested in using a different technology stack to implement the Pet Clinic, please join the community there.

## Interaction with Other Open-Source Projects

One of the best parts about working on the Spring Petclinic application is that we have the opportunity to work in direct contact with many Open Source projects. We found bugs/suggested improvements on various topics such as Spring, Spring Data, Bean Validation and even Eclipse! In many cases, they've been fixed/implemented in just a few days.
Here is a list of them:

| Name | Issue |
|------|-------|
| Spring JDBC: simplify usage of NamedParameterJdbcTemplate | [SPR-10256](https://github.com/spring-projects/spring-framework/issues/14889) and [SPR-10257](https://github.com/spring-projects/spring-framework/issues/14890) |
| Bean Validation / Hibernate Validator: simplify Maven dependencies and backward compatibility | [HV-790](https://hibernate.atlassian.net/browse/HV-790) and [HV-792](https://hibernate.atlassian.net/browse/HV-792) |
| Spring Data: provide more flexibility when working with JPQL queries | [DATAJPA-292](https://github.com/spring-projects/spring-data-jpa/issues/704) |

## Contributing

The [issue tracker](https://github.com/spring-projects/spring-petclinic/issues) is the preferred channel for bug reports, feature requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <https://editorconfig.org>. All commits must include a __Signed-off-by__ trailer at the end of each commit message to indicate that the contributor agrees to the Developer Certificate of Origin.
For additional details, please refer to the blog post [Hello DCO, Goodbye CLA: Simplifying Contributions to Spring](https://spring.io/blog/2025/01/06/hello-dco-goodbye-cla-simplifying-contributions-to-spring).

## License

The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).

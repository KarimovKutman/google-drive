# Google Drivve

Spring Boot 4.1.2 backend using Java 21, PostgreSQL, Spring Data JPA, Spring Security and JJWT.

## Requirements

- JDK 21
- PostgreSQL
- Internet access on the first Maven build so Maven can download dependencies

## 1. Configure local secrets

Copy the example properties file:

**Windows PowerShell**

```powershell
Copy-Item src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

**Linux / macOS**

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

Edit `application-local.properties` and set your PostgreSQL username/password. Generate a fresh Base64 JWT secret containing 64 random bytes.

**PowerShell secret generator**

```powershell
[Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(64))
```

Put the generated value in `JWT_SECRET`. Do not reuse a secret that has been shared or committed before.

The application expects a PostgreSQL database named `google_drive_db` by default. You can change the JDBC URL with the `DB_URL` environment variable.

## 2. Build and test

**Windows**

```powershell
.\mvnw.cmd clean verify
```

**Linux / macOS**

```bash
./mvnw clean verify
```

The first build downloads Maven and the project dependencies. Keep Maven's offline mode disabled for that first build. The automated Spring context test uses an in-memory H2 database and a test-only JWT secret, so `clean verify` does not require your local PostgreSQL server or local credentials.

## 3. Run

**Windows**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

## 4. IntelliJ IDEA

1. Open the project root (the folder containing `pom.xml`).
2. Set Project SDK and Maven JDK to Java 21.
3. Open the Maven tool window and reload all Maven projects.
4. Ensure Maven work offline is disabled, then run `clean verify`.

Do not add JJWT JAR files manually. `jjwt-api` is a compile dependency in `pom.xml`; Maven resolves the JWT imports when the project is imported successfully.

## Authentication endpoints

See [AUTH_API.md](AUTH_API.md) for register, login, refresh, logout and current-user endpoints.

## Secrets

`src/main/resources/application-local.properties` is intentionally ignored by Git. Keep credentials only in that local file or environment variables. The `.example` file contains placeholders, not working credentials.

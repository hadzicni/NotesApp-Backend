# 📝 NotesApp Backend

A full-featured **Spring Boot** backend API for a note-taking application. Supports notebooks, notes, tags, todos, PDF export, and advanced user roles. Designed for extensibility and clean RESTful architecture.

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![License](https://img.shields.io/badge/license-MIT-green)

---

## ✨ Features

- 🧾 CRUD for Notes, Notebooks, Tags & Todos
- 📄 PDF export support via `/export`
- 🔐 Role-based access control
- 📦 Organized architecture (Controller, Service, Repository, Entity)
- 🪝 Global exception handling & logging
- 🧰 Swagger / OpenAPI 3.0 integration
- ⚙️ Externalized configuration via `application.yml`

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- IDE: IntelliJ / VS Code / Eclipse

### 🛠️ Run the app

```bash
./mvnw spring-boot:run
```

Or via your IDE (run `NotesAppApplication`).

### 🌐 API Docs

Once running, visit:

```
http://localhost:9090/swagger-ui/index.html
```

---

## 📦 Endpoints Overview

> Full Swagger available at runtime – here's a quick overview:

- `GET /api/notes` – list all notes
- `POST /api/notes` – create note
- `GET /api/notebooks` – list notebooks
- `POST /api/tags` – create tag
- `GET /api/todos` – list todos
- `GET /api/meta` – get app metadata
- `GET /export/pdf` – export notes to PDF

Each entity has full CRUD support with proper validation & status codes.

---

## 🛡️ Security

- Role handling via JWT/OAuth2 (via `AuthenticationRoleConverter`)
- `CustomAccessDeniedHandler` & `CustomAuthenticationEntryPoint`
- Predefined roles in `Roles.java`

---

## ⚙️ Configuration

Edit `src/main/resources/application.yml`:

```yaml
server:
  port: 9090

spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

---

## 👨‍💻 Author

Made by **Nikola Hadzic**  
GitHub: [@hadzicni](https://github.com/hadzicni)

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.

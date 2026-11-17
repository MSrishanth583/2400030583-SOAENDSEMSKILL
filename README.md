# SOA and Microservices: Containerization

Practical submission for containerizing a Spring Boot CRUD microservice with Docker and Docker Compose. The service stores records in PostgreSQL and publishes its HTTP port as **8080** on the host.

## Build and run

From the repository root:

```bash
docker compose up --build -d
docker compose ps
```

Compose waits for PostgreSQL's health check before starting the service. The first Maven build downloads dependencies and may take a few minutes.

Verify the service:

```bash
curl http://localhost:8080/api/all
```

The endpoint initially returns an empty JSON array. Add a record and list it:

```bash
curl -X POST http://localhost:8080/api/items -H "Content-Type: application/json" -d '{"name":"Docker practical","description":"First containerized record"}'
curl http://localhost:8080/api/all
```

## CRUD endpoints

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET | `/api/all` | List all records |
| GET | `/api/items/{id}` | Read one record |
| POST | `/api/items` | Create a record |
| PUT | `/api/items/{id}` | Update a record |
| DELETE | `/api/items/{id}` | Delete a record |

Example request body: `{"name":"Item name","description":"Optional details"}`. The name is required.

## Docker commands

The root Dockerfile uses a Maven build stage to package the executable JAR, then copies only that JAR into a Java 17 runtime image.

```bash
docker build -t soa-2400030583-crud-service .
docker run --rm -p 8080:8081 soa-2400030583-crud-service
```

The standalone `docker run` command needs a reachable PostgreSQL database. For the complete application and database stack, use Docker Compose as described above.

Every push to `main` also runs the GitHub Actions workflow in `.github/workflows/publish-image.yml`. After it succeeds, the image is published to GitHub Container Registry:

```bash
docker pull ghcr.io/msrishanth583/2400030583-soaendsemskill:latest
```

The published image contains the API service. PostgreSQL is a separate service, so use the Compose file to run the complete stack locally.

Stop the stack with `docker compose down`. Data is stored in the named `postgres_data` volume and remains after stopping the containers. Remove the volume too with `docker compose down -v`.

## Architecture

- **crud-service:** Spring Boot REST API, container port 8081 (host port 8080)
- **postgresdb:** PostgreSQL 16 database on the private Compose network
- **postgres_data:** persistent database storage

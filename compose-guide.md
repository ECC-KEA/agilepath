# 🐳 Docker & Compose Guide – AgilePath

Denne guide forklarer, hvordan container-setup’et i AgilePath fungerer, og hvordan du arbejder med `docker-compose`,
Dockerfiles og Makefile på en struktureret måde gennem profilerne `dev`, `ci` og `prod`

---

## 🚀 Formål

Setup'et containeriserer hele systemet – backend, frontend, database og caching – med fleksibel støtte for:

- **Udvikling** (`dev`): Hot reload, debugging, udviklervenlig opsætning
- **CI** (`ci`): Byg og kør tests i headless-miljø (uden udviklingsfeatures)
- **Produktion** (`prod`): Build og kørsel af optimeret, statisk site og backend

---

## 📦 `docker-compose.yml` struktur

### Profiler

Hver service er knyttet til en eller flere profiler via `profiles:` i `docker-compose.yml`. Du aktiverer profiler via
`docker compose --profile <name> up`.

| Service           | Profiler            | Funktion                                   |
|-------------------|---------------------|--------------------------------------------|
| `postgres`        | alle                | Persistent database                        |
| `redis`           | alle                | Cache service                              |
| `redisinsight`    | `dev`               | UI til Redis-inspektion                    |
| `backend`         | `dev`, `ci`, `prod` | Kotlin/Spring Boot app                     |
| `frontend`        | `dev`               | React frontend med hot reload              |
| `frontend_static` | `ci`, `prod`        | Optimeret statisk build serveret via Nginx |

---

### Volumes

Der er tre named volumes:

- `postgres_data`: Persisterer database
- `backend-gradle-cache`: Cacher Gradle-dependencies
- `frontend-node-modules`: Bruges ved behov for lokal node_modules caching

---

## 🧱 Dockerfiles (kort fortalt)

### `backend/Dockerfile`

- 2-staget build:
    1. Builder stage med `gradle:8.5.0-jdk21-alpine`
    2. Runtime stage med `eclipse-temurin:21-jdk-alpine`
- Producerer en `app.jar`, som køres med `java -jar`

### `frontend/Dockerfile`

- Kører Vite dev-server (`npm run dev -- --host`)
- Bruges kun i `dev`-profilen
- Volume sync med `develop.watch` i `compose.yml` tillader live reload

### `frontend/Dockerfile.nginx`

- Bygger React-appen via `npm run build`
- Serverer resultatet via en tilpasset `nginx.conf`
- Bruges i `ci` og `prod`

---

## 🛠️ Makefile: Nem profilstyring

`Makefile` indeholder targets til at styre de tre profiler samt oprydning:

| Mål          | Effekt                                            |
|--------------|---------------------------------------------------|
| `make dev`   | Starter `dev`-profilen med hot reload (`--watch`) |
| `make ci`    | Starter `ci`-profilen i headless mode (`-d`)      |
| `make prod`  | Starter `prod`-profilen (Nginx + backend)         |
| `make stop`  | Stopper alle profiler                             |
| `make clean` | Pruner ubrugte images og netværk                  |
| `make nuke`  | Fuld reset inkl. volumes                          |

---

## 💡 Udviklingstips

- Brug make dev til lokal udvikling med hot reload på både frontend og backend
- Hvis du ændrer Dockerfile eller profiler i compose.yml, husk at opdatere Makefile og dokumentationen her
- I development er hele systemet i watch mode, så logs fra alle services vises i terminalen løbende
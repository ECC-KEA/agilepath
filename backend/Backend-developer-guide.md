# AgilePath – Developer Guide

Dette dokument forklarer projektets arkitektoniske principper, package-struktur og konventioner samt tips og tricks til Kotlin.
---
## 🧱 Arkitekturprincipper

AgilePath bygger på principper fra Clean Architecture og Ports and Adapters (også kendt som Hexagonal Architecture).

Disse principper hjælper os med at skabe en modulær og testbar kodebase, hvor domænelogik holdes adskilt fra infrastruktur, frameworks og anden platformafhængig kode.

### Clean Architecture

- Koden opdeles i lag, hvor domænet er centrum.

- Forretningslogik er isoleret i model/ og service/, og al adgang til ydre teknologier (API, DB, UI) går via klare grænseflader.

- Flowet går indad: UI kalder application/use case → service → repository interface.

### Ports and Adapters (Hexagonal Architecture)

- **Ports:** Interfaces som definerer adgang til vores domæne (fx GamificationRepository)

- **Adapters:** Konkrete implementeringer (fx XpEntity, JPA-mapping) i repository/entity/, infrastructure/github, osv.

Dette gør det nemt at erstatte teknologi (f.eks. database eller ekstern API) uden at påvirke domænelogikken.

Med denne arkitektur opnår vi:

- Høj testbarhed (ingen afhængighed til frameworks i domæne- og application-lag)

- Lav kobling og høj sammenhæng i koden (separation of concerns)

- Fleksibilitet til at udvikle hvert domæne isoleret

## 📦 Projektstruktur (overview)
```markdown
src/
└── main/
├── kotlin/
│   └── dev.ecckea.agilepath.backend/
│       ├── BackendApplication.kt
│       ├── config/
│       ├── infrastructure/
│       ├── shared/
│       └── domain/
│           ├── user/
│           ├── board/
│           ├── issue/
│           └── gamification/
└── resources/
├── application.yml
└── static/
└── templates/
```

---

## 🧩 Package-beskrivelser

### `config/`
Indeholder konfigurationer til fx:
- `SecurityConfig.kt`: JWT validation, adgangskontrol
- `SwaggerConfig.kt`: OpenAPI dokumentation
- `CorsConfig.kt`: CORS rules for frontend-integration
- `RedisConfig.kt` konfiguration af Redis til cache

---

### `infrastructure/`
Adaptere til eksterne systemer:
- `github/`: Kald til GitHub API
- `openai/`: Kald til OpenAI API
- `persistence/`: Evt. teknologispecifik lagring/logik

Disse kalder ind til interfaces i `repository/`-lagene i domænerne.

---

### `shared/`
Fælles kode der bruges på tværs af domæner:
- `dto/`: Globale eller generiske DTO’er
- `mapper/`: Fælles mappere (hvis ikke bundet til ét domæne)
- `errors/`: Exceptions og error responses
- `utils/`: Hjælpefunktioner

---

### `domain/<feature>/`
Hvert domæne har sin egen package med fuld vertikal opdeling:

```markdown
domain/
└── gamification/
    ├── controller/                         // REST API endpoints
    │   ├── GamificationController.kt
    │   └── LeaderboardController.kt
    ├── application/                        // Use cases
    │   ├── AwardXpHandler.kt
    │   └── CalculateLevelHandler.kt
    ├── service/                            // Forretningslogik
    │   ├── GamificationService.kt
    │   └── LevelService.kt
    ├── repository/                         // Interfaces + JPA
    │   ├── GamificationRepository.kt
    │   └── entity/                         // JPA-entities
    │       ├── XpEntity.kt
    │       └── BadgeEntity.kt
    ├── model/                              // Domænemodeller
    │   ├── Xp.kt
    │   ├── Badge.kt
    └── dto/                                // Data transfer objekter (request og response)
        ├── AwardXpRequest.kt
        ├── AwardXpResponse.kt
        └── BadgeResponse.kt
```
--- 
### 🧠 Application-laget

application/ indeholder use cases, som er de centrale "entry points" til domænets funktionalitet. De beskriver hvad systemet skal gøre – ikke nødvendigvis hvordan det gøres teknisk.

Typiske karakteristika:

Ét use case per forretningsflow, fx AwardXpHandler.kt, CreateIssueHandler.kt

Koordinerer kald til services, repositories og eventuelle sideeffekter

Er designet til at være tynde, fokuserede og testbare

Eksempel:
```kotlin
class AwardXpHandler(
    private val gamificationService: GamificationService
) {
    fun handle(request: AwardXpRequest): AwardXpResponse {
        val xp = gamificationService.awardXp(request.userId, request.amount)
        return xp.toResponse()
    }
}
```
---

## ✅ Konventioner

🧱 Struktur og ansvar
- Følg domæneopdeling: domain/<name>/ med controller/, application/, service/, repository/, dto/, model/
- Controllers må kun kalde application/-laget, ikke service/ direkte
- Domænemodeller placeres i model/, og JPA-entities i repository/entity/
- Repositories er defineret som interfaces og implementeret via Spring Data JPA

🔤 Navngivning
- Brug PascalCase til klasser og filnavne: UserService.kt, GamificationController.kt
- Brug camelCase til funktioner og variabler: calculateTotal(), userName
- Brug UPPER_SNAKE_CASE til konstanter: MAX_XP, DEFAULT_TIMEOUT
- DTO’er bør hedde SomethingRequest / SomethingResponse
- Brug ental i modelnavne: Xp.kt, ikke Xps.kt
- Undgå navne som Data, Info, Stuff

🧪 Test
- Placér tests spejlet efter domænestrukturen
- Brug @SpringBootTest til integrationstests
- Brug Mockk til mocking i unit tests

Kotlin-specifikke konventioner
- Brug data class til DTO’er og domænemodeller (undtagen entities)
- Brug extension functions til mapping, fx .toDto(), .toEntity()
- Brug suspend-funktioner til IO-kald og integrationer
- Brug @Cacheable på dyre operationer, hvor konsistenskrav tillader det
- 

--- 

## 🤖 Kotlin-funktionaliteter og valg

### Hvornår skal man bruge `data class` vs. `class`

| Lag             | Type         | Forklaring |
|----------------|--------------|------------|
| `dto/`         | `data class` | DTO’er er simple, immutable data-containere, og skal kunne sammenlignes og logges nemt. |
| `model/`       | `data class` (oftest) | Domænemodeller er ofte immutable og bør kunne kopieres og sammenlignes. Brug almindelig `class` hvis de har kompleks adfærd. |
| `application/` | `class`       | Use cases indeholder logik og afhængigheder, og har derfor ikke fordel af `data class`. |
| `service/`     | `class`       | Services er stateless og operationelle. `data class` giver ikke værdi her. |
| `repository/`  | `interface` / `class` | Repository interfaces er kontrakter, ikke databærere. |
| `entity/`      | `class`       | JPA-entities skal være mutable og fungerer dårligt med `data class` (fx pga. `equals()`/`hashCode()` og proxier). |

> **Tommelregel:** Brug `data class` når klassen **kun repræsenterer data**, og almindelig `class` når den **repræsenterer adfærd eller indeholder logik og tilstand**.

### Kotlin-fordele i praksis
Kotlin giver en række moderne sprogfeatures, som reducerer boilerplate og forbedrer læsbarheden:
### data class: 
- Kotlin’s data class-konstrukt gør det nemt at arbejde med immutable objekter, som kun indeholder data. Den genererer automatisk equals(), hashCode(), toString(), componentN() og copy()-metoder, hvilket gør dem ideelle til:
- DTO’er i API’er
- Domænemodeller uden kompleks adfærd
- Testobjekter, hvor lighed og kopiering er vigtig

**Fordele:**
- Objekter kan nemt sammenlignes på værdi frem for reference
- Understøtter immutability, hvilket reducerer bugs og sideeffekter
- copy() gør det nemt at skabe nye objekter med ændrede felter

**Eksempel:**
```kotlin
data class Xp(val userId: UUID, val amount: Int)
val original = Xp(UUID.randomUUID(), 50)
val updated = original.copy(amount = 100)
```

### unit: 
Kotlin’s svar på void, men som faktisk er en type. Det gør det nemmere at behandle funktionskald som værdier, især i funktionel programmering og tests.
```kotlin
val result: Unit = println("Hello")
```

### Null-safety: 
Tydelig adskillelse af nullable og non-nullable typer.
```kotlin
val name: String = "Alice"
val nickname: String? = null
println(nickname?.length ?: 0) // Safe call med Elvis-operator
```

### Smart casts: 
Automatisk casting efter is-check.
```kotlin
fun printLength(input: Any) {
if (input is String) {
println(input.length) // Smart cast til String
}
}
```

### Named/default arguments:
```kotlin
fun greet(name: String, title: String = "Student") {
println("Hello, $title $name")
}
greet("Chris") // Hello, Student Chris
greet(name = "Chris", title = "Developer") // Hello, Developer Chris
```

### Destructuring:
```kotlin
data class User(val id: Int, val name: String)
val (id, name) = User(1, "Emma")
println("$id -> $name")
```

### Top-level functions: 
Funktioner udenfor klasser.
```kotlin
// File: DateUtils.kt
fun today(): LocalDate = LocalDate.now()
```

### Sealed classes: 
Bruges til at udtrykke lukkede hierarkier som fejltyper, events eller states.

Eksempel på when() med sealed class:

```kotlin
sealed class LoginResult
object Success : LoginResult()
data class Failure(val reason: String) : LoginResult()
object LockedOut : LoginResult()

fun showMessage(result: LoginResult): String = when (result) {
    is Success -> "Velkommen!"
    is Failure -> "Login fejlede: ${result.reason}"
    is LockedOut -> "Din konto er låst"
    else -> "Error"
}
```

---

## 🔁 Kotlin Coroutines

Vi anvender **Kotlin Coroutines** til at skrive asynkron kode med en synkron struktur. Det giver os mulighed for at håndtere IO-tunge operationer (som databasekald, netværkskald og caching) uden at blokere tråde – og uden kompleksiteten fra reactive streams.

### Fordele ved Coroutines
- Nem at læse og forstå (frem for callback- eller reactive-baserede løsninger)
- Let at teste og debugge
- Understøttes bredt i Kotlin-økosystemet (Spring, OpenAI SDK, HTTP-klienter, m.fl.)

### Anvendelse i AgilePath
- Alle repository- og servicefunktioner der udfører IO-operationer er markeret med `suspend`
- Vi bruger `runBlocking` eller `coroutineScope` i test og bootstrapping hvor nødvendigt

Eksempel:
```kotlin
suspend fun getUserProfile(userId: UUID): UserProfile {
    val user = userRepository.findById(userId)
    val stats = statsService.fetchStats(userId)
    return UserProfile(user, stats)
}
```

Spring Boot 3 understøtter Coroutines direkte i Web-kontrollere og services via WebFlux eller Servlet-stacken.

---

## 🧠 Caching med Redis

Vi bruger Redis som cachelag for performance-intensive data, der er kostbare at hente eller beregne, men ikke ændrer sig ofte. Eksempler inkluderer statistik, brugerdata og leaderboard-elementer.

### Brug
- Spring Cache abstraction anvendes med `@EnableCaching`
- Brug af `@Cacheable`, `@CacheEvict`, `@CachePut` styres direkte på metodeniveau

### Eksempel: Cache XP-sum
```kotlin
@Cacheable("userXp")
suspend fun getTotalXp(userId: UUID): Int = xpRepository.findTotalXp(userId)
```
---

## 🔧 Logging

Vi bruger SLF4J sammen med Kotlin og Spring Boot til at lave ensartet og nem logging i hele backenden.
For at logge i en klasse, skal du blot lade den arve fra `Logged`, som automatisk giver dig adgang til en logger:

### ✅ Sådan logger du

```kotlin
class RegisterUserHandler(...) : Logged() {
    fun handle(request: UserSignupRequest) {
        log.info("Registrerer bruger med email: {}", request.email)
    }
}
```

### 📋 Logging pr. miljø

Logniveauer er sat i `application.yml` og varierer afhængigt af miljø (dev, test, prod).
I `dev` bruger vi f.eks. `DEBUG`, så vi kan se detaljerede beskeder under udvikling.
Mens vi i `test` og `prod` i udgangspunktet bruger `INFO` og opefter.

### 🧠 Retningslinjer for logging

| Niveau    | Bruges til                                      |
|-----------|--------------------------------------------------|
| `TRACE`   | Meget detaljeret debugging (bruges sjældent)     |
| `DEBUG`   | Udviklingsinformationer og mellemtrin            |
| `INFO`    | Almindelige hændelser (f.eks. bruger oprettet)   |
| `WARN`    | Noget uventet, men som ikke giver fejl           |
| `ERROR`   | Fejl eller undtagelser, der kræver opmærksomhed  |

### 💡 Gode råd til logging

- Tilføj altid relevant kontekst i dine logs:
  ```kotlin
  log.info("Projekt oprettet: navn={}, ejerId={}", name, userId)
  ```
- Log aldrig følsomme data som adgangskoder eller tokens
- Vælg det rette logniveau så logs er informative uden at støje
- Brug `log.info("foo={}, bar={}", ...)` fremfor at sammenkæde strings
---

### ✅ Validation

Vi bruger annotations som `@field:NotBlank`, `@field:Email` og `@field:Schema` direkte i vores DTO-klasser for at sikre korrekt inputvalidering og automatisk dokumentation i Swagger.

#### 🛠 Brug i DTO’er (eksempel)
```kotlin
data class UserSignupRequest(
    @field:Schema(description = "Brugernavn som vises offentligt", example = "sprintmaster42")
    @field:NotBlank(message = "'username' må ikke være tom")
    val username: String,

    @field:Schema(description = "Brugerens email-adresse", example = "example@kea.dk")
    @field:NotBlank(message = "'email' er tom eller mangler")
    @field:Email(message = "Skal være en gyldig email-adresse")
    val email: String,

    @field:Schema(description = "Adgangskode til kontoen", example = "MySecret123!")
    @field:NotBlank(message = "'password' må ikke være tom")
    @field:Size(min = 8, message = "Adgangskode skal være mindst 8 tegn lang")
    val password: String
)
```

#### ⚙️ Validering i controller
```kotlin
@PostMapping("/signup")
suspend fun signUp(@Valid @RequestBody request: UserSignupRequest): ResponseEntity<UserDto> {
    return ResponseEntity.ok(userAuthService.registerUser(request))
}
```

Spring håndterer automatisk `400 Bad Request` med fejlbeskeder hvis validering fejler.

#### ❓ Skal man bruge `@NotNull` i Kotlin?

Som udgangspunkt nej:
- I Kotlin er `val navn: String` allerede **non-nullable**
- Derfor er `@NotNull` ofte overflødig – brug hellere `@NotBlank` på strings

Brug dog `@NotNull` hvis feltet er nullable i din model:
```kotlin
val alder: Int? // brug @field:NotNull hvis det skal være påkrævet
```

#### 📋 Anbefaling

| Type             | Anbefalet annotation        |
|------------------|------------------------------|
| `String`         | `@NotBlank`                 |
| `String?`        | `@NotNull` + `@NotBlank`     |
| `Int?`, `UUID?`  | `@NotNull`                  |
| Dokumentation    | `@Schema(description = …)`  |

---

## 🛠 TODO / Tips
- Hold `shared/` minimal og fokuseret
- Overvej test-data builders for større modeller
- Vær opmærksom på at `RequestContext` og `SecurityContext` kan være udfordret når man benytter Coroutines på grund af trådskifte

---
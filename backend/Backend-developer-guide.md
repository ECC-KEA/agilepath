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

# 🔁 Kotlin Coroutines

## Hvad er Kotlin Coroutines?

Kotlin Coroutines er et kraftfuldt værktøj til asynkron programmering, der gør det muligt at skrive ikke-blokerende kode
i en sekventiel, letlæselig form. Coroutines fungerer som letvægts-tråde, der kan suspenderes og genoptages uden at
blokere den underliggende tråd. Det gør dem ideelle til IO-operationer og langvarige opgaver som f.eks. database- eller
netværkskald.

## På overfladen: Sekventiel syntax for asynkron kode

Det unikke ved coroutines er, at asynkron kode kan skrives som synkron kode. I stedet for komplekse callback-kæder,
Futures eller reactive streams, kan vi skrive ren, sekventiel kode:

```kotlin
suspend fun getUserProfile(userId: String): UserProfile {
    val user = userRepository.findOneById(userId) ?: throw ResourceNotFoundException("User not found")
    val stats = statsService.fetchStats(userId)
    val preferences = preferencesService.getPreferences(userId)

    return UserProfile(user.toModel(), stats, preferences)
}
```

Selvom denne kode involverer potentielt langvarige operationer, blokerer den ikke nogen tråde. Når en suspend-funktion
rammer en IO-operation, suspenderes den, og tråden kan arbejde videre med andre coroutines. Når resultatet er klar,
genoptages funktionen fra det sted, den blev suspenderet.

### Nøgleelementer i coroutines:

1. **`suspend`**: Markerer en funktion som værende i stand til at suspendere sin udførelse uden at blokere tråden.

2. **Coroutine Builders**: Funktioner som `launch`, `async`, og `runBlocking`, der starter coroutines.

3. **Coroutine Context og Dispatchers**: Definerer i hvilken kontekst og på hvilke tråde coroutines skal køres.

4. **Coroutine Scope**: Definerer levetiden for coroutines og sikrer, at de ryddes op korrekt.

## Under motorhjelmen: Hvordan coroutines virker

Når en `suspend`-funktion kaldes, genererer Kotlin-kompilatoren en state machine, der holder styr på, hvor i funktionen
udførelsen befinder sig. Denne state machine gør det muligt at gemme funktionens tilstand, når den suspenderes, og
genoptage udførelsen fra samme punkt senere.

### Konceptuelt eksempel:

```kotlin
// Hvad vi skriver:
suspend fun fetchUserData(userId: String): UserData {
    val basicInfo = userService.getBasicInfo(userId)
    val extendedInfo = userService.getExtendedInfo(userId)
    return UserData(basicInfo, extendedInfo)
}

// Hvad kompilatoren (konceptuelt) genererer:
fun fetchUserData(userId: String, continuation: Continuation<UserData>): Any {
    // State machine for at holde styr på, hvor vi er i funktionen
    val stateMachine = continuation as? FetchUserDataStateMachine ?: FetchUserDataStateMachine(userId, continuation)

    when (stateMachine.label) {
        0 -> { // Start
            stateMachine.label = 1
            return userService.getBasicInfo(userId, stateMachine)
        }
        1 -> { // Efter getBasicInfo
            val basicInfo = stateMachine.result as BasicInfo
            stateMachine.label = 2
            return userService.getExtendedInfo(userId, stateMachine)
        }
        2 -> { // Efter getExtendedInfo
            val extendedInfo = stateMachine.result as ExtendedInfo
            val basicInfo = stateMachine.basicInfo
            return UserData(basicInfo, extendedInfo)
        }
    }
}
```

Dette er naturligvis en simplificeret version, men den illustrerer kernekonceptet. Coroutines bygger på *
*continuation-passing style (CPS)**, hvor hver funktion tager en "continuation" som argument, der repræsenterer "hvad
der skal ske bagefter".

### Dispatchers: Styring af tråde

Dispatchers bestemmer, hvilke tråde coroutines kører på:

- **Dispatchers.Default**: Optimeret til CPU-intensive opgaver.
- **Dispatchers.IO**: Optimeret til IO-operationer (netværk, disk, database).
- **Dispatchers.Main**: Bruges i UI-applikationer (fx Android).

I AgilePath bruger vi `withIO` utility-funktionen til at sikre, at IO-operationer kører på den rigtige dispatcher:

```kotlin
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.IO, block)
```

## Hvorfor coroutines er smarte

### 1. Ressourceeffektivitet

Coroutines bruger meget få systemressourcer. Hvor en tråd typisk kræver 1 MB stack-memory, kan tusindvis af coroutines
dele en håndfuld tråde.

### 2. Fejlhåndtering med struktur

Coroutines bruger structured concurrency. Det betyder, at fejl i én coroutine kan håndteres centralt, og scopes sikrer
korrekt oprydning:

```kotlin
coroutineScope {
    try {
        val user = async { userRepository.findOneById(userId) }
        val stats = async { statsService.fetchStats(userId) }
        UserProfile(user.await(), stats.await())
    } catch (e: Exception) {
        log.error("Failed to fetch user profile", e)
        throw e
    }
}
```

### 3. Testbarhed

Coroutines er simple at teste, fx med runTest:

```kotlin
@Test
fun `should fetch user profile`() = runTest {
        val userId = "123"
        val mockUser = User("123", "user@example.com")
        val mockStats = UserStats(5, 10)

        coEvery { userRepository.findOneById(userId) } returns mockUser
        coEvery { statsService.fetchStats(userId) } returns mockStats

        val result = userService.getUserProfile(userId)

        assertEquals("user@example.com", result.email)
        assertEquals(5, result.stats.points)
    }

```

### 4. Sekventiel læsbarhed

Asynkrone flows kan ofte blive uoverskuelige i callbacks eller flatMap()-kæder. Coroutines bevarer en lineær struktur,
hvilket gør koden mere vedligeholdelsesvenlig og intuitiv.

## Hvornår er coroutines særligt vigtige?

### 1. Høj samtidighed

Når applikationen skal håndtere mange samtidige anmodninger, giver coroutines en meget mere skalerbar løsning end
tråd-per-anmodning-modellen.

### 2. IO-tunge applikationer

De fleste backends, inkl. AgilePath, bruger mange database- og netværkskald. Coroutines sikrer, at disse operationer
ikke blokerer ressourcer unødigt.

```kotlin
// Uden coroutines ville dette blokere tråde under ventetiden
suspend fun getOrCreate(principal: UserPrincipal): User = withIO {
        val exists = userRepository.existsById(principal.id)
        if (!exists) {
            userRepository.save(principal.toEntity())
        }
        userRepository.findOneById(principal.id)?.toModel() ?: throw ResourceNotFoundException("User not found")
    }
```

### 3. Kompleks logik med flere async steps

Coroutines gør det let at koordinere flere asynkrone operationer uden callback hell:

```kotlin
suspend fun processOrder(orderId: String) {
    val order = orderRepository.findById(orderId) ?: throw NotFoundException()
    val paymentStatus = paymentService.verifyPayment(order.paymentId)

    if (paymentStatus.isConfirmed) {
        val inventory = inventoryService.checkAvailability(order.items)
        if (inventory.allAvailable) {
            val shippingDetails = shippingService.createShipment(order)
            orderRepository.updateStatus(orderId, OrderStatus.PROCESSING)
            notificationService.notifyCustomer(order.customerId, shippingDetails)
        }
    }
}
```

## Coroutines i Spring Boot

Spring Boot 3+ understøtter `suspend` direkte i controllere og services — også i Servlet stack:

```kotlin
@RestController
@RequestMapping("/auth")
class UserAuthController(private val userAuthApplication: UserAuthApplication) {

    @GetMapping("/profile")
    suspend fun getProfile(): UserResponse {
        val user = userAuthApplication.getCurrentUser(currentUser())
        return user.toDTO()
    }
}
```

## Best Practices i AgilePath

1. **Marker alle IO-funktioner med `suspend`**
   Alle funktioner, der involverer database-operationer, netværkskald, filsystemadgang eller andre potentielt langvarige
   operationer, skal markeres med `suspend`.

2. **Brug `withIO` til eksplicitte IO-operationer**
   For at sikre at IO-operationer ikke blokerer tråde, pakkes de ind i vores `withIO`-funktion, der sikrer korrekt
   dispatcher-brug.

3. **Udnyt struktureret concurrency**
   Brug `coroutineScope` og `supervisorScope` til at håndtere relaterede coroutines som en enhed.

4. **Brug timeout og cancellation**
   Implementer timeouts for langvarige operationer for at undgå at blokere ressourcer for længe:
   ```kotlin
   withTimeout(5000L) {
       service.longRunningOperation()
   }
   ```

---

## 🧠 Redis Caching i Spring Boot med Kotlin

### Introduktion

Redis er en in-memory datastrukturlager, der kan anvendes som cache, database eller message broker. I Spring Boot
anvendes Redis ofte som et cachelag for at forbedre applikationsydelsen ved at gemme resultater af dyre operationer og
beregninger.

### Nøglekomponenter i implementeringen

#### ***1. CacheConfig Klasse***

Dette er hovedkonfigurationsklassen, der opsætter Redis som cachebackend:

```kotlin
@Configuration
@EnableCaching
class CacheConfig : Logged() {
    // Konfiguration her
}
```

#### Centrale elementer i konfigurationen:

- **@EnableCaching**: Aktiverer Springs caching-infrastruktur
- **customObjectMapper()**: Konfigurerer en ObjectMapper til korrekt serialisering/deserialisering af komplekse objekter
- **cacheManager()**: Opsætter Redis som cache med standardindstillinger:
    - 15 minutters Time-To-Live (TTL) for cache-indgange
    - StringRedisSerializer til nøgler
    - GenericJackson2JsonRedisSerializer til værdier
- **cacheErrorHandler()**: Håndterer fejl ved cacheoperationer med detaljeret logning
- **redisTemplate()**: Giver direkte adgang til Redis gennem et template

#### ***2. RedisCacheService***

En wrapper-service, der forenkler direkte Redis-operationer:

```kotlin
@Component
class RedisCacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun get(key: String): Any? = redisTemplate.opsForValue().get(key)
    fun set(key: String, value: Any, ttlMinutes: Long = 15) {
        redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES)
    }
    fun delete(key: String) = redisTemplate.delete(key)
}
```

#### ***3. Model klasser og JSON-serialisering***

For at cache-objekter kan serialiseres/deserialiseres korrekt, skal model-klasserne annoteres:

```kotlin
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonTypeName("dev.ecckea.agilepath.backend.domain.user.model.User")
data class User(
    val id: String,
    val email: String,
    // Andre felter
)
```

## Anvendelsesmetoder

Der er to primære måder at bruge Redis-caching på i implementeringen:

#### ***1. Annotations-baseret caching (deklarativ)***

Spring Cache abstraktion anvendes med `@EnableCaching` og annotations på metodeniveau:

```kotlin
@Cacheable("users", key = "#principal.id")
fun getOrCreate(principal: UserPrincipal): User {
    // Implementeringslogik
}
```

Andre nyttige annotations:

- `@CacheEvict`: Fjerner indgange fra cachen
- `@CachePut`: Opdaterer cache uden at påvirke metodens udførelse

#### ***2. Direkte cache-manipulation (imperativ)***

Gennem RedisCacheService for mere kontrol:

```kotlin
// Gemme i cache
cacheService.set("user:${userId}", user)

// Hente fra cache
val user = cacheService.get("user:${userId}") as User?

// Fjerne fra cache
cacheService.delete("user:${userId}")
```

### Typiske Use Cases

- **Brugerdata**: Cache brugerprofiler for at reducere databaseforespørgsler
- **Konfigurationsdata**: Cache af sjældent ændret konfiguration
- **Beregningstunge resultater**: Resultater af komplekse beregninger eller aggregeringer
- **Leaderboards og statistikker**: Hyppigt læste, men sjældent ændrede data
- **API-svar**: Cache af data fra eksterne API'er

### Serialisering og Type-håndtering

En kritisk del af Redis-caching er korrekt serialisering/deserialisering, især for Kotlin data classes:

1. **Type Information**: ObjectMapper konfigureres med `activateDefaultTyping` for at bevare typeoplysninger
2. **Moduler**:

- `JavaTimeModule`: Understøtter moderne dato/tid-typer
- `KotlinModule`: Forbedrer Kotlin-kompatibilitet (data classes, nullability, default values)

3. **Type Validation**: Begrænser deserialisering til sikre pakker via `BasicPolymorphicTypeValidator`

### Fejlhåndtering

Implementeringen inkluderer robust fejlhåndtering:

```kotlin
@Bean
fun cacheErrorHandler(): CacheErrorHandler {
    return object : SimpleCacheErrorHandler() {
        override fun handleCacheGetError(/* parametre */) {
            log.error("Cache get error for key $key in cache ${cache.name}", exception)
            super.handleCacheGetError(exception, cache, key)
        }
        // Andre fejlhåndteringsmetoder
    }
}
```

### Bedste Praksis

1. **Definer passende TTL**: Balancer mellem friskhed og performance (standard: 15 minutter)
2. **Brug nøglenavnekonventioner**: Strukturerede cachenøgler for bedre organisering (fx "users:123")

### Eksempel: Caching af brugerdata

```kotlin
@Service
class UserService(
    private val userRepository: UserRepository,
) : Logged() {
    @Cacheable("users", key = "#principal.id")
    fun getOrCreate(principal: UserPrincipal): User {
        val exists = userRepository.existsById(principal.id)
        if (!exists) {
            log.info("Bruger med id ${principal.id} findes ikke, opretter den")
            userRepository.save(principal.toEntity())
        }
        return userRepository.findOneById(principal.id)?.toModel()
            ?: throw ResourceNotFoundException("Bruger med id ${principal.id} ikke fundet")
    }
}
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
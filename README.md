# Employee API: Spring Boot Revision Project

Small CRUD API built to revise Spring Boot's layered structure. Java 17, Spring Boot 3.3.x, Spring Data JPA, H2 in-memory DB.

## Run it

```bash
mvn spring-boot:run          # starts on http://localhost:8080
mvn test                     # runs the MockMvc tests
```

H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:employeedb`, user `sa`, blank password)

```bash
# create
curl -X POST localhost:8080/api/employees -H "Content-Type: application/json" \
  -d '{"name":"Asha","email":"asha@example.com","department":"Engineering","salary":90000}'
# list / filter / get / update / delete
curl localhost:8080/api/employees
curl "localhost:8080/api/employees?department=engineering"
curl localhost:8080/api/employees/1
curl -X PUT localhost:8080/api/employees/1 -H "Content-Type: application/json" \
  -d '{"name":"Asha K","email":"asha@example.com","department":"Platform","salary":95000}'
curl -X DELETE localhost:8080/api/employees/1
```

## Folder structure

```
employee-api/
├── pom.xml                                  # deps + build (like package.json)
└── src/
    ├── main/
    │   ├── java/com/example/employeeapi/
    │   │   ├── EmployeeApiApplication.java  # entry point, @SpringBootApplication
    │   │   ├── controller/                  # HTTP layer (routes)
    │   │   ├── service/                     # business logic
    │   │   ├── repository/                  # DB access (interfaces)
    │   │   ├── entity/                      # DB-mapped classes
    │   │   ├── dto/                         # request/response shapes
    │   │   └── exception/                   # custom exceptions + global handler
    │   └── resources/
    │       └── application.properties       # config (like .env + config module)
    └── test/java/...                        # tests
```

Request flow: **Controller → Service → Repository → DB**, and back as DTOs.

## NestJS to Spring Boot cheat sheet

| NestJS | Spring Boot |
|---|---|
| `main.ts` + `AppModule` | `@SpringBootApplication` main class (no modules; component scan finds beans) |
| `@Controller('employees')` | `@RestController` + `@RequestMapping("/api/employees")` |
| `@Get() @Post() @Put() @Delete()` | `@GetMapping @PostMapping @PutMapping @DeleteMapping` |
| `@Param() @Query() @Body()` | `@PathVariable @RequestParam @RequestBody` |
| `@Injectable()` provider | `@Service` / `@Component` / `@Repository` |
| Constructor DI | Constructor injection (same idea, preferred over `@Autowired` on fields) |
| DTO + class-validator + `ValidationPipe` | Record/class + Bean Validation (`@NotBlank`, `@Email`) + `@Valid` |
| Exception filters | `@RestControllerAdvice` + `@ExceptionHandler` |
| TypeORM entity / Mongoose schema | `@Entity` (JPA/Hibernate) |
| TypeORM repository | `JpaRepository<Entity, Id>` (implementation auto-generated) |
| `package.json` | `pom.xml` (Maven) |
| `.env` / ConfigModule | `application.properties` / `application.yml`, `@Value`, profiles |
| Guards / Interceptors / Middleware | Spring Security filters / `HandlerInterceptor` / servlet `Filter` |

## What each piece demonstrates

- **`@SpringBootApplication`**: `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`. Scan starts at this class's package.
- **Auto-configuration**: sees `h2` and `data-jpa` on the classpath and configures DataSource, EntityManager and transaction manager for you.
- **Starters**: `spring-boot-starter-*` are curated dependency bundles; the parent POM manages versions.
- **IoC / DI**: Spring creates beans (singletons by default) and injects them through constructors.
- **Entity vs DTO**: entity maps the table; DTOs control what comes in and goes out (no leaking fields, no mass assignment).
- **Spring Data derived queries**: `findByDepartmentIgnoreCase` is turned into SQL from the method name.
- **`@Transactional`**: `update()` has no `save()` call; the managed entity is flushed on commit (dirty checking). `readOnly = true` on reads.
- **Validation flow**: `@Valid` fails → `MethodArgumentNotValidException` → handled in `GlobalExceptionHandler` → 400 with field errors.
- **Status codes**: POST returns 201 + `Location`; DELETE returns 204; missing id gives 404.

## Likely interview questions (with short answers)

1. **What does `@SpringBootApplication` do?** Combines `@Configuration`, `@EnableAutoConfiguration`, `@ComponentScan`.
2. **`@Component` vs `@Service` vs `@Repository` vs `@Controller`?** All are stereotypes that register beans. `@Repository` also translates persistence exceptions; `@Controller`/`@RestController` mark web handlers; `@Service` is semantic.
3. **`@Controller` vs `@RestController`?** `@RestController` = `@Controller` + `@ResponseBody`, so return values become JSON instead of view names.
4. **Constructor vs field injection?** Constructor: immutable `final` fields, easy unit testing, fails fast on missing deps. Field: hidden dependencies, needs reflection in tests.
5. **Default bean scope?** Singleton (one per application context). Others: prototype, request, session.
6. **How does auto-configuration work?** Conditional configs (`@ConditionalOnClass`, `@ConditionalOnMissingBean`) listed in `AutoConfiguration.imports`; they back off if you define your own bean.
7. **`@PathVariable` vs `@RequestParam`?** Path segment (`/employees/1`) vs query string (`?department=x`).
8. **How do you handle exceptions globally?** `@RestControllerAdvice` with `@ExceptionHandler` methods.
9. **What is `@Transactional` and where do you put it?** Wraps a method in a DB transaction; usually on service methods, not controllers. Rolls back on unchecked exceptions by default.
10. **`JpaRepository` vs `CrudRepository`?** `JpaRepository` adds paging/sorting, batch ops and `flush()`.
11. **`ddl-auto` options?** `none`, `validate`, `update`, `create`, `create-drop`. Use migrations (Flyway/Liquibase) in production.
12. **N+1 problem?** Lazy associations trigger one extra query per row; fix with `JOIN FETCH` or `@EntityGraph`.
13. **Application profiles?** `application-dev.properties`, activated with `spring.profiles.active=dev`.
14. **Why DTOs?** Decouple API contract from the DB model, hide fields, validate input.

## Good extensions to try if you have time

1. Swap H2 for PostgreSQL (change datasource properties + driver dependency).
2. Add pagination: `Page<Employee> findAll(Pageable pageable)` and `?page=0&size=10`.
3. Add a `Department` entity with `@ManyToOne` / `@OneToMany` to see relationships and the N+1 issue.
4. Add Spring Security with basic auth or JWT.

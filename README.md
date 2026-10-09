# Spring Boot Authentication & Authorization Demo

A small Spring Boot project for a teaching/demo video.

It demonstrates:

- Authentication using username + password
- BCrypt password hashing
- JWT generation after successful login
- JWT validation on protected APIs
- Authorization using roles
- `STUDENT` vs `ADMIN`
- `401 Unauthorized` vs `403 Forbidden`
- User data stored only in an in-memory `Map`
- No database

## Requirements

- Java 17+
- Maven 3.9+ (or use Maven Wrapper if you add one)
- Postman/curl

## Demo users

| Username | Password | Role |
|---|---|---|
| student | student123 | STUDENT |
| admin | admin123 | ADMIN |

Passwords are BCrypt encoded when the application starts.

## Run

```bash
mvn spring-boot:run
```

Or:

```bash
mvn clean package
java -jar target/spring-security-jwt-map-demo-0.0.1-SNAPSHOT.jar
```

Application:

```text
http://localhost:8080
```

## API 1 — Login

### Student login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"student","password":"student123"}'
```

You will receive a JWT token.

Copy the value of `token`.

### Admin login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## API 2 — Student profile

Replace `<STUDENT_JWT>` with the token from student login:

```bash
curl http://localhost:8080/api/student/profile \
  -H "Authorization: Bearer <STUDENT_JWT>"
```

Expected result:

```json
{
  "message": "Student API accessed successfully",
  "username": "student",
  "role": "ROLE_STUDENT"
}
```

## API 3 — Admin users

Use the admin token:

```bash
curl http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <ADMIN_JWT>"
```

Only ADMIN can access this endpoint.

## API 4 — Demonstrate Authorization failure

Try calling the admin API using the STUDENT token:

```bash
curl http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer <STUDENT_JWT>"
```

The request is authenticated, because the JWT is valid.

But the student does not have the ADMIN role.

Spring Security therefore returns:

```text
403 Forbidden
```

This is the best part to demonstrate Authorization in your video.

## API 5 — Demonstrate Authentication failure

Call a protected API without a token:

```bash
curl http://localhost:8080/api/student/profile
```

The user is not authenticated, so the request is rejected.

Depending on the Spring Security entry-point handling, this can be shown as `401 Unauthorized`.

## API 6 — Delete user

Only ADMIN can call:

```bash
curl -X DELETE http://localhost:8080/api/admin/users/student \
  -H "Authorization: Bearer <ADMIN_JWT>"
```

The user is removed from the in-memory Map.

Restarting the application recreates the demo users because there is no database.

## What to explain in your demo

Use this sequence:

1. "Authentication means — Who are you?"
2. Login with `student / student123`.
3. Backend verifies the credentials against the Map.
4. Password is stored as a BCrypt hash, not plain text.
5. Backend generates a JWT.
6. Client sends the JWT using `Authorization: Bearer <token>`.
7. JWT filter validates the token.
8. Spring Security identifies the user and role.
9. `/api/student/**` requires `STUDENT`.
10. `/api/admin/**` requires `ADMIN`.
11. Use the student token on `/api/admin/users` and show `403 Forbidden`.
12. Explain: valid identity does not automatically mean permission.

## Simple architecture

```text
             LOGIN
               |
               v
       username + password
               |
               v
      AuthenticationManager
               |
               v
       UserDetailsService
               |
               v
       In-memory Map<User>
               |
        password matches?
          /          \
        YES           NO
         |             |
         v             v
      Create JWT      401
         |
         v
   Protected API
         |
         v
   JWT Filter
         |
         v
   Authentication
         |
         v
   Role / Permission
       Check
       /    \
     YES     NO
      |       |
     200     403
```

## Important teaching note

This project intentionally uses a `Map` instead of a database so that the authentication and authorization flow is easy to understand.

For production applications, user data should normally be persisted in a database, secrets should come from secure configuration/secret management, and JWT handling should follow your application's security requirements.

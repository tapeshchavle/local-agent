# 5-Minute Demo Flow

## 1. Show the users

Say:

"Database use nahi kar raha hoon. Demo ko simple rakhne ke liye users ko Java Map mein store kiya hai."

```java
users.put("student", ...);
users.put("admin", ...);
```

## 2. Authentication

Call:

```text
POST /api/auth/login
```

Body:

```json
{
  "username": "student",
  "password": "student123"
}
```

Say:

"Backend credentials verify karta hai. Agar credentials correct hain, JWT generate hota hai."

## 3. JWT

Show:

```text
Authorization: Bearer <JWT>
```

Say:

"Ab client har protected request ke saath ye token send karega."

## 4. Authentication filter

Show `JwtAuthenticationFilter`.

Say:

"Ye filter request se JWT read karta hai, token validate karta hai aur valid hone par SecurityContext mein authenticated user set karta hai."

## 5. Authorization

Show:

```java
.requestMatchers("/api/student/**").hasRole("STUDENT")
.requestMatchers("/api/admin/**").hasRole("ADMIN")
```

Say:

"Yaha Authentication ke baad Authorization hota hai. Student sirf student APIs access kar sakta hai aur admin admin APIs."

## 6. Show 403

Use a STUDENT token:

```text
GET /api/admin/users
```

Result:

```text
403 Forbidden
```

Say:

"Token valid hai, so user authenticated hai. Lekin user ke paas ADMIN role nahi hai, isliye authorization fail hua."

## One-line conclusion

"Authentication tells us who you are; Authorization tells us what you are allowed to do."

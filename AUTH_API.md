# Authentication API

Base URL: `http://localhost:8080`

All requests and responses use JSON.

## Register

`POST /api/auth/register`

```json
{
  "name": "Kutman",
  "surname": "Karimov",
  "username": "kutman_dev",
  "email": "kutman@example.com",
  "password": "change-this-password"
}
```

Returns `201 Created` with an access token, a refresh token, their type/expiry metadata, and a safe user profile. The response never includes the password hash.

## Login

`POST /api/auth/login`

```json
{
  "email": "kutman@example.com",
  "password": "change-this-password"
}
```

Returns `200 OK` with a new access token and refresh token.

## Refresh access token

`POST /api/auth/refresh`

```json
{
  "refreshToken": "the-refresh-token-from-the-previous-response"
}
```

Returns a new token pair. The previous refresh token is revoked (rotation).

## Logout

`POST /api/auth/logout`

```json
{
  "refreshToken": "the-current-refresh-token"
}
```

Returns `204 No Content` and revokes that refresh token. An already issued access token remains valid until its short expiry (15 minutes).

## Current user profile

`GET /api/users/me`

Requires this header:

```text
Authorization: Bearer <accessToken>
```

Returns the authenticated user's public profile.

## Notes

- Access tokens expire after 15 minutes by default.
- Refresh tokens expire after 7 days by default and are stored in the database only as SHA-256 hashes.
- Passwords are stored as BCrypt hashes.
- `application-local.properties` contains machine-local secrets and must not be committed. It is already listed in `.gitignore`.
- The current implementation does not yet include email verification, SMS verification, Google Sign-In, rate limiting, or password-reset email flows. Those are separate features, not silently assumed to exist.

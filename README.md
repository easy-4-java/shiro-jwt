# shiro-jwt-extension

[English](./README.md) | [简体中文](./README.zh-CN.md)

JWT authentication and authorization extension for Apache Shiro, built on `shiro-biz` and `jwt-issuer-api` (easy4j). It provides JWT-aware filters (header `X-Authorization` or `token` parameter), stateful/stateless realms, principal repositories, credentials matching and i18n messages for Shiro-based applications.

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

**What it is**

`shiro-jwt-extension` brings JWT login to Shiro web applications:

- `JwtAuthenticatingFilter` (extends `TrustableRestAuthenticatingFilter` from `shiro-biz`) accepts JWTs from the `X-Authorization` header or the `token` request parameter.
- `JwtStatefulAuthorizingRealm` / `JwtStatelessAuthorizingRealm` cover session-based and stateless JWT authentication.
- `JwtPayloadRepository` / `JwtPrincipalRepository` / `JwtPayloadPrincipal` map JWT payloads (from `jwt-issuer-api`) onto the Shiro principal model.
- `JwtAuthorizationFilter` and `JwtWithinExpiryFilter` enforce authorization and expiry checks.
- `ShiroJwtMessageSource` provides i18n authentication/authorization messages (EN / zh-CN).

**What it is not**

- It is not a JWT signing/verification library — token issuance and parsing are delegated to `io.github.easy4j:jwt-issuer-api` (`JwtPayload`).
- It is not a Spring Boot starter; filters/realms must be wired into your Shiro configuration.

**Typical scenarios**

| Scenario | Description |
| :--- | :--- |
| REST API JWT authentication | `JwtAuthenticatingFilter` extracts the JWT from `X-Authorization` / `token` and authenticates it. |
| Stateless JWT services | `JwtStatelessAuthorizingRealm` for services without server-side sessions. |
| Session-based JWT | `JwtStatefulAuthorizingRealm` for classic session applications. |
| Expiry enforcement | `JwtWithinExpiryFilter` rejects requests whose JWT is no longer within the expiry window. |

## 2. Features & Status

| Capability | Status | Notes |
| :--- | :--- | :--- |
| JWT authenticating filter | Available | `JwtAuthenticatingFilter` — `X-Authorization` header or `token` parameter; extends `TrustableRestAuthenticatingFilter`. |
| Stateful / stateless realms | Available | `JwtStatefulAuthorizingRealm`, `JwtStatelessAuthorizingRealm`. |
| Payload & principal repository | Available | `JwtPayloadRepository` (interface), `JwtPrincipalRepository`, `JwtPayloadPrincipal`. |
| Credentials matching | Available | `JwtCredentialsMatcher`. |
| Authorization filters | Available | `JwtAuthorizationFilter`, `JwtWithinExpiryFilter` (`X-Authorization` / `token`). |
| Handlers | Available | `JwtAuthenticationFailureHandler`, `JwtAuthenticationSuccessHandler` (payload repository + expiry check), `JwtAuthorizationFailureHandler` (all `Ordered`). |
| Subject factory | Available | `JwtSubjectFactory` (session-creation enabled/disabled). |
| Tokens | Available | `JwtAuthenticationToken` (extends `DefaultAuthenticationToken`), `JwtAuthorizationToken`. |
| Exceptions | Available | `ExpiredJwtException`, `IncorrectJwtException`, `InvalidJwtToken`, `NotObtainedJwtException`. |
| i18n messages | Available | `messages.properties` (+ `en_US`, `zh_CN`) via `ShiroJwtMessageSource`. |
| Utilities | Available | `SubjectJwtUtils`, `JSONResult`, `StringUtils` (under `org.apache.shiro.spring.boot.utils`). |

> Status is reported as of `1.0.x.20260630-SNAPSHOT` on the `feature/1.0.x` branch.

## 3. Requirements & Compatibility

| Item | Version |
| :--- | :--- |
| JDK | 8+ |
| Maven | 3.0+ (Maven Wrapper 3.5.0 bundled) |
| Apache Shiro | 1.13.0 (`shiro-core`, `shiro-web`) |
| easy4j dependencies | `shiro-biz`, `jwt-issuer-api` (both `1.0.x.20260630-SNAPSHOT`) |
| JSON | fastjson 2.0.62, jackson-databind 2.17.2 |
| Other | spring-context / spring-web, commons-lang3, guava, javax.servlet-api 4.0.1 |

**Version lines**

| Branch | JDK baseline | Version pattern |
| :--- | :--- | :--- |
| `feature/1.0.x` | JDK 8 | `1.0.x.*` |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` |

## 4. Architecture & Modules

```text
 Client (JWT in X-Authorization header / token parameter)
        |
        v
 JwtAuthenticatingFilter (authc)
        |  createJwtToken() -> JwtAuthenticationToken
        v
 JwtStatefulAuthorizingRealm / JwtStatelessAuthorizingRealm
        |  JwtPrincipalRepository -> JwtPayloadRepository (jwt-issuer-api)
        |  JwtCredentialsMatcher
        v
 Subject (JwtPayloadPrincipal)
        |
        v
 JwtAuthorizationFilter / JwtWithinExpiryFilter (authz)
        |
        +-- handlers --> ShiroJwtMessageSource (i18n)
```

This is a **single-module** project (packaging `jar`), classes under `org.apache.shiro.spring.boot.jwt` (plus `org.apache.shiro.spring.boot.utils`):

| Package | Role |
| :--- | :--- |
| `jwt` | Principal/payload repositories, message source |
| `jwt.authc` (+ `jwt.authc.credential`) | JWT authenticating filter, handlers, subject factory, credentials matcher |
| `jwt.authz` | Authorization filter, within-expiry filter, failure handler |
| `jwt.realm` | Stateful and stateless JWT realms |
| `jwt.token` | `JwtAuthenticationToken`, `JwtAuthorizationToken` |
| `jwt.exception` | JWT-specific authentication exceptions |
| `utils` | `SubjectJwtUtils`, `JSONResult`, `StringUtils` |

## 5. Installation

The artifact is not yet published to Maven Central. Resolve it from the project's configured artifact repository (Aliyun Packages) or install it locally from source; the snapshot version currently used on the `feature/1.0.x` branch is `1.0.x.20260630-SNAPSHOT`.

**Maven**

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>shiro-jwt-extension</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

**Gradle**

```groovy
implementation 'io.github.easy4j:shiro-jwt-extension:1.0.x.20260630-SNAPSHOT'
```

## 6. Quick Start

Wire the JWT authenticating filter into your Shiro filter chain:

```java
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticatingFilter;
import org.apache.shiro.spring.boot.jwt.realm.JwtStatelessAuthorizingRealm;

// 1. Stateless realm (no server-side session required)
JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();

// 2. Filter: accepts "X-Authorization: <jwt>" or "?token=<jwt>"
JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
filter.setLoginUrl("/login/jwt");
// register filter + realm with your SecurityManager / filter chain
```

**Expected result:** requests carrying a valid JWT are authenticated by the realm; the Shiro subject principal becomes the `JwtPayloadPrincipal` extracted from the token payload; requests without a valid JWT are redirected to the login URL.

## 7. Configuration

This library has no configuration properties or prefix; it is configured programmatically:

| Extension point | Configurable via |
| :--- | :--- |
| `JwtAuthenticatingFilter` | `setLoginUrl(...)`, request parameter name (default `token`) and header name (default `X-Authorization`). |
| `JwtAuthenticationSuccessHandler` | constructor `(JwtPayloadRepository, boolean checkExpiry)` |
| `JwtPrincipalRepository` | constructor `(JwtPayloadRepository)`, `setCheckExpiry(boolean)` |
| `JwtSubjectFactory` | constructor `(boolean sessionCreationEnabled)` |
| Message texts | bundled `org/apache/shiro/spring/boot/jwt/messages*.properties` |

## 8. Core Usage / API

| Class | Role |
| :--- | :--- |
| `JwtAuthenticatingFilter` | Extracts JWT from `X-Authorization` header / `token` parameter and authenticates (`AUTHORIZATION_HEADER`, `AUTHORIZATION_PARAM` constants). |
| `JwtAuthorizationFilter` | Authorization filter using the same header/parameter extraction. |
| `JwtWithinExpiryFilter` | Rejects JWTs outside the expiry window. |
| `JwtStatefulAuthorizingRealm` / `JwtStatelessAuthorizingRealm` | Realms for session-based / stateless JWT auth. |
| `JwtPayloadRepository` / `JwtPrincipalRepository` / `JwtPayloadPrincipal` | JWT payload → Shiro principal mapping. |
| `JwtCredentialsMatcher` | Credentials matching for JWT tokens. |
| `ShiroJwtMessageSource` | `ResourceBundleMessageSource` subclass; `getAccessor()` returns the `MessageSourceAccessor`. |

## 9. Testing & Build

```bash
# Full build with JaCoCo coverage report/check
./mvnw clean verify

# Install into the local repository
./mvnw install
```

Test & gate facts (as configured in the pom):

- No unit tests exist in this module yet.
- JaCoCo is bound to `prepare-agent` / `report` / `check`; the `check` rule requires a **90% line coverage ratio** (configured with `haltOnFailure=false`).

## 10. Versioning & Branches

| Branch | JDK baseline | Version pattern | Status |
| :--- | :--- | :--- | :--- |
| `feature/1.0.x` | JDK 8 | `1.0.x.*` | Active; current snapshot `1.0.x.20260630-SNAPSHOT` |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` | Maintained |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` | Maintained |

Maintenance strategy: the 1.0.x line keeps JDK 8 compatibility for legacy deployments; the 2.0.x and 3.0.x lines are the modern JDK baselines. Release artifacts are published to the project's configured artifact repository (Aliyun Packages) and GitHub Releases; the project has not yet published to Maven Central.

## 11. Contributing & License

Contributions are welcome — please open an issue or a pull request on the [GitHub repository](https://github.com/easy-4-java/shiro-jwt-extension).

This project is licensed under the **Apache License 2.0**. See [LICENSE](LICENSE) for details.

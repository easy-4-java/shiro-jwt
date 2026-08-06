# shiro-jwt-extension

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-17-orange)](https://github.com/easy-4-java/shiro-jwt-extension) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0.txt)

Apache Shiro 的 JWT 认证与授权扩展，构建于 `shiro-biz` 与 `jwt-issuer-api`（easy4j）之上。为基于 Shiro 的应用提供 JWT 感知的过滤器（请求头 `X-Authorization` 或 `token` 参数）、有状态/无状态 Realm、主体仓库、凭证匹配与 i18n 消息。

## 目录

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

**是什么**

`shiro-jwt-extension` 将 JWT 登录引入 Shiro Web 应用：

- `JwtAuthenticatingFilter`（继承 `shiro-biz` 的 `TrustableRestAuthenticatingFilter`）从 `X-Authorization` 请求头或 `token` 请求参数中提取 JWT。
- `JwtStatefulAuthorizingRealm` / `JwtStatelessAuthorizingRealm` 分别覆盖基于会话与无状态的 JWT 认证。
- `JwtPayloadRepository` / `JwtPrincipalRepository` / `JwtPayloadPrincipal` 将 JWT 载荷（来自 `jwt-issuer-api`）映射到 Shiro 主体模型。
- `JwtAuthorizationFilter` 与 `JwtWithinExpiryFilter` 负责授权与有效期校验。
- `ShiroJwtMessageSource` 提供 i18n 认证/授权消息（英文 / 简体中文）。

**不是什么**

- 它不是 JWT 签名/校验库——Token 的签发与解析委托给 `io.github.easy4j:jwt-issuer-api`（`JwtPayload`）。
- 它不是 Spring Boot Starter；过滤器与 Realm 需要装配进你自己的 Shiro 配置。

**典型场景**

| 场景 | 说明 |
| :--- | :--- |
| REST API JWT 认证 | `JwtAuthenticatingFilter` 从 `X-Authorization` / `token` 提取 JWT 并完成认证。 |
| 无状态 JWT 服务 | 服务端无会话场景使用 `JwtStatelessAuthorizingRealm`。 |
| 基于会话的 JWT | 传统会话应用使用 `JwtStatefulAuthorizingRealm`。 |
| 有效期强制校验 | `JwtWithinExpiryFilter` 拒绝已超出有效期窗口的 JWT 请求。 |

## 2. Features & Status

| 能力 | 状态 | 说明 |
| :--- | :--- | :--- |
| JWT 认证过滤器 | 可用 | `JwtAuthenticatingFilter`——`X-Authorization` 请求头或 `token` 参数；继承 `TrustableRestAuthenticatingFilter`。 |
| 有状态 / 无状态 Realm | 可用 | `JwtStatefulAuthorizingRealm`、`JwtStatelessAuthorizingRealm`。 |
| 载荷与主体仓库 | 可用 | `JwtPayloadRepository`（接口）、`JwtPrincipalRepository`、`JwtPayloadPrincipal`。 |
| 凭证匹配 | 可用 | `JwtCredentialsMatcher`。 |
| 授权过滤器 | 可用 | `JwtAuthorizationFilter`、`JwtWithinExpiryFilter`（`X-Authorization` / `token`）。 |
| 处理器 | 可用 | `JwtAuthenticationFailureHandler`、`JwtAuthenticationSuccessHandler`（载荷仓库 + 有效期校验）、`JwtAuthorizationFailureHandler`（均实现 `Ordered`）。 |
| Subject 工厂 | 可用 | `JwtSubjectFactory`（可开关会话创建）。 |
| Token | 可用 | `JwtAuthenticationToken`（继承 `DefaultAuthenticationToken`）、`JwtAuthorizationToken`。 |
| 异常 | 可用 | `ExpiredJwtException`、`IncorrectJwtException`、`InvalidJwtToken`、`NotObtainedJwtException`。 |
| i18n 消息 | 可用 | 经 `ShiroJwtMessageSource` 提供 `messages.properties`（+`en_US`、`zh_CN`）。 |
| 工具类 | 可用 | `SubjectJwtUtils`、`JSONResult`、`StringUtils`（位于 `org.apache.shiro.spring.boot.utils`）。 |

> 状态以 `feature/2.0.x` 分支上的 `2.0.x.x.20260630-SNAPSHOT` 为准。

## 3. Requirements & Compatibility

| 项目 | 版本 |
| :--- | :--- |
| JDK | 17+ |
| Maven | 3.0+（内置 Maven Wrapper 3.5.0） |
| Apache Shiro | 1.13.0（`shiro-core`、`shiro-web`） |
| easy4j 依赖 | `shiro-biz`、`jwt-issuer-api`（均为 `2.0.x.x.20260630-SNAPSHOT`） |
| JSON | fastjson 2.0.62、jackson-databind 2.17.2 |
| 其他 | spring-context / spring-web、commons-lang3、guava、javax.servlet-api 4.0.1 |

**版本线**

| 分支 | JDK 基线 | 版本模式 |
| :--- | :--- | :--- |
| `feature/1.0.x` | JDK 8 | `1.0.x.*` |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` |

## 4. Architecture & Modules

```text
 客户端（JWT 位于 X-Authorization 请求头 / token 参数）
        |
        v
 JwtAuthenticatingFilter（authc）
        |  createJwtToken() -> JwtAuthenticationToken
        v
 JwtStatefulAuthorizingRealm / JwtStatelessAuthorizingRealm
        |  JwtPrincipalRepository -> JwtPayloadRepository（jwt-issuer-api）
        |  JwtCredentialsMatcher
        v
 Subject（JwtPayloadPrincipal）
        |
        v
 JwtAuthorizationFilter / JwtWithinExpiryFilter（authz）
        |
        +-- 处理器 --> ShiroJwtMessageSource（i18n）
```

本项目为**单模块**工程（packaging 为 `jar`），类位于 `org.apache.shiro.spring.boot.jwt`（另有 `org.apache.shiro.spring.boot.utils`）：

| 包 | 职责 |
| :--- | :--- |
| `jwt` | 主体/载荷仓库、消息源 |
| `jwt.authc`（+ `jwt.authc.credential`） | JWT 认证过滤器、处理器、Subject 工厂、凭证匹配器 |
| `jwt.authz` | 授权过滤器、有效期过滤器、失败处理器 |
| `jwt.realm` | 有状态与无状态 JWT Realm |
| `jwt.token` | `JwtAuthenticationToken`、`JwtAuthorizationToken` |
| `jwt.exception` | JWT 专属认证异常 |
| `utils` | `SubjectJwtUtils`、`JSONResult`、`StringUtils` |

## 5. Installation

该构件尚未发布到 Maven Central。请从项目配置的制品仓库（阿里云制品仓库）获取，或从源码本地安装；`feature/2.0.x` 分支当前使用的快照版本为 `2.0.x.x.20260630-SNAPSHOT`。

**Maven**

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>shiro-jwt-extension</artifactId>
    <version>2.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

**Gradle**

```groovy
implementation 'io.github.easy4j:shiro-jwt-extension:2.0.x.x.20260630-SNAPSHOT'
```

## 6. Quick Start

将 JWT 认证过滤器接入你的 Shiro 过滤器链：

```java
import org.apache.shiro.spring.boot.jwt.authc.JwtAuthenticatingFilter;
import org.apache.shiro.spring.boot.jwt.realm.JwtStatelessAuthorizingRealm;

// 1. 无状态 Realm（无需服务端会话）
JwtStatelessAuthorizingRealm realm = new JwtStatelessAuthorizingRealm();

// 2. 过滤器：接受 "X-Authorization: <jwt>" 或 "?token=<jwt>"
JwtAuthenticatingFilter filter = new JwtAuthenticatingFilter();
filter.setLoginUrl("/login/jwt");
// 将 filter 与 realm 注册到你的 SecurityManager / 过滤器链
```

**预期结果：** 携带有效 JWT 的请求由 Realm 完成认证；Shiro 主体为从 Token 载荷解析出的 `JwtPayloadPrincipal`；未携带有效 JWT 的请求被重定向到登录 URL。

## 7. Configuration

本库没有配置属性与前缀，采用编程方式配置：

| 扩展点 | 配置方式 |
| :--- | :--- |
| `JwtAuthenticatingFilter` | `setLoginUrl(...)`；请求参数名（默认 `token`）与请求头名（默认 `X-Authorization`）。 |
| `JwtAuthenticationSuccessHandler` | 构造器 `(JwtPayloadRepository, boolean checkExpiry)` |
| `JwtPrincipalRepository` | 构造器 `(JwtPayloadRepository)`、`setCheckExpiry(boolean)` |
| `JwtSubjectFactory` | 构造器 `(boolean sessionCreationEnabled)` |
| 消息文案 | 随包 `org/apache/shiro/spring/boot/jwt/messages*.properties` |

## 8. Core Usage / API

| 类 | 职责 |
| :--- | :--- |
| `JwtAuthenticatingFilter` | 从 `X-Authorization` 请求头 / `token` 参数提取 JWT 并认证（常量 `AUTHORIZATION_HEADER`、`AUTHORIZATION_PARAM`）。 |
| `JwtAuthorizationFilter` | 使用相同请求头/参数提取方式的授权过滤器。 |
| `JwtWithinExpiryFilter` | 拒绝超出有效期窗口的 JWT。 |
| `JwtStatefulAuthorizingRealm` / `JwtStatelessAuthorizingRealm` | 基于会话 / 无状态 JWT 认证的 Realm。 |
| `JwtPayloadRepository` / `JwtPrincipalRepository` / `JwtPayloadPrincipal` | JWT 载荷到 Shiro 主体的映射。 |
| `JwtCredentialsMatcher` | JWT Token 的凭证匹配。 |
| `ShiroJwtMessageSource` | `ResourceBundleMessageSource` 子类；`getAccessor()` 返回 `MessageSourceAccessor`。 |

## 9. Testing & Build

```bash
# 完整构建（含 JaCoCo 覆盖率报告/检查）
./mvnw clean verify

# 安装到本地仓库
./mvnw install
```

测试与门禁事实（以 pom 配置为准）：

- 本模块暂无单元测试。
- JaCoCo 绑定 `prepare-agent` / `report` / `check`；`check` 规则要求**行覆盖率不低于 90%**（配置了 `haltOnFailure=false`）。

## 10. Versioning & Branches

| 分支 | JDK 基线 | 版本模式 | 状态 |
| :--- | :--- | :--- | :--- |
| `feature/1.0.x` | JDK 8 | `1.0.x.*` | 活跃；当前快照 `1.0.x.20260630-SNAPSHOT` |
| `feature/2.0.x` | JDK 17 | `2.0.x.*` | 维护中 |
| `feature/3.0.x` | JDK 21 | `3.0.x.*` | 维护中 |

维护策略：1.0.x 版本线保持 JDK 8 兼容，服务于存量部署；2.0.x 与 3.0.x 版本线为现代 JDK 基线。发布制品发布到项目配置的制品仓库（阿里云制品仓库）与 GitHub Releases；项目尚未发布到 Maven Central。

## 11. Contributing & License

欢迎参与贡献——请在 [GitHub 仓库](https://github.com/easy-4-java/shiro-jwt-extension) 提交 Issue 或 Pull Request。

本项目基于 **Apache License 2.0** 开源。详见 [LICENSE](LICENSE)。

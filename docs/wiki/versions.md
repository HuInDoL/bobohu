# 버전 및 호환성

> 기준일: 2026-09-25. 버전을 바꾸면 이 페이지를 먼저 고치고 `log.md`에 이유를 남긴다.

## 한눈에 보기

### 서버 (Spring)

| 항목 | 버전 | 호환 조건 |
|---|---|---|
| Java (JDK) | **25** (LTS) | Spring Boot 4.1은 Java 17~26 지원 |
| Spring Boot | **4.1.x** (Spring Framework 7.0.x) | Java 17 이상, Gradle 8.14 이상 또는 9.x |
| Gradle | **9.8.x** | Java 25로 빌드하려면 Gradle 9.1 이상 필요 |
| 내장 서버 | Tomcat 11.0.x (Servlet 6.1) | Spring Boot 4.1 기본값 |
| PostgreSQL | **18** | 2030-11까지 지원. JDBC 드라이버 버전은 Spring Boot가 관리 |
| pgvector | AI 기능 착수 시 확정 | PostgreSQL 18 지원 버전 사용 |
| Spring AI | AI 기능 착수 시 확정 | Spring Boot 4.1 호환 버전 사용 |

### Android 앱

| 항목 | 버전 | 호환 조건 |
|---|---|---|
| Kotlin | **2.4.x** | Kotlin 2.4는 AGP 8.5.2 이상 필요 |
| Android Gradle Plugin (AGP) | **9.4.x** | Gradle 9.6 이상, JDK 17 이상 필요 |
| Gradle | **9.8.x** | AGP 9.0~9.5와 호환 테스트됨. 서버와 버전을 맞춤 |
| Compose BOM | **2026.09.00** | Compose UI 1.12.1, Material3 1.4.0 |
| 네이버 지도 SDK | **3.24.0** | minSdk 21 이상 |
| compileSdk / targetSdk | **36** (Android 16) | 2026-08-31부터 Google Play 신규 앱과 업데이트는 targetSdk 36 이상 필수 |
| minSdk | **26** (Android 8.0) | 네이버 지도 SDK 최소 조건(21)보다 높음 |
| Kotlin jvmTarget | **17** | Android Studio로 프로젝트를 만들 때 기본값과 비교해서 확인 |

### 웹 (MVP 이후 예정, 착수할 때 다시 확인)

| 항목 | 후보 버전 | 비고 |
|---|---|---|
| Node.js | 24 LTS | 2026년 10월에 Node 26이 LTS가 되면 다시 검토 |
| Next.js | 16.x | React 19 기반 |
| Tailwind CSS | 4.x | |
| TypeScript | 5.x | |

## 버전 호환 관계

```
[서버]  JDK 25 ──▶ Gradle 9.8 (Java 25는 9.1 이상 필요)
                └─▶ Spring Boot 4.1 (Java 17~26) ──▶ Spring Framework 7 ──▶ Tomcat 11 / Jakarta EE 11
                                                 └─▶ PostgreSQL 18 (+ pgvector 예정)

[앱]    Gradle 9.8 ──▶ AGP 9.4 (Gradle 9.6 이상, JDK 17 이상)
                    ├─▶ Kotlin 2.4 (AGP 8.5.2 이상)
                    ├─▶ Compose BOM 2026.09.00 (Compose 라이브러리끼리 버전을 맞춰 줌)
                    └─▶ compileSdk/targetSdk 36, minSdk 26
```

## 왜 이 버전인가

### Java 25
- **LTS 중 가장 최신**이다. Oracle 기준 기본 지원(Premier Support)이 2030년 9월까지라, 새 프로젝트를 가장 오래 안정적으로 쓸 수 있다.
- **Java 26, 27은 쓰지 않는다.** LTS가 아니라 지원 기간이 6개월뿐이다.
- **Java 21을 쓰지 않는 이유**: 21도 좋은 선택이고 금융권에서도 많이 쓴다. 다만 2026년 9월에 시작하는 새 프로젝트라 지원 기간이 더 긴 25를 골랐다. Java 24에서 들어온 개선(JEP 491, `synchronized` 블록 안에서도 가상 스레드가 캐리어 스레드에 고정(pinning)되지 않음)으로 가상 스레드를 더 안전하게 쓸 수 있다는 점도 이유다.
- **Java 17을 쓰지 않는 이유**: Oracle 기본 지원이 2026년 9월에 끝난다.

### Spring Boot 4.1
- 현재 최신 안정 버전이다. 오픈소스 무료 지원이 2027-07-31까지다.
- Spring Boot 3.x는 새로 시작하는 프로젝트에 쓸 이유가 없다. 3.5가 마지막 3.x 버전이고 무료 지원은 끝났다(확인 필요).

### PostgreSQL 18
- 2025년 9월에 나와 1년 동안 검증된 최신 메이저 버전이다. 지원 기간은 2030-11까지다.
- Java 25와 같은 기준이다. **충분히 검증된 최신 버전**을 고른다.
- 왜 MySQL이 아닌지는 [PostgreSQL 페이지](tech/postgresql.md#대안-기술)를 참고한다.

### Gradle 9.8 (Maven 대신)
- Android도 Gradle을 쓰므로 서버와 앱의 빌드 도구를 하나로 통일할 수 있다.
- 서버와 앱의 Gradle 버전을 똑같이 맞춰서 관리할 것을 줄인다.

### Android: targetSdk 36, minSdk 26
- **targetSdk 36**은 선택이 아니라 필수다. 2026-08-31부터 Google Play에 앱을 올리거나 업데이트하려면 36 이상이어야 한다.
- **minSdk 26**(Android 8.0)으로 하면 현재 사용 중인 기기 대부분을 지원한다. 또한 `java.time` 같은 Java 8 API를 별도 처리(desugaring) 없이 쓸 수 있다.

### Android는 왜 Java 25가 아니라 17인가
Android 앱은 JVM이 아니라 **ART(Android Runtime)**에서 실행된다. Kotlin/Java 코드는 JVM 바이트코드로 컴파일된 뒤 D8이 ART용 DEX로 다시 변환한다. 그래서 서버의 Java 버전과 앱의 Java 타깃 버전은 서로 관계가 없다. 앱은 Android 툴체인이 지원하는 수준(17)에 맞춘다.

## 면접 대비: "Java 몇 버전 썼어요? 왜요?"

> Java 25를 썼습니다. LTS 버전 중 가장 최신이라 지원 기간이 2030년까지로 가장 깁니다. Spring Boot 4.1도 공식 지원하는 버전입니다.
> 금융권에서 많이 쓰는 21도 검토했습니다. 하지만 2026년에 새로 시작하는 프로젝트라 지원 기간이 더 긴 25를 골랐습니다. 21에서 도입된 가상 스레드도 24부터 `synchronized` 블록 안에서 pinning 문제가 개선되어, 25에서 더 안정적으로 쓸 수 있다는 점도 고려했습니다.
> 26처럼 LTS가 아닌 버전은 지원 기간이 6개월뿐이라 제외했습니다.

## 버전 올릴 때 규칙
1. 올리려는 버전의 공식 릴리스 노트에서 호환 조건을 확인한다.
2. 이 페이지의 표와 호환 관계를 먼저 고친다.
3. 올리는 과정에서 문제가 생기면 트러블슈팅으로 기록한다.

## 참고 자료
- [Spring Boot 4.1 System Requirements](https://docs.spring.io/spring-boot/system-requirements.html)
- [Spring Boot 지원 기간 (endoflife.date)](https://endoflife.date/spring-boot)
- [Oracle JDK 지원 기간 (endoflife.date)](https://endoflife.date/oracle-jdk)
- [Gradle 호환성 표](https://docs.gradle.org/current/userguide/compatibility.html)
- [AGP 릴리스 노트](https://developer.android.com/build/releases/gradle-plugin)
- [Kotlin과 AGP 호환성](https://developer.android.com/build/kotlin-support)
- [Compose BOM 매핑](https://developer.android.com/develop/ui/compose/bom/bom-mapping)
- [Google Play 타깃 API 수준 요구사항](https://support.google.com/googleplay/android-developer/answer/11926878)
- [네이버 지도 Android SDK 가이드](https://navermaps.github.io/android-map-sdk/guide-ko/1.html)
- [Kotlin 릴리스](https://kotlinlang.org/docs/releases.html)
- [PostgreSQL 지원 기간 (endoflife.date)](https://endoflife.date/postgresql)
- [Next.js 지원 기간 (endoflife.date)](https://endoflife.date/nextjs), [Node.js 지원 기간 (endoflife.date)](https://endoflife.date/nodejs)

# Kotlin

> JetBrains가 만든 JVM 언어. Java와 완전히 호환되며 Android 공식 권장 언어다. 이 프로젝트의 Android 앱 언어다.

## 개념
Java의 장황함과 null 문제를 줄이려고 만든 언어다. Java 코드와 섞어 쓸 수 있고, Java 라이브러리를 그대로 쓸 수 있다. 2019년부터 Google은 Android 개발에서 Kotlin을 우선 지원(Kotlin-first)하고 있다.

## 핵심 내용 (Java와 비교)
| 기능 | Kotlin | Java |
|---|---|---|
| null 안전성 | 타입에서 null 가능 여부를 구분 (`String` vs `String?`). 컴파일 시점에 NPE를 막는다 | 모든 참조 타입이 null이 될 수 있다 |
| 데이터 클래스 | `data class`로 한 줄에 `equals`, `hashCode`, `toString` 생성 | `record` (16 이상) 또는 Lombok |
| 불변 변수 | `val` / `var` | `final` |
| 확장 함수 | 기존 클래스를 고치지 않고 메서드 추가 가능 | 없음 (유틸 클래스 사용) |
| 비동기 | 코루틴(coroutine): 비동기 코드를 순서대로 읽히게 작성 | CompletableFuture, 가상 스레드 |

## 이 프로젝트에서 사용한 방법
- **버전**: 2.4.x. 호환 조건은 [버전 및 호환성](../versions.md#android-앱)을 참고한다.
- **왜 Kotlin인가**: Android의 현재 표준이고, 최신 UI 도구인 [Jetpack Compose](jetpack-compose.md)는 Kotlin으로만 쓸 수 있다. 서버는 Java로 유지해서 두 JVM 언어를 비교하며 경험한다.
- **어디에 쓰는가**: Android 앱 전체 (구현 후 경로 추가)

## 트레이드오프
| 장점 | 단점 |
|---|---|
| 코드가 간결하고 null 안전성이 있다 | Java보다 컴파일이 느린 편이다 |
| Java 라이브러리를 그대로 쓸 수 있다 | 국내 금융권 서버 개발에서는 아직 Java보다 덜 쓰인다 |
| Android 공식 문서와 예제가 모두 Kotlin 기준이다 | 문법 기능이 많아 팀마다 스타일 차이가 커질 수 있다 |

## 대안 기술
| 대안 | 특징 | 선택하지 않은 이유 |
|---|---|---|
| Java (Android) | 이미 알고 있는 언어 | Compose를 쓸 수 없고 예전 방식(XML 레이아웃)에 머문다. 최신 자료가 적다 |
| Flutter (Dart) | 코드 하나로 Android, iOS, 웹 지원 | JVM 생태계에서 벗어난다. 네이버 지도가 공식 SDK가 아닌 커뮤니티 플러그인이다 |
| React Native (JS/TS) | 코드 하나로 Android, iOS 지원. 웹 생태계와 가깝다 | JS와 React를 새로 배워야 해서 Spring 학습 시간이 줄어든다 |

## 면접에서 나올 만한 질문
- Q. 서버는 Java, 앱은 Kotlin으로 한 이유는?
  - A. 서버는 금융권 채용 시장의 표준인 Java로 역량을 쌓고, 앱은 Android 공식 권장 언어인 Kotlin을 썼습니다. 두 언어 모두 JVM 기반이라 개념을 공유하면서, null 안전성 같은 언어 설계 차이를 직접 비교해 볼 수 있었습니다.
- Q. Kotlin은 null 안전성을 어떻게 보장하나요?
- Q. 코루틴과 Java 가상 스레드는 무엇이 다른가요?

## 관련 트러블슈팅
- 

## 참고 자료
- [Kotlin 릴리스](https://kotlinlang.org/docs/releases.html)
- [Kotlin과 AGP 호환성](https://developer.android.com/build/kotlin-support)

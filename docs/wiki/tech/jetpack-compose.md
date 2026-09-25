# Jetpack Compose

> Kotlin 코드로 화면을 선언하는 Android 공식 UI 툴킷.

## 개념
예전 Android는 XML로 화면 구조를 만들고 Java/Kotlin 코드에서 그 화면을 찾아 값을 바꾸는 **명령형** 방식이었다. Compose는 "상태가 이러면 화면은 이렇게 생겼다"를 함수로 적는 **선언형** 방식이다. React, SwiftUI, Flutter와 같은 흐름이다.

## 핵심 내용
- **Composable 함수**: `@Composable`이 붙은 함수가 UI 조각이다. 함수를 조합해서 화면을 만든다.
- **상태(State)와 리컴포지션(Recomposition)**: 상태 값이 바뀌면 그 상태를 읽는 Composable만 다시 실행되어 화면이 갱신된다.
- **상태 끌어올리기(State Hoisting)**: 상태는 상위에서 관리하고, 하위 Composable은 값과 이벤트 콜백만 받는다. 재사용하기 쉽고 테스트하기 좋다.
- **Material 3**: Google의 최신 디자인 시스템. 색상, 글꼴, 모양을 테마로 한 번에 정할 수 있다.
- **BOM(Bill of Materials)**: Compose 라이브러리 여러 개의 버전을 서로 호환되게 한 번에 맞춰준다. BOM 버전 하나만 정하면 된다.

## 이 프로젝트에서 사용한 방법
- **버전**: Compose BOM 2026.09.00 (UI 1.12.1, Material3 1.4.0)
- **왜 Compose인가**: 세련되고 쓰고 싶은 화면을 만드는 것이 목표다. Compose는 애니메이션과 상태에 따라 바뀌는 화면을 XML보다 훨씬 쉽게 만든다. Android 신규 개발의 표준이기도 하다.
- **지도 연동**: 네이버 지도 SDK는 기존 View 방식이다. Compose에서는 `AndroidView`로 감싸서 쓴다. [네이버 지도](naver-maps.md) 참고.
- **어디에 쓰는가**: 구현 후 경로 추가

## 트레이드오프
| 장점 | 단점 |
|---|---|
| 코드가 짧고, UI와 상태를 한곳에서 관리한다 | 리컴포지션을 잘못 다루면 성능 문제가 생긴다 |
| 애니메이션, 테마 적용이 쉽다 | 기존 View 기반 라이브러리(지도 SDK 등)는 감싸서 써야 한다 |
| Google이 앞으로 집중 지원하는 방식이다 | 오래된 회사 프로젝트는 아직 XML이 많아서 둘 다 알아야 할 수 있다 |

## 대안 기술
| 대안 | 특징 | 선택하지 않은 이유 |
|---|---|---|
| XML View | 오래되고 안정적이며 레거시 코드에 많다 | 신규 개발 표준이 아니고, 화면을 세련되게 만들기 어렵다 |
| Flutter, React Native | 크로스 플랫폼 | [Kotlin 페이지의 대안 비교](kotlin.md#대안-기술) 참고 |

## 면접에서 나올 만한 질문
- Q. 선언형 UI와 명령형 UI의 차이는?
- Q. 리컴포지션이란 무엇이고, 불필요한 리컴포지션은 어떻게 줄이나요?
- Q. `remember`와 `rememberSaveable`의 차이는?

## 관련 트러블슈팅
- 

## 참고 자료
- [Compose BOM 매핑](https://developer.android.com/develop/ui/compose/bom/bom-mapping)
- [Compose 릴리스 노트](https://developer.android.com/jetpack/androidx/releases/compose)

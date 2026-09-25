# 프로젝트 개요

## 목표
"이 세상 모든 아이들을 위해"
- 보육원에 대한 **정리된 정보**를 쉽게 찾아볼 수 있게 한다.
- **물품이나 금전 후원**을 간편하게 할 수 있게 한다.
- **보안을 최우선으로** 한다. 개인정보 유출 사고가 늘고 있어서, 후원자, 담당자, 아동의 정보를 설계 단계부터 보호한다.

전체 구조는 [아키텍처](architecture.md)에 정리한다.

## 개인 목표
- 금융 IT 취업을 위한 포트폴리오로 쓴다.
- Spring/Java 실력을 깊게 키운다.
- 사용하는 기술마다 개념, 핵심, 트레이드오프, 대안을 정리한다.
- 겪은 문제를 트러블슈팅 기록으로 남긴다.

## 기술 스택
Java/Spring 서버 하나에 Android 앱과 웹이 붙는 구조다. 서버는 JSON만 응답하는 REST API로 만든다.

```
[Android 앱: Kotlin + Compose] ──┐
                                 ├──▶ [서버: Java 25 + Spring Boot 4.1] ──▶ PostgreSQL 18
[웹: Next.js + React (예정)]   ──┘              │
                                                ├──▶ 네이버 지도 Geocoding API
                                                └──▶ LLM API (AI 검색, 예정)
```

| 영역 | 기술 | 선택 이유 |
|---|---|---|
| 서버 | [Java](tech/java.md) + [Spring Boot](tech/spring-boot.md) | 금융권 백엔드 표준, 핵심 역량 |
| 앱 (메인) | [Kotlin](tech/kotlin.md) + [Jetpack Compose](tech/jetpack-compose.md) | Android 공식 표준. 금융권 앱도 네이티브가 기본 |
| 웹 (MVP 이후) | Next.js (React) + Tailwind CSS | 상호작용이 많은 지도 화면을 매끄럽게 만들기 좋음 |
| 지도 | [네이버 지도](tech/naver-maps.md) | 국내 사용자 친숙도와 데이터 정확도 |
| DB | [PostgreSQL](tech/postgresql.md) 18 | pgvector로 RAG용 벡터 검색까지 한 DB에서 처리 |
| AI (예정) | Spring AI + [Function Calling](tech/function-calling.md), [RAG](tech/rag.md) | Java/Spring 안에서 LLM 연동 |

버전과 호환성은 [버전 및 호환성](versions.md)에 정리한다.

## 주요 결정 기록
- **Flutter, React Native 대신 Android 네이티브**: 금융 IT가 목표라 JVM 생태계로 일관되게 맞췄다. 금융권 앱도 네이티브가 기본이다. 대신 iOS는 지원하지 않는다.
- **MySQL 대신 PostgreSQL**: 처음에는 채용 시장과 자료량 때문에 MySQL을 추천했다. 그런데 AI 검색(RAG) 계획이 생기면서 벡터 검색이 필요해졌다. MySQL 커뮤니티판은 벡터 검색을 지원하지 않는다. 경로 탐색은 DB가 아니라 네이버 Directions API가 맡으므로 DB 선택과 관계없다.
- **세부 정보는 시설 담당자가 직접 입력**: 운영자가 모든 데이터를 모을 수 없고, 그래서도 안 된다. 공공데이터는 초기 시설 목록(시설명, 주소)으로만 쓴다. 담당자는 인증을 거쳐 권한을 받는다. 아동 개인정보는 받지 않는다.
- **후원은 법적 테두리 안에서만**: 플랫폼은 돈을 직접 다루지 않고 후원자와 시설을 직접 연결한다(A안). 플랫폼 결제(B안)는 법률 검토 후에만 진행하고, 불가능하면 하지 않는다. 후원 내역, 사용 내역, 사용 계획은 수정 없이 추가만 하는 원장 방식으로 기록한다.
- **AI는 LLM이 DB에 직접 접근하지 않는 구조**: Text-to-SQL 대신 Function Calling을 쓰고, 자유 텍스트는 RAG로 처리한다. 정확성과 보안을 우선한다.
- **웹은 Thymeleaf 대신 React**: 지도 서비스는 상호작용이 많아서, 서버가 페이지를 통째로 다시 그리는 방식으로는 매끄러운 화면을 만들기 어렵다. 대신 배울 게 늘어나므로 MVP 이후로 미룬다.

## 로드맵
| 단계 | 내용 | 상태 |
|---|---|---|
| 1 (MVP) | [보육원 지도](features/orphanage-map.md): 서버 + Android 앱 | 계획 중 |
| 2 | [시설 담당자 정보 관리](features/facility-management.md): 담당자 인증, 세부 정보 입력 | 예정 |
| 2 | 웹 (Next.js) | 예정 |
| 3 | [AI 보육원 검색](features/ai-search.md): Function Calling → RAG | 예정 |
| 4 | [후원](features/donation.md): 직접 연결, 후원 및 사용 내역 기록 (플랫폼 결제는 법률 검토 후) | 예정 |
| 이후 | 점진적으로 고도화 | 미정 |

## 미정 사항
- 시설 담당자 인증에 쓸 증빙 서류 (확인 필요)
- LLM, 임베딩 모델
- 배포 환경
- UI 디자인 방향 (코드 작성 전에 Figma로 먼저 설계)

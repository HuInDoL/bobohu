# 위키 변경 기록

새 기록은 맨 아래에 추가합니다.

## [2026-09-25] setup | LLM Wiki 구조 생성
- CLAUDE.md, docs/raw, docs/wiki (index, log, overview, tech, features), docs/troubleshooting 생성
- 첫 기능 페이지 features/orphanage-map.md 작성 (계획 단계)

## [2026-09-25] tech | 기술 스택과 버전 확정
- 서버 Java 25 + Spring Boot 4.1, 앱 Kotlin + Jetpack Compose, 지도 네이버 지도, 웹 Next.js(예정)
- 추가: versions.md, tech/java.md, tech/spring-boot.md, tech/kotlin.md, tech/jetpack-compose.md, tech/naver-maps.md
- 수정: overview.md (기술 스택, 주요 결정), index.md, features/orphanage-map.md

## [2026-09-25] tech | DB를 PostgreSQL 18로 확정, AI 검색 기능 계획
- MySQL 8.4를 검토했으나, RAG용 벡터 검색 때문에 PostgreSQL로 변경 (MySQL 커뮤니티판은 벡터 검색 미지원)
- 추가: tech/postgresql.md, tech/function-calling.md, tech/rag.md, features/ai-search.md
- 수정: versions.md, overview.md (스택, 주요 결정, 로드맵), index.md, features/orphanage-map.md

## [2026-09-25] ingest | 보육원 데이터 출처 조사
- 추가: data-sources.md (전국 단일 API 없음, 보건복지부 PDF와 아동권리보장원 목록을 기본으로 지역 데이터로 보강)
- 수정: index.md, overview.md

## [2026-09-25] ingest | 보건복지부 아동복지시설 현황 CSV 분석
- 추가: docs/raw/보건복지부_아동복지시설 현황_20221231.csv
- 수정: data-sources.md (항목이 시설명과 주소뿐이고, 시설 유형 구분이 없으며, 주소 형식이 제각각임)

## [2026-09-25] feature | 세부 정보는 시설 담당자가 직접 입력하는 구조로 결정
- 추가: features/facility-management.md (역할, 담당자 인증, 입력 항목, 아동 개인정보 원칙)
- 수정: data-sources.md (공공데이터는 초기 목록으로만 사용), features/ai-search.md (필요한 데이터 출처), overview.md, index.md

## [2026-09-25] setup | 아키텍처 문서 뼈대 추가, 서비스 목적과 보안 원칙 명시
- 추가: architecture.md (시스템 구성도, 모듈러 모놀리스 계획, 주요 흐름, 보안 아키텍처, 미정 사항)
- 수정: overview.md (목적: 정보 제공, 물품과 금전 후원, 보안 최우선 / 로드맵에 후원 추가), CLAUDE.md (보안 원칙, architecture.md 갱신 규칙), index.md

## [2026-09-25] feature | 후원 기능 설계: 법적 검토와 직접 연결 방식
- 추가: features/donation.md (기부금품법, 사회복지사업법 제45조 검토, A안 직접 연결 / B안 플랫폼 결제 보류, 원장 방식 투명성 기록)
- 수정: architecture.md (후원 흐름을 A안과 B안으로 분리, 보안 표 갱신), overview.md, index.md

## [2026-09-25] tech | Spring Boot 서버 프로젝트 생성
- server/ 생성: Spring Boot 4.1.1, Java 25 (toolchain 자동 설치), Gradle 9.8.0, PostgreSQL 18 (Docker Compose, Testcontainers)
- 보안 기본값: health 외 모든 요청 거부, 비밀 정보는 .env로 분리
- 추가: tech/spring-security.md, tech/flyway.md, tech/docker-compose.md, tech/testcontainers.md
- 수정: tech/spring-boot.md (주요 설정, Lombok 미사용), versions.md, architecture.md (저장소 구조 추가, 섹션 번호 변경), index.md

## [2026-09-25] trouble | 기본 계정 비밀번호가 로그에 출력되는 문제 해결
- 추가: troubleshooting/2026-09-25-generated-password-in-log.md
- 수정: troubleshooting/README.md, tech/spring-security.md
- 확인: 테스트 통과(Testcontainers, PostgreSQL 18.6), 로컬 실행 시 health 200, 그 외 경로 403

## [2026-09-25] feature | 시설 테이블 설계와 엔티티 구현 (보육원 지도 1~3단계)
- 추가: data-model.md (facility 테이블 설계, 좌표와 시간 타입 선택 이유)
- 구현: V1__create_facility.sql, Facility, FacilityType, FacilityRepository, BaseTimeEntity, JpaConfig, 테스트 7개
- 수정: tech/flyway.md, architecture.md (저장소 구조), features/orphanage-map.md, index.md

## [2026-09-25] setup | 구현 진행 방식 규칙 추가
- 수정: CLAUDE.md (코드 리서치 → 구현 계획 → 피드백 → 구현, 단계별 md 파일은 docs/work/에 둔다)

## [2026-09-26] tech | Android 패키지 이름 확정, NCP 애플리케이션 등록
- Android applicationId를 `com.bobohu`로 확정 (NCP 서비스 환경에 등록)
- 수정: tech/naver-maps.md, architecture.md

## [2026-09-26] research | 작업 01 주소 정리: 코드 리서치
- 추가: docs/work/01-address-normalization/research.md

## [2026-09-26] plan | 작업 01 주소 정리: 구현 계획
- 추가: docs/work/01-address-normalization/plan.md

## [2026-09-26] feature | 작업 01 주소 정리: 피드백과 구현
- 추가: docs/work/01-address-normalization/feedback.md, implementation.md
- 구현: Sido, NormalizedAddress, AddressNormalizer, 테스트 74개 (826건 중 819건 도로명 인식, 시·도와 시·군·구 100%)
- 수정: data-model.md (address, sido, sigungu 형식), data-sources.md, work/01 research.md (경북 줄임 표기 정정)

## [2026-09-26] feature | 작업 01 주소 정리 결과 확인용 시각화 페이지
- claude.ai 비공개 Artifact로 게시. 기록: work/01 implementation.md 9장

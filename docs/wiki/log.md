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

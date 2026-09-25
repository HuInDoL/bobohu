# 위키 목차

LLM이 질문에 답하거나 작업을 시작할 때 가장 먼저 읽는 페이지입니다. 페이지를 추가하거나 지우면 여기도 함께 고칩니다.

## 개요
- [프로젝트 개요](overview.md): 목표, 기술 스택, 주요 결정, 로드맵, 미정 사항
- [버전 및 호환성](versions.md): 모든 기술의 버전, 호환 조건, 버전 선택 이유, 면접 답변
- [보육원 데이터 출처](data-sources.md): 아동양육시설 데이터 후보 출처, 용어 주의점, 수집 전략

## 기술 (tech/)
- [Java](tech/java.md): 서버 언어. Java 25 LTS, 17 이후 주요 기능, 가상 스레드
- [Spring Boot](tech/spring-boot.md): 서버 프레임워크. IoC/DI, AOP, 자동 설정, Boot 4 변경점
- [Kotlin](tech/kotlin.md): Android 앱 언어. Java와 비교, 선택하지 않은 크로스 플랫폼 대안
- [Jetpack Compose](tech/jetpack-compose.md): Android 선언형 UI. 상태, 리컴포지션, BOM
- [네이버 지도](tech/naver-maps.md): 지도 SDK와 Geocoding. 카카오맵, Google Maps와 비교
- [PostgreSQL](tech/postgresql.md): DB. MVCC, pgvector, MySQL 대신 선택한 이유
- [Function Calling](tech/function-calling.md): LLM이 검색 조건만 추출하고 조회는 서버가 하는 방식. Text-to-SQL과 비교
- [RAG](tech/rag.md): 임베딩, 벡터 검색, 하이브리드 검색. 파인튜닝과 비교

## 기능 (features/)
- [보육원 지도](features/orphanage-map.md): 지도에 보육원 위치와 정보를 표시하는 MVP 기능 (계획 중)
- [시설 담당자 정보 관리](features/facility-management.md): 담당자 인증과 세부 정보 입력, 아동 개인정보 원칙 (예정)
- [AI 보육원 검색](features/ai-search.md): 자연어 질문 → 답변 + 보육원 목록. Function Calling → RAG 단계적 도입 (예정)

## 트러블슈팅
- [트러블슈팅 목록](../troubleshooting/README.md)

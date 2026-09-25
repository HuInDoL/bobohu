# PostgreSQL

> 오픈소스 객체-관계형 데이터베이스. 확장 기능으로 위치(PostGIS)와 벡터(pgvector) 검색까지 한 DB에서 처리할 수 있다. 이 프로젝트의 DB다.

## 개념
SQL 표준을 엄격하게 따르는 관계형 데이터베이스다. 확장(Extension) 구조가 강점이라 필요한 기능을 DB에 플러그인처럼 추가할 수 있다.

## 핵심 내용
- **MVCC(다중 버전 동시성 제어)**: 데이터를 수정할 때 기존 행을 덮어쓰지 않고 새 버전을 만든다. 읽기와 쓰기가 서로 막지 않는다. 그 대신 오래된 버전을 정리하는 **VACUUM**이 필요하다.
- **트랜잭션 격리 수준**: 기본값은 READ COMMITTED다. (MySQL InnoDB의 기본값은 REPEATABLE READ)
- **풍부한 타입**: JSONB(인덱스 가능한 JSON), 배열, 범위 타입 등
- **확장 기능**
  - **pgvector**: 벡터 타입과 유사도 검색(코사인 거리 등), HNSW/IVFFlat 인덱스. [RAG](rag.md)에서 사용
  - **PostGIS**: 고급 공간 연산 (다각형 포함 여부, 공간 집계 등)
- **인덱스 종류**: B-Tree(기본), GIN(JSONB, 전문 검색), GiST(공간 데이터), HNSW(벡터, pgvector)

## 이 프로젝트에서 사용한 방법
- **버전**: 18. 선택 이유는 [버전 및 호환성](../versions.md#postgresql-18)을 참고한다.
- **왜 PostgreSQL인가**: AI 기능(RAG)에 벡터 검색이 필요하다. PostgreSQL은 pgvector로 일반 데이터, 위치, 벡터를 DB 하나에서 처리할 수 있다. 별도의 벡터 DB를 운영하지 않아도 된다.
- **확장 도입 계획**: 지금은 기본 기능만 쓴다. pgvector는 RAG 단계에서, PostGIS는 복잡한 공간 분석이 필요해질 때 추가한다.
- **어디에 쓰는가**: 구현 후 경로 추가

## 트레이드오프
| 장점 | 단점 |
|---|---|
| 확장 기능으로 벡터, 공간 검색을 한 DB에서 처리 | 국내 채용 공고와 한국어 자료는 MySQL보다 적다 |
| SQL 표준을 엄격하게 따르고 Oracle과 비슷한 부분이 많다 | VACUUM 등 운영할 때 이해해야 할 개념이 있다 |
| JSONB 등 타입이 풍부하다 | 단순 읽기 위주 작업에서는 MySQL이 더 가볍다는 평가가 있다 |

## 대안 기술
| 대안 | 특징 | 선택하지 않은 이유 |
|---|---|---|
| MySQL 8.4 / 9.7 | 국내에서 가장 흔하고 자료가 많다. 위치 검색 기본 기능은 충분하다 | 커뮤니티판은 벡터 거리 계산 함수와 벡터 인덱스가 없다(HeatWave 전용). RAG를 하려면 벡터 DB를 따로 운영해야 한다 |
| Oracle | 금융권 레거시의 표준. Spatial, Vector 기능 내장 | 개인 프로젝트에서 운영하기에 비용과 제약이 크다 |
| MySQL + 별도 벡터 DB (Qdrant, Pinecone 등) | 각자 잘하는 일에 전문화 | 운영할 시스템이 늘고 데이터 동기화 문제가 생긴다 |

## 면접에서 나올 만한 질문
- Q. 왜 MySQL이 아니라 PostgreSQL을 선택했나요?
  - A. 처음에는 채용 시장과 자료량 때문에 MySQL을 검토했습니다. 그런데 RAG 기반 AI 검색을 계획하면서 벡터 검색이 필요해졌습니다. MySQL 커뮤니티판은 벡터를 저장만 할 수 있고 유사도 검색 함수와 인덱스가 없습니다. PostgreSQL은 pgvector 확장으로 하나의 DB에서 관계형 데이터와 벡터 검색을 함께 처리할 수 있어서 운영 복잡도를 줄일 수 있었습니다.
- Q. MVCC란 무엇이고 PostgreSQL과 MySQL은 어떻게 다르게 구현하나요?
  - A. 둘 다 읽기와 쓰기가 서로 막지 않도록 여러 버전을 유지합니다. PostgreSQL은 테이블 안에 새 행 버전을 만들고 나중에 VACUUM으로 정리합니다. MySQL InnoDB는 원본 행을 바꾸고 이전 버전을 Undo 로그에 둡니다.
- Q. 트랜잭션 격리 수준 4가지와 각각에서 생길 수 있는 문제는?

## 관련 트러블슈팅
- 

## 참고 자료
- [PostgreSQL 지원 기간 (endoflife.date)](https://endoflife.date/postgresql)
- [MySQL 9.7 Vector Functions](https://dev.mysql.com/doc/refman/9.7/en/vector-functions.html)
- [pgvector](https://github.com/pgvector/pgvector)

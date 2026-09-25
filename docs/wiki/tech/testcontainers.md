# Testcontainers

> 테스트 코드에서 실제 DB 같은 외부 시스템을 Docker 컨테이너로 띄워 쓰게 해주는 라이브러리.

## 개념
DB가 필요한 테스트를 H2 같은 가짜 DB로 돌리면, 운영 DB(PostgreSQL)와 동작이 달라서 테스트는 통과했는데 운영에서 실패하는 일이 생긴다. Testcontainers는 테스트를 시작할 때 진짜 PostgreSQL 컨테이너를 띄우고, 테스트가 끝나면 지운다.

## 핵심 내용
- **@ServiceConnection**: Spring Boot가 컨테이너의 주소, 포트, 계정을 자동으로 읽어서 DataSource에 연결한다. 접속 정보를 직접 설정할 필요가 없다.
- **테스트마다 깨끗한 DB**: 컨테이너가 새로 뜨므로 이전 테스트 데이터가 남지 않는다.
- **Docker 필요**: 테스트를 실행하는 PC와 CI 환경에 Docker가 있어야 한다.

## 이 프로젝트에서 사용한 방법
- **파일**: `server/src/test/java/com/bobohu/TestcontainersConfiguration.java`
- **이미지**: `postgres:18`로 고정 (운영과 같은 버전)
- `TestBobohuServerApplication`을 실행하면 컨테이너 DB로 앱을 띄울 수도 있다.

## 트레이드오프
| 장점 | 단점 |
|---|---|
| 운영과 같은 DB로 테스트해서 신뢰도가 높다 | 컨테이너를 띄우는 시간만큼 테스트가 느려진다 |
| 접속 정보를 자동으로 연결해 준다 | Docker가 없으면 테스트를 실행할 수 없다 |

## 대안 기술
| 대안 | 특징 | 선택하지 않은 이유 |
|---|---|---|
| H2 인메모리 DB | 빠르고 설치가 필요 없다 | PostgreSQL 전용 기능(pgvector 등)을 테스트할 수 없다 |
| 공용 테스트 DB 서버 | 컨테이너를 띄울 필요가 없다 | 테스트끼리 데이터가 섞이고, 동시에 돌리기 어렵다 |

## 면접에서 나올 만한 질문
- Q. 통합 테스트와 단위 테스트는 어떻게 나눴나요?
- Q. Testcontainers를 쓰면 테스트가 느려지는데 어떻게 대응했나요?

## 관련 트러블슈팅
- 

## 참고 자료
- [Spring Boot Testcontainers](https://docs.spring.io/spring-boot/reference/testing/testcontainers.html)

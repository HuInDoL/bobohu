# Docker Compose (로컬 개발 환경)

> 여러 컨테이너(DB 등)를 설정 파일 하나로 정의하고 한 번에 실행하는 도구.

## 개념
Docker는 프로그램과 실행 환경을 컨테이너라는 격리된 단위로 묶는다. PostgreSQL을 PC에 직접 설치하지 않고 컨테이너로 띄우면 누구의 PC에서든 같은 버전, 같은 설정의 DB를 쓸 수 있다. Compose는 이 컨테이너 구성을 `compose.yaml` 파일로 관리한다.

## 핵심 내용
- **이미지와 컨테이너**: 이미지는 설치 파일이고, 컨테이너는 그 이미지로 실행 중인 프로그램이다.
- **포트 매핑**: `호스트:컨테이너` 형식이다. `127.0.0.1::5432`처럼 쓰면 내 PC에서만 접속할 수 있고, 호스트 포트는 비어 있는 번호로 자동 배정된다.
- **`.env` 파일**: Compose는 같은 폴더의 `.env`에서 `${변수}` 값을 읽는다.
- **Spring Boot Docker Compose 지원**: `spring-boot-docker-compose` 의존성이 있으면, 앱을 실행할 때 Spring Boot가 `compose.yaml`의 컨테이너를 자동으로 띄운다. DB 접속 정보도 자동으로 연결해 준다. 개발할 때만 쓰는 의존성(`developmentOnly`)이라 운영 빌드에는 포함되지 않는다.

## 이 프로젝트에서 사용한 방법
- **파일**: `server/compose.yaml`, `server/.env.example`
- **DB 이미지**: `postgres:18`로 고정한다. `latest`는 언제 버전이 바뀔지 몰라서 쓰지 않는다.
- **비밀번호**: `compose.yaml`에 직접 적지 않고 `.env`에서 읽는다. `.env`는 `.gitignore`에 넣었고, 형식은 `.env.example`로 공유한다.
- **포트**: 내 PC에서만 접속 가능하게 `127.0.0.1`에 묶었다. 로컬에 이미 설치된 PostgreSQL(5432)과 겹치지 않게 호스트 포트를 자동 배정한다.

## 트레이드오프
| 장점 | 단점 |
|---|---|
| 누구나 같은 DB 환경을 명령 한 번으로 만든다 | Docker Desktop이 실행 중이어야 한다 |
| PC에 DB를 설치하지 않아도 된다 | 메모리를 더 쓴다 |
| 버전을 파일에 고정해서 관리할 수 있다 | Windows에서는 WSL2 설정이 필요하다 |

## 대안 기술
| 대안 | 특징 | 선택하지 않은 이유 |
|---|---|---|
| PC에 PostgreSQL 직접 설치 | Docker가 필요 없다 | 개발자마다 버전과 설정이 달라진다. 지금 PC에는 17이 설치되어 있는데 프로젝트는 18을 쓴다 |
| H2 같은 인메모리 DB | 설치가 필요 없고 빠르다 | 운영 DB와 SQL 문법, 기능(pgvector 등)이 달라서 운영에서만 생기는 버그를 놓친다 |

## 면접에서 나올 만한 질문
- Q. 테스트나 개발에 H2 대신 실제 PostgreSQL을 쓴 이유는?
- Q. 컨테이너와 가상 머신(VM)의 차이는?

## 관련 트러블슈팅
- 

## 참고 자료
- [Spring Boot Docker Compose Support](https://docs.spring.io/spring-boot/reference/features/dev-services.html)

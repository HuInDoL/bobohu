# bobohu server

보육원 정보와 후원 연결 서비스의 API 서버입니다. Java 25, Spring Boot 4.1, PostgreSQL 18을 씁니다.
설계와 기술 문서는 [`docs/wiki`](../docs/wiki/index.md)에 있습니다.

## 준비
- JDK 17 이상: Gradle 실행용. 빌드에 필요한 JDK 25는 Gradle이 자동으로 내려받습니다.
- Docker Desktop: 로컬 DB와 테스트 DB를 띄우는 데 필요합니다.

## 처음 한 번
```bash
cp .env.example .env   # 비밀번호를 원하는 값으로 바꿉니다. .env는 git에 올라가지 않습니다
```

## 실행
```bash
./gradlew bootRun      # compose.yaml의 PostgreSQL 컨테이너가 자동으로 뜹니다
```
확인: `curl http://localhost:8080/actuator/health` → `{"status":"UP"}`

## 테스트
```bash
./gradlew test         # Testcontainers가 PostgreSQL 18 컨테이너를 띄워서 테스트합니다
```

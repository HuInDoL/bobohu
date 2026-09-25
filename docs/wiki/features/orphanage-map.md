# 보육원 지도

> 상태: 계획 중

## 목적
사용자가 지도에서 주변 보육원을 찾고 기본 정보를 볼 수 있게 한다. 후원 기능의 출발점이 되는 MVP 기능이다.

## 요구사항
- [ ] 보육원 데이터(이름, 주소, 위치 좌표, 연락처 등)를 저장한다
- [ ] 지도에 보육원 위치를 마커로 표시한다
- [ ] 마커를 누르면 보육원 정보를 보여준다
- [ ] 앱과 웹이 같은 API를 사용한다

## 동작 흐름
_설계 후 작성. 초안:_
1. 보육원 데이터(주소 포함)를 서버에 적재한다.
2. 서버가 네이버 Geocoding API로 주소를 좌표로 바꿔 DB에 저장한다.
3. 앱이 서버 API로 보육원 목록(좌표 포함)을 받는다.
4. 앱이 네이버 지도에 마커를 표시하고, 마커를 누르면 정보를 보여준다.

## 구현 위치
- 테이블: `server/src/main/resources/db/migration/V1__create_facility.sql` ([데이터 모델](../data-model.md))
- 엔티티: `server/src/main/java/com/bobohu/facility/domain/`

## 사용 기술
- 서버: [Java](../tech/java.md), [Spring Boot](../tech/spring-boot.md)
- 앱: [Kotlin](../tech/kotlin.md), [Jetpack Compose](../tech/jetpack-compose.md)
- 지도: [네이버 지도](../tech/naver-maps.md)
- DB: [PostgreSQL](../tech/postgresql.md)

## 관련 트러블슈팅
- 

## 이후 개선할 점
- 내 위치 기준 가까운 보육원 찾기
- 지역, 이름으로 검색하기

# 네이버 지도 (NAVER Maps)

> 네이버 클라우드 플랫폼(NCP)이 제공하는 지도 API. Android SDK, iOS SDK, Web JavaScript API, Geocoding API를 제공한다.

## 개념
앱이나 웹에 네이버 지도를 띄우고, 마커나 정보 창을 올릴 수 있게 해주는 API다. 주소를 좌표로 바꾸는 Geocoding 같은 서버용 REST API도 함께 제공한다.

## 핵심 내용
- **인증**: NCP 콘솔에서 애플리케이션을 등록하고 Client ID를 발급받는다. Android는 앱 패키지 이름, 웹은 서비스 URL을 등록해야 한다. 등록할 때 **Dynamic Map**을 선택하지 않으면 인증 오류가 난다.
- **Android SDK**: `com.naver.maps:map-sdk`. 네이버 전용 Maven 저장소(`https://repository.map.naver.com/archive/maven`)를 추가해야 한다.
- **주요 객체**: `MapView`/`MapFragment`(지도 화면), `NaverMap`(지도 조작), `Marker`, `InfoWindow`, `LatLng`(위도와 경도)
- **Geocoding**: 주소 → 좌표 변환. 서버(Spring)에서 호출하고, 결과 좌표를 DB에 저장해 둔다.

## 이 프로젝트에서 사용한 방법
- **버전**: Android SDK 3.24.0
- **NCP 애플리케이션 등록** (2026-09-26)
  - 서비스 환경의 Android 앱 패키지 이름: `com.bobohu`. 앱의 `applicationId`와 같아야 지도 인증이 통과한다.
  - 웹 서비스 URL: 웹 개발(MVP 이후)을 시작할 때 추가한다.
  - Geocoding은 서버가 Client ID와 Client Secret으로 호출하므로 서비스 환경 등록과 관계없다. **Client Secret은 코드와 git에 넣지 않고 `server/.env`에 둔다.**
- **왜 네이버 지도인가**: 한국 사용자에게 가장 익숙한 지도이고, 국내 데이터 정확도가 높다.
- **데이터 흐름 (계획)**: 보육원 주소 → 서버에서 Geocoding으로 좌표 변환 → DB 저장 → 앱이 API로 좌표를 받아 마커 표시
- **Compose 연동**: SDK가 View 기반이라 `AndroidView`로 감싸서 쓴다. [Jetpack Compose](jetpack-compose.md) 참고.
- **어디에 쓰는가**: 구현 후 경로 추가

## 트레이드오프
| 장점 | 단점 |
|---|---|
| 한국 사용자에게 가장 익숙하다 | NCP 가입과 결제 수단 등록이 필요하다 |
| 국내 지도 데이터가 정확하다 | 요금 정책이 자주 바뀐다. 무료 사용량을 반드시 확인해야 한다 |
| Android, iOS, Web 공식 SDK가 모두 있다 | 해외 지도 데이터는 약하다 |
| | Compose 전용 공식 API가 없어 View를 감싸서 써야 한다 |

## 대안 기술
| 대안 | 특징 | 선택하지 않은 이유 |
|---|---|---|
| 카카오맵 | 국내 데이터 품질이 비슷하고, Kakao Developers에서 앱 등록만 하면 시작할 수 있다. 로컬 API(주소 검색)가 강하다 | 사용자 친숙도에서 네이버가 조금 앞선다고 판단했다 |
| Google Maps | Compose 공식 라이브러리가 있어 개발 경험이 좋다. 해외 확장에 유리하다 | 국내 지도 데이터 반출 제한으로 한국 지도 정보가 부족하다. 국내 사용자에게 낯설다 |

## 확인할 것
- [ ] NCP Maps의 현재 요금과 무료 사용량 (확인 필요)
- [ ] Geocoding 결과를 저장하고 표시하는 방법에 대한 이용약관 조건 (확인 필요)

## 면접에서 나올 만한 질문
- Q. 지도 API로 무엇을 골랐고, 왜 그걸 골랐나요?
  - A. 네이버 지도를 골랐습니다. 국내 서비스라 데이터 정확도와 사용자 친숙도가 가장 중요했습니다. Google Maps는 국내 지도 데이터 반출 제한으로 한국 정보가 부족해서 제외했습니다. 카카오맵도 품질은 비슷했지만 사용자 친숙도를 우선했습니다.
- Q. 주소를 좌표로 바꾸는 작업은 앱과 서버 중 어디서 하나요? 왜요?
  - A. 서버에서 합니다. 보육원 주소는 거의 바뀌지 않으니, 한 번 변환한 좌표를 DB에 저장해 두면 API 호출 비용이 줄고 앱이 더 빨리 뜹니다. API 키도 클라이언트에 노출되지 않습니다.

## 관련 트러블슈팅
- 

## 참고 자료
- [네이버 지도 Android SDK 가이드](https://navermaps.github.io/android-map-sdk/guide-ko/1.html)

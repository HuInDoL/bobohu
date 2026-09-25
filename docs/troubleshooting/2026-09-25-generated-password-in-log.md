# Spring Security가 기본 계정 비밀번호를 로그에 출력함

- 날짜: 2026-09-25
- 관련 기술: [Spring Security](../wiki/tech/spring-security.md)
- 관련 기능: 서버 기본 설정

## 문제 상황
서버를 처음 실행했더니 시작 로그에 다음 경고가 찍혔다.

```
WARN ... UserDetailsServiceAutoConfiguration :
Using generated security password: <비밀번호 원문>
```

`SecurityFilterChain`에서 HTTP Basic과 로그인 폼을 모두 꺼서 이 계정으로 로그인할 방법은 없었다. 하지만 **비밀번호 원문이 로그에 남는 것**은 "로그에 비밀 정보를 남기지 않는다"는 보안 원칙에 어긋난다. 운영 환경이었다면 로그 수집 시스템에 비밀번호가 쌓였을 것이다.

## 원인
Spring Boot는 `UserDetailsService` Bean이 없으면, 개발 편의를 위해 `user`라는 계정을 자동으로 만든다. 비밀번호는 무작위로 생성해서 로그에 출력한다(`UserDetailsServiceAutoConfiguration`). `SecurityFilterChain`을 직접 정의해도 이 자동 설정은 꺼지지 않는다.

## 해결
`UserDetailsService` Bean을 직접 등록했다. 어떤 사용자도 찾지 못하는 구현이다. 직접 등록한 Bean이 있으면 자동 설정이 물러난다.

```java
@Bean
UserDetailsService userDetailsService() {
	return username -> {
		throw new UsernameNotFoundException(username);
	};
}
```

파일: `server/src/main/java/com/bobohu/common/config/SecurityConfig.java`
확인: 다시 실행한 로그에 `generated security password`가 나오지 않았다. `/actuator/health`는 200, 그 외 경로는 403으로 응답했다.

## 배운 점
- Spring Boot의 자동 설정은 "Bean이 없으면 기본값을 만든다"(`@ConditionalOnMissingBean`)는 방식으로 동작한다. 자동 설정을 끄는 가장 확실한 방법은 같은 타입의 Bean을 직접 등록하는 것이다.
- 기능이 막혀 있어도 비밀 정보가 로그에 찍히면 문제다. 첫 실행 로그의 WARN은 반드시 읽는다.
- 회원 인증을 구현하면 이 Bean을 실제 사용자 조회 로직으로 바꾼다.

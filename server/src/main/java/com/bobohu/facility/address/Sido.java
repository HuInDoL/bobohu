package com.bobohu.facility.address;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 시·도 17개. 공식 이름과 주소에 흔히 쓰이는 별칭을 가진다.
 * 옛 이름(강원도, 전라북도)도 별칭으로 받아서 현재 이름으로 바꾼다.
 */
public enum Sido {
	SEOUL("서울특별시", "서울시", "서울"),
	BUSAN("부산광역시", "부산시", "부산"),
	DAEGU("대구광역시", "대구시", "대구"),
	INCHEON("인천광역시", "인천시", "인천"),
	// "광주시"는 경기도 광주시와 헷갈리므로 별칭으로 받지 않는다
	GWANGJU("광주광역시", "광주"),
	DAEJEON("대전광역시", "대전시", "대전"),
	ULSAN("울산광역시", "울산시", "울산"),
	SEJONG("세종특별자치시", "세종시", "세종"),
	GYEONGGI("경기도", "경기"),
	// 2023-06-11 강원도 → 강원특별자치도
	GANGWON("강원특별자치도", "강원도", "강원"),
	CHUNGBUK("충청북도", "충북"),
	CHUNGNAM("충청남도", "충남"),
	// 2024-01-18 전라북도 → 전북특별자치도
	JEONBUK("전북특별자치도", "전라북도", "전북"),
	JEONNAM("전라남도", "전남"),
	GYEONGBUK("경상북도", "경북"),
	GYEONGNAM("경상남도", "경남"),
	JEJU("제주특별자치도", "제주도", "제주");

	private final String officialName;
	private final List<String> aliases;

	Sido(String officialName, String... aliases) {
		this.officialName = officialName;
		this.aliases = List.of(aliases);
	}

	public String officialName() {
		return officialName;
	}

	/**
	 * 공식 이름이나 별칭에 해당하는 시·도를 찾는다.
	 */
	public static Optional<Sido> fromAlias(String token) {
		return Arrays.stream(values())
			.filter(sido -> sido.matches(token))
			.findFirst();
	}

	boolean matches(String token) {
		return officialName.equals(token) || aliases.contains(token);
	}
}

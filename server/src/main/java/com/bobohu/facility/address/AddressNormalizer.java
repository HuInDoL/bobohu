package com.bobohu.facility.address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 공공데이터의 주소를 Geocoding에 넣을 수 있는 형태로 정리한다.
 * 표준 주소와 시·군·구는 Geocoding 응답으로 확정하므로, 여기서는 명백한 오류만 고친다.
 * 규칙과 설계 이유는 docs/work/01-address-normalization/plan.md 참고.
 *
 * <p>상태가 없는 순수 로직이라 스프링에 의존하지 않는다.
 */
public class AddressNormalizer {

	static final int MAX_LENGTH = 300;

	/** 시·도 없이 시 이름으로 시작하는 주소에 붙일 시·도 (CSV에서 발견한 것만) */
	private static final Map<String, Sido> SIDO_BY_CITY = Map.of(
			"천안시", Sido.CHUNGNAM,
			"당진시", Sido.CHUNGNAM,
			"용인시", Sido.GYEONGGI,
			"안성시", Sido.GYEONGGI);

	private static final Pattern PARENTHESES = Pattern.compile("\\([^)]*\\)|\\([^)]*$");

	/** 도로명주소에 잘못 붙은 "번지". 예: 군왕로119번지 → 군왕로119, 22-7번지 → 22-7 */
	private static final Pattern BEONJI = Pattern.compile("(\\d)번지");

	/** 토큰 끝의 쉼표와 마침표. 예: "415," → "415", "24." → "24" */
	private static final Pattern TRAILING_PUNCTUATION = Pattern.compile("[,.]+$");

	/** 상세주소로 보이는 토큰. 예: 501호, 3층, 1,2층, 103동 */
	private static final Pattern DETAIL = Pattern.compile("^.*\\d+[호층].*$|^\\d+동$");

	/** 읍·면 이름과 도로명이 붙은 토큰. 예: 합덕읍버그내1길193-11 */
	private static final Pattern EUP_MYEON_GLUED = Pattern.compile("^([가-힣]+[읍면])([가-힣0-9]+(?:로|길).*)$");

	/** 도로명 뒤에 떨어져 나온 "숫자+길" 토큰. 예: "상도로 39길"의 "39길", "한글비석로 49길20"의 "49길20" */
	private static final Pattern ROAD_SUFFIX = Pattern.compile("^\\d+번?[가-힣]?길.*$");

	/** 도로명과 건물번호가 붙은 토큰. 숫자 뒤에 한글이 오면(예: 상도로39길) 도로명의 일부이므로 나누지 않는다 */
	private static final Pattern ROAD_WITH_NUMBER = Pattern.compile("^(.*(?:로|길))(\\d+(?:-\\d+)?)(?![0-9가-힣])(.*)$");

	/** 한글이 하나 이상 있고 "로"나 "길"로 끝나는 토큰. 예: 용마산로, 봉선2로, 자양로44길, 덕천로234번길 */
	private static final Pattern ROAD_NAME = Pattern.compile("^(?=.*[가-힣])[가-힣0-9]+(?:로|길)$");

	private static final Pattern BUILDING_NUMBER = Pattern.compile("^\\d+(?:-\\d+)?$");

	private static final Pattern HANGUL_WORD = Pattern.compile("^[가-힣]+$");

	public NormalizedAddress normalize(String raw) {
		validate(raw);

		String cleaned = PARENTHESES.matcher(raw).replaceAll(" ");
		cleaned = BEONJI.matcher(cleaned).replaceAll("$1");
		List<String> tokens = tokenize(cleaned);

		Optional<Sido> sido = resolveSido(tokens);
		if (sido.isPresent()) {
			sido = Optional.of(applyJurisdictionChange(sido.get(), tokens));
			removeRepeatedSido(sido.get(), tokens);
		}
		removeConsecutiveDuplicates(tokens);
		splitEupMyeonFromRoad(tokens);
		joinSplitRoadNames(tokens);
		splitRoadAndBuildingNumber(tokens);

		int buildingIndex = findBuildingNumberIndex(tokens);
		boolean roadAddressFound = buildingIndex >= 0;
		List<String> addressTokens = roadAddressFound
				? tokens.subList(0, buildingIndex + 1)
				: withoutDetail(tokens);

		return new NormalizedAddress(
				String.join(" ", addressTokens),
				sido.map(Sido::officialName).orElse(null),
				sido.map(s -> extractSigungu(s, tokens)).orElse(null),
				roadAddressFound);
	}

	private List<String> tokenize(String text) {
		List<String> tokens = new ArrayList<>();
		for (String token : text.trim().split("\\s+")) {
			String trimmed = TRAILING_PUNCTUATION.matcher(token).replaceAll("");
			if (!trimmed.isEmpty()) {
				tokens.add(trimmed);
			}
		}
		return tokens;
	}

	/**
	 * 건물번호를 찾지 못했을 때의 주소. 도로명이 있으면 도로명까지 남기고, 없으면 상세주소로 보이는 토큰만 뺀다.
	 * 어느 경우든 동·호·층이 주소에 남지 않게 한다.
	 */
	private List<String> withoutDetail(List<String> tokens) {
		for (int i = tokens.size() - 1; i >= 1; i--) {
			if (ROAD_NAME.matcher(tokens.get(i)).matches()) {
				return tokens.subList(0, i + 1);
			}
		}
		return tokens.stream().filter(token -> !DETAIL.matcher(token).matches()).toList();
	}

	private void validate(String raw) {
		if (raw == null || raw.isBlank()) {
			throw new IllegalArgumentException("주소가 비어 있습니다");
		}
		if (raw.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("주소가 " + MAX_LENGTH + "자를 넘습니다");
		}
	}

	/**
	 * 첫 토큰을 시·도 공식 이름으로 바꾼다. 시·도 없이 시 이름으로 시작하면 시·도를 앞에 붙인다.
	 */
	private Optional<Sido> resolveSido(List<String> tokens) {
		Optional<Sido> sido = Sido.fromAlias(tokens.getFirst());
		if (sido.isPresent()) {
			tokens.set(0, sido.get().officialName());
			return sido;
		}
		Sido inferred = SIDO_BY_CITY.get(tokens.getFirst());
		if (inferred != null) {
			tokens.addFirst(inferred.officialName());
			return Optional.of(inferred);
		}
		return Optional.empty();
	}

	/**
	 * 관할이 바뀐 지역을 반영한다. 2023-07-01 경상북도 군위군 → 대구광역시 군위군.
	 */
	private Sido applyJurisdictionChange(Sido sido, List<String> tokens) {
		if (sido == Sido.GYEONGBUK && tokens.size() > 1 && tokens.get(1).equals("군위군")) {
			tokens.set(0, Sido.DAEGU.officialName());
			return Sido.DAEGU;
		}
		return sido;
	}

	/**
	 * 시·도가 한 번 더 나오는 경우를 지운다. 예: "세종특별자치시 세종시", "서울특별시 영등포구 서울 영등포구"
	 */
	private void removeRepeatedSido(Sido sido, List<String> tokens) {
		for (int i = tokens.size() - 1; i >= 1; i--) {
			if (sido.matches(tokens.get(i))) {
				tokens.remove(i);
			}
		}
	}

	/**
	 * 같은 이름이 두 번 나오면 하나를 지운다.
	 * 줄인 이름 뒤에 정식 이름이 오는 경우(예: "포항 포항시")는 줄인 이름을 지운다.
	 */
	private void removeConsecutiveDuplicates(List<String> tokens) {
		for (int i = tokens.size() - 1; i >= 1; i--) {
			String previous = tokens.get(i - 1);
			String current = tokens.get(i);
			if (current.equals(previous)) {
				tokens.remove(i);
			}
			else if (i >= 2 && (current.equals(previous + "시") || current.equals(previous + "군"))) {
				tokens.remove(i - 1);
			}
		}
	}

	/** 예: "합덕읍버그내1길193-11" → "합덕읍", "버그내1길193-11" */
	private void splitEupMyeonFromRoad(List<String> tokens) {
		for (int i = tokens.size() - 1; i >= 0; i--) {
			Matcher matcher = EUP_MYEON_GLUED.matcher(tokens.get(i));
			if (matcher.matches()) {
				tokens.set(i, matcher.group(1));
				tokens.add(i + 1, matcher.group(2));
			}
		}
	}

	/** 예: "상도로", "39길" → "상도로39길" / "터미널", "9길" → "터미널9길" */
	private void joinSplitRoadNames(List<String> tokens) {
		for (int i = tokens.size() - 2; i >= 1; i--) {
			String current = tokens.get(i);
			if (HANGUL_WORD.matcher(current).matches() && !isAdministrativeUnit(current)
					&& ROAD_SUFFIX.matcher(tokens.get(i + 1)).matches()) {
				tokens.set(i, current + tokens.get(i + 1));
				tokens.remove(i + 1);
			}
		}
	}

	/** 예: "용마산로271" → "용마산로", "271" / "동소문로15길99" → "동소문로15길", "99" */
	private void splitRoadAndBuildingNumber(List<String> tokens) {
		for (int i = tokens.size() - 1; i >= 0; i--) {
			Matcher matcher = ROAD_WITH_NUMBER.matcher(tokens.get(i));
			if (matcher.matches()) {
				List<String> parts = new ArrayList<>(List.of(matcher.group(1), matcher.group(2)));
				if (!matcher.group(3).isBlank()) {
					parts.add(matcher.group(3));
				}
				tokens.remove(i);
				tokens.addAll(i, parts);
			}
		}
	}

	/**
	 * 도로명 바로 뒤에 오는 건물번호의 위치. 없으면 -1.
	 * 첫 토큰은 시·도나 시이므로 건너뛴다.
	 */
	private int findBuildingNumberIndex(List<String> tokens) {
		for (int i = 1; i < tokens.size() - 1; i++) {
			if (ROAD_NAME.matcher(tokens.get(i)).matches() && !isAdministrativeUnit(tokens.get(i))
					&& BUILDING_NUMBER.matcher(tokens.get(i + 1)).matches()) {
				return i + 1;
			}
		}
		return -1;
	}

	/**
	 * 시·도 다음의 시·군·구. 시 다음에 구가 오면 함께 담는다. 세종특별자치시는 시·군·구가 없다.
	 */
	private String extractSigungu(Sido sido, List<String> tokens) {
		if (sido == Sido.SEJONG || tokens.size() < 2) {
			return null;
		}
		String first = tokens.get(1);
		if (!first.matches(".+[시군구]")) {
			return null;
		}
		if (first.endsWith("시") && tokens.size() > 2 && tokens.get(2).matches(".+구")) {
			return first + " " + tokens.get(2);
		}
		return first;
	}

	private boolean isAdministrativeUnit(String token) {
		return token.matches(".+[시군구읍면동리가]");
	}
}

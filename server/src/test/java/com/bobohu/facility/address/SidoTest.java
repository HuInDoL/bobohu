package com.bobohu.facility.address;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SidoTest {

	@ParameterizedTest(name = "{0} → {1}")
	@CsvSource({
			"서울특별시, 서울특별시",
			"서울시, 서울특별시",
			"서울, 서울특별시",
			"부산시, 부산광역시",
			"인천, 인천광역시",
			"광주광역시, 광주광역시",
			"울산, 울산광역시",
			"세종시, 세종특별자치시",
			"충북, 충청북도",
			"충남, 충청남도",
			"경북, 경상북도",
			"강원도, 강원특별자치도",
			"전라북도, 전북특별자치도",
			"전북, 전북특별자치도",
			"제주특별자치도, 제주특별자치도"
	})
	void 별칭을_공식_이름으로_바꾼다(String alias, String officialName) {
		assertThat(Sido.fromAlias(alias)).get().extracting(Sido::officialName).isEqualTo(officialName);
	}

	@ParameterizedTest
	@ValueSource(strings = { "천안시", "광주시", "서울특별시청", "" })
	void 시_도가_아니거나_헷갈리는_이름은_찾지_않는다(String token) {
		assertThat(Sido.fromAlias(token)).isEmpty();
	}
}

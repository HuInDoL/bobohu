package com.bobohu.facility.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class FacilityTest {

	@Test
	void 공공데이터로_만든_시설의_유형은_확인되지_않은_상태다() {
		Facility facility = sample();

		assertThat(facility.isTypeConfirmed()).isFalse();
		assertThat(facility.getLatitude()).isNull();
	}

	@Test
	void 시설명이_비어_있으면_만들_수_없다() {
		assertThatThrownBy(() -> Facility.fromPublicData(" ", FacilityType.UNKNOWN, "주소", "주소", null, null,
				"MOHW_2022", "1", null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 좌표를_기록한다() {
		Facility facility = sample();

		facility.locate(37.5, 127.0);

		assertThat(facility.getLatitude()).isEqualTo(37.5);
		assertThat(facility.getLongitude()).isEqualTo(127.0);
	}

	@Test
	void 범위를_벗어난_좌표는_기록할_수_없다() {
		Facility facility = sample();

		assertThatThrownBy(() -> facility.locate(91, 127.0)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> facility.locate(37.5, 181)).isInstanceOf(IllegalArgumentException.class);
	}

	private Facility sample() {
		return Facility.fromPublicData("테스트보육원", FacilityType.CHILD_CARE, "서울시 광진구 자양로 1",
				"서울특별시 광진구 자양로 1", "서울특별시", "광진구", "MOHW_2022", "1", LocalDate.of(2022, 12, 31));
	}
}

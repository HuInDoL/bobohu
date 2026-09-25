package com.bobohu.facility.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import com.bobohu.TestcontainersConfiguration;
import com.bobohu.common.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@Import({ TestcontainersConfiguration.class, JpaConfig.class })
class FacilityRepositoryTest {

	@Autowired
	FacilityRepository facilityRepository;

	@Test
	void 시설을_저장하면_생성_시각과_수정_시각이_채워진다() {
		Facility saved = facilityRepository.saveAndFlush(sample("1"));

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getCreatedAt()).isNotNull();
		assertThat(saved.getUpdatedAt()).isNotNull();
	}

	@Test
	void 출처와_출처_식별값으로_시설을_찾는다() {
		facilityRepository.saveAndFlush(sample("7"));

		assertThat(facilityRepository.findBySourceAndSourceRef("MOHW_2022", "7")).isPresent();
		assertThat(facilityRepository.findBySourceAndSourceRef("MOHW_2022", "8")).isEmpty();
	}

	@Test
	void 같은_출처와_출처_식별값은_두_번_저장할_수_없다() {
		facilityRepository.saveAndFlush(sample("3"));

		assertThatThrownBy(() -> facilityRepository.saveAndFlush(sample("3")))
			.isInstanceOf(DataIntegrityViolationException.class);
	}

	private Facility sample(String sourceRef) {
		return Facility.fromPublicData("테스트보육원", FacilityType.CHILD_CARE, "서울시 광진구 자양로 1",
				"서울특별시 광진구 자양로 1", "서울특별시", "광진구", "MOHW_2022", sourceRef, LocalDate.of(2022, 12, 31));
	}
}

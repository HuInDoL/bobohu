package com.bobohu.facility.address;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * 입력 주소는 대부분 보건복지부 아동복지시설 현황 CSV(2022-12-31)의 실제 값이다.
 * 시·도 별칭과 행정구역 변경처럼 CSV의 실제 주소로 보여주기 어려운 규칙은 형식만 같은 예시 주소를 쓴다.
 */
class AddressNormalizerTest {

	private final AddressNormalizer normalizer = new AddressNormalizer();

	@ParameterizedTest(name = "[{index}] {0}")
	@CsvSource(delimiter = '|', nullValues = "NULL", textBlock = """
			# 입력                                                        | 정리된 주소                                   | 시·도          | 시·군·구
			# 공백 정리
			서울시 광진구 자양로44길 149                                    | 서울특별시 광진구 자양로44길 149               | 서울특별시      | 광진구
			서울시 성북구 장위로32길 11                                     | 서울특별시 성북구 장위로32길 11                | 서울특별시      | 성북구
			# 시·도 별칭
			부산시 서구 감천로237 (암남동)                                  | 부산광역시 서구 감천로 237                     | 부산광역시      | 서구
			인천 남동구 남동대로 100                                        | 인천광역시 남동구 남동대로 100                 | 인천광역시      | 남동구
			충북 청주시 상당구 상당로 1                                     | 충청북도 청주시 상당구 상당로 1                 | 충청북도        | 청주시 상당구
			울산 중구 종가로 1                                              | 울산광역시 중구 종가로 1                       | 울산광역시      | 중구
			# 행정구역 명칭 변경
			강원도 원주시 원일로 1                                          | 강원특별자치도 원주시 원일로 1                 | 강원특별자치도  | 원주시
			전라북도 전주시 완산구 전주객사3길 1                            | 전북특별자치도 전주시 완산구 전주객사3길 1      | 전북특별자치도  | 전주시 완산구
			전북 군산시 대학로 1                                            | 전북특별자치도 군산시 대학로 1                 | 전북특별자치도  | 군산시
			# 관할 변경 (경상북도 군위군 → 대구광역시 군위군)
			경북 군위군  가호1길 118-22 성바오로 청소년의 집                  | 대구광역시 군위군 가호1길 118-22               | 대구광역시      | 군위군
			# 시·도 누락
			천안시 서북구 서북구 노태산로 21                                 | 충청남도 천안시 서북구 노태산로 21             | 충청남도        | 천안시 서북구
			용인시 수지구 고기로45번길 40-11 본관, 동천동                     | 경기도 용인시 수지구 고기로45번길 40-11        | 경기도          | 용인시 수지구
			안성시 원곡면 청룡길 1                                          | 경기도 안성시 원곡면 청룡길 1                  | 경기도          | 안성시
			# 중복 표기
			서울시 동작구 동작구 상도로 39길 48                              | 서울특별시 동작구 상도로39길 48                | 서울특별시      | 동작구
			강원도 횡성군 횡성군 우천면 전재로33                              | 강원특별자치도 횡성군 우천면 전재로 33          | 강원특별자치도  | 횡성군
			제주특별자치도 제주시 제주시 원노형로 59 1층                        | 제주특별자치도 제주시 원노형로 59             | 제주특별자치도  | 제주시
			경상남도 창원시 창원시 팔용로 270 인애의집                        | 경상남도 창원시 팔용로 270                     | 경상남도        | 창원시
			세종시 세종시 연서면 월성로 239 영명보육원                        | 세종특별자치시 연서면 월성로 239               | 세종특별자치시  | NULL
			# 줄인 이름 뒤에 정식 이름이 한 번 더 나옴
			경상북도 포항 포항시 북구 삼호로 480-33                          | 경상북도 포항시 북구 삼호로 480-33             | 경상북도        | 포항시 북구
			경상북도 청도 청도군 청도읍 원동길 29                            | 경상북도 청도군 청도읍 원동길 29               | 경상북도        | 청도군
			서울시 영등포구 서울 영등포구 여의대방로 65 돈보스코청소년센터 3,4층 | 서울특별시 영등포구 여의대방로 65              | 서울특별시      | 영등포구
			# 도로명 안의 공백
			천안시 동남구 동남구 터미널 9길 59 205동 602호 (신부동, 대림한들아파트) | 충청남도 천안시 동남구 터미널9길 59          | 충청남도        | 천안시 동남구
			서울시 노원구 한글비석로 49길20  301호                           | 서울특별시 노원구 한글비석로49길 20            | 서울특별시      | 노원구
			서울시 송파구 올림픽로 47길 9-10 301, 302호                     | 서울특별시 송파구 올림픽로47길 9-10            | 서울특별시      | 송파구
			# 도로명과 건물번호가 붙음
			서울시 중랑구 용마산로271 103동 604호                           | 서울특별시 중랑구 용마산로 271                 | 서울특별시      | 중랑구
			서울시 성북구 동소문로15길99 122동1002호                         | 서울특별시 성북구 동소문로15길 99               | 서울특별시      | 성북구
			서울시 은평구 백련산로14길20-11 초록꿈터                          | 서울특별시 은평구 백련산로14길 20-11            | 서울특별시      | 은평구
			부산시 북구 덕천로234번길34 207동302호(만덕동,럭키아파트)           | 부산광역시 북구 덕천로234번길 34               | 부산광역시      | 북구
			서울시 강남구 양재대로344-27                                    | 서울특별시 강남구 양재대로 344-27              | 서울특별시      | 강남구
			# 도로명 끝의 숫자는 도로명의 일부 (나누지 않음)
			부산시 동래구 아시아드대로164번길 16 203호(사직동)                 | 부산광역시 동래구 아시아드대로164번길 16         | 부산광역시      | 동래구
			광주광역시 남구 봉선2로 96-14 202동 804호(봉선동)                  | 광주광역시 남구 봉선2로 96-14                  | 광주광역시      | 남구
			광주광역시 남구 효우2로90 605동 1004호(행암동)                     | 광주광역시 남구 효우2로 90                     | 광주광역시      | 남구
			# 번지, 쉼표, 마침표가 붙은 건물번호
			광주광역시 북구 군왕로119번지 1동 101호                          | 광주광역시 북구 군왕로 119                     | 광주광역시      | 북구
			전라북도 전주시 오정2길 22-7번지 201호                           | 전북특별자치도 전주시 오정2길 22-7             | 전북특별자치도  | 전주시
			경기도 성남시 희망로 415,  101동 1401호                          | 경기도 성남시 희망로 415                       | 경기도          | 성남시
			대전광역시 유성구 배울2로 24. 303동 502호                         | 대전광역시 유성구 배울2로 24                   | 대전광역시      | 유성구
			# 읍·면과 도로명이 붙음
			충남 당진시 합덕읍버그내1길193-11                               | 충청남도 당진시 합덕읍 버그내1길 193-11         | 충청남도        | 당진시
			제주특별자치도 서귀포시 대정읍신영로36번길6                        | 제주특별자치도 서귀포시 대정읍 신영로36번길 6    | 제주특별자치도  | 서귀포시
			# 읍으로 시작하는 도로명은 나누지 않음
			경상남도 창원시 의창구 읍성로 102-1 3층                          | 경상남도 창원시 의창구 읍성로 102-1            | 경상남도        | 창원시 의창구
			# 상세주소 제거
			서울시 성북구 북악산로1다길 3 1층                                | 서울특별시 성북구 북악산로1다길 3              | 서울특별시      | 성북구
			서울시 구로구 개봉로15길 74-2 201호,202호                        | 서울특별시 구로구 개봉로15길 74-2              | 서울특별시      | 구로구
			서울시 서초구 서초대로 114-14 1,2층                              | 서울특별시 서초구 서초대로 114-14              | 서울특별시      | 서초구
			광주광역시 북구 유림로175 102동101호(삼익아파트, 동림동)           | 광주광역시 북구 유림로 175                     | 광주광역시      | 북구
			경상남도 사천시 정동면 읍동길 27 사천청구타운 103동 506호          | 경상남도 사천시 정동면 읍동길 27              | 경상남도        | 사천시
			""")
	void 주소를_정리한다(String raw, String address, String sido, String sigungu) {
		NormalizedAddress result = normalizer.normalize(raw);

		assertThat(result.address()).isEqualTo(address);
		assertThat(result.sido()).isEqualTo(sido);
		assertThat(result.sigungu()).isEqualTo(sigungu);
		assertThat(result.roadAddressFound()).isTrue();
	}

	@Test
	void 시_군_구가_시_군_구로_끝나지_않으면_비워_둔다() {
		NormalizedAddress result = normalizer.normalize("경상북도 포항 북구 삼흥로 1");

		assertThat(result.sido()).isEqualTo("경상북도");
		assertThat(result.sigungu()).isNull();
		assertThat(result.address()).isEqualTo("경상북도 포항 북구 삼흥로 1");
	}

	@ParameterizedTest(name = "[{index}] {0}")
	@CsvSource(delimiter = '|', textBlock = """
			대구시 중구 국채보상로 149길 501호               | 대구광역시 중구 국채보상로149길
			대구시 남구 현충로 26길 3층                      | 대구광역시 남구 현충로26길
			충남 예산군 건지화1길  202호                     | 충청남도 예산군 건지화1길
			광주광역시 북구 북문대로 242번 40 광주애육원       | 광주광역시 북구 북문대로
			""")
	void 건물번호가_없으면_도로명까지만_남기고_표시한다(String raw, String address) {
		NormalizedAddress result = normalizer.normalize(raw);

		assertThat(result.roadAddressFound()).isFalse();
		assertThat(result.address()).isEqualTo(address);
	}

	@Test
	void 도로명을_찾지_못하면_상세주소만_빼고_돌려주고_표시한다() {
		NormalizedAddress result = normalizer.normalize("경기도 시흥시 사미실90 송암동산");

		assertThat(result.roadAddressFound()).isFalse();
		assertThat(result.address()).isEqualTo("경기도 시흥시 사미실90 송암동산");
		assertThat(result.sido()).isEqualTo("경기도");
		assertThat(result.sigungu()).isEqualTo("시흥시");
	}

	@Test
	void 시_도를_알아내지_못하면_추측하지_않는다() {
		NormalizedAddress result = normalizer.normalize("광주시 오포읍 문형산길 1");

		assertThat(result.sido()).isNull();
		assertThat(result.sigungu()).isNull();
		assertThat(result.address()).isEqualTo("광주시 오포읍 문형산길 1");
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "   " })
	void 빈_주소는_거부한다(String raw) {
		assertThatThrownBy(() -> normalizer.normalize(raw)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 너무_긴_주소는_거부한다() {
		String tooLong = "가".repeat(AddressNormalizer.MAX_LENGTH + 1);

		assertThatThrownBy(() -> normalizer.normalize(tooLong)).isInstanceOf(IllegalArgumentException.class);
	}
}

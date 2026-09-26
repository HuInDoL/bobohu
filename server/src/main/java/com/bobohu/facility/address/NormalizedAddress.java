package com.bobohu.facility.address;

/**
 * 주소 정리 결과.
 *
 * @param address          정리된 주소. 도로명을 찾았으면 건물번호까지만 담는다. 예: "서울특별시 중랑구 용마산로 271"
 * @param sido             시·도 공식 이름. 알아내지 못하면 null
 * @param sigungu          시·군·구. 일반구가 있는 시는 "천안시 동남구"처럼 함께 담는다. 알아내지 못하면 null
 * @param roadAddressFound 도로명과 건물번호를 찾았는지. false면 사람이 확인해야 한다
 */
public record NormalizedAddress(String address, String sido, String sigungu, boolean roadAddressFound) {
}

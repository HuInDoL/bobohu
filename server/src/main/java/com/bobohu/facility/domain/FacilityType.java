package com.bobohu.facility.domain;

/**
 * 아동복지시설 유형. DB에는 이름(문자열)으로 저장한다.
 */
public enum FacilityType {
	/** 아동양육시설 (보육원) */
	CHILD_CARE,
	/** 공동생활가정 (그룹홈) */
	GROUP_HOME,
	/** 아동일시보호시설 */
	TEMPORARY_PROTECTION,
	/** 아동보호치료시설 */
	PROTECTIVE_TREATMENT,
	/** 자립지원시설 */
	SELF_RELIANCE,
	/** 그 밖의 아동복지시설 */
	OTHER,
	/** 아직 판별하지 못함 */
	UNKNOWN
}

package com.bobohu.facility.domain;

import java.time.LocalDate;
import java.util.Objects;

import com.bobohu.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * 아동복지시설. 테이블 설계는 docs/wiki/data-model.md 참고.
 */
@Entity
public class Facility extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private FacilityType facilityType;

	@Column(nullable = false)
	private boolean typeConfirmed;

	@Column(nullable = false, length = 300)
	private String addressRaw;

	@Column(nullable = false, length = 300)
	private String address;

	@Column(length = 20)
	private String sido;

	@Column(length = 30)
	private String sigungu;

	private Double latitude;

	private Double longitude;

	@Column(nullable = false, length = 30)
	private String source;

	@Column(length = 50)
	private String sourceRef;

	private LocalDate sourceBaseDate;

	/** JPA 전용 생성자. 코드에서는 정적 팩토리 메서드를 쓴다. */
	protected Facility() {
	}

	private Facility(String name, FacilityType facilityType, boolean typeConfirmed, String addressRaw,
			String address, String sido, String sigungu, String source, String sourceRef, LocalDate sourceBaseDate) {
		this.name = requireText(name, "name");
		this.facilityType = Objects.requireNonNull(facilityType, "facilityType");
		this.typeConfirmed = typeConfirmed;
		this.addressRaw = requireText(addressRaw, "addressRaw");
		this.address = requireText(address, "address");
		this.sido = sido;
		this.sigungu = sigungu;
		this.source = requireText(source, "source");
		this.sourceRef = sourceRef;
		this.sourceBaseDate = sourceBaseDate;
	}

	/**
	 * 공공데이터에서 가져온 시설을 만든다. 유형은 추정값이므로 확인되지 않은 상태로 둔다.
	 */
	public static Facility fromPublicData(String name, FacilityType estimatedType, String addressRaw,
			String address, String sido, String sigungu, String source, String sourceRef, LocalDate sourceBaseDate) {
		return new Facility(name, estimatedType, false, addressRaw, address, sido, sigungu, source, sourceRef,
				sourceBaseDate);
	}

	/**
	 * Geocoding 결과로 좌표를 기록한다.
	 */
	public void locate(double latitude, double longitude) {
		if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("좌표 범위를 벗어났습니다: " + latitude + ", " + longitude);
		}
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private static String requireText(String value, String field) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(field + "은(는) 비어 있을 수 없습니다");
		}
		return value;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public FacilityType getFacilityType() {
		return facilityType;
	}

	public boolean isTypeConfirmed() {
		return typeConfirmed;
	}

	public String getAddressRaw() {
		return addressRaw;
	}

	public String getAddress() {
		return address;
	}

	public String getSido() {
		return sido;
	}

	public String getSigungu() {
		return sigungu;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getSource() {
		return source;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public LocalDate getSourceBaseDate() {
		return sourceBaseDate;
	}
}

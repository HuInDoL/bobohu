# 데이터 모델

> 테이블을 추가하거나 바꾸면 이 페이지와 Flyway 마이그레이션을 함께 고친다.
> 실제 스키마의 기준은 `server/src/main/resources/db/migration/`이다.

## facility (시설)

아동복지시설 한 곳. 보육원(아동양육시설)뿐 아니라 그룹홈 등 모든 유형을 저장하고, `facility_type`으로 구분한다.
세부 정보(아동 인원, 소속, 소개 등)는 [시설 담당자 정보 관리](features/facility-management.md)를 구현할 때 별도로 추가한다.

| 컬럼 | 타입 | NULL | 설명 |
|---|---|---|---|
| `id` | `BIGINT` IDENTITY | X | 내부 식별자 |
| `name` | `VARCHAR(100)` | X | 시설명. **이름이 같은 시설이 있어서 식별자로 쓰지 않는다** |
| `facility_type` | `VARCHAR(30)` | X | 시설 유형 (아래 표) |
| `type_confirmed` | `BOOLEAN` | X | 유형이 확인된 값인지 여부. 이름으로 추정한 값이면 `false` |
| `address_raw` | `VARCHAR(300)` | X | 원본 주소. 정리 규칙이 틀렸을 때 다시 처리할 수 있게 보존한다 |
| `address` | `VARCHAR(300)` | X | 정리한 주소 (Geocoding과 화면 표시에 사용). 시·도 공식 이름으로 시작하고 **건물번호까지만** 담는다. 동·호·층은 담지 않는다. 예: `서울특별시 중랑구 용마산로 271` |
| `sido` | `VARCHAR(20)` | O | 시·도 공식 이름 (예: 서울특별시, 강원특별자치도). 주소에서 알아내지 못하면 NULL |
| `sigungu` | `VARCHAR(30)` | O | 시·군·구 (예: 광진구). 일반구가 있는 시는 `천안시 동남구`처럼 함께 담는다. 세종특별자치시는 NULL |
| `latitude` | `DOUBLE PRECISION` | O | 위도. Geocoding 전에는 NULL |
| `longitude` | `DOUBLE PRECISION` | O | 경도. Geocoding 전에는 NULL |
| `source` | `VARCHAR(30)` | X | 데이터 출처 (예: `MOHW_2022`, `MANAGER`) |
| `source_ref` | `VARCHAR(50)` | O | 출처 안에서의 식별값 (예: CSV 연번) |
| `source_base_date` | `DATE` | O | 출처 데이터의 기준일 (예: 2022-12-31) |
| `created_at` | `TIMESTAMPTZ` | X | 생성 시각 |
| `updated_at` | `TIMESTAMPTZ` | X | 마지막 수정 시각 |

**제약 조건**
- `UNIQUE (source, source_ref)`: 같은 CSV를 여러 번 가져와도 같은 시설이 두 번 들어가지 않게 한다(멱등성).

### facility_type 값
| 값 | 의미 |
|---|---|
| `CHILD_CARE` | 아동양육시설 (보육원) |
| `GROUP_HOME` | 공동생활가정 (그룹홈) |
| `TEMPORARY_PROTECTION` | 아동일시보호시설 |
| `PROTECTIVE_TREATMENT` | 아동보호치료시설 |
| `SELF_RELIANCE` | 자립지원시설 |
| `OTHER` | 그 밖의 아동복지시설 |
| `UNKNOWN` | 아직 판별하지 못함 |

DB에는 문자열로 저장한다. Java enum의 순서(숫자)로 저장하면, 나중에 값을 중간에 추가할 때 기존 데이터의 의미가 바뀌어 버린다.

### 설계에서 고민한 점
- **좌표 타입을 `DOUBLE PRECISION`으로 한 이유**: 위도와 경도는 금액처럼 정확한 소수 계산이 필요한 값이 아니다. `double`은 유효숫자가 약 15자리라 위치 오차가 1mm 미만이다. 반대로 **금액은 반드시 정수나 `NUMERIC`**으로 저장한다([후원](features/donation.md) 참고). 나중에 복잡한 공간 연산이 필요하면 PostGIS의 `geography` 타입으로 옮긴다.
- **원본 주소를 따로 보존하는 이유**: 주소 정리 규칙은 계속 고쳐질 것이다. 원본이 남아 있어야 규칙을 고친 뒤 다시 처리할 수 있다.
- **시간은 `TIMESTAMPTZ`**: 시간대 정보와 함께 저장해서, 서버가 어느 지역에서 돌든 같은 시각을 가리키게 한다. Java에서는 `Instant`로 다룬다.
- **식별자는 DB가 만든 번호(IDENTITY)**: 시설명과 주소는 겹치거나 바뀔 수 있어서 식별자로 쓸 수 없다.

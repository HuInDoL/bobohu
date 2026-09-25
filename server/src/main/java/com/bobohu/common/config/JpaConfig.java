package com.bobohu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 엔티티의 생성 시각과 수정 시각을 자동으로 채운다.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}

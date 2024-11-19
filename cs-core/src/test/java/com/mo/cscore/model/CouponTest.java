package com.mo.cscore.model;

import com.mo.cscore.exception.CouponException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_DATE;
import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_QUANTITY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

	@Test
	@DisplayName("쿠폰 기한 - 유효한 쿠폰 true")
	void availableIssueDate_1() {
		//given
		Coupon coupon = Coupon.builder()
			.issueStartedDate(LocalDateTime.now().minusDays(1))
			.issueEndedDate(LocalDateTime.now().plusDays(1))
			.build();

		//when
		boolean result = coupon.availableIssueDate();

		//then
		assertTrue(result);
	}

	@Test
	@DisplayName("쿠폰 기한 - 아직 기한 전 쿠폰 false")
	void availableIssueDate_2() {
		//given
		Coupon coupon = Coupon.builder()
			.issueStartedDate(LocalDateTime.now().plusDays(1))
			.issueEndedDate(LocalDateTime.now().plusDays(2))
			.build();

		//when
		boolean result = coupon.availableIssueDate();

		//then
		assertFalse(result);
	}

	@Test
	@DisplayName("쿠폰 기한 - 기한 만료 쿠폰 false")
	void availableIssueDate_3() {
		//given
		Coupon coupon = Coupon.builder()
			.issueStartedDate(LocalDateTime.now().minusDays(2))
			.issueEndedDate(LocalDateTime.now().minusDays(1))
			.build();

		//when
		boolean result = coupon.availableIssueDate();

		//then
		assertFalse(result);
	}

	@Test
	@DisplayName("쿠폰 수량 - 총 수량 제한이 없을시(null) true")
	void availableIssueQuantity_1() {
		//given
		Coupon coupon = Coupon.builder()
			.totalQuantity(null)
			.issuedQuantity(1000)
			.build();

		//when
		boolean result = coupon.availableIssueQuantity();

		//then
		assertTrue(result);
	}

	@Test
	@DisplayName("쿠폰 수량 - 발급 가능한 수량일 시 true")
	void availableIssueQuantity_2() {
		//given
		Coupon coupon = Coupon.builder()
			.totalQuantity(500)
			.issuedQuantity(100)
			.build();

		//when
		boolean result = coupon.availableIssueQuantity();

		//then
		assertTrue(result);
	}

	@Test
	@DisplayName("쿠폰 수량 - 쿠폰 수량 만료 시 false")
	void availableIssueQuantity_3() {
		//given
		Coupon coupon = Coupon.builder()
			.totalQuantity(500)
			.issuedQuantity(500)
			.build();

		//when
		boolean result = coupon.availableIssueQuantity();

		//then
		assertFalse(result);
	}

	@Test
	@DisplayName("쿠폰 등록 - 수량 및 기한이 정상적인 쿠폰일 시 true")
	void issue_1() {
		//given
		Coupon coupon = Coupon.builder()
			.totalQuantity(100)
			.issuedQuantity(0)
			.issueStartedDate(LocalDateTime.now().minusDays(1))
			.issueEndedDate(LocalDateTime.now().plusDays(1))
			.build();

		//when
		coupon.issue();

		//then
		assertEquals(1, coupon.getIssuedQuantity());
	}

	@Test
	@DisplayName("쿠폰 등록 - 수량이 초과된 쿠폰일 시 throw")
	void issue_2() {
		//given
		Coupon coupon = Coupon.builder()
			.totalQuantity(100)
			.issuedQuantity(100)
			.issueStartedDate(LocalDateTime.now().minusDays(1))
			.issueEndedDate(LocalDateTime.now().plusDays(1))
			.build();

		//when & then
		assertThatThrownBy(coupon::issue)
			.isInstanceOf(CouponException.class)
			.hasMessage("[%s] %s".formatted(INVALID_COUPON_QUANTITY, INVALID_COUPON_QUANTITY.message));
	}

	@Test
	@DisplayName("쿠폰 등록 - 기한 초과된 쿠폰일 시 throw")
	void issue_3() {
		//given
		Coupon coupon = Coupon.builder()
			.totalQuantity(100)
			.issuedQuantity(0)
			.issueStartedDate(LocalDateTime.now().minusDays(2))
			.issueEndedDate(LocalDateTime.now().minusDays(1))
			.build();

		//when & then
		assertThatThrownBy(coupon::issue)
			.isInstanceOf(CouponException.class)
			.hasMessage("[%s] %s".formatted(INVALID_COUPON_DATE, INVALID_COUPON_DATE.message));
	}
}
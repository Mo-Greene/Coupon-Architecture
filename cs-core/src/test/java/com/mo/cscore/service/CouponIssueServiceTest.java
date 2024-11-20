package com.mo.cscore.service;

import com.mo.cscore.TestConfig;
import com.mo.cscore.exception.CouponException;
import com.mo.cscore.model.Coupon;
import com.mo.cscore.model.CouponIssue;
import com.mo.cscore.repository.jpa.CouponIssueJpaRepository;
import com.mo.cscore.repository.jpa.CouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.mo.cscore.exception.ErrorCode.DUPLICATE_COUPON;
import static com.mo.cscore.exception.ErrorCode.NOT_EXIST_COUPON;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CouponIssueServiceTest extends TestConfig {

	@Autowired CouponIssueService sut;
	@Autowired CouponJpaRepository couponJpaRepository;
	@Autowired CouponIssueJpaRepository couponIssueJpaRepository;

	@BeforeEach
	void setUp() {
		couponJpaRepository.deleteAllInBatch();
		couponIssueJpaRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("존재하는 쿠폰 - 쿠폰 발급 시 예외 반환")
	void createCouponIssue_1() {
		//given
		long userId = 1;
		Coupon coupon = Coupon.builder()
			.title("선착순 쿠폰")
			.issueStartedDate(LocalDateTime.now().minusDays(2))
			.issueEndedDate(LocalDateTime.now().plusDays(2))
			.issuedQuantity(0)
			.totalQuantity(1000)
			.build();
		Coupon savedCoupon = couponJpaRepository.save(coupon);

		CouponIssue couponIssue = CouponIssue.builder()
			.coupon(savedCoupon)
			.userId(userId)
			.build();
		couponIssueJpaRepository.save(couponIssue);

		//when & then
		assertThatThrownBy(() -> sut.createCouponIssue(userId, savedCoupon))
			.isInstanceOf(CouponException.class)
			.hasMessage("[%s] %s".formatted(DUPLICATE_COUPON, DUPLICATE_COUPON.message));
	}

	@Test
	@DisplayName("존재하지 않는 쿠폰 - 쿠폰 발급 시 true")
	void createCouponIssue_2() {
		//given
		long userId = 1;
		Coupon coupon = Coupon.builder()
			.title("선착순 쿠폰")
			.issueStartedDate(LocalDateTime.now().minusDays(2))
			.issueEndedDate(LocalDateTime.now().plusDays(2))
			.issuedQuantity(0)
			.totalQuantity(1000)
			.build();
		Coupon savedCoupon = couponJpaRepository.save(coupon);

		//when
		sut.createCouponIssue(userId, savedCoupon);

		//then
		assertTrue(couponIssueJpaRepository.findById(savedCoupon.getId()).isPresent());
	}

	@Test
	@DisplayName("쿠폰 없을 시 예외 반환")
	void issueCoupon_1() {
	    //given
		long couponId = 1;
		long userId = 1;

	    //when & then
		assertThatThrownBy(() -> sut.issueCoupon(couponId, userId))
			.isInstanceOf(CouponException.class)
			.hasMessage("[%s] %s".formatted(NOT_EXIST_COUPON, NOT_EXIST_COUPON.message));
	}

	@Test
	@DisplayName("정상 쿠폰 발급 시 쿠폰 발급 완료")
	void issueCoupon_2() {
	    //given
		Coupon coupon = Coupon.builder()
			.title("선착순 쿠폰")
			.issueStartedDate(LocalDateTime.now().minusDays(2))
			.issueEndedDate(LocalDateTime.now().plusDays(2))
			.issuedQuantity(0)
			.totalQuantity(1000)
			.build();
	    couponJpaRepository.save(coupon);

		long userId = 1;

	    //when
	    sut.issueCoupon(coupon.getId(), userId);

		//then
		Coupon resultCoupon = couponJpaRepository.findById(coupon.getId()).get();
		assertEquals(1, resultCoupon.getIssuedQuantity());
	}
}
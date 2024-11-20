package com.mo.cscore.service;

import com.mo.cscore.exception.CouponException;
import com.mo.cscore.model.Coupon;
import com.mo.cscore.model.CouponIssue;
import com.mo.cscore.repository.jpa.CouponIssueJpaRepository;
import com.mo.cscore.repository.jpa.CouponIssueRepository;
import com.mo.cscore.repository.jpa.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mo.cscore.exception.ErrorCode.DUPLICATE_COUPON;
import static com.mo.cscore.exception.ErrorCode.NOT_EXIST_COUPON;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

	private final CouponJpaRepository couponJpaRepository;
	private final CouponIssueJpaRepository couponIssueJpaRepository;
	private final CouponIssueRepository couponIssueRepository;

	@Transactional
	public void issueCoupon(long couponId, long userId) {
		Coupon coupon = findCoupon(couponId);
		coupon.issue();
		createCouponIssue(userId, coupon);
	}

	@Transactional(readOnly = true)
	public Coupon findCoupon(long couponId) {
		return couponJpaRepository.findById(couponId)
			.orElseThrow(() -> new CouponException(NOT_EXIST_COUPON, NOT_EXIST_COUPON.message));
	}

	@Transactional
	public void createCouponIssue(long userId, Coupon coupon) {
		validateCouponIssue(coupon.getId(), userId);
		CouponIssue couponIssue = CouponIssue.builder()
			.coupon(coupon)
			.userId(userId)
			.build();
		couponIssueJpaRepository.save(couponIssue);
	}

	private void validateCouponIssue(long couponId, long userId) {
		CouponIssue issuedCoupon = couponIssueRepository.findFirstCouponIssue(couponId, userId);
		if (issuedCoupon != null) {
			throw new CouponException(DUPLICATE_COUPON, DUPLICATE_COUPON.message);
		}
	}
}

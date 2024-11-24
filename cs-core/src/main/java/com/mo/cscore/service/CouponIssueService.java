package com.mo.cscore.service;

import com.mo.cscore.exception.CouponException;
import com.mo.cscore.model.Coupon;
import com.mo.cscore.model.CouponIssue;
import com.mo.cscore.repository.jpa.CouponIssueJpaRepository;
import com.mo.cscore.repository.jpa.CouponIssueRepository;
import com.mo.cscore.repository.redis.RedisRepository;
import com.mo.cscore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mo.cscore.exception.ErrorCode.DUPLICATE_COUPON;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

	private final CouponIssueJpaRepository couponIssueJpaRepository;
	private final CouponIssueRepository couponIssueRepository;
	private final CouponCacheService couponCacheService;
	private final RedisRepository redisRepository;


	@Transactional
	public void issueCoupon(long couponId, long userId) {
		CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
		coupon.checkIssuableCoupon();
		issueRequest(couponId, userId, coupon.totalQuantity());
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

	private void issueRequest(long couponId, long userId, Integer totalIssueQuantity) {
		if (totalIssueQuantity == null) {
			redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
		} else {
			redisRepository.issueRequest(couponId, userId, totalIssueQuantity);
		}
	}
}

package com.mo.cscore.service;

import com.mo.cscore.model.Coupon;
import com.mo.cscore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponCacheService {

	private final CouponService couponService;

	@Cacheable(cacheNames = "coupons")
	public CouponRedisEntity getCouponCache(long couponId) {
		Coupon coupon = couponService.findCoupon(couponId);
		return new CouponRedisEntity(coupon);
	}
}

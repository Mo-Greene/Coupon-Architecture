package com.mo.cscore.service;

import com.mo.cscore.exception.CouponException;
import com.mo.cscore.model.Coupon;
import com.mo.cscore.repository.jpa.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mo.cscore.exception.ErrorCode.NOT_EXIST_COUPON;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponJpaRepository couponJpaRepository;

	@Transactional(readOnly = true)
	public Coupon findCoupon(long couponId) {
		return couponJpaRepository.findById(couponId)
			.orElseThrow(() -> new CouponException(NOT_EXIST_COUPON, NOT_EXIST_COUPON.message));
	}
}

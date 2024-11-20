package com.mo.cscoupon.service;

import com.mo.cscore.service.CouponIssueService;
import com.mo.cscoupon.controller.dto.CouponIssueRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponRequestService {

	private final CouponIssueService couponIssueService;

	public void issueRequest(CouponIssueRequest request) {

		couponIssueService.issueCoupon(request.couponId(), request.userId());
		log.info("쿠폰 발급 - coupon: %s, user: %s".formatted(request.couponId(), request.userId()));
	}
}

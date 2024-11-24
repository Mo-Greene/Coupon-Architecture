package com.mo.cscore.repository.redis.dto;

public record CouponIssueRequest(
	long couponId,
	long userId
) {
}

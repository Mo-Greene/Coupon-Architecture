package com.mo.cscoupon.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CouponResponse<T> (
	boolean isSuccess,
	T message
) {
}

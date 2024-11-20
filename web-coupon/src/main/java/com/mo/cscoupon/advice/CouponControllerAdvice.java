package com.mo.cscoupon.advice;

import com.mo.cscore.exception.CouponException;
import com.mo.cscoupon.controller.dto.CouponResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponControllerAdvice {

	@ExceptionHandler(CouponException.class)
	public CouponResponse<?> handleCouponException(CouponException e) {
		return new CouponResponse<>(
			false,
			e.getErrorCode().message
		);
	}
}

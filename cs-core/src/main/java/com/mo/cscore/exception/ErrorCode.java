package com.mo.cscore.exception;

public enum ErrorCode {

	INVALID_COUPON_QUANTITY("쿠폰 발급 수량이 유효하지 않습니다."),
	INVALID_COUPON_DATE("쿠폰 발급 기간이 유효하지 않습니다.");

	public final String message;

	ErrorCode(String message) {
		this.message = message;
	}
}

package com.mo.cscore.exception;

public enum ErrorCode {

	INVALID_COUPON_QUANTITY("쿠폰 발급 수량이 유효하지 않습니다."),
	INVALID_COUPON_DATE("쿠폰 발급 기간이 유효하지 않습니다."),
	NOT_EXIST_COUPON("존재하지 않는 쿠폰입니다."),
	DUPLICATE_COUPON("중복 발급 된 쿠폰이 존재합니다.");

	public final String message;

	ErrorCode(String message) {
		this.message = message;
	}
}

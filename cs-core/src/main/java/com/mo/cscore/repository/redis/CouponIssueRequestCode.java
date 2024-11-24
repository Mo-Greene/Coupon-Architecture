package com.mo.cscore.repository.redis;

import com.mo.cscore.exception.CouponException;

import static com.mo.cscore.exception.ErrorCode.DUPLICATE_COUPON;
import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_QUANTITY;

public enum CouponIssueRequestCode {
	SUCCESS(1),
	DUPLICATE_COUPON_ISSUE(2),
	INVALID_COUPON_ISSUE_QUANTITY(3);

	CouponIssueRequestCode(int code) {
	}

	public static CouponIssueRequestCode find(String code) {
		int codeValue = Integer.parseInt(code);
		if (codeValue == 1) return SUCCESS;
		if (codeValue == 2) return DUPLICATE_COUPON_ISSUE;
		if (codeValue == 3) return INVALID_COUPON_ISSUE_QUANTITY;
		throw new IllegalArgumentException("존재하지 않는 코드입니다.");
	}

	public static void checkRequestResult(CouponIssueRequestCode code) {
		if (code == INVALID_COUPON_ISSUE_QUANTITY) {
			throw new CouponException(INVALID_COUPON_QUANTITY, INVALID_COUPON_QUANTITY.message);
		}

		if (code == DUPLICATE_COUPON_ISSUE) {
			throw new CouponException(DUPLICATE_COUPON, DUPLICATE_COUPON.message);
		}
	}
}

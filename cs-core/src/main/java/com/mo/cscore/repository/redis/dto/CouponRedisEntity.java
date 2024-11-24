package com.mo.cscore.repository.redis.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mo.cscore.exception.CouponException;
import com.mo.cscore.model.Coupon;

import java.time.LocalDateTime;

import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_DATE;
import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_QUANTITY;

public record CouponRedisEntity(
	Long id,
	Integer totalQuantity,

	boolean availableIssueQuantity,

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime dateIssueStart,

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	LocalDateTime dateIssueEnd
) {

	public CouponRedisEntity(Coupon coupon) {
		this (
			coupon.getId(),
			coupon.getTotalQuantity(),
			coupon.availableIssueQuantity(),
			coupon.getIssueStartedDate(),
			coupon.getIssueEndedDate()
		);
	}

	public boolean availableIssueDate() {
		LocalDateTime now  = LocalDateTime.now();
		return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
	}

	public void checkIssuableCoupon() {
		if (!availableIssueQuantity) {
			throw new CouponException(INVALID_COUPON_QUANTITY, INVALID_COUPON_QUANTITY.message);
		}

		if (!availableIssueDate()) {
			throw new CouponException(INVALID_COUPON_DATE, INVALID_COUPON_DATE.message);
		}
	}
}

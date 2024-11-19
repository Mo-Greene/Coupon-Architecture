package com.mo.cscore.model;

import com.mo.cscore.exception.CouponException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_DATE;
import static com.mo.cscore.exception.ErrorCode.INVALID_COUPON_QUANTITY;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String title;

	@Column
	private Integer totalQuantity;

	@Column(nullable = false)
	private int issuedQuantity;

	@Column(nullable = false)
	private LocalDateTime issueStartedDate;

	@Column(nullable = false)
	private LocalDateTime issueEndedDate;

	@Builder
	public Coupon(String title, Integer totalQuantity, int issuedQuantity, LocalDateTime issueStartedDate, LocalDateTime issueEndedDate) {
		this.title = title;
		this.totalQuantity = totalQuantity;
		this.issuedQuantity = issuedQuantity;
		this.issueStartedDate = issueStartedDate;
		this.issueEndedDate = issueEndedDate;
	}

	public boolean availableIssueDate() {
		LocalDateTime now = LocalDateTime.now();
		return now.isAfter(issueStartedDate) && now.isBefore(issueEndedDate);
	}

	public boolean availableIssueQuantity() {
		if (totalQuantity == null) return true;
		return totalQuantity > issuedQuantity;
	}

	public void issue() {
		if (!availableIssueDate()) {
			throw new CouponException(INVALID_COUPON_DATE, INVALID_COUPON_DATE.message);
		}

		if (!availableIssueQuantity()) {
			throw new CouponException(INVALID_COUPON_QUANTITY, INVALID_COUPON_QUANTITY.message);
		}
		issuedQuantity++;
	}
}

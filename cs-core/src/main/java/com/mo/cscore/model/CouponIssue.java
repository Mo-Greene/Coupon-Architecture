package com.mo.cscore.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "coupon_issue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id", nullable = false)
	private Coupon coupon;

	@Column(nullable = false)
	private Long userId;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime dateIssued;

	@Column
	private LocalDateTime dateUsed;

	@Builder
	public CouponIssue(Coupon coupon, Long userId, LocalDateTime dateIssued, LocalDateTime dateUsed) {
		this.coupon = coupon;
		this.userId = userId;
		this.dateIssued = dateIssued;
		this.dateUsed = dateUsed;
	}
}

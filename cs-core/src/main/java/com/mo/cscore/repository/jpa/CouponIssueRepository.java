package com.mo.cscore.repository.jpa;

import com.mo.cscore.model.CouponIssue;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.mo.cscore.model.QCouponIssue.couponIssue;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {

	private final JPAQueryFactory queryFactory;

	public CouponIssue findFirstCouponIssue(long couponId, long userId) {
		return queryFactory
			.selectFrom(couponIssue)
			.where(
				couponIssue.coupon.id.eq(couponId),
				couponIssue.userId.eq(userId)
			)
			.fetchFirst();
	}
}

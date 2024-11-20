package com.mo.cscoupon.controller;

import com.mo.cscoupon.controller.dto.CouponIssueRequest;
import com.mo.cscoupon.controller.dto.CouponResponse;
import com.mo.cscoupon.service.CouponRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {

	private final CouponRequestService couponRequestService;

	@PostMapping("/v1/issue")
	public CouponResponse<?> issue(@RequestBody CouponIssueRequest request) {

		couponRequestService.issueRequest(request);
		return new CouponResponse<>(true, null);
	}
}

package com.mo.cscore.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mo.cscore.exception.CouponException;
import com.mo.cscore.repository.redis.dto.CouponIssueRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.mo.cscore.exception.ErrorCode.FAIL_COUPON;
import static com.mo.cscore.util.CouponRedisUtil.getIssueRequestKey;
import static com.mo.cscore.util.CouponRedisUtil.getIssueRequestQueueKey;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

	private final RedisTemplate<String,String> redisTemplate;
	private final RedisScript<String> issueScript = issueRequestScript();
	private final String issueRequestQueueKey = getIssueRequestQueueKey();
	private final ObjectMapper objectMapper = new ObjectMapper();

	public void issueRequest(long couponId, long userId, int totalIssueQuantity) {
		String issueRequestKey = getIssueRequestKey(couponId);
		CouponIssueRequest couponIssueRequest = new CouponIssueRequest(couponId, userId);
		try {
			String code = redisTemplate.execute(
				issueScript,
				//key
				List.of(issueRequestKey, issueRequestQueueKey),
				//argv 1,2,3
				String.valueOf(userId),
				String.valueOf(totalIssueQuantity),
				objectMapper.writeValueAsString(couponIssueRequest)
			);

			CouponIssueRequestCode.checkRequestResult(CouponIssueRequestCode.find(code));
		} catch (JsonProcessingException e) {
			throw new CouponException(FAIL_COUPON, FAIL_COUPON.message);
		}
	}

	private RedisScript<String> issueRequestScript() {
		String script = """
			if redis.call('SISMEMBER', KEYS[1], ARGV[1]) == 1 then
				return '2'
			end
			
			if tonumber(ARGV[2]) > redis.call('SCARD', KEYS[1]) then
				redis.call('SADD', KEYS[1], ARGV[1])
				redis.call('RPUSH', KEYS[2], ARGV[3])
				return '1'
			end
			
			return '3'
			""";
		return RedisScript.of(script, String.class);
	}
}

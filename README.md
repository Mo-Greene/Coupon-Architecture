### 선착순 쿠폰 발급 로직

## MySQL 직접적으로 사용시


최대 사용자 : 1000명
부하 증가 : 100명 단위

| Type | Name       | # Requests | # Fails | Average (ms) | Min (ms) | Max (ms) | Average size (bytes) | RPS    | Failures/s |
| ---- | ---------- | ---------- | ------- | ------------ | -------- | -------- | -------------------- | ------ | ---------- |
| POST | /v1/issue  | 3416       | 2743    | 2016.33      | 433      | 3997     | 91.07                | 255.15 | 204.88     |
|      | Aggregated | 3416       | 2743    | 2016.33      | 433      | 3997     | 91.07                | 255.15 | 204.88     |

![mysql-성능](https://github.com/user-attachments/assets/472a471e-544e-404c-882d-8e11f196ce3d)

Response Times 와 실패 요청이 점점 올라가게 된다.

```
2024-11-20T16:13:16.191+09:00 ERROR 22064 --- [cs-coupon] [io-8080-exec-14] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.dao.CannotAcquireLockException: could not execute statement [Deadlock found when trying to get lock; try restarting transaction] [update coupons set created_date=?,issue_ended_date=?,issue_started_date=?,issued_quantity=?,title=?,total_quantity=?,updated_date=? where id=?]; SQL [update coupons set created_date=?,issue_ended_date=?,issue_started_date=?,issued_quantity=?,title=?,total_quantity=?,updated_date=? where id=?]] with root cause

com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: Deadlock found when trying to get lock; try restarting transaction
```

요청 후 특정 시간이 지나면 서버에선 Deadlock 이 걸리게 된다. 그 이유는?

```yml
spring:
	...
  datasource:  
    hikari:
	    ...
      maximum-pool-size: 10  
      max-lifetime: 30000  
      connection-timeout: 30000
      ...
```
요청량이 최대 커넥션 풀의 수를 넘게 되었을 때와(maximum-pool-size: 10)  
연결을 기다리지 못하고 초과 되었을 때 (connection-timeout: 30000)  
MySQL이 요청을 견디지 못하고 실패가 올라가는 걸로 확인됨
## Sprinkling

## Contents
* [Specifications](#chapter-1)
* [Requirement](#chapter-2) 
* [Strategy](#chapter-3)
* [Entity](#chapter-4)
* [Redis Key](#chapter-5)
* [Explanation of REST](#chapter-6)
* [Api Feature list](#chapter-7)
* [Api Endpoint](#chapter-8)
* [How to run](#chapter-9)


### <a name="chapter-1"></a>Specifications 
````
 OpenJDK11
 Spring Boot 2.3.3.RELEASE
 Spring Data Jpa
 Spring Data Redis
 Swagger2
 CQRS Pettern
````
### <a name="chapter-2"></a>Requirement 
````
REST API 기반 뿌리기 서비스
- 
````

### <a name="chapter-3"></a>Strategy 
```` 
- Embedded H2 DB, Embedded Redis 를 사용
- 랜덤한 토큰 생성
- REDIS를 이용하여 CQRS(Command and Query Responsibility Segregation) 패턴 구현 
````

## <a name="chapter-4"></a>Entity
```
쿠폰(Coupon) 
   쿠폰번호
   쿠폰상태
   유저아이디
   만료일
   생성일   
   사용일
   발급일

```

### <a name="chapter-5"></a>Redis Key
````
- 쿠폰( key : 쿠폰 ID, type : 쿠폰 )  
- 해당 일자에 만료되는 쿠폰(key : 만료일자, type : 쿠폰 ID 리스트)
````

### <a name="chapter-6"></a>Explanation of REST 
|HTTP Method|Usage|
|:---|:---|
|GET   |Receive a read-only data      |
|PUT   |Overwrite an existing resource|
|POST  |Creates a new resource        |
|DELETE|Deletes the given resource    |

### <a name="chapter-7"></a>Api Feature list 
```
- 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관
``` 

### <a name="chapter-8"></a>Api Endpoint
```
API 실행 && 테스트 절차
1. 회원가입을 합니다 
2. 로그인 후 인증 토큰을 받습니다
3. Header userId, Authorization Header Bearer Token 값을 넣고 각 Coupon API를 호출합니다

EndPoint : /v1/coupons/{id}
Method : PUT 
Description : 생성된 쿠폰중 하나를 사용자에게 지급 
              사용자에게 지급한 쿠폰을 사용
              사용된 쿠폰을 사용 취소
Return value: HTTP status 200 (OK), 404 (NOT_FOUND)

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| id        | @PathParam   | Coupon id                                         |
|-----------|--------------|---------------------------------------------------|

Payload Example (required parameters)
뿌리기 하나를 생성 
{
    "status" : "ISSUED",
    "userId" : "joyworld007"
}
사용자에게 지급한 쿠폰을 사용
{
    "status" : "USED"
}
사용된 쿠폰을 사용 취소
{
    "status" : "ISSUED"
}

Response Body example
성공시 
{
    "code": "SUCCESS",
    "message": "OK"
}            
뿌리기 만료시
{
    "code": "EXPIRED",
    "message": "Expired"
}
요청 조건이 맞지 않을시   
{
    "code": "BAD_REQUEST",
    "message": "Bad Request"
}

----------------------------------------------------------------------------------------------------

EndPoint : /v1/Sprinkling/{id}
Method : GET
Description : 쿠폰 정보 조회 ( CQRS 성능 테스트 용 )
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| id        | @PathParam   | Coupon id                                         |
|-----------|--------------|---------------------------------------------------|

```
### <a name="chapter-9"></a>How to Run
```
1. 실행
./gradlew bootrun

2. Test 
./gradlew test

3. Swagger URI
http://localhost:8080/swagger-ui.html
```
 
 

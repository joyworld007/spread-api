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
- Embedded H2 DB
- Embedded Redis
- REDIS를 이용하여 CQRS(Command and Query Responsibility Segregation) 패턴 구현
- 랜덤한 토큰 생성
````

## <a name="chapter-4"></a>Entity
```
뿌리기(Sprinkling) 
   아이디
   토큰
   생성한 유저 아이디
   금액
   받을사용자 수 
   만료일자
   생성일자  
   
받기(Sprinkling_Receive)
   아디디
   뿌리기 아이디
   금액
   상태(받기대기/받기완료)
   받은사람아이디

사용자(User) 
   유저아이디
   유저이메일
   생성일자  
   사용자아이디

```

### <a name="chapter-5"></a>Redis Key
````
- 뿌리기( key : 뿌리기 ID, type : 뿌리기 DTO )  
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
- 뿌리기 생성
- 뿌린 금액 받기
- 뿌리기 조회 하기
``` 

### <a name="chapter-8"></a>Api Endpoint
```

EndPoint : /v1/sprinkling
Method : POST
Description : 뿌리기 하나를 생성 
Return value: HTTP status 200 (OK)

Payload Example (required parameters)
{
    "money" : "10000",
    "count" : "5"
}

Response Body example
성공시 
{
    "code": "SUCCESS",
    "message": "OK"
}            

요청 조건이 맞지 않을시   
{
    "code": "BAD_REQUEST",
    "message": "Bad Request"
}

----------------------------------------------------------------------------------------------------

EndPoint : /v1/sprinkling/{id}
Method : GET
Description : 뿌리기 정보 조회
Return value: HTTP status 200 (OK) 

|-----------|--------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                       |
|-----------|--------------|---------------------------------------------------|
| id        | @PathParam   | sprinkling id                                         |
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
 
 

## 카카오 페이 뿌리기 서비스

## Contents
* [Specifications](#chapter-1)
* [Requirement](#chapter-2) 
* [Strategy](#chapter-3)
* [Entity](#chapter-4)
* [Explanation of REST](#chapter-5)
* [Api Feature list](#chapter-6)
* [Api Endpoint](#chapter-7)
* [How to run](#chapter-8)


### <a name="chapter-1"></a>Specifications 
````
 OpenJDK11
 Spring Boot 2.3.3.RELEASE
 Spring Data Jpa
 Embedded H2 DB
 Swagger2
````
### <a name="chapter-2"></a>Requirement 
````
REST API 기반 뿌리기 서비스
- 뿌리기, 받기, 조회 기능을 수행하는 REST API를 구현
````

### <a name="chapter-3"></a>Strategy 
```` 
- Embedded H2 DB를 사용
- Spring data jpa를 이용하여 데이터 Read, Write
- Junit5를 이용한 단위 테스트 코드 작성
- 인원 별로 기본 가중치, 추가 가중치를 두어 랜덤한 받기 금액 생성
````

## <a name="chapter-4"></a>Entity
```
뿌리기(Sprinkling) 
    아이디
    룸아이디   
    토큰
    유저넘버
    금액
    최고받기금액
    받을사람수
    만료일
    생성일
   
뿌리기 받기(Receive)
    아디디
    뿌리기 아이디
    받을금액
    상태(받기대기/받기완료)
    받은유저
    받은잘자
```

### <a name="chapter-5"></a>Explanation of REST 
|HTTP Method|Usage|
|:---|:---|
|GET   |Receive a read-only data      |
|PUT   |Overwrite an existing resource|
|POST  |Creates a new resource        |
|DELETE|Deletes the given resource    |

### <a name="chapter-6"></a>Api Feature list 
```
- 뿌리기 생성
- 뿌린 금액 받기
- 뿌리기 조회 하기
``` 

### <a name="chapter-7"></a>Api Endpoint
```
공통 Request Header
{
    "X-USER-ID" : 유저 아이디
    "X-ROOM-ID" : 룸 아이디
}

EndPoint : /v1/sprinkling
Method : POST
Description : 뿌리기 생성 
Return value: HTTP status 200 (OK), 400 (BAD Request)

Payload Example (required parameters)
{
	"roomId" : "12345",
    "count" : "5",
    "money" : "1000"
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

|-----------|---------------|---------------------------------------------------|
| Parameter |Parameter Type | Description                                       |
|-----------|---------------|---------------------------------------------------|
| id        | @PathParam    | sprinkling id                                     |
|-----------|---------------|---------------------------------------------------|

---------------------------------------------------------------------------------------------------

EndPoint : /v1/sprinkling/{id}
Method : PUT
Description : 뿌리기 금액 받기
Return value: HTTP status 200 (OK) 

|-----------|---------------|---------------------------------------------------|
| Parameter |Parameter Type| Description                                        |
|-----------|---------------|---------------------------------------------------|
| id        | @PathParam    | sprinkling id                                     |
|-----------|---------------|---------------------------------------------------|
| token     | @RequestParam | token                                             |
|-----------|---------------|---------------------------------------------------|
Response Body example
성공시 
{
    "code": "SUCCESS",
    "message": "OK"
}  

실패시 
{
    "code": "EXPIRED",
    "message": "Expired"
}  
{
    "code": "NOT_FOUND",
    "message": "Not found"
}  
{
    "code": "ROOM_ID_IS_NOT_INVALID",
    "message": "Room id is not Invalid"
}  
{
    "code": "FINISHED",
    "message": "sprinkling is finished"
}  
{
    "code": "ALREADY_ACCEPTED",
    "message": "Already accepted"
}  
```
### <a name="chapter-8"></a>How to Run
```
1. 실행
./gradlew bootrun

2. Test 
./gradlew test

3. Swagger URI
http://localhost:8080/swagger-ui.html
```
 
 

# ScheduleApp

## 👨‍🏫 프로젝트 소개
일정 관리 시스템을 설계하고, 자바의 객체 지향 설계와 함께 Spring의 유효성 검증, 예외 처리, JPA 연관관계, 페이징 기능을 활용한 프로젝트입니다.

---

## ⏲️ 개발 기간
- 2025.05.07(수) ~ 2023.05.17(수)

---

## 💻 개발환경
- **Version** : Java 17
- **Framework** : Spring Boot
- **IDE** : IntelliJ

---

## 📂 프로젝트 구조
```bash
Schedule/
├── build.gradle
├── settings.gradle
├── schedule.sql
├── README.md
├── HELP.md
├── .gitignore
├── src/
│   └── main/
│       ├── java/
│       │   └── com.example.schedule/
│       │       ├── controller/
│       │       │   └── ScheduleAddController.java
│       │       ├── dto/
│       │       │   ├── ScheduleRequestDto.java
│       │       │   └── ScheduleResponseDto.java
│       │       ├── entity/
│       │       ├── repository/
│       │       │   ├── ScheduleRepository.java
│       │       │   └── JdbcTemplateScheduleRepository.java
│       │       ├── service/
│       │       │   ├── ScheduleService.java
│       │       │   └── ScheduleServiceImpl.java
│       │       └── ScheduleApplication.java
│       └── resources/
│           ├── static/
│           ├── templates/
│           └── application.properties
└── test/

```

---

## ⚙️ 프로젝트 설명 
### Lv0. -- 완료 --
- API 명세서 작성
- ERD 작성
- SQL 작성

### Lv1. -- 완료 --
- 일정 생성(일정 작성하기)
  - 일정 생성 시 할일, 작성자명, 비밀번호, 작성/수정일을 저장
  - 작성/수정일은 날짜와 시간을 모두 포함한 상태
  - 각 일정의 고유 식별자를 자동으로 생성하여 관리
  - 최초 입력 시 수정일은 작성일과 동일

- 전체 일정 조회(등록된 일정 불러오기)
  - 다음 조건을 바탕으로 등록된 일정 목록을 전부 조회
     - 수정일(형식 : YYYY-MM-DD)
     - 작성자명
     - 위 조건 중 한가지만 충족하거나 둘 다 충족하지 않을 수도 둘 다 충족할 해도 실행
     - 수정일 기준 내림차순으로 정렬

- 선택 일정 조회(선택한 일정 정보 불러오기)
  - 일정의 고유 식별자를 사용하여 조회

### Lv2. -- 완료 --
- 선택한 일정 수정
   - 제목, 내용, 작성자명만 수정 가능
   - 수정을 요청할 때 비밀번호를 함께 전달
   - 수정 완료시 수정일이 수정 시점으로 변경

- 선택한 일정 삭제
   - 서버에 일정 삭제를 요청할 때 비밀번호를 함께 전달
 
### Lv3. -- 완료 --
- 작성자와 일정의 연결
  - 작성자를 식별하기 위해 이름으로만 관리하던 작성자에게 고유 식별자를 부여
  - 작성자를 할 일과 분리해서 관리
  - 작성자 테이블을 생성하고 일정 테이블에 FK를 생성하여 연관관계 설정
  - 조건
     - 작성자는 이름, 이메일, 등록일, 수정일 정보를 가지고 있다.
     - 작성자의 고유 식별자를 통해 일정이 검색이 될 수 있도록 전체 일정 조회 코드 수정
     - 작성자의 고유 식별자가 일정 테이블의 외래키가 될 수 있도록 한다.

### Lv4. -- 진행 중 --
- 페이지 네이션
  - 많은 양의 데이터를 효율적으로 표시하기 위해 데이터를 여러 페이지로 나눈다.
  - `페이지 번호`와 `페이지 크기`를 쿼리 파라미터로 전달하여 요청하는 항목을 나타낸다.
  - 전달받은 페이지 번호와 크기를 기준으로 쿼리를 작성하여 필요한 데이터만을 조회하고 반환
  -  조건
      - 등록된 일정 목록을 `페이지 번호`와 `크기`를 기준으로 모두 조회
      - 조회한 일정 목록에는 `작성자 이름`이 포함
      - 범위를 넘어선 페이지를 요청하는 경우 빈 배열을 반환
      - Paging 객체 활용

### Lv5. -- 완료 --
- 예외 발생 처리
    - 예외 상황에 대한 처리를 위해 `HTTP 상태 코드(링크)`와 `에러 메시지`를 포함한 정보를 사용하여 예외를 관리
    - 예외가 발생할 경우 적절한 HTTP 상태 코드와 함께 사용자에게 메시지를 전달하여 상황을 관리
- 조건
    - 수정, 삭제 시 요청할 때 보내는 `비밀번호`가 일치하지 않을 때 예외 발생
    - 선택한 일정 정보를 조회할 수 없을 때 예외 발생
        1. 잘못된 정보로 조회하려고 할 때
        2. 이미 삭제된 정보를 조회하려고 할 때

### Lv6. -- 완료 --
- null 체크 및 특정 패턴에 대한 검증 수행
  - 유효성 검사
    1. 잘못된 입력이나 요청을 미리 방지할 수 있다.
    2. 데이터의`무결성을 보장`하고 애플리케이션의 예측 가능성을 높여준다.
    3. Spring에서 제공하는`@Valid`어노테이션을 이용할 수 있다.
  - 조건
  - `할일`은 최대 200자 이내로 제한, 필수값 처리
    - `비밀번호`는 필수값 처리
    - 담당자의 `이메일` 정보가 형식에 맞는지 확인

---

## 📌 주요 기능
- 일정 생성 시 할일, 작성자명, 비밀번호, 작성/수정일 저장 (자동 ID 생성)
- 전체 일정 조회 시 수정일·작성자명 조건으로 필터링 가능, 수정일 기준 내림차순 정렬
- 고유 ID로 일정 상세 조회
- 일정 수정 시 제목·내용·작성자명 변경 가능, 비밀번호 필요, 수정일 갱신
- 일정 삭제 시 비밀번호 확인 후 삭제
- 작성자 정보를 별도 테이블로 관리 (이름, 이메일, 등록일, 수정일)
- 일정과 작성자 연관관계 설정 (작성자 ID → 일정 FK)
- 페이징 처리로 일정 목록을 페이지 단위로 조회 (작성자 이름 포함)
- 페이지 범위 벗어나면 빈 결과 반환
- 비밀번호 불일치 또는 잘못된 ID로 접근 시 예외 처리
- 할일은 200자 제한, 할일·비밀번호는 필수
- 이메일 형식 유효성 검사, @Valid로 입력 검증

---

## 📌 API 명세

### 1. [POST] /schedules
- 설명: 일정 생성하기
- Request
```java
{
  "name": "이름",
  "password" : 1234,
  "title" : "제목입니다.",
  "contents" : "내용입니다."
}
```
- Response
```java
{
  "id" : 1,
  "userId" : 1,
  "name": "이름",
  "title" : "제목입니다.",
  "contents" : "내용입니다."
  “createdAt” : "2025-05-08 12:00:00",
  “updatedAt” : "2025-05-08 12:00:00"
}
```

### 2. [GET] /schedules?updated_at=...&name=…
- 설명: 일정 전체 조회

- Response
```java
// 성공시 200 OK
[
	{
		"id": 1,
                "userId" : 1,
                "name": "이름1",
		"title": "제목입니다.",
		"contents": "내용입니다.",
                “createdAt” : "2025-05-08 12:00:00",
                “updatedAt” : "2025-05-08 12:00:00"
	},
	{
		"id": 2,
                "userId" : 2,
                "name": "이름1",
		"title": "제목입니다.2",
		"contents": "내용입니다.2",
                “createdAt” : "2025-05-08 12:10:00",
                “updatedAt” : "2025-05-08 12:10:00"
	}
	// ...
]
// 없으면 200 OK와 비어있는 배열 응답
[]
```

### 3. [GET] /schedules/{user_id}
- 설명: 일정 선택 조회
- Response
```java
// 성공시 200 OK
{
  "id" : 1,
  "userId" : 1,
  "name": "이름",
  "title" : "제목입니다.",
  "contents" : "내용입니다.",
  “createdAt” : "2025-05-08 12:00:00",
  “updatedAt” : "2025-05-08 12:00:00"
}
// 실패시 404 NotFound 해당 식별자의 메모가 존재하지 않는 경우
```

### 4. [PATCH] /schedules/{user_id}
- 설명: 일정 수정(제목, 내용, 이름만 변경)
- Request
```java
{
  "name": "이름",
  "password" : 1234,
  "title" : "수정된 제목입니다.",
  "contents" : "수정된 내용입니다."
}
```
- Response
```java
// 성공시 200 OK
{
  "id" : 1,
  "userId" : 1,
  "name": "이름",
  "title" : "수정된 제목입니다.",
  "contents" : "수정된 내용입니다.",
  “createdAt” : "2025-05-08 12:00:00",
  “updatedAt” : "2025-05-08 12:25:00"
}
// 실패시 404 NotFound
해당 식별자의 메모가 존재하지 않는 경우
// 400 BadRequest
필수값이 없는 경우
```

### 5. [DEL] /schedules/{user_id}
- 설명: 일정 삭제
- Request
```java
{
   "password" : "1234"
}
```
- Response
```java
// 성공시 200 OK,  실패시 404 NotFound 해당 식별자의 메모가 존재하지 않는 경우
```

## 📌 ERD
![img.png](img.png)

---

## 📚 TIL
이 프로젝트를 진행하면서 배운 내용을 아래 벨로그에 정리하였습니다.
- [Lv0 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv0)
- [Lv1 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv1)
- [Lv2 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv2)
- [Lv3 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv3)
- [Lv4 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv4)
- [Lv5 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv5)
- [Lv6 TIL 링크](https://velog.io/@syumz/%EC%9D%BC%EC%A0%95-%EA%B4%80%EB%A6%AC-%EC%95%B1-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Lv6)


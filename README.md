# kimhchul-sample-20260220

Spring Boot + H2 기반 간단한 CRUD 샘플 프로젝트입니다.

## 기술 스택

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- H2 (인메모리 DB)
- Thymeleaf

## 실행 방법

```bash
./mvnw spring-boot:run
```

또는

```bash
mvn spring-boot:run
```

## 접속 정보

- **웹 앱**: http://localhost:8080
- **H2 콘솔**: http://localhost:8080/h2-console  
  - JDBC URL: `jdbc:h2:mem:sampledb`  
  - 사용자명: `sa`  
  - 비밀번호: (비워두기)

## 기능

- **Item** 테이블 CRUD
  - 목록 조회 (`/items`)
  - 등록 (`/items/new` → POST `/items`)
  - 상세 조회 (`/items/{id}`)
  - 수정 (`/items/{id}/edit` → POST `/items/{id}`)
  - 삭제 (POST `/items/{id}/delete`)

## 엔티티 (Item)

| 필드       | 타입    | 설명   |
|-----------|---------|--------|
| id        | Long    | PK     |
| name      | String  | 이름(필수) |
| description | String | 설명   |
| quantity  | Integer | 수량   |

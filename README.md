# 리소스 관리 API (AI 기능 포함)

## 프로젝트 개요

리소스 관리 API, 시설 내 다양한 자산을 효율적으로 관리하도록 설계 자산, 시설 및 사용자를 위한 핵심 CRUD 기능을 제공 목적 
이 프로젝트의 주요 특징은 두 가지 AI 기능의 통합으로: **지능형 검색을 위한 자연어 처리(NLP)**와 **유지보수 예측을 위한 예측 분석**입니다.

이 API는 다양한 프론트엔드 애플리케이션과 통합하여 리소스 관리 솔루션을 제공하는 백엔드 서비스 역할입니다.

## 주요 기능

1.  **핵심 리소스 관리:**
    *   **자산:** 자산 생성, 읽기, 업데이트, 삭제(CRUD) 작업. 분류, 상태 추적, 시설과의 연관성 포함.
    *   **시설:** 다양한 유형의 시설(예: 건물, 층, 방) 관리를 위한 CRUD 작업.
    *   **사용자:** 역할 기반 접근 제어 기능이 있는 기본 사용자 관리.
    *   **자산 상태 이력:** 시간 경과에 따른 자산 상태 변경 기록 추적.

2.  **AI 기반 자연어 검색:**
    *   "Building A의 고장난 센서", "활성 IT 장비"와 같은 자연어 문구를 사용하여 자산을 쿼리가 가능합니다.
    *   **모의 대규모 언어 모델(LLM)** 통합을 활용하여 자연어 쿼리를 구조화된 검색 필터로 변환, 자연어 이해를 위해 실제 LLM API와 통합할 수 있는 기능
    *   파싱된 필터를 기반으로 동적 JPA 사양이 구축되어 관련 자산을 검색

3.  **예측 유지보수 분석:**
    *   자산의 이력 오류 및 유지보수 기록을 기반으로 다음 예상 유지보수 날짜 예측
    *   이력 `AssetStatusHistory` 데이터를 분석하여 오류/유지보수 이벤트 간의 평균 간격 계산
    *   사용 가능한 이력 데이터 양을 반영하여 예측에 대한 `신뢰도` 수준(LOW, MEDIUM, HIGH) 제공
    *   유지보수 예측을 검색하기 위한 전용 API 엔드포인트 노출

## 사용된 기술

*   **백엔드 프레임워크:** Spring Boot 3.x
*   **언어:** Java 17+
*   **데이터베이스:** PostgreSQL
*   **ORM:** Spring Data JPA, Hibernate
*   **빌드 도구:** Maven
*   **AI 통합:** NLP를 위한 모의 LLM, 예측 분석을 위한 사용자 정의 알고리즘
*   **테스트:** JUnit (단위/통합 테스트용)

## 아키텍처

Spring Boot 애플리케이션에 대한 표준 계층형 아키텍처

*   **컨트롤러 계층:** 들어오는 HTTP 요청을 처리하고 서비스 메서드에 매핑하며 API 응답을 반환
*   **서비스 계층:** 비즈니스 로직을 포함하고, 작업을 조정하며, 리포지토리와 상호 작용합니다. 핵심 AI 로직(NLP 파싱, 예측 알고리즘)
*   **리포지토리 계층:** Spring Data JPA를 사용하여 데이터 영속성을 위해 데이터베이스와 상호 작용
*   **엔티티 및 DTO 계층:** 데이터 모델(엔티티) 및 API 통신을 위한 데이터 전송 객체(DTO) 정의

## 애플리케이션 실행 방법

1.  **사전 준비:**
    *   Java 17 이상
    *   Maven
    *   PostgreSQL 데이터베이스 인스턴스
    *   API 테스트를 위한 Postman 또는 `curl`

2.  **데이터베이스 설정:**
    *   `resource_db`라는 이름의 PostgreSQL 데이터베이스를 생성합니다.
    *   `src/main/resources/application.properties` 파일을 열어 기본값과 다른 경우 PostgreSQL 데이터베이스 자격 증명(예: `spring.datasource.username`, `spring.datasource.password`)으로 업데이트합니다.
    *   다음 SQL 스크립트를 실행하여 데이터베이스에 필요한 샘플 데이터를 채웁니다.

        ```sql
        -- psql 또는 GUI 도구(DBeaver, pgAdmin)를 사용하여 'resource_db'에 연결하십시오.
        -- 예시: psql -d resource_db -U your_username

        -- 1. 더미 사용자 생성
        INSERT INTO users (id, email, name, role, created_at, updated_at) VALUES
        (1, 'admin@example.com', 'Admin User', 'ADMIN', NOW(), NOW());

        -- 2. 시설 생성
        INSERT INTO facilities (id, name, type, description, created_at, updated_at) VALUES
        (1, 'Building A', 'BUILDING', 'Main office building', NOW(), NOW()),
        (2, 'Warehouse 1', 'BUILDING', 'Storage facility', NOW(), NOW());

        -- 3. 자산 생성
        -- "Building A의 고장난 센서" NLP 쿼리용 자산
        INSERT INTO assets (id, facility_id, name, category, current_status, created_at, updated_at) VALUES
        (101, 1, 'Sensor 101', 'SENSOR', 'ERROR', NOW(), NOW());

        -- "활성 IT 장비" NLP 쿼리용 자산
        INSERT INTO assets (id, facility_id, name, category, current_status, created_at, updated_at) VALUES
        (102, 2, 'Server Rack 001', 'IT_EQUIPMENT', 'ACTIVE', NOW(), NOW());

        -- 유지보수 예측용 자산 (Machine X)
        INSERT INTO assets (id, facility_id, name, category, current_status, created_at, updated_at) VALUES
        (103, 2, 'Machine X', 'ETC', 'ACTIVE', NOW(), NOW());

        -- 4. Machine X (asset_id = 103)에 대한 자산 상태 이력 생성
        -- 높은 신뢰도를 얻기 위해 ERROR 또는 MAINTENANCE 기록 5개 이상 필요 
        INSERT INTO asset_status_histories (id, asset_id, from_status, to_status, changed_by, changed_at, reason, created_at, updated_at) VALUES
        (1, 103, 'ACTIVE', 'ERROR', 1, '2025-01-10 10:00:00', 'FAILURE', NOW(), NOW()),
        (2, 103, 'ERROR', 'ACTIVE', 1, '2025-01-15 11:00:00', 'MAINTENANCE_END', NOW(), NOW()),
        (3, 103, 'ACTIVE', 'MAINTENANCE', 1, '2025-03-20 09:00:00', 'MAINTENANCE_START', NOW(), NOW()),
        (4, 103, 'MAINTENANCE', 'ACTIVE', 1, '2025-03-22 14:00:00', 'MAINTENANCE_END', NOW(), NOW()),
        (5, 103, 'ACTIVE', 'ERROR', 1, '2025-05-01 16:00:00', 'FAILURE', NOW(), NOW()),
        (6, 103, 'ERROR', 'ACTIVE', 1, '2025-05-05 10:00:00', 'MAINTENANCE_END', NOW(), NOW()),
        (7, 103, 'ACTIVE', 'ERROR', 1, '2025-07-10 13:00:00', 'FAILURE', NOW(), NOW()),
        (8, 103, 'ERROR', 'ACTIVE', 1, '2025-07-12 17:00:00', 'MAINTENANCE_END', NOW(), NOW());

        -- 자동 증가 ID에 대한 시퀀스 조정
        SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
        SELECT setval('facilities_id_seq', (SELECT MAX(id) FROM facilities));
        SELECT setval('assets_id_seq', (SELECT MAX(id) FROM assets));
        SELECT setval('asset_status_histories_id_seq', (SELECT MAX(id) FROM asset_status_histories));
        ```

3.  **빌드 및 실행:**
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```
    `http://localhost:8080`

## AI 기능 테스트 방법

Postman 또는 `curl`을 사용하여 기능 테스트

### 1. 자연어 검색

**엔드포인트:** `GET http://localhost:8080/search/assets`

**시나리오:**

*   **쿼리: "Building A의 고장난 센서"**
    *   **URI:** `http://localhost:8080/search/assets?query=broken%20sensors%20in%20building%20a`
    *   **예상:** `currentStatus: ERROR`, `category: SENSOR`, `facility.name: Building A`를 가진 `Sensor 101` (자산 ID 101)을 반환합니다.

*   **쿼리: "활성 IT 장비"**
    *   **URI:** `http://localhost:8080/search/assets?query=active%20it%20equipment`
    *   **예상:** `currentStatus: ACTIVE`, `category: IT_EQUIPMENT`를 가진 `Server Rack 001` (자산 ID 102)을 반환합니다.

*   **쿼리: "모든 자산"**
    *   **URI:** `http://localhost:8080/search/assets?query=all%20assets`
    *   **예상:** 모든 자산(자산 ID 101, 102, 103)을 반환합니다.

### 2. 예측 유지보수 분석

**엔드포인트:** `GET http://localhost:8080/assets/{id}/maintenance-prediction`

**시나리오:** `Machine X` (자산 ID 103)에 대한 유지보수 예측.

*   **URI:** `http://localhost:8080/assets/103/maintenance-prediction`
*   **예상:** 다음을 포함하는 `Machine X` (자산 ID 103)에 대한 `MaintenancePredictionResponse`를 반환합니다.
    *   `assetId`: 103
    *   `predictedMaintenanceDate`: 이력 데이터에 따라 계산된 날짜.
    *   `confidence`: `HIGH` 
    *   `analysisDetails`: 예측을 설명하는 문자열.

## 향후 작업

*   **실제 LLM 통합:** `SearchService`의 모의 LLM을 실제 외부 LLM API로 대체하여 더욱 동적이고 지능적인 자연어 쿼리 파싱을 구현 목표
*   **고급 예측 모델:** 유지보수 예측을 위한 더욱 정교한 기계 학습 모델(예: 시계열 분석, 생존 분석)을 구현하고, Java용 TensorFlow와 같은 라이브러리를 활용하거나 Python ML 서비스와 통합하기
*   **프론트엔드 통합:** 사용자 인터페이스 개발하여 자산 관리 AI를 위한 시각적 대시보드를 제공
*   **인증 및 권한 부여:** 사용자 인증 및 역할 기반 접근 제어를 위한 강력한 보안 메커니즘을 구현하기
*   **컨테이너화:** Docker를 사용하여 애플리케이션을 컨테이너화하여 배포 및 확장
*   **모니터링 및 로깅:** 모니터링 도구 및 중앙 집중식 로깅 솔루션과 통합

---

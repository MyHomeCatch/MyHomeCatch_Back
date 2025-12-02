# MyHomeCatch MSA (Microservices Architecture) 전환 가이드

## 📋 목차
1. [현재 아키텍처 분석](#1-현재-아키텍처-분석)
2. [도메인 경계 분석](#2-도메인-경계-분석)
3. [MSA 전환 전략](#3-msa-전환-전략)
4. [서비스 분해 계획](#4-서비스-분해-계획)
5. [MSA 인프라 구성](#5-msa-인프라-구성)
6. [서비스 간 통신 전략](#6-서비스-간-통신-전략)
7. [데이터베이스 전략](#7-데이터베이스-전략)
8. [배포 및 운영 전략](#8-배포-및-운영-전략)
9. [마이그레이션 로드맵](#9-마이그레이션-로드맵)
10. [참고 자료](#10-참고-자료)

---

## 1. 현재 아키텍처 분석

### 1.1 현재 상태: Modular Monolith (모듈형 모놀리스)

MyHomeCatch 백엔드는 현재 **단일 Spring Framework 애플리케이션**으로 구현되어 있으며, 다음과 같은 특징을 가지고 있습니다:

#### 기술 스택
- **프레임워크**: Spring Framework 5.3.37 (Spring MVC)
- **빌드 도구**: Gradle
- **데이터베이스**: MySQL + MyBatis
- **보안**: Spring Security + JWT
- **API 문서**: Swagger 2.9.2
- **배포**: WAR 파일 (Tomcat)

#### 패키지 구조 (도메인별 모듈화)
```
org.scoula/
├── auth/              # 인증/인가 (카카오, 구글 OAuth2)
├── member/            # 회원 관리
├── house/             # 주택 정보 통합
├── lh/                # LH 공고 정보
├── applyHome/         # 청약홈 정보
├── chapi/             # 청약홈 API 통합
├── calendar/          # 캘린더/일정 관리
├── selfCheck/         # 자격 진단
├── bookmark/          # 북마크
├── comment/           # 댓글
├── summary/           # PDF 요약
├── ChatBot/           # AI 챗봇 (Gemini)
├── email/             # 이메일 알림
├── statics/           # 통계
├── common/            # 공통 유틸리티 (JwtUtil, Response 등)
├── config/            # 설정
└── exception/         # 예외 처리
```

**참고**: MSA 전환 시 `common/` 패키지는 다음과 같이 분리됩니다:
- `common-dto`: 서비스 간 공유되는 DTO
- `common-utils`: 공통 유틸리티 (날짜, 문자열 처리 등)
- `common-security`: JWT 관련 공통 라이브러리

### 1.2 현재 아키텍처의 강점

✅ **이미 구현된 MSA 준비 요소들:**

1. **도메인 주도 설계 (DDD) 적용**
   - 각 도메인별로 패키지가 명확히 분리되어 있음
   - Controller-Service-Mapper(Repository)-Domain 계층 구조
   - 명확한 책임 분리 (Separation of Concerns)

2. **RESTful API 설계**
   - `/api/auth/*`, `/api/house/*`, `/api/lh/*` 등 도메인별 엔드포인트
   - HTTP 메서드 기반 CRUD 작업
   - Swagger를 통한 API 문서화

3. **인증/인가 중앙화**
   - JWT 기반 Stateless 인증
   - `JwtFilter`를 통한 토큰 검증
   - OAuth2 통합 (Kakao, Google)

4. **외부 API 통합 경험**
   - 공공데이터포털 API (LH, 청약홈)
   - Google Gemini API (AI)
   - Kakao Maps API
   - `RestTemplate` 사용 경험

5. **스케줄링 작업 분리**
   - `@EnableScheduling` 사용
   - 각 도메인별 스케줄러 (`LhNoticeScheduler`, `ApplyHomeScheduler` 등)

6. **설정 외부화**
   - `application.properties`를 통한 설정 관리
   - `@PropertySource` 사용

### 1.3 현재 아키텍처의 한계

⚠️ **MSA 전환이 필요한 이유:**

1. **단일 데이터베이스 의존**
   - 모든 도메인이 하나의 MySQL DB 공유
   - 스키마 변경 시 전체 시스템 영향

2. **단일 배포 단위**
   - 작은 변경에도 전체 애플리케이션 재배포 필요
   - 빌드 및 배포 시간 증가

3. **확장성 제약**
   - 특정 기능(예: AI 챗봇)만 확장 불가능
   - 전체 애플리케이션을 확장해야 함

4. **기술 스택 고정**
   - 모든 모듈이 Spring Framework 5에 종속
   - 새로운 기술 도입 어려움

5. **장애 전파**
   - 한 모듈의 오류가 전체 시스템에 영향
   - 예: PDF 파싱 실패 → 전체 서비스 다운 가능

---

## 2. 도메인 경계 분석

### 2.1 Bounded Context 식별

Domain-Driven Design(DDD) 관점에서 각 도메인의 경계를 분석:

#### 🔐 **인증/회원 컨텍스트** (Authentication & Member Context)
**책임**: 사용자 인증, 회원 정보 관리
- `auth/` - 로그인, OAuth2, JWT 발급
- `member/` - 회원 CRUD, 프로필 관리
- **핵심 엔티티**: User, RefreshToken
- **외부 의존성**: 카카오/구글 OAuth2

#### 🏠 **주택 정보 컨텍스트** (Housing Information Context)
**책임**: 주택 청약 정보 통합 제공
- `house/` - 주택 정보 통합 조회
- `lh/` - LH 공사 공고
- `applyHome/` - 청약홈 데이터
- `chapi/` - 청약홈 API 통합
- **핵심 엔티티**: House, Notice, Danzi
- **외부 의존성**: 공공데이터포털 API

#### ✅ **자격 진단 컨텍스트** (Eligibility Check Context)
**책임**: 사용자 청약 자격 진단
- `selfCheck/` - 자격 진단 로직
- **핵심 엔티티**: EligibilityResult
- **의존 관계**: 회원 정보, 주택 정보

#### 📅 **일정 관리 컨텍스트** (Calendar Context)
**책임**: 청약 일정 관리
- `calendar/` - 일정 CRUD
- **핵심 엔티티**: Calendar
- **의존 관계**: 주택 정보

#### 🔖 **북마크 컨텍스트** (Bookmark Context)
**책임**: 사용자 관심 항목 관리
- `bookmark/` - 북마크 CRUD
- **핵심 엔티티**: Bookmark
- **의존 관계**: 회원, 주택 정보

#### 💬 **커뮤니티 컨텍스트** (Community Context)
**책임**: 사용자 커뮤니티 기능
- `comment/` - 댓글 관리
- **핵심 엔티티**: Comment
- **의존 관계**: 회원, 주택 정보

#### 🤖 **AI 서비스 컨텍스트** (AI Service Context)
**책임**: AI 기반 서비스 제공
- `ChatBot/` - AI 챗봇 (Gemini)
- `summary/` - PDF 요약
- **외부 의존성**: Google Gemini API

#### 📧 **알림 컨텍스트** (Notification Context)
**책임**: 사용자 알림 발송
- `email/` - 이메일 발송
- **외부 의존성**: Gmail SMTP

#### 📊 **통계 컨텍스트** (Statistics Context)
**책임**: 데이터 분석 및 통계
- `statics/` - 통계 정보 제공
- **의존 관계**: 주택 정보, 회원

### 2.2 컨텍스트 간 의존성 맵

```
┌─────────────────┐
│  회원/인증 서비스  │◄──────────────┐
└────────┬────────┘               │
         │ uses                   │
         ▼                        │
┌─────────────────┐         ┌────┴──────┐
│   주택정보 서비스  │◄────────│  북마크    │
└────────┬────────┘         └───────────┘
         │                        ▲
         ├────────┬───────┬───────┤
         ▼        ▼       ▼       │
    ┌────────┐ ┌────┐ ┌─────┐ ┌──┴──┐
    │자격진단 │ │일정│ │댓글 │ │통계 │
    └────────┘ └────┘ └─────┘ └─────┘
         
┌──────────┐    ┌────────┐
│ AI 서비스 │    │  알림   │
└──────────┘    └────────┘
```

---

## 3. MSA 전환 전략

### 3.1 전환 접근 방식: Strangler Fig Pattern

**점진적 마이그레이션** (Big Bang 방식 지양)

1. **Phase 1**: API Gateway 도입 (현재 모놀리스 유지)
2. **Phase 2**: 독립성 높은 서비스부터 분리 (AI, 알림)
3. **Phase 3**: 핵심 도메인 분리 (인증, 주택정보)
4. **Phase 4**: 의존성 높은 서비스 분리 (북마크, 댓글 등)
5. **Phase 5**: 모놀리스 완전 제거

### 3.2 우선순위 기준

**서비스 분리 우선순위** (높음 → 낮음):

1. **독립적 실행 가능** ✅
   - AI 서비스 (ChatBot, Summary)
   - 알림 서비스 (Email)

2. **높은 확장성 요구** 📈
   - 주택 정보 서비스 (트래픽 집중)
   - 인증 서비스 (모든 요청 통과)

3. **외부 API 의존** 🌐
   - LH/청약홈 데이터 수집 (스케줄러)

4. **비즈니스 로직 복잡도** 🧩
   - 자격 진단 서비스

5. **데이터 일관성 요구** 💾
   - 회원/인증 서비스
   - 북마크, 댓글 (나중에 분리)

---

## 4. 서비스 분해 계획

### 4.1 마이크로서비스 목록

#### 1️⃣ **API Gateway Service**
- **기술**: Spring Cloud Gateway 또는 Kong
- **책임**:
  - 라우팅 및 로드밸런싱
  - JWT 검증 (중앙화)
  - Rate Limiting
  - CORS 처리
- **포트**: 8080

#### 2️⃣ **Authentication Service** (인증 서비스)
- **기술**: Spring Boot 3.x
- **책임**:
  - 로그인/로그아웃
  - OAuth2 통합 (Kakao, Google)
  - JWT 발급/갱신
  - 회원 가입
- **DB**: `auth_db` (User, RefreshToken)
- **포트**: 8081
- **현재 코드**: `auth/`, `member/`, `common/util/JwtUtil.java`

#### 3️⃣ **Housing Information Service** (주택 정보 서비스)
- **기술**: Spring Boot 3.x
- **책임**:
  - LH 공고 조회
  - 청약홈 데이터 조회
  - 주택 검색/필터링
  - 외부 API 데이터 수집 (스케줄러)
- **DB**: `housing_db` (LH, ApplyHome, Danzi)
- **포트**: 8082
- **현재 코드**: `lh/`, `applyHome/`, `chapi/`, `house/`

#### 4️⃣ **AI Service** (AI 서비스)
- **기술**: Spring Boot 3.x 또는 Python FastAPI
- **책임**:
  - AI 챗봇 (Gemini API)
  - PDF 요약
- **DB**: 필요시 `ai_db` (대화 이력)
- **포트**: 8083
- **현재 코드**: `ChatBot/`, `summary/`
- **외부 의존성**: Google Gemini API

#### 5️⃣ **Notification Service** (알림 서비스)
- **기술**: Spring Boot 3.x
- **책임**:
  - 이메일 발송
  - 스케줄 알림
- **DB**: `notification_db` (알림 이력)
- **포트**: 8084
- **현재 코드**: `email/`
- **메시징**: RabbitMQ 또는 Kafka (비동기 발송)

#### 6️⃣ **User Preference Service** (사용자 선호 서비스)
- **기술**: Spring Boot 3.x
- **책임**:
  - 자격 진단
  - 북마크
  - 캘린더
- **DB**: `preference_db` (SelfCheck, Bookmark, Calendar)
- **포트**: 8085
- **현재 코드**: `selfCheck/`, `bookmark/`, `calendar/`

#### 7️⃣ **Community Service** (커뮤니티 서비스)
- **기술**: Spring Boot 3.x
- **책임**:
  - 댓글 관리
- **DB**: `community_db` (Comment)
- **포트**: 8086
- **현재 코드**: `comment/`

#### 8️⃣ **Analytics Service** (통계 서비스)
- **기술**: Spring Boot 3.x
- **책임**:
  - 데이터 분석
  - 통계 정보 제공
- **DB**: `analytics_db` (또는 Data Warehouse)
- **포트**: 8087
- **현재 코드**: `statics/`

### 4.2 서비스별 책임 매트릭스

| 서비스 | 데이터 소유 | 외부 API | 스케줄러 | 인증 필요 | 우선순위 |
|--------|------------|---------|---------|----------|----------|
| API Gateway | - | - | - | ✅ | 1 |
| Authentication | User | OAuth2 | - | 부분 | 1 |
| Housing Info | House, Notice | 공공API | ✅ | ✅ | 2 |
| AI Service | - | Gemini | - | ✅ | 2 |
| Notification | Email Log | SMTP | ✅ | - | 2 |
| User Preference | Bookmark, Calendar | - | - | ✅ | 3 |
| Community | Comment | - | - | ✅ | 3 |
| Analytics | Stats | - | - | ✅ | 4 |

---

## 5. MSA 인프라 구성

### 5.1 필수 인프라 컴포넌트

#### 1. **Service Discovery & Registration**
**도구**: Eureka (Netflix OSS) 또는 Consul

```yaml
# Eureka Server 설정 예시
eureka:
  client:
    registerWithEureka: false
    fetchRegistry: false
  server:
    port: 8761
```

**역할**:
- 서비스 인스턴스 등록
- 동적 서비스 디스커버리
- 헬스 체크

#### 2. **API Gateway**
**도구**: Spring Cloud Gateway

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://AUTH-SERVICE
          predicates:
            - Path=/api/auth/**
        - id: housing-service
          uri: lb://HOUSING-SERVICE
          predicates:
            - Path=/api/house/**, /api/lh/**
        - id: ai-service
          uri: lb://AI-SERVICE
          predicates:
            - Path=/api/chat/**, /api/summary/**
```

**기능**:
- 중앙 라우팅
- JWT 검증 (GlobalFilter)
- Rate Limiting
- Circuit Breaker

#### 3. **Configuration Management**
**도구**: Spring Cloud Config Server

```
config-repo/
├── application.yml          # 공통 설정
├── auth-service.yml         # 인증 서비스 설정
├── housing-service.yml      # 주택 정보 서비스 설정
└── ...
```

**관리 항목**:
- DB 연결 정보
- API 키 (외부 API)
- JWT Secret
- 기능 플래그

#### 4. **Message Queue**
**도구**: RabbitMQ 또는 Apache Kafka

**사용 사례**:
- 비동기 이메일 발송
- 이벤트 기반 통신 (Event-Driven)
- 데이터 동기화

#### 5. **Distributed Tracing**
**도구**: Zipkin + Sleuth

**목적**:
- 요청 추적 (Trace ID)
- 성능 병목 발견
- 서비스 간 호출 관계 시각화

#### 6. **Centralized Logging**
**도구**: ELK Stack (Elasticsearch, Logstash, Kibana)

**로그 수집**:
```
각 서비스 → Logstash → Elasticsearch → Kibana (시각화)
```

#### 7. **Monitoring & Alerting**
**도구**: Prometheus + Grafana

**메트릭**:
- CPU/메모리 사용률
- HTTP 요청 수/응답 시간
- DB 연결 풀
- 비즈니스 메트릭 (가입자 수, 조회수 등)

### 5.2 인프라 아키텍처 다이어그램

```
                    ┌──────────────┐
                    │   클라이언트   │
                    │  (Vue.js)    │
                    └───────┬──────┘
                            │ HTTPS
                            ▼
                    ┌──────────────┐
                    │  API Gateway │
                    │   (8080)     │
                    └───────┬──────┘
                            │
            ┌───────────────┼───────────────┐
            ▼               ▼               ▼
    ┌──────────────┐┌──────────────┐┌──────────────┐
    │  Auth Service││Housing Service││ AI Service   │
    │   (8081)     ││   (8082)      ││   (8083)     │
    └──────┬───────┘└──────┬────────┘└──────┬───────┘
           │               │                 │
           ▼               ▼                 ▼
    ┌──────────────┐┌──────────────┐┌──────────────┐
    │   auth_db    ││  housing_db  ││  Gemini API  │
    └──────────────┘└──────────────┘└──────────────┘

    ┌──────────────────────────────────────────────┐
    │         공통 인프라 (Shared Services)          │
    ├──────────────────────────────────────────────┤
    │ Eureka | Config Server | RabbitMQ | Zipkin  │
    │ ELK Stack | Prometheus | Grafana             │
    └──────────────────────────────────────────────┘
```

---

## 6. 서비스 간 통신 전략

### 6.1 동기 통신 (Synchronous)

#### REST API (내부 서비스 호출)
**도구**: RestTemplate → **WebClient** (권장, 비동기)

**예시**: 자격진단 서비스 → 회원 서비스
```java
// WebClient 설정
@Bean
public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
}

// 서비스에서 사용 - Reactive 방식 (권장)
public Mono<UserInfo> getUserInfo(Long userId) {
    return webClient.get()
        .uri("http://AUTH-SERVICE/api/members/{id}", userId)
        .retrieve()
        .bodyToMono(UserInfo.class);
}

// 또는 동기적 호출이 필요한 경우 (특별한 경우에만)
public UserInfo getUserInfoSync(Long userId) {
    return webClient.get()
        .uri("http://AUTH-SERVICE/api/members/{id}", userId)
        .retrieve()
        .bodyToMono(UserInfo.class)
        .block();  // 주의: 가능하면 Reactive Chain 유지
}
```

#### gRPC (고성능 필요 시)
**사용 사례**: 실시간 데이터 스트리밍, 대용량 데이터 전송

### 6.2 비동기 통신 (Asynchronous)

#### Event-Driven Architecture (이벤트 기반)
**도구**: RabbitMQ 또는 Kafka

**이벤트 예시**:
1. **UserRegisteredEvent**: 회원 가입 → 이메일 발송
2. **BookmarkAddedEvent**: 북마크 추가 → 통계 업데이트
3. **HousingDataUpdatedEvent**: 주택 정보 갱신 → 캐시 무효화

**구현**:
```java
// 이벤트 발행 (Publisher)
@Service
public class MemberService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void registerUser(UserDTO user) {
        // ... 회원 가입 로직
        
        UserRegisteredEvent event = new UserRegisteredEvent(user.getEmail());
        rabbitTemplate.convertAndSend("user-events", "user.registered", event);
    }
}

// 이벤트 구독 (Subscriber)
@Service
public class EmailService {
    @RabbitListener(queues = "email-queue")
    public void handleUserRegistered(UserRegisteredEvent event) {
        sendWelcomeEmail(event.getEmail());
    }
}
```

### 6.3 Circuit Breaker Pattern

**도구**: Resilience4j

**목적**: 장애 전파 방지

```java
@CircuitBreaker(name = "housingService", fallbackMethod = "getHouseFallback")
public HouseInfo getHouse(Long houseId) {
    return housingServiceClient.getHouse(houseId);
}

public HouseInfo getHouseFallback(Long houseId, Exception ex) {
    // 캐시된 데이터 반환 또는 기본값
    return cachedHouseInfo.get(houseId);
}
```

---

## 7. 데이터베이스 전략

### 7.1 Database per Service Pattern

**원칙**: 각 서비스는 자신의 데이터베이스를 가짐

```
auth-service        → auth_db (User, RefreshToken)
housing-service     → housing_db (House, Notice, Danzi)
preference-service  → preference_db (Bookmark, Calendar, SelfCheck)
community-service   → community_db (Comment)
notification-service→ notification_db (EmailLog)
analytics-service   → analytics_db (Statistics)
```

### 7.2 데이터 일관성 전략

#### 1. **Saga Pattern** (분산 트랜잭션)

**시나리오**: 회원 탈퇴
1. Auth Service: 사용자 비활성화
2. Preference Service: 북마크 삭제
3. Community Service: 댓글 삭제

**구현**: Choreography-based Saga (이벤트 기반)
```
UserDeletedEvent → PreferenceService → BookmarkDeletedEvent
                 → CommunityService  → CommentDeletedEvent
```

**보상 트랜잭션**: 실패 시 롤백 이벤트 발행

#### 2. **CQRS (Command Query Responsibility Segregation)**

**읽기 전용 복제본** 활용:
- 쓰기: 각 서비스의 DB
- 읽기: 통합 Read Model (예: Elasticsearch)

**데이터 동기화**: 
```
Housing Service (Write) → Kafka → Analytics Service (Read Model Update)
```

### 7.3 공유 데이터 처리

**문제**: 여러 서비스에서 User 정보 필요

**해결책 1**: API 호출
```java
// Preference Service에서 User 정보 필요 시
UserInfo user = authServiceClient.getUser(userId);
```

**해결책 2**: 데이터 복제 (Eventual Consistency)
```
preference_db.user_cache (id, email, name) ← UserUpdatedEvent
```

### 7.4 데이터베이스 마이그레이션 전략

#### Phase 1: Schema 분리
```sql
-- 기존 monolith_db
-- 1. 스키마별 분리
CREATE DATABASE auth_db;
CREATE DATABASE housing_db;

-- 2. 데이터 이관
INSERT INTO auth_db.users SELECT * FROM monolith_db.users;
INSERT INTO housing_db.houses SELECT * FROM monolith_db.houses;
```

#### Phase 2: 외래키 제거
```sql
-- ❌ 서비스 간 FK 제거
ALTER TABLE preference_db.bookmarks DROP FOREIGN KEY fk_user_id;

-- ✅ 애플리케이션 레벨에서 참조 무결성 관리
-- 방법 1: API 호출로 검증
@Service
public class BookmarkService {
    private final AuthServiceClient authClient;
    
    public void createBookmark(BookmarkDTO bookmark) {
        // User 존재 여부 확인
        UserInfo user = authClient.getUser(bookmark.getUserId());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        // Bookmark 저장
        bookmarkRepository.save(bookmark);
    }
}

-- 방법 2: 이벤트 기반 데이터 동기화
// User 삭제 시 Bookmark도 삭제
@RabbitListener(queues = "user-deleted-queue")
public void handleUserDeleted(UserDeletedEvent event) {
    bookmarkRepository.deleteByUserId(event.getUserId());
}
```

#### Phase 3: 이벤트 소싱 (선택적)
```java
// Event Store에 모든 변경 기록
UserCreatedEvent → Event Store → User Aggregate 재구성
```

---

## 8. 배포 및 운영 전략

### 8.1 컨테이너화 (Docker)

#### Dockerfile 예시 (Spring Boot 서비스)
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/auth-service.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### docker-compose.yml (개발 환경)
```yaml
version: '3.8'
services:
  eureka:
    image: netflix/eureka:latest
    ports:
      - "8761:8761"
  
  auth-service:
    build: ./auth-service
    ports:
      - "8081:8081"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://auth-db:3306/auth_db
    depends_on:
      - auth-db
      - eureka
  
  auth-db:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: auth_db
      MYSQL_ROOT_PASSWORD: password
```

### 8.2 오케스트레이션 (Kubernetes)

#### Deployment 예시
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auth-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: auth-service
  template:
    metadata:
      labels:
        app: auth-service
    spec:
      containers:
      - name: auth-service
        image: myhomecatch/auth-service:1.0
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8081
          initialDelaySeconds: 30
          periodSeconds: 10
```

#### Service 예시
```yaml
apiVersion: v1
kind: Service
metadata:
  name: auth-service
spec:
  selector:
    app: auth-service
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8081
  type: LoadBalancer
```

### 8.3 CI/CD 파이프라인

#### GitHub Actions 예시
```yaml
name: Auth Service CI/CD

on:
  push:
    branches: [ main ]
    paths:
      - 'auth-service/**'

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      
      - name: Build with Gradle
        run: ./gradlew :auth-service:build
      
      - name: Build Docker Image
        run: docker build -t myhomecatch/auth-service:${{ github.sha }} ./auth-service
      
      - name: Push to Docker Hub
        run: docker push myhomecatch/auth-service:${{ github.sha }}
      
      - name: Deploy to Kubernetes
        run: kubectl set image deployment/auth-service auth-service=myhomecatch/auth-service:${{ github.sha }}
```

### 8.4 모니터링 및 알림

#### Health Check Endpoint
```java
// Spring Boot Actuator
@RestController
public class HealthController {
    @GetMapping("/actuator/health")
    public HealthStatus health() {
        return new HealthStatus("UP");
    }
}
```

#### Custom Metrics
```java
@Service
public class HousingService {
    private final MeterRegistry meterRegistry;
    
    public List<House> getHouses() {
        Timer.Sample sample = Timer.start(meterRegistry);
        // ... 비즈니스 로직
        sample.stop(meterRegistry.timer("housing.search.time"));
        
        meterRegistry.counter("housing.search.count").increment();
        return houses;
    }
}
```

---

## 9. 마이그레이션 로드맵

### Phase 1: 준비 단계 (1-2개월)

#### 주요 작업:
1. **인프라 구축**
   - [ ] Docker 환경 설정
   - [ ] Kubernetes 클러스터 구성 (또는 Docker Compose)
   - [ ] Eureka 서버 구축
   - [ ] Config Server 구축
   - [ ] RabbitMQ 설치

2. **공통 라이브러리 개발**
   - [ ] 공통 DTO 모듈 (`common-dto`)
   - [ ] 공통 유틸리티 모듈 (`common-utils`)
   - [ ] JWT 라이브러리 모듈 (`common-security`)

3. **API Gateway 구축**
   - [ ] Spring Cloud Gateway 프로젝트 생성
   - [ ] JWT 검증 GlobalFilter
   - [ ] 라우팅 규칙 정의

4. **CI/CD 파이프라인**
   - [ ] GitHub Actions 워크플로우
   - [ ] Docker 이미지 빌드/푸시
   - [ ] 배포 자동화

**결과물**: MSA 인프라 기반 완성

---

### Phase 2: 독립 서비스 분리 (2-3개월)

#### 우선순위 1: AI Service 분리
**이유**: 외부 API 의존, 독립적 실행 가능

**작업**:
1. [ ] `ai-service` Spring Boot 프로젝트 생성
2. [ ] `ChatBot/`, `summary/` 코드 이관
3. [ ] Gemini API 설정 외부화
4. [ ] API Gateway 라우팅 추가 (`/api/chat/**`, `/api/summary/**`)
5. [ ] 배포 및 테스트

**검증**:
- [ ] 기존 챗봇 기능 정상 작동
- [ ] PDF 요약 기능 정상 작동

---

#### 우선순위 2: Notification Service 분리
**이유**: 비동기 처리 적합, 독립적 실행 가능

**작업**:
1. [ ] `notification-service` 프로젝트 생성
2. [ ] `email/` 코드 이관
3. [ ] RabbitMQ 연동 (이벤트 리스너)
4. [ ] 이메일 발송 이벤트 정의 (`SendEmailEvent`)
5. [ ] 기존 서비스들이 이벤트 발행하도록 수정

**검증**:
- [ ] 회원 가입 시 이메일 발송
- [ ] 청약 알림 이메일 발송

---

### Phase 3: 핵심 서비스 분리 (3-4개월)

#### 우선순위 3: Authentication Service 분리
**이유**: 모든 요청의 진입점, 높은 확장성 요구

**작업**:
1. [ ] `auth-service` 프로젝트 생성
2. [ ] `auth/`, `member/` 코드 이관
3. [ ] `auth_db` 스키마 분리
   ```sql
   CREATE DATABASE auth_db;
   -- User, RefreshToken 테이블 이관
   ```
4. [ ] OAuth2 설정 (`application.yml`)
5. [ ] JWT 발급/검증 API 구현
6. [ ] API Gateway에서 인증 필터 수정 (Auth Service 호출)

**검증**:
- [ ] 로그인/로그아웃
- [ ] 카카오/구글 OAuth2
- [ ] JWT 토큰 발급/갱신

---

#### 우선순위 4: Housing Information Service 분리
**이유**: 데이터량 많음, 외부 API 연동, 스케줄러 존재

**작업**:
1. [ ] `housing-service` 프로젝트 생성
2. [ ] `lh/`, `applyHome/`, `chapi/`, `house/` 코드 이관
3. [ ] `housing_db` 스키마 분리
   ```sql
   CREATE DATABASE housing_db;
   -- LH, ApplyHome, Danzi 관련 테이블 이관
   ```
4. [ ] 스케줄러 동작 확인 (`LhNoticeScheduler`, `ApplyHomeScheduler`)
5. [ ] 공공 API 키 설정
6. [ ] RESTful API 엔드포인트 유지
   - `/api/lh/**`
   - `/api/house/**`
   - `/api/applyHome/**`

**검증**:
- [ ] 주택 정보 조회
- [ ] 스케줄러 정상 작동
- [ ] 외부 API 데이터 수집

---

### Phase 4: 부가 서비스 분리 (2-3개월)

#### 우선순위 5: User Preference Service 분리
**작업**:
1. [ ] `preference-service` 프로젝트 생성
2. [ ] `selfCheck/`, `bookmark/`, `calendar/` 코드 이관
3. [ ] `preference_db` 스키마 분리
4. [ ] User 정보 필요 시 Auth Service API 호출 또는 이벤트 기반 캐싱

**검증**:
- [ ] 자격 진단 기능
- [ ] 북마크 CRUD
- [ ] 캘린더 CRUD

---

#### 우선순위 6: Community Service 분리
**작업**:
1. [ ] `community-service` 프로젝트 생성
2. [ ] `comment/` 코드 이관
3. [ ] `community_db` 스키마 분리

---

#### 우선순위 7: Analytics Service 분리
**작업**:
1. [ ] `analytics-service` 프로젝트 생성
2. [ ] `statics/` 코드 이관
3. [ ] CQRS 패턴 적용 (읽기 전용 모델)

---

### Phase 5: 모놀리스 제거 및 최적화 (1-2개월)

**작업**:
1. [ ] 모놀리스 애플리케이션 종료
2. [ ] 레거시 DB 제거
3. [ ] API Gateway 최적화 (캐싱, Rate Limiting)
4. [ ] 성능 테스트 및 튜닝
5. [ ] 문서화 업데이트

---

## 10. 참고 자료

### 10.1 MSA 패턴 및 베스트 프랙티스

- **Microservices Patterns**: Chris Richardson
  - https://microservices.io/patterns/index.html
- **12-Factor App**: https://12factor.net/ko/
- **Domain-Driven Design**: Eric Evans

### 10.2 Spring Cloud 공식 문서

- Spring Cloud Gateway: https://spring.io/projects/spring-cloud-gateway
- Spring Cloud Config: https://spring.io/projects/spring-cloud-config
- Spring Cloud Netflix (Eureka): https://spring.io/projects/spring-cloud-netflix

### 10.3 인프라 도구

- **Docker**: https://docs.docker.com/
- **Kubernetes**: https://kubernetes.io/docs/
- **RabbitMQ**: https://www.rabbitmq.com/documentation.html
- **Kafka**: https://kafka.apache.org/documentation/

### 10.4 모니터링 및 추적

- **Prometheus**: https://prometheus.io/docs/
- **Grafana**: https://grafana.com/docs/
- **Zipkin**: https://zipkin.io/
- **ELK Stack**: https://www.elastic.co/guide/

---

## 결론

MyHomeCatch 백엔드는 이미 **도메인 주도 설계**를 기반으로 모듈화되어 있어, MSA 전환을 위한 좋은 기반을 갖추고 있습니다.

### 🎯 다음 단계:

1. **Phase 1부터 시작**: API Gateway 및 인프라 구축
2. **점진적 마이그레이션**: 독립적인 서비스부터 분리
3. **이벤트 기반 아키텍처 도입**: 서비스 간 느슨한 결합
4. **모니터링 및 추적 시스템 구축**: 운영 안정성 확보

### ✅ 핵심 원칙:

- **Database per Service**: 각 서비스는 자신의 DB 소유
- **API First**: 명확한 API 계약 (Swagger/OpenAPI)
- **Resilience**: Circuit Breaker, Retry, Timeout 적용
- **Observability**: 로깅, 메트릭, 추적 필수
- **Automation**: CI/CD 자동화로 배포 부담 최소화

이 문서를 기반으로 단계별로 MSA 전환을 진행하시면, 확장 가능하고 유연한 시스템을 구축할 수 있습니다! 🚀

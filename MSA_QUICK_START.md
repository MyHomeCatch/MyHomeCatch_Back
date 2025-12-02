# MyHomeCatch MSA 전환 빠른 시작 가이드

## 🚀 즉시 시작 가능한 MSA 전환 가이드

이 문서는 MyHomeCatch 프로젝트를 MSA로 전환하기 위한 **실용적인 첫 걸음**을 제시합니다.

---

## 📊 현재 상태 요약

### ✅ 이미 구현되어 있는 MSA 준비 요소

1. **도메인별 패키지 분리** ✅
   ```
   auth/           → 인증 서비스
   member/         → 회원 관리
   house/          → 주택 정보 통합
   lh/             → LH 공사 데이터
   applyHome/      → 청약홈 데이터
   ChatBot/        → AI 챗봇
   email/          → 알림
   bookmark/       → 북마크
   calendar/       → 일정
   selfCheck/      → 자격 진단
   ```

2. **RESTful API 설계** ✅
   - `/api/auth/*` - 인증
   - `/api/house/*` - 주택 정보
   - `/api/lh/*` - LH 공고
   - `/api/chat/*` - AI 챗봇
   - 등등...

3. **JWT 기반 인증** ✅
   - Stateless 인증 (MSA 적합)
   - `JwtFilter`로 중앙 검증

4. **외부 API 통합** ✅
   - 공공데이터포털 API
   - Google Gemini API
   - OAuth2 (Kakao, Google)

5. **설정 외부화** ✅
   - `application.properties`

### ⚠️ 추가 필요한 요소

1. **API Gateway** ❌
   - 현재: 단일 애플리케이션
   - 필요: 중앙 라우팅

2. **Service Discovery** ❌
   - 현재: 하드코딩된 URL
   - 필요: 동적 서비스 탐색 (Eureka)

3. **독립 데이터베이스** ❌
   - 현재: 단일 MySQL DB
   - 필요: 서비스별 DB 분리

4. **비동기 통신** ❌
   - 현재: 동기 호출만
   - 필요: 이벤트 기반 통신 (RabbitMQ)

---

## 🎯 1단계: API Gateway 도입 (권장 첫 단계)

### 왜 API Gateway부터?
- 기존 모놀리스를 유지하면서 점진적 전환 가능
- 모든 요청의 진입점 통제
- JWT 검증 중앙화

### 구현 방법

#### Step 1: Spring Cloud Gateway 프로젝트 생성

```bash
# 새 디렉토리 생성
cd /path/to/workspace
mkdir myhomecatch-gateway
cd myhomecatch-gateway

# Spring Initializr로 프로젝트 생성
# https://start.spring.io/
# Dependencies: Spring Cloud Gateway, Eureka Discovery Client, Config Client
```

#### Step 2: `build.gradle`

```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.2.0'
    id 'io.spring.dependency-management' version '1.1.4'
}

group = 'org.myhomecatch'
version = '1.0.0'

java {
    sourceCompatibility = '17'
}

ext {
    set('springCloudVersion', "2023.0.0")
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
    implementation 'io.jsonwebtoken:jjwt-jackson:0.11.5'
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
```

#### Step 3: `application.yml`

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        # 모놀리스로 모든 요청 라우팅 (일단)
        - id: monolith
          uri: http://localhost:8090  # 기존 애플리케이션 포트
          predicates:
            - Path=/api/**
          filters:
            - name: JwtAuthenticationFilter  # 커스텀 필터

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### Step 4: JWT 검증 GlobalFilter

```java
package org.myhomecatch.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
    
    private final String SECRET_KEY = "sexyRyusexyRyusexyRyusexyRyusexyRyusexyRyusexyRyusexyRyusexyRyusexyRyu";
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        
        // 인증 제외 경로
        if (path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }
        
        // JWT 검증
        String token = extractToken(exchange);
        if (token == null || !isValidToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        return chain.filter(exchange);
    }
    
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    private boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public int getOrder() {
        return -100;  // 높은 우선순위
    }
}
```

#### Step 5: 기존 애플리케이션 포트 변경

`application.properties` (기존 모놀리스)
```properties
# 기존: 8080 → 변경: 8090
server.port=8090
```

#### Step 6: 실행

```bash
# 1. Eureka 서버 (선택적, 나중에)
# 2. 기존 모놀리스 (8090 포트)
./gradlew bootRun

# 3. API Gateway (8080 포트)
cd myhomecatch-gateway
./gradlew bootRun
```

#### 검증

```bash
# API Gateway를 통한 요청
curl http://localhost:8080/api/auth/login

# 직접 모놀리스 요청 (테스트용)
curl http://localhost:8090/api/auth/login
```

---

## 🎯 2단계: 첫 마이크로서비스 분리 (AI Service)

### 왜 AI Service?
- 외부 API 의존 (Gemini)
- 다른 서비스와 독립적
- 데이터베이스 불필요

### 구현 방법

#### Step 1: 새 프로젝트 생성

```bash
mkdir myhomecatch-ai-service
cd myhomecatch-ai-service
# Spring Initializr: Web, Eureka Client, Config Client
```

#### Step 2: 기존 코드 복사

```bash
# ChatBot/, summary/ 패키지 복사
cp -r /path/to/monolith/src/main/java/org/scoula/ChatBot ./src/main/java/org/myhomecatch/ai/
cp -r /path/to/monolith/src/main/java/org/scoula/summary ./src/main/java/org/myhomecatch/ai/
```

#### Step 3: `application.yml`

```yaml
server:
  port: 8083

spring:
  application:
    name: ai-service

gemini:
  api:
    key: ${GEMINI_API_KEY}
    url: ${GEMINI_API_URL}

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### Step 4: API Gateway 라우팅 업데이트

```yaml
# API Gateway의 application.yml
spring:
  cloud:
    gateway:
      routes:
        # AI 서비스로 라우팅
        - id: ai-service
          uri: lb://AI-SERVICE  # Eureka 서비스 이름
          predicates:
            - Path=/api/chat/**, /api/summary/**
        
        # 나머지는 모놀리스로
        - id: monolith
          uri: http://localhost:8090
          predicates:
            - Path=/api/**
```

#### Step 5: 모놀리스에서 제거

```bash
# 기존 모놀리스에서 ChatBot/, summary/ 삭제 (선택적)
# 또는 일단 유지하고 트래픽만 AI Service로 전환
```

---

## 🎯 3단계: Service Discovery (Eureka) 도입

### Eureka 서버 구축

#### Step 1: 프로젝트 생성

```bash
mkdir myhomecatch-eureka
cd myhomecatch-eureka
```

#### Step 2: `build.gradle`

```gradle
dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-server'
}
```

#### Step 3: `application.yml`

```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

#### Step 4: Main 클래스

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

#### Step 5: Eureka 대시보드 확인

```
http://localhost:8761
```

---

## 🎯 4단계: 비동기 통신 (RabbitMQ)

### RabbitMQ 설치 (Docker)

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

### Notification Service 생성

#### Step 1: 이메일 발송 이벤트 정의

```java
// common-dto 모듈 (공유)
public class SendEmailEvent {
    private String to;
    private String subject;
    private String body;
    // getters, setters
}
```

#### Step 2: 이벤트 발행 (Auth Service)

```java
@Service
public class MemberService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void registerUser(UserDTO user) {
        // 회원 가입 로직
        saveUser(user);
        
        // 이메일 발송 이벤트 발행
        SendEmailEvent event = new SendEmailEvent(
            user.getEmail(),
            "회원 가입 완료",
            "환영합니다!"
        );
        rabbitTemplate.convertAndSend("email-exchange", "email.send", event);
    }
}
```

#### Step 3: 이벤트 구독 (Notification Service)

```java
@Service
public class EmailService {
    @RabbitListener(queues = "email-queue")
    public void handleSendEmail(SendEmailEvent event) {
        // 이메일 발송 로직
        sendEmail(event.getTo(), event.getSubject(), event.getBody());
    }
}
```

---

## 📈 다음 단계 (우선순위)

### Phase 1 (1-2개월)
1. ✅ API Gateway 도입
2. ✅ Eureka 서버 구축
3. ✅ AI Service 분리
4. ✅ Notification Service 분리

### Phase 2 (2-3개월)
5. Authentication Service 분리
   - `auth/`, `member/` 코드 이관
   - `auth_db` 스키마 분리

6. Housing Information Service 분리
   - `lh/`, `applyHome/`, `house/` 코드 이관
   - `housing_db` 스키마 분리
   - 스케줄러 동작 확인

### Phase 3 (2-3개월)
7. User Preference Service 분리
   - `selfCheck/`, `bookmark/`, `calendar/`

8. Community Service 분리
   - `comment/`

9. Analytics Service 분리
   - `statics/`

---

## 🛠️ 개발 환경 설정

### 권장 도구

1. **IDE**: IntelliJ IDEA Ultimate (Spring 지원)
2. **Docker Desktop**: 로컬 인프라 (MySQL, RabbitMQ, Eureka)
3. **Postman**: API 테스트
4. **Git**: 버전 관리

### Docker Compose (로컬 개발)

`docker-compose.yml`
```yaml
version: '3.8'
services:
  eureka:
    image: springcloud/eureka
    ports:
      - "8761:8761"
  
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"
  
  mysql-auth:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: auth_db
      MYSQL_ROOT_PASSWORD: password
    ports:
      - "3307:3306"
  
  mysql-housing:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: housing_db
      MYSQL_ROOT_PASSWORD: password
    ports:
      - "3308:3306"
```

실행:
```bash
docker-compose up -d
```

---

## 📚 학습 자료

### 필수 개념
1. **Spring Cloud**: https://spring.io/projects/spring-cloud
2. **API Gateway Pattern**: https://microservices.io/patterns/apigateway.html
3. **Service Discovery**: https://www.nginx.com/blog/service-discovery-in-a-microservices-architecture/
4. **Event-Driven Architecture**: https://martinfowler.com/articles/201701-event-driven.html

### 실습 튜토리얼
- Spring Cloud Gateway: https://spring.io/guides/gs/gateway/
- Eureka: https://spring.io/guides/gs/service-registration-and-discovery/
- RabbitMQ: https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html

---

## 🎯 핵심 원칙

### DO ✅
- 점진적 마이그레이션 (한 번에 하나씩)
- 독립적인 서비스부터 분리
- 이벤트 기반 통신 활용
- 모니터링 및 로깅 필수

### DON'T ❌
- Big Bang 마이그레이션 (한 번에 전환)
- 서비스 간 DB 직접 접근
- 동기 호출 남용
- 테스트 없이 배포

---

## 🆘 문제 해결

### 1. "Eureka에 서비스가 등록되지 않아요"
```yaml
# application.yml 확인
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

### 2. "API Gateway가 서비스를 찾지 못해요"
```yaml
# URI를 lb://SERVICE-NAME 형식으로
spring:
  cloud:
    gateway:
      routes:
        - id: ai-service
          uri: lb://AI-SERVICE  # Eureka 서비스 이름 (대문자)
```

### 3. "RabbitMQ 연결 실패"
```bash
# RabbitMQ 실행 확인
docker ps | grep rabbitmq

# 로그 확인
docker logs rabbitmq
```

---

## ✅ 체크리스트

### Phase 1 완료 기준
- [ ] Eureka 서버 실행 (http://localhost:8761)
- [ ] API Gateway 실행 (http://localhost:8080)
- [ ] AI Service가 Eureka에 등록됨
- [ ] `/api/chat` 요청이 AI Service로 라우팅됨
- [ ] JWT 검증이 API Gateway에서 작동함

### Phase 2 완료 기준
- [ ] Auth Service 분리
- [ ] Housing Service 분리
- [ ] Notification Service 분리
- [ ] RabbitMQ로 이메일 발송 비동기 처리
- [ ] 모든 서비스가 Eureka에 등록됨

---

이 가이드를 따라 단계별로 진행하면, MyHomeCatch를 MSA로 성공적으로 전환할 수 있습니다! 🚀

**다음 단계**: `MSA_ARCHITECTURE.md` 문서에서 전체 아키텍처 및 상세 전략 확인

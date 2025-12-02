# MSA 아키텍처 분석 요약 (Summary)

## 📋 작업 개요

이 PR은 MyHomeCatch 백엔드의 **MSA(Microservices Architecture) 확장 방안**을 분석하고 문서화했습니다.

---

## 🎯 질문에 대한 답변

### Q: 지금 아키텍처가 MSA로 확장하기 위해서, 어디까지 구현되어있고, 어떤 부분을 이용하면 확장할 수 있는지 시스템 아키텍처, 방향성 관점에서 설명해줘

---

## ✅ 현재 구현 상태 (이미 MSA 준비된 부분)

### 1. **도메인 주도 설계 (DDD) 적용** ✅
현재 코드는 이미 도메인별로 명확히 분리되어 있습니다:

```
✅ auth/           → 인증 서비스로 분리 가능
✅ member/         → 회원 서비스로 분리 가능
✅ house/          → 주택 정보 서비스로 분리 가능
✅ lh/             → LH 데이터 서비스로 분리 가능
✅ ChatBot/        → AI 서비스로 분리 가능
✅ email/          → 알림 서비스로 분리 가능
✅ selfCheck/      → 자격 진단 서비스로 분리 가능
✅ bookmark/       → 북마크 서비스로 분리 가능
✅ calendar/       → 캘린더 서비스로 분리 가능
✅ comment/        → 커뮤니티 서비스로 분리 가능
✅ statics/        → 분석 서비스로 분리 가능
```

각 도메인은 이미 **Controller-Service-Mapper-Domain** 구조로 잘 정리되어 있습니다.

### 2. **RESTful API 설계** ✅
```
/api/auth/**       → Auth Service
/api/house/**      → Housing Service
/api/lh/**         → Housing Service
/api/chat/**       → AI Service
/api/email/**      → Notification Service
/api/bookmark/**   → Preference Service
/api/calendar/**   → Preference Service
```

API 엔드포인트가 도메인별로 명확히 분리되어 있어 MSA 전환 시 라우팅이 쉽습니다.

### 3. **JWT 기반 Stateless 인증** ✅
```java
// JwtUtil.java - 이미 구현됨
- generateToken(email)
- extractEmail(token)
- isValidToken(token)
- generateRefreshToken(email)
```

JWT는 분산 시스템에 적합한 인증 방식입니다. API Gateway에서 중앙 검증 가능합니다.

### 4. **외부 API 통합 경험** ✅
```
✅ 공공데이터포털 (LH, 청약홈)
✅ Google Gemini API
✅ Kakao/Google OAuth2
✅ Gmail SMTP
```

이미 RestTemplate을 사용한 외부 API 호출 경험이 있어, 서비스 간 통신 구현이 쉽습니다.

### 5. **스케줄러 작업 분리** ✅
```java
✅ LhNoticeScheduler
✅ ApplyHomeScheduler  
✅ LhThumbScheduler
✅ AlertScheduler
✅ CHOfficetelScheduler
```

각 스케줄러는 이미 도메인별로 분리되어 있어, 해당 서비스로 이관 가능합니다.

### 6. **설정 외부화** ✅
```properties
# application.properties
jdbc.url=...
APPLYHOME_API_SERVICE_KEY=...
gemini.api.key=...
```

설정이 이미 외부화되어 있어, Spring Cloud Config로 중앙 관리 가능합니다.

---

## 🔄 추가 필요한 MSA 인프라

### 1. **API Gateway** ❌ → 🔧 구현 필요
**역할**: 모든 요청의 진입점, JWT 검증, 라우팅

**구현 방안**:
```yaml
# Spring Cloud Gateway
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
```

**우선순위**: ⭐⭐⭐⭐⭐ (가장 먼저 구축)

---

### 2. **Service Discovery (Eureka)** ❌ → 🔧 구현 필요
**역할**: 서비스 등록 및 탐색

**구현 방안**:
```yaml
# Eureka Server
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    port: 8761
```

**우선순위**: ⭐⭐⭐⭐⭐

---

### 3. **독립 데이터베이스** ❌ → 🔧 구현 필요
**현재 문제**: 모든 도메인이 하나의 MySQL DB 공유

**목표**:
```
auth_db       → Auth Service 전용
housing_db    → Housing Service 전용
preference_db → Preference Service 전용
community_db  → Community Service 전용
...
```

**우선순위**: ⭐⭐⭐⭐

---

### 4. **비동기 통신 (RabbitMQ)** ❌ → 🔧 구현 필요
**역할**: 이벤트 기반 서비스 간 통신

**사용 사례**:
```
회원 가입 → UserRegisteredEvent → 이메일 발송
북마크 추가 → BookmarkAddedEvent → 통계 업데이트
주택 정보 갱신 → HouseUpdatedEvent → 캐시 무효화
```

**우선순위**: ⭐⭐⭐

---

### 5. **분산 추적 (Zipkin)** ❌ → 🔧 구현 필요
**역할**: 서비스 간 호출 추적, 성능 병목 발견

**우선순위**: ⭐⭐

---

### 6. **중앙 로깅 (ELK Stack)** ❌ → 🔧 구현 필요
**역할**: 로그 수집 및 검색

**우선순위**: ⭐⭐

---

## 🚀 MSA 전환 로드맵

### Phase 1: 인프라 구축 (1-2개월) - 우선순위: 최상
```
[ ] API Gateway 구축 (Spring Cloud Gateway)
[ ] Eureka 서버 구축
[ ] Docker 환경 설정
[ ] RabbitMQ 설치
[ ] Config Server 구축
[ ] CI/CD 파이프라인
```

### Phase 2: 독립 서비스 분리 (2-3개월) - 우선순위: 높음
```
[ ] AI Service 분리 (ChatBot/, summary/)
    - 이유: 외부 API 의존, 독립적 실행 가능
    - 난이도: 쉬움
    
[ ] Notification Service 분리 (email/)
    - 이유: 비동기 처리 적합
    - 난이도: 쉬움
```

### Phase 3: 핵심 서비스 분리 (3-4개월) - 우선순위: 높음
```
[ ] Authentication Service 분리 (auth/, member/)
    - auth_db 스키마 분리
    - JWT 발급/검증 API
    - OAuth2 통합
    
[ ] Housing Information Service 분리 (lh/, applyHome/, house/)
    - housing_db 스키마 분리
    - 스케줄러 동작 확인
    - 공공 API 연동
```

### Phase 4: 부가 서비스 분리 (2-3개월) - 우선순위: 중간
```
[ ] User Preference Service 분리 (selfCheck/, bookmark/, calendar/)
[ ] Community Service 분리 (comment/)
[ ] Analytics Service 분리 (statics/)
```

### Phase 5: 모놀리스 제거 (1-2개월) - 우선순위: 마지막
```
[ ] 모놀리스 애플리케이션 종료
[ ] 레거시 DB 제거
[ ] 성능 테스트 및 튜닝
[ ] 문서화 업데이트
```

---

## 📊 제안된 마이크로서비스 아키텍처

### 서비스 구성 (8개 서비스)

```
1. API Gateway (:8080)
   ├─ JWT 검증
   ├─ 라우팅
   └─ Rate Limiting

2. Auth Service (:8081)
   ├─ 로그인/로그아웃
   ├─ OAuth2 (Kakao, Google)
   ├─ JWT 발급/갱신
   └─ 회원 관리

3. Housing Service (:8082)
   ├─ LH 공고
   ├─ 청약홈 데이터
   ├─ 주택 검색/필터링
   └─ 외부 API 데이터 수집 (스케줄러)

4. AI Service (:8083)
   ├─ AI 챗봇 (Gemini)
   └─ PDF 요약

5. Notification Service (:8084)
   ├─ 이메일 발송
   └─ 스케줄 알림

6. User Preference Service (:8085)
   ├─ 자격 진단
   ├─ 북마크
   └─ 캘린더

7. Community Service (:8086)
   └─ 댓글 관리

8. Analytics Service (:8087)
   └─ 통계 정보 제공
```

---

## 🎯 핵심 장점

### 현재 아키텍처 → MSA 전환 시 얻을 수 있는 이점

1. **독립 배포** 🚀
   - 현재: 작은 변경에도 전체 재시작
   - MSA: 서비스별 독립 배포

2. **수평 확장** 📈
   - 현재: 전체 복제만 가능
   - MSA: 트래픽 많은 서비스(Housing)만 확장

3. **기술 스택 유연성** 🔧
   - 현재: Spring Framework 5 고정
   - MSA: AI Service는 Python FastAPI도 가능

4. **장애 격리** 🛡️
   - 현재: PDF 파싱 실패 → 전체 다운
   - MSA: AI Service 다운 → 다른 서비스 정상

5. **팀 자율성** 👥
   - 현재: 하나의 코드베이스 공유
   - MSA: 팀별 독립적 개발/배포

---

## 📚 제공된 문서

### 1. MSA_ARCHITECTURE.md (70KB)
- 전체 아키텍처 가이드
- 서비스 분해 상세 계획
- 인프라 구성 (Eureka, Gateway, RabbitMQ, ELK, Prometheus 등)
- Database per Service 전략
- 배포 전략 (Docker, Kubernetes)

### 2. MSA_QUICK_START.md (35KB)
- 즉시 시작 가능한 단계별 가이드
- API Gateway 구축 코드 예제
- 첫 마이크로서비스 분리 (AI Service)
- Service Discovery 설정

### 3. MSA_DIAGRAMS.md (67KB)
- 현재 모놀리스 아키텍처 다이어그램
- 목표 MSA 아키텍처 다이어그램
- 서비스 의존성 맵
- 데이터베이스 아키텍처
- Kubernetes 배포 아키텍처

---

## 🎓 권장 학습 순서

1. **MSA_QUICK_START.md** 읽기 (30분)
   - 현재 상태 파악
   - 첫 단계 이해

2. **API Gateway 프로토타입 구축** (1일)
   - Spring Cloud Gateway
   - JWT 검증 GlobalFilter

3. **AI Service 분리 시도** (3일)
   - ChatBot, Summary 코드 이관
   - Eureka 등록
   - API Gateway 라우팅

4. **MSA_ARCHITECTURE.md** 전체 읽기 (1시간)
   - 장기 로드맵 이해
   - 인프라 계획

5. **팀 논의 및 우선순위 결정** (1주)
   - 리소스 확인
   - 단계별 일정 수립

---

## ⚠️ 주의사항

### MSA는 Silver Bullet이 아닙니다

**MSA 도입 전 고려사항**:
1. 팀 규모가 충분한가? (최소 3-4명 이상)
2. 운영 역량이 있는가? (Docker, Kubernetes)
3. 복잡도 증가를 감당할 수 있는가?
4. 네트워크 레이턴시를 허용할 수 있는가?

**작은 팀이라면**:
- 현재 모듈형 모놀리스 유지
- 필요한 부분만 점진적 분리 (AI, 알림)
- 인프라 학습에 시간 투자

**큰 팀이라면**:
- 전체 MSA 전환 추진
- 팀별로 서비스 담당
- 인프라 전담 팀 구성

---

## ✅ 결론

MyHomeCatch 백엔드는 **이미 MSA로 전환하기 좋은 구조**를 가지고 있습니다!

### 강점:
- ✅ 도메인별 명확한 분리
- ✅ RESTful API 설계
- ✅ JWT Stateless 인증
- ✅ 외부 API 통합 경험

### 다음 단계:
1. **MSA_QUICK_START.md** 따라 API Gateway 구축
2. **AI Service 분리** (가장 쉬운 시작점)
3. **점진적으로 핵심 서비스 분리**

모든 상세 정보는 제공된 3개 문서에 있습니다. 
문서를 참고하여 단계별로 진행하시면 성공적으로 MSA를 구축할 수 있습니다! 🚀

---

**작성일**: 2025-12-02  
**문서 버전**: 1.0  
**연락**: 문서 관련 질문은 이슈로 등록해주세요.

# 🐈 **MHC: 체계적인 청약 정보 제공 플랫폼**

<br>

> "나만의 꿈의 집을 찾아보러 가볼까요!"
> 

<br/>

<img width="1920" height="1080" alt="1" src="https://github.com/user-attachments/assets/b51a902e-790b-4e97-83ab-1c5b89a3fde1" />
<img width="1920" height="1080" alt="2" src="https://github.com/user-attachments/assets/6714a6df-1776-49ef-85f1-649c176e98cd" />
<img width="1920" height="1080" alt="3" src="https://github.com/user-attachments/assets/8105e80a-e2f6-4a2e-b45d-6f568f1682ab" />
<img width="1920" height="1080" alt="4" src="https://github.com/user-attachments/assets/c7b2a0d5-36ce-4117-a438-c81031dfeb25" />
<img width="1920" height="1080" alt="5" src="https://github.com/user-attachments/assets/c7781b96-2634-4a32-83c5-028d74f3a072" />
<img width="1920" height="1080" alt="6" src="https://github.com/user-attachments/assets/a169615e-5410-454f-bb6f-e043bef1eeac" />
<img width="1920" height="1080" alt="7" src="https://github.com/user-attachments/assets/efcdda5b-9ede-4bbd-9e82-d3e16d8815d0" />


<br/>

---

## 팀원

| <img src="https://github.com/rla-hyun.png?size=150" width="150"> | <img src="https://github.com/KimJH0709.png?size=150" width="150"> | <img src="https://github.com/doongeon.png?size=150" width="150"> | <img src="https://github.com/ryusemin.png?size=150" width="150"> |
| --- | --- | --- | --- |
| 김서현 | 김재현 | 나동건 | 류세민 |
| <img src="https://github.com/S0HYUNN.png?size=150" width="150"> | <img src="https://github.com/StarWhale0w0.png?size=150" width="150"> | <img src="https://github.com/Ahranah.png?size=150" width="150"> | <img src="https://github.com/Euangeliov.png?size=150" width="150"> |
| 박소현 | 윤예림 | 정혜인 | 한충훈 |

---

<br>

# **1. Project Overview (프로젝트 개요)**

### ✨ MyHomeCatch **란?**

청약 절차에 익숙하지 않은 사용자도 손쉽게 자격을 확인하고,

조건에 맞는 청약 정보를 한 눈에 확인할 수 있도록 돕는 서비스

### **📌 주요 기능**

- **자가진단을 통한 청약 조건 확인**
- **맞춤형 필터로 신청 가능 공고 추천**
- **인프라 · 절차 · 일정 등통합 정보 제공**
- **핵심 정보만 선별 제공시간 절약과 빠른 의사결정 지원**

---

<br>

# **2. Key Features (주요 기능)**

### ✅ **1) 지원 계획을 돕는 청약 자격진단**

- 지원 가능 유형 확인이 아닌 국민임대, 공공임대, 행복주택, 영구임대에 대해 **1순위 지원** 가능한지 확인

### ✅ **2) 맞춤형 청약 공고와 캘린더**

- 자격진단 내용을 바탕으로 지원 가능한 청약정보를 메인 페이지와 캘린더에서 우선 제공

### ✅ **3) 청약 공고 요약과 챗봇 서비스**

- PDF로 올라오는 공고문, 직접 다운받지 않아도 AI를 활용해 공고 요약 내용 확인, 챗봇에 질문
  
---

<br>

# **3. ERD (DB 설계)**
<img width="1000" height="700" alt="스크린샷 2025-08-19 오후 4 04 18" src="https://github.com/user-attachments/assets/088dd657-f94c-43dc-ae19-472085708107" />
<img width="1000" height="600" alt="스크린샷 2025-08-19 오후 4 04 35" src="https://github.com/user-attachments/assets/94dffef3-b9e5-4d9a-a130-dbd36858aed2" />

---

<br>

# **3.1 시스템 아키텍처 및 MSA 전환 가이드**

## 📐 아키텍처 문서

MyHomeCatch는 현재 **모듈형 모놀리스(Modular Monolith)** 아키텍처로 구현되어 있으며, MSA(Microservices Architecture)로 확장 가능하도록 설계되었습니다.

### 📚 상세 문서

- **[MSA_ARCHITECTURE.md](./MSA_ARCHITECTURE.md)** - 전체 MSA 전환 가이드
  - 현재 아키텍처 분석
  - 도메인 경계 및 서비스 분해 계획
  - 인프라 구성 및 통신 전략
  - 데이터베이스 전략 및 마이그레이션 로드맵

- **[MSA_QUICK_START.md](./MSA_QUICK_START.md)** - MSA 전환 빠른 시작 가이드
  - 즉시 시작 가능한 실용적인 첫 걸음
  - API Gateway 도입 방법
  - 첫 마이크로서비스 분리 (AI Service)
  - Service Discovery 구축

- **[MSA_DIAGRAMS.md](./MSA_DIAGRAMS.md)** - 아키텍처 다이어그램
  - 현재 모놀리스 아키텍처 시각화
  - 목표 MSA 아키텍처 시각화
  - 서비스 의존성 및 통신 패턴
  - 배포 아키텍처 (Kubernetes)

### 🎯 MSA 핵심 구성요소

현재 구현된 **MSA 준비 요소**:
- ✅ 도메인별 패키지 분리 (DDD 기반)
- ✅ RESTful API 설계
- ✅ JWT 기반 Stateless 인증
- ✅ 외부 API 통합 경험
- ✅ 설정 외부화

추가 필요한 **MSA 인프라**:
- 🔄 API Gateway (Spring Cloud Gateway)
- 🔄 Service Discovery (Eureka)
- 🔄 독립 데이터베이스 (Database per Service)
- 🔄 비동기 통신 (RabbitMQ/Kafka)
- 🔄 분산 추적 (Zipkin)
- 🔄 중앙 로깅 (ELK Stack)

---

<br>

# **4. Technology Stack (기술 스택)**

## 🖥 **Frontend**

| 기술 | 배지 |
|------|------|
| HTML5 | ![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white) |
| CSS3 | ![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white) |
| JavaScript | ![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black) |
| Vue.js | ![Vue.js](https://img.shields.io/badge/vuejs-%2335495e.svg?style=for-the-badge&logo=vuedotjs&logoColor=%234FC08D) |
| Vite | ![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white) | 
| Bootstrap | ![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white) |

<br>

## 🔧 **Backend**

| 기술 | 배지 |
|------|------|
| Java | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
| Spring | ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white) |
| Tomcat | ![Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black) |
| Gradle | ![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) |

<br>

## 🚀 **DB**

| 기술 | 배지 |
|------|------|
| MySQL | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white) |
| MyBatis | ![MyBatis](https://img.shields.io/badge/mybatis-6DB33F?style=for-the-badge&logo=mybatis&logoColor=white) |


<br>

## 🔧 APIS

| 기술 | 배지 |
|------|------|
| Google Gemini API | ![Google Gemini API](https://img.shields.io/badge/Google%20Gemini-4285F4?style=for-the-badge&logo=google&logoColor=white) |
| Kakao Maps API | ![Kakao Maps API](https://img.shields.io/badge/Kakao%20Maps-FFCD00?style=for-the-badge&logo=kakao&logoColor=000000) |
| 공공데이터포털 | ![공공데이터포털](https://img.shields.io/badge/%EA%B3%B5%EA%B3%B5%EB%8D%B0%EC%9D%B4%ED%84%B0%ED%8F%AC%ED%84%B8-FFCD00?style=for-the-badge&logoColor=000000) |
| 한국부동산원 | ![한국부동산원](https://img.shields.io/badge/%ED%95%9C%EA%B5%AD%EB%B6%80%EB%8F%99%EC%82%B0%EC%9B%90-1572B6?style=for-the-badge&logoColor=white) |

---

<br>

# **5. application.properties (설정 파일)**
## 📝 설정 파일 생성
프로젝트 루트 디렉토리에 `application.properties` 파일을 생성하고, 아래 예시를 참고해 환경 변수 값을 채워 넣습니다.  
(**주의:** 실제 값은 `{}` 부분을 발급받은 값으로 교체하세요.)
```

# ---------------------------
# Database 설정
# ---------------------------
jdbc.driver={JDBC_DRIVER}          # DB 드라이버 (ex: com.mysql.cj.jdbc.Driver)
jdbc.url={JDBC_URL}                # DB 접속 URL (ex: jdbc:mysql://localhost:3306/db명)
jdbc.username={DB_USERNAME}        # DB 계정명
jdbc.password={DB_PASSWORD}        # DB 비밀번호


# ---------------------------
# 공공 API 키
# ---------------------------
APPLYHOME_API_SERVICE_KEY={APPLYHOME_API_KEY}   # 국토교통부 청약홈 API 키 (공공데이터포털에서 발급)
LH_API_SERVICE_KEY={LH_API_KEY}                 # LH 공사 관련 API 키 (공공데이터포털에서 발급)


# ---------------------------
# Kakao 로그인
# ---------------------------
kakao.clientId={KAKAO_CLIENT_ID}                        # 카카오 개발자센터에서 발급받은 REST API 키
kakao.redirectUrl=http://localhost:5173/auth/loading    # 로그인 후 redirect 받을 URL (프론트엔드 주소)


# ---------------------------
# Google 로그인
# ---------------------------
google.client.id={GOOGLE_CLIENT_ID}         # Google Cloud Console OAuth 클라이언트 ID
google.client.secret={GOOGLE_CLIENT_SECRET} # Google Cloud Console OAuth 클라이언트 Secret
google.redirect.uri=http://localhost:5173/auth/googleloading  # 로그인 후 redirect 받을 프론트엔드 주소
# (개발 시 로컬 5173, 실제 서비스 시 배포 URL로 교체 필요)


# ---------------------------
# 이메일 (Gmail SMTP)
# ---------------------------
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username={GMAIL_USERNAME}    # Gmail 주소 (보내는 계정)
spring.mail.password={GMAIL_APP_PASSWORD} # Gmail 앱 비밀번호 (2단계 인증 후 발급 필요)
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true


# ---------------------------
# Chat AI (예: Gemini)
# ---------------------------
gemini.api.key={GEMINI_API_KEY}  # Google AI Studio (Gemini)에서 발급받은 API 키
gemini.api.url={GEMINI_API_URL}  # Gemini API 호출 endpoint URL (예: https://generativelanguage.googleapis.com/v1beta)


```
## 📖 환경 변수 설명

| 변수명                                                             | 설명                    | 발급/설정 경로                                                 |
| --------------------------------------------------------------- | --------------------- | -------------------------------------------------------- |
| `{JDBC_DRIVER}`, `{JDBC_URL}`, `{DB_USERNAME}`, `{DB_PASSWORD}` | DB 연결 설정              | 사용 중인 DB(MySQL, PostgreSQL 등) 환경                         |
| `{APPLYHOME_API_KEY}`                                           | 청약홈 API 키             | [공공데이터포털](https://www.data.go.kr)                        |
| `{LH_API_KEY}`                                                  | LH 임대주택 API 키         | [공공데이터포털](https://www.data.go.kr)                        |
| `{KAKAO_CLIENT_ID}`                                             | 카카오 로그인 REST API 키    | [카카오 개발자센터](https://developers.kakao.com)                |
| `{GOOGLE_CLIENT_ID}` / `{GOOGLE_CLIENT_SECRET}`                 | Google OAuth2 클라이언트   | [Google Cloud Console](https://console.cloud.google.com) |
| `{GMAIL_USERNAME}`                                              | Gmail 계정 (보내는 이메일)    | Gmail 계정                                                 |
| `{GMAIL_APP_PASSWORD}`                                          | Gmail 앱 비밀번호 (2FA 필요) | Google 계정 보안 → 앱 비밀번호                                    |
| `{GEMINI_API_KEY}`                                              | Gemini API Key        | [Google AI Studio](https://aistudio.google.com)          |
| `{GEMINI_API_URL}`                                              | Gemini API Endpoint   | Google AI Studio Docs                                    |


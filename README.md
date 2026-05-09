# Shoply

Spring Boot 기반의 오픈마켓 웹 애플리케이션입니다.  
사용자 쇼핑 기능과 관리자 운영 기능을 함께 제공하는 커머스 서비스입니다.

- [시연 영상 보기](https://www.youtube.com/watch?v=AlNPJQY9OeU)


## 프로젝트 개요

- 프로젝트명: Shoply
- 개발 형태: 팀 프로젝트
- 개발 기간: 2025.09 ~ 2025.10


## 기술 스택

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Security

### Frontend / View
- Thymeleaf
- HTML
- CSS
- JavaScript

### Database / Persistence
- Oracle Database
- JPA
- MyBatis

### Build / Infra
- Gradle
- AWS
- GitHub Actions


## 주요 기능

### 사용자 기능

- 회원가입 / 로그인
- 소셜 로그인
- 상품 목록 및 상세 조회
- 상품 검색
- 장바구니
- 주문 및 주문 완료
- 마이페이지
- 고객센터

### 관리자 기능

- 관리자 메인 대시보드
- 주문 관리
- 상품 관리
- 회원 관리
- 쿠폰 관리
- 고객센터 관리
- 기본 정보 / 배너 / 카테고리 / 정책 / 버전 관리


## 담당 기능

### 1. 인증/인가

- Spring Security 기반 로그인 기능 구현
- 사용자 권한에 따른 접근 제어 처리
- 일반 사용자와 관리자 페이지 접근 경로 분리

### 2. 관리자 메인 대시보드

- 관리자 메인 화면 및 메뉴 구조 구현
- 주문, 회원, 상품 등 주요 운영 정보 조회 화면 구성

### 3. 관리자 주문 관리

- 주문 목록 및 상세 조회 기능 구현
- 배송 상태 관리 기능 구현

### 4. 관리자 쇼핑몰 관리

- 쇼핑몰 운영 관련 관리 화면 구현
- 관리자용 데이터 조회 및 수정 기능 구현

### 5. 관리자 설정 관리

- 기본 정보, 배너, 카테고리, 정책, 버전 관리 기능 구현

### 6. 배포 및 CI/CD

- AWS 기반 배포 환경 구성
- GitHub Actions 기반 CI/CD 파이프라인 구축
- Gradle 빌드 및 배포 자동화


## 배포

본 프로젝트는 AWS 환경에 배포하여 운영했습니다.  
현재는 AWS 프리티어 기간 만료로 서버 운영을 종료한 상태입니다.

- 배포 환경: AWS
- CI/CD: GitHub Actions
- 빌드 도구: Gradle
- 서버 상태: 운영 종료

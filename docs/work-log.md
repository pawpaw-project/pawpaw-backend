# Work Log

## 2026-04-22

### 프로젝트 분석
pawpaw-backend 전체 구조 분석 완료.

**기술 스택:** Spring Boot 3.5.11, Java 17, Spring Security + JWT, Spring Data JPA + MySQL, WebSocket (STOMP)

**도메인 구조:**
- `user` — 회원가입/로그인/JWT 발급
- `pet` — 반려동물 CRUD
- `walk` — 산책 요청 게시 + 지원·수락 매칭
- `post` — 커뮤니티 게시글 + 댓글 + 좋아요
- `chat` — 1:1 채팅방 + STOMP WebSocket
- `hospital` — 동물병원 검색 + 리뷰

---

### 보안 이슈 처리

#### Issue #20 — WebSocket 메시지 senderId 클라이언트 신뢰 취약점
- **브랜치:** `fix/#20-websocket-sender-auth`
- **PR:** [#22](https://github.com/pawpaw-project/pawpaw-backend/pull/22)
- **변경 파일:**
  - `MessageRequestDto.java` — `senderId` 필드 제거
  - `ChatController.java` — STOMP `Principal`로 인증된 사용자 이메일 추출 후 DB 조회
  - `ChatService.java` — `saveMessage()` 시그니처를 `User sender`를 직접 받도록 변경

#### Issue #21 — JWT Secret 키 강도 개선
- **브랜치:** `fix/#21-jwt-secret-strength`
- **PR:** [#23](https://github.com/pawpaw-project/pawpaw-backend/pull/23)
- **변경 파일:**
  - `JwtTokenProvider.java` — 생성자에서 secret 64바이트 미만 시 서버 시작 즉시 예외 발생
  - `application.yaml` — `${JWT_SECRET:CHANGE_ME_IN_PRODUCTION}` 환경변수 주입 방식으로 변경

---

### 기능 작업

#### Issue #9 — 병원 검색 API 네이버 → 카카오 교체
- **브랜치:** `feat/#9-kakao-map-api`
- **PR:** [#24](https://github.com/pawpaw-project/pawpaw-backend/pull/24)
- **변경 파일:**
  - `KakaoSearchResponse.java` — 신규 추가 (카카오 로컬 검색 API 응답 구조)
  - `NaverSearchResponse.java` — 삭제
  - `HospitalService.java` — 카카오 로컬 검색 API로 전환
  - `application.yaml` — `kakao.rest-api-key` 환경변수 설정 추가

| 항목 | 변경 전 (네이버) | 변경 후 (카카오) |
|------|-----------------|-----------------|
| 엔드포인트 | `openapi.naver.com/v1/search/local.json` | `dapi.kakao.com/v2/local/search/keyword.json` |
| 인증 | `X-Naver-Client-Id` / `X-Naver-Client-Secret` | `Authorization: KakaoAK {key}` |
| 좌표 파싱 | `mapx`/`mapy` (정수형, 1e7 나누기) | `x`(경도) / `y`(위도) (소수형 직접 파싱) |

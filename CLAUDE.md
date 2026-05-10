# pawpaw-backend

반려동물 산책 매칭 플랫폼 백엔드 서버.

## 기술 스택

- Java 17 / Spring Boot 3.5.11
- Spring Security + JWT 인증
- Spring Data JPA + Hibernate
- WebSocket (STOMP) — 채팅
- Kakao Local API — 병원 검색
- Gradle

## 프로젝트 구조

```
src/main/java/com/pawpaw/pawpaw/
├── domain/
│   ├── user/      # 인증(JWT), 회원가입/로그인, 프로필
│   ├── pet/       # 반려동물 등록/수정/삭제
│   ├── walk/      # 산책 요청 + 신청 (WalkRequest / WalkApplication)
│   ├── post/      # 커뮤니티 게시글, 댓글, 좋아요
│   ├── chat/      # WebSocket 채팅방 + 메시지
│   └── hospital/  # Kakao Local API 기반 동물병원 검색 + 리뷰
└── global/
    ├── config/    # Security, WebSocket, AppConfig
    ├── jwt/       # JwtTokenProvider, JwtAuthenticationFilter
    ├── entity/    # BaseEntity (createdAt, updatedAt)
    └── upload/    # 파일 업로드 (FileService, UploadController)
```

## 인증

- JWT Bearer 토큰 방식
- `/api/auth/**`, `/ws/**`, `/files/**` 은 인증 없이 접근 가능
- 나머지 모든 엔드포인트는 인증 필요
- `@AuthenticationPrincipal User user` 로 현재 사용자 주입

## 주요 API 엔드포인트

### 인증 (`/api/auth`)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 (JWT 발급) |
| POST | `/api/auth/refresh` | 토큰 재발급 |

### 사용자 (`/api/users`)
| Method | Path | 설명 |
|--------|------|------|
| GET | `/api/users/me` | 내 프로필 조회 |
| PUT | `/api/users/me` | 닉네임, 주소 수정 |

### 반려동물 (`/api/pets`)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/pets` | 펫 등록 |
| GET | `/api/pets` | 내 펫 목록 |
| PUT | `/api/pets/{petId}` | 펫 수정 |
| DELETE | `/api/pets/{petId}` | 펫 삭제 |

### 산책 요청 (`/api/walk-requests`)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/walk-requests` | 산책 요청 등록 |
| GET | `/api/walk-requests` | 전체 산책 요청 목록 |
| GET | `/api/walk-requests/my` | 내 산책 요청 목록 |
| GET | `/api/walk-requests/{id}` | 산책 요청 상세 |
| DELETE | `/api/walk-requests/{id}` | 산책 요청 삭제 |

### 커뮤니티 게시글 (`/api/posts`)
| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/posts` | 게시글 작성 |
| GET | `/api/posts` | 전체 목록 (category 쿼리 파라미터로 필터 가능) |
| GET | `/api/posts/my` | 내 게시글 목록 |
| GET | `/api/posts/{postId}` | 게시글 상세 |
| PUT | `/api/posts/{postId}` | 게시글 수정 |
| DELETE | `/api/posts/{postId}` | 게시글 삭제 |

### 파일 업로드
| Method | Path | 설명 |
|--------|------|------|
| POST | `/api/upload` | 파일 업로드 → URL 반환 |
| GET | `/files/{filename}` | 업로드된 파일 접근 (인증 불필요) |

### 병원 (`/api/hospitals`)
Kakao Local API 기반 동물병원 검색 + 리뷰 CRUD

### 채팅 (`/ws`)
STOMP WebSocket 기반 실시간 채팅

## 코딩 컨벤션

- 레이어: Controller → Service → Repository
- DTO: 요청은 `*RequestDto`, 응답은 `*ResponseDto`
- 엔티티 생성자는 `@Builder` 사용, 직접 수정은 엔티티 내 메서드로 처리
- `BaseEntity` 상속으로 `createdAt` / `updatedAt` 자동 관리
- 날짜는 ISO 8601 문자열 직렬화 (`write-dates-as-timestamps: false`)
- 파일 업로드: 로컬 저장소 (`~/pawpaw-uploads`), UUID 파일명

## 브랜치 전략

- `develop` — 기본 통합 브랜치
- `feat/{이슈번호}-{설명}` — 기능 개발
- `fix/{이슈번호}-{설명}` — 버그 수정
- 각 기능은 독립된 브랜치에서 개발 후 develop으로 PR

## 환경 설정

민감한 설정(JWT secret, DB, Kakao API key 등)은 `application-local.yaml`에 관리 (gitignore 처리됨).

```yaml
# application-local.yaml 예시 (gitignore)
spring.datasource.url: ...
jwt.secret: ...
kakao.rest-api-key: ...
```

# 📚 전공서적 중고거래 플랫폼 - Second Books

![홈1](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/6ba9f048-374b-45fd-9ba5-0a6ed7787057)
![홈2](https://github.com/ShinJaeMin98/SecondBooks_Project/blob/master/src/main/resources/static/common/image/home_2.jpg)

## 1. 프로젝트 소개

### 📚 개요

- 프로젝트명 : 세컨북스
- 개발 기간 : 2023.12.27 ~ 2024.01.31
- 개발 인원 : 5명
- 핵심 기능 : 대학생을 위한 전공서적 중고거래 플랫폼

### 📚 기획 배경 및 기대 효과

- 주요 타겟 : 대학생
- 기획 배경
  - 기존 중고거래 플랫폼은 많지만 대학생들이 자신의 중고 서적을 안전하게 거래할 수 있는 플랫폼은 없음.
  - 대학교 인증을 통해 안전성과 신뢰성이 제공되도록 설계하면, '세컨북스'만의 개성있는 프로젝트가 완성될 것으로 기대되어 기획함.

## 2. 개발 환경

<img width="950" alt="개발환경" src="https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/47bdfdce-8a53-431b-b22b-1f972fcd4fa4">


# 3. UserFlow 작성

- Client 가 웹 사이트 방문 시 flow chart 작성

![Flowchart](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/047428a8-f28d-4b5f-85ac-b69bb1afedf2)


# 4. 기능 명세서

### :mag: 미리보기

<details>
    <summary>자세히 보기/접기</summary>
    
   
___
|로그인(소셜 로그인)|회원가입|회원정보 수정|
|:-:|:-:|:-:|
|![로그인 화면](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/8fdca634-ca56-433d-bf9b-dae3cdabf543)|![회원가입](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/3985f2f7-b4f4-4795-8b72-7958291abbb9)|![회원정보 수정](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/b61fc1ba-b259-4f00-adbc-02a40c297a6a)|

<br><br>


    
|게시판|게시글|게시글 검색|
|:-:|:-:|:-:|
|![책방](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/96bd5d90-55d7-4871-84c9-7bd7a2e14e3b)|![게시글](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/84c79c78-992b-4115-995c-282a9bf3259f)|![게시글 검색](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/74429513-7aa0-4a5e-b0e0-2e11c133f652)|
    
<br><br>

|찜 게시글|최신 게시글|관리자 페이지|
|:-:|:-:|:-:|
|![찜 게시글](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/075cf14a-fe49-4276-9dfe-8fc42bd410ca)|![최근 게시글](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/f366aeea-50e7-4378-811c-bb26394e3bd5)|![관리자 페이지](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/84c23fb3-18e9-4325-b0d5-e854f57c8d33)|

</details>


***


## 🧑🏻‍💻 관리자 페이지

### 📖 기본 설정

- 사이트 설정 (제목, 설명, 키워드)
- 썸네일 이미지 설정
- 약관 설정
- 학교 설정

### 📖 회원 관리

- 권한 변경(Member, Admin)
- 회원 전체 조회
- 회원 탈퇴

### 📖 학교 관리

- 학교 전체 조회
- 학교 정보 등록 (로고, 설명)
- 학교 수정 및 삭제

### 📖 게시판 관리

- 게시판 등록
  - 기본설정(게시판 ID, 이름, 페이지 관련 기능, 세부 기능 설정 등)
  - 카테고리 분류
  - 접근 권한 설정
  - 로고 삽입
- 게시판 전체 조회
- 게시판 수정 및 삭제
- 게시글 전체 조회
- 게시글 관리 및 삭제

## 🧑🏻‍💻 회원

### 📖 공통

- 권한
  - ADMIN / MEMBER 인가.
  - 가입 시 일반 회원(Member)

### 📖 로그인

- 비밀번호는 암호화(hashing) 과정을 거쳐 DB에 저장.

### 📖 회원가입

- E-mail : 학교 이메일로 제한, 인증코드 발송 및 일치여부 확인.
- ID : 영문과 숫자 조합으로 6자리 이상 제한, 중복 여부 확인.
- PW : 영문, 대소문자, 숫자, 특문 조합으로 8자리 이상 제한.
- 회원명 : 공백 입력 제한, 중복 여부 확인.
- 프로필 이미지 : 단일 이미지 제한, 제거 가능.
- 약관 동의 : 체크박스 클릭 필수.

### 📖 마이페이지

- 찜 게시글 조회(제목, 작성자, 작성일, 내용, 해당 게시글 이동, 찜 해제)
- 내가  게시글 조회(제목, 작성자, 작성일, 내용, 해당 게시글 이동)
- 회원정보 수정(프로필 이미지, 회원명, 비밀번호 수정 가능)
- 회원 탈퇴(비밀번호 확인, 인증코드 전송 및 일치 여부 확인)

## 🧑🏻‍💻 게시판

### 📖 게시판 구분

- 책방(전체, 팔아요, 구해요)
- 커뮤니티(자유, 스터디)
- Q&A, Faq

### 📖 게시글 구성

- 이미지, 카테고리, 제목, 가격, 조회수, 관심수, 회원명, 작성일
- 거래 상태, 찜 버튼
-  1:1 채팅, 댓글

### 📖 게시글 작성

- 카테고리 분류 클릭
- 제목, 가격, 내용 입력
- 이미지 파일 업로드 가능하도록 구현

### 📖 게시글 검색

- 제목, 내용, 제목+내용, 작성자 기준으로 조회.

# 5. ERD 작성

![erd](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/742c8d8b-e365-474d-853f-11dbfbf0b695)

# 6. 역할 분담

![역할](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/b046e1f3-ec87-4e44-ace2-9cbd5af2cc18)

# 7. 일정표

![계획서](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/83035d4a-27c9-4efc-bf9b-f01fa4d3b364)

# 8. 프로젝트 회고

![회고](https://github.com/ShinJaeMin98/SecondBooks_Project/assets/124487601/06f318ce-3964-4676-b33f-174dec44a9fb)


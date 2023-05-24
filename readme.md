# 할일
Jenkins Test
- [x] 회원가입 폼
    - [x] 로그인 상태에서 들어올 수 없다.
    - [x] 폼이 있어야 한다.
    - [x] input[name="username"] 필드가 있어야 한다.
    - [x] input[name="password"] 필드가 있어야 한다.
    - [x] 폼 체크
- [x] 회원가입 폼 처리
    - [x] 로그인 상태에서 들어올 수 없다.
    - [x] 유효성 체크를 해야 한다.
    - [x] member 테이블에 회원이 저장되어야 한다.
    - [x] 처리 후에 / 로 이동해야 한다. 302
    - [x] 회원가입이 완료되었습니다. /member/login 으로 302
- [x] 로그인 폼
    - [x] 로그인 상태에서 들어올 수 없다.
    - [x] 폼이 있어야 한다.
    - [x] input[name="username"] 필드가 있어야 한다.
    - [x] input[name="password"] 필드가 있어야 한다.
    - [x] 폼 체크
- [x] 로그인 폼 처리(스프링 시큐리티가 알아서 해줌)
    - [x] 세션에 데이터가 들어있는지 확인
- [x] 레이아웃 네비바 구현
    - [x] 로그인 버튼
    - [x] 회원가입 버튼
    - [x] 로그아웃 버튼
- [x] 로그인 후에는 내비바에 로그인된 회원의 username 이 보여야 한다.
- [x] 정적파일 정리
    - [x] 두루두루 사용되는 CSS 를 common.css 로 모으기
    - [x] 두루두루 사용되는 JS 를 common.js 로 모으기
- [x] toastMsg 에 ttl 기능 추가
- [x] 인스타그램 회원정보 입력
    - [x] 입력한 인스타그램 ID가 이미 존재하더라도, 그것의 성별이 아직 U 이면 연결가능
    - [x] 로그인한 사람만 가능
    - [x] 아이디
    - [x] 성별
- [x] 인스타그램 회원정보 입력 폼 처리
    - [x] 로그인한 사람만 가능
    - [x] 아이디
    - [x] 성별
    - [x] 회원과 인스타회원의 연결
    - [ ] 성공했을 때 호감표시 페이지로 이동
- [x] 본인이 좋아하는 사람 등록 폼
    - [x] 본인의 인스타그램 회원정보 입력을 완료한 사람만 가능
    - [x] 인스타그램 아이디
    - [x] 매력포인트(외모, 성격, 능력)
- [x] 본인이 좋아하는 사람 등록 폼 처리
    - [x] 아직 우리 서비스에 등록되지 않은 인스타 유저에게도 호감표시 가능

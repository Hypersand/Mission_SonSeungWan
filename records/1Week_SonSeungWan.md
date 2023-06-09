### 미션 요구사항 분석 & 체크리스트
- 삭제 버튼을 누르면, 목록에서 해당 호감 상대 삭제되어야 함.
  - 이때, 현재 로그인한 사용자가 소유권이 있는지 확인
- 삭제 후 호감목록 페이지로 다시 돌아와야 함.
- 상황에 맞는 적절한 응답 데이터 작성
- `LikeablePerson` 영역 안에서 해결 가능 할 것 같다.

#### LikeablePersonRepository
- [X]  삭제 요청 시, 넘어오는 `id`를 매개변수로 받아 `Optional` 객체 반환
  <br> -->  스프링 데이터 Jpa가 findById 메서드를 자동 생성

#### LikeablePersonService

- [X] 리포지토리를 통해 `Optional` 객체를 받고 검증 진행
- [X] 검증 통과 시 `LikeablePerson` 객체 반환
- [X] 현재 로그인 한 사용자가 호감을 표시한 사람이 맞는지 검증
- [X] 검증 통과 시 삭제

#### LikeablePersonController
- [X] 삭제할 `LikeablePerson` 객체 `id`를 경로 변수로 설정
- [X] 서비스단으로 보낼 삭제에 필요한 객체 매개변수 설정
- [X] 반환받은 RsData 객체에 대한 실패 처리
- [X] 호감목록으로 RsData와 함께 리다이렉트 처리

#### LikeablePersonControllerTests
- [X] MockMvc를 이용한 호감표시 등록 및 삭제 테스트


### 선택미션 - 구글 소셜 로그인
- [X] application.yml에 인증 정보 추가

---

### 1주차 미션 요약

**[접근 방법]**
- 점프투스프링부트에서 질문 글에 답변을 달았던 방식으로 접근<br>
- 질문 글에 여러 답변을 달 수 있었고, 삭제도 가능했었다. <br>
- 그램그램의 호감 상대 추가 및 삭제 기능이 위의 기능과 비슷하다 생각이 들었음.


**[특이사항]**

- 커밋 메시지 작성 요령 부족
  - 커밋 메시지 목록을 보고 뭔가 일관성이 없는 느낌을 받음
- 테스트 코드 디테일 부족
  - MockMvc만을 이용해서 컨트롤러만 테스트했음.
  - Assertions를 이용해서 서비스와 리포지터리도 테스트 했으면 좋았을 것 같다.
  
  
  **[Refactoring]** 
- [X] JpaRepository PK 타입 수정
- [X] 오류 처리 메시지 수정
- [X] Assertions를 이용한 삭제 테스트 추가

  



# Chapter 2
## TDD 흐름
테스트 -> 코딩 -> 리팩토링
기능을 검증하는 테스트 하나를 먼저 작성 뒤 테스트를 통과할 만큼만 코드를 작성, 테스트 통과 뒤에 리팩토링
이 과정을 반복적으로 수행하여 점진적으로 기능을 완성 해나가는 것이 TDD의 흐름
Red-Green-Refactor 라고도 부름

> Red(테스트 실패), Green (테스트 성공), Refactor

## 테스트 주도 개발
테스트를 추가한 뒤 모든 테스트를 통과시킬 만큼 기능을 구현하고 추가 하지 않은 테스트에 대해서는 고려하지 않음
즉 테스트가 개발을 주도

## 지속적인 코드 정리
구현을 완료한 뒤에는 프로덕트 코드, 테스트 코드를 정리
테스트 코드가 있으면 기능 동작 정상 여부를 바로 알 수 있기 때문에 리팩토링을 과감하게 진행 가능
지속적인 코드 정리로 품질 향상

# Chapter 3
## 테스트 작성 순서
### 쉬운 경우에서 어려운 경우로 진행
- 처음부터 복잡한 테스트부터 시작할 경우 한번에 구현할 코드가 많아져 버그가 생길 가능성이 높다.
- **구현하기 쉬운 테스트 부터 시작하여 그다음으로 구현하기 쉬운 테스트를 선택**한다.
> 모든 규칙을 충족하는 경우 -> 한가지 규칙을 충족하는 경우 -> 두가지 규칙을 충족하는 경우 -> ...  : **점진적으로 테스트**
> 모든 규칙을 충족하는 경우 -> 모든 규칙을 충족하지 않는 경우 -> ... : **구현할 코드가 갑자기 많아짐**

### 예외 경우에서 정상적인 경우로 진행
- **초반에 예외 상황을 테스트를 작성**한다면 코드 구조가 덜 복잡해질 수 있다.
- 정상적인 흐름만을 구현하다가 예외 상황을 반영하려면 코드 구조를 뒤집어야할 수도 있다.

### 완급 조절
- 한번에 많은 코드를 작성하지 말고 테스트 - 구현 - 확인을 점진적으로 나아간다.
- 처음 구현은 테스트만 통과할 정해진 값을 바로 리턴하고 테스트를 추가 시키며 점차 구현을 일반화 시킨다.
> 1. 정해진 값을 리턴
> 2. 값 비교를 이용해서 정해진 값을 리턴
> 3. 다양한 테스트를 추가하면서 구현을 일반환
```
if (param.eqauls("FIRST")) return false;

if (param.eqauls("FIRST") || param.equals("SECOND")) return false;

if (exceptStrList.contains(param)) return false;

```

# Chapter 4
## 기능 명세
- 기능 명세는 보통 문서나 Jira 같은 프로젝트 관리 도구에 명세 되어 공유 된다.
- 기능은 크게 입력과 출력으로 나눌 수 있다

> 로그인 기능의 입력은 아이디,패스워드 이고 출력은 성공/실패 여부 이다.
> 
> 만료일 계산의 입력은 결제일, 결제금액 이고 출력은 만료일 이다.
- 기능 실행 결과는 예외 발생 및 변경도 포함 (DB 저장 등)
- 테스트 코드를 작성하려다가 보면 입력과 출력을 도출해가는 과정에서 기능 명세를 구체화 할 수 있다.
> **만원을 결제 시 결제일 기준으로 한달 뒤가 만료일이 된다**
> 
> 한달 뒤 라는 출력을 기획자와 커뮤니케이션하여 구체화 할 수 있다
> 
> -> 1월 31일이 결제일이면 한달 뒤면 2월 28일인지 3월 2일인지

## 설계 과정의 TDD
- 테스트 작성을 위해서는 클래스 명, 메서드 명, 입출력 값을 정하게 되며 이는 곧 설계의 한 부분이다.
- 테스트를 통과할 만큼 코드를 작성하게 되는데, 설계도 마찬가지로 필요한만큼 설계를 변경한다.
- 요구사항을 분석하여 만드는 초기 설계는 초안이며 점점 설계가 변경이 되는 과정에 TDD 도 과정이 된다.

> 예시
> 
> ID가 중복되어 회원가입이 실패하는 테스트를 작성하는 시점에 DuplicationIdException 클래스가 추가 됨
> 
> 구독 만료일 계산하는 로직에서 입력 값이 "결제일", "결제금액" 2개에서 첫 납부일에 따라 만료일이 변경 되는 테스트 케이스 작성하는 과정에서 "첫 결제일" 입력 값이 추가 됨

# Chapter 5
## JUnit
- 자바 테스트 프레임워크
- 버전은 5.x 을 많이 사용
- Junit5 의 구현체는 JUpiter 를 사용하고, Junit3,4는 Vantage 를 사용
### @Test
- 테스트 클래스는 통상적으로 접미사로 ~Test 를 붙힘
- @Test 어노테이션을 테스트할 메서드에 붙혀 사용 (private 메서드는 안됨)
- Assertions 클래스의 정적 메서드(assertEquals 등)을 사용하여 테스트 검증
```
@Test
void sum() {
    int result = Math.addExact(2,3);
    Assertions.assertEquals(5, result);
}
```

### 라이프 사이클
- @Test 가 붙은 메서드를 실행할 때 마다 테스트 클래스의 객체를 생성하고 실행
- 테스트 메서드 실행 전 @BeforeEach 어노테이션이 붙은 메서드를 실행
- 테스트 메서드 실행 후 @AfterEach 어노테이션이 붙은 메서드를 실행
- 한 클래스의 모든 테스트 메서드가 실행 전 @BeforeAll 어노테이션이 붙은 정적 메서드를 실행 (정적 메서드가 아니면 컴파일 에러)
- 한 클래스의 모든 테스트 메서드가 실행 후 @AfterAll 어노테이션이 붙은 정적 메서드를 실행 (정적 메서드가 아니면 컴파일 에러)

>1. @BeforeAll 정적 메서드 실행
>2. @Test 메서드가 있는 클래스 객체 생성
>3. @BeforeEach 메서드 실행
>4. @Test 메서드 실행
>5. @AfterEach 메서드 실행
>6. @Test 메서드 개수만큼 2,3,4,5 반복 실행
>7. @AfterAll 정적 메서드 실행

# Chapter 6
## 테스트 코드 구성
### 기능에서 상황
- 기능은 주어진 상황에 따라 다르게 동작
- 똑같이 실행 하였지만 주어진 상활에 따라 결과가 달라진다.
```
@Test
void baseballGameTest() {
    // 정답이 123인 상황
    BaseballGame game1 = new BaseballGame("123");
    Score score1 = game1.guess("456");
    assertEquals(0, score1.srike());
    // 정답이 456인 상황
    BaseballGame game2 = new BaseballGame("456");
    Score score2 = game2.guess("456");
    assertEquals(3, score2.srike());
}
```

### 테스트 코드 구성 요소
- 테스트는 상황, 실행, 결과로 이루어 진다.
- 상황은 @BeforeEach 를 적용한 메서드에서 설정할 수 도 있다.
- 상황이 없는 경우도 존재
```
@Test
void baseballGameTest() {
    // 상황
    BaseballGame game1 = new BaseballGame("123");
    // 실행
    Score score1 = game1.guess("456");
    // 결과
    assertEquals(0, score1.srike());
}
```

### 외부 상황과 외부 결과
- DB, 파일, 외부 API 같은 외부 상황을 이용할 경우가 존재
- 파일의 경우 테스트 경로에 미리 파일을 생성하거나 기존 파일을 삭제 하여 일관성을 유지
- DB의 경우 테스트 전 데이터를 미리 넣어놓고 실행 후 트랜잭션을 롤백하여 일관성을 유지
- 외부 API의 경우 연결 불가나 응답 지연 상황을 미리 만들 수 없으므로 테스트가 어렵다.
- **대역**을 사용하면 외부 상황에 대한 테스트 작성이 쉬워진다.

# Chapter 7
## 대역
- 실제 객체를 대신하여 동작하는 가짜 객체
- 외부 요인 (파일, DB, 외부 통신)이 테스트에 관여하는 상황에서 주로 사용
- 종류는 Stub, Fake, Spy, Mock 이 존재
  - Stub : 구현을 단순한 것으로 대체, 테스트에 맞게 단순히 원하는 동작만 수행 (단순 반환 값만 주는 객체 등)
  - Fake : 프로덕트 코드에는 적합하지 않지만 실제 동작하는 구현을 제공 (DB 대신 메모리 사용 등)
  - Spy : 실제 동작을 하며 호출된 내역을 기록하고 테스트 결과에서 검증할 때 사용
  - Mock : 호출 여부, 횟수, 순서 같은 행위 검증을 위한 객체

```
// Stub
class StubPasswordChecker implements PasswordChecker {

    @Override
    public boolean check(String password) {
        retrun true;
    }

}

// Fake
public class FakeUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }
}

```
- 대역을 이용하면 외부 요인에 의하여 테스트 및 개발이 지연되는 것을 방지 할 수 있다.
- 외부 요인은 따로 클래스를 분리하여 대역을 만드는 것이 테스트가 용이
- 당장 구현이 오래걸리는 로직도 별도 클래스로 분리하여 대역을 만드는 것도 테스트에 용이

## Mockito
- 자바 모의 객체 생성, 검증, 스텁을 지원하는 프레임워크
- Mockito.mock 메서드로 모의 객체를 생성 가능 (클래스, 추상 클래스, 인터페이스 가능)
- BDDMockito.given 메서드로 스텁을 정의할 모의 객체 메서드 호출을 전달하고 willReturn 메서드로 리턴 값을 지정
- ArgumentMatchers.any 메서드로 스텁 설정 시 인자와 일치 여부를 확인하고 리턴 값을 지정 가능
- BDDMockito.then 메서드로 실제 모의 객체가 특정 메서드를 호출하는지 검증
- ArgumentCaptor 클래스를 이용하여 실제 메서드에 들어가는 인자를 캡처 가능
```
@Test
void test() {
    // 생성 
    Game mockGame = Mockito.mock(Game.class);
    // Stub
    BDDMockito.given(mockGame.generate(Level.EASY)).willReturn("123");
    // 일치된 인자 Stub
    BDDMockito.given(mockGame.generate(ArgumentMatchers.any())).willReturn("123");
    
    String result = mockGame.generate(Level.EASY);
    
    // 호출 검증
    BDDMockito.then(mockGame).should().generate(Level.EASY);
    
    // 인자 캡쳐
    ArgumentCaptor<Level> captor = ArgumentCaptor.forClass(Level.class);
    BDDMockito.then(mockGame).should().generate(captor.capture());
    Level realLevel = captor.getValue();
}

```